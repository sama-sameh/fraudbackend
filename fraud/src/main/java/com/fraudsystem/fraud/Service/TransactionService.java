package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.TransactionDTO;
import com.fraudsystem.fraud.DTO.TransactionScatterDTO;
import com.fraudsystem.fraud.Entity.*;
import com.fraudsystem.fraud.Repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private DeviceService deviceService;
    private AlertService alertService;
    private CustomerActivityService customerActivityService;
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, DeviceService deviceService, RuleService ruleService, AlertService alertService, EntityManager entityManager, ConditionService conditionService, CustomerActivityService customerActivityService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.deviceService = deviceService;
        this.alertService = alertService;
        this.customerActivityService = customerActivityService;
    }
    @Transactional
    public boolean manageTransaction(Transaction transaction) {
        Account accountTo = this.accountService.getAccount(transaction.getAccountTo().getAccount_no());
        Account accountFrom = this.accountService.getAccount(transaction.getAccountFrom().getAccount_no());
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setDate(new Date());
        Device device = this.deviceService.getDeviceByIpAndType(transaction.getDevice());
        if (device == null) {
            device = this.deviceService.addDevice(transaction.getDevice());
        }
        transaction.setDevice(device);

        if (transaction.getType().equals("transfer"))
        {
           if(this.withdraw(transaction.getAmount(), transaction.getAccountFrom().getAccount_no())){
               this.deposit(transaction.getAmount(), transaction.getAccountTo().getAccount_no());
               transaction.setStatus("Accepted");
               transaction.setLocation( transaction.getLocation().split(",")[0]);
               save(transaction);
               alertService.runAllRules(transaction);

               return true;
           }
        }
        transaction.setStatus("Refused");
        return false;

    }

    @Transactional
    public boolean deposit(double amount,int to) {
        return accountService.updateBalance(to, amount) != null;
    }
    @Transactional
    public boolean withdraw(double amount,int from) {
        Account account = accountService.getAccount(from);
        System.out.println("Withdraw function");
        if (account.getBalance()<amount)
            return false;
        return accountService.updateBalance(from, -amount) != null;
    }
    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
    public Transaction findById(int id) {
        return transactionRepository.findById(id).get();
    }
    public List<TransactionScatterDTO> getTransactionScatterDTO() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<Account,Long> transactionFrequency = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountFrom,Collectors.counting()));
        return transactions.stream()
                .map(tx -> {
                    boolean isFraud = alertService.existsByTransaction(tx);
                    int frequency = transactionFrequency.getOrDefault(tx.getAccountFrom(), 1L).intValue();
                    return new TransactionScatterDTO(tx.getAmount(), frequency, isFraud);
                })
                .collect(Collectors.toList());
    }
    public void runRules(List<Integer> rulesId){
        List<Transaction> transactions = transactionRepository.findAll();
        alertService.runSpecificRules(rulesId,transactions);
    }
    public TransactionDTO toFeatures(Transaction tx) {
        TransactionDTO dto = new TransactionDTO();
        int accountNo = tx.getAccountFrom().getAccount_no();
        double accountBalance = tx.getAccountFrom().getBalance();
        Customer customer= tx.getAccountFrom().getCustomer();
        CreditCard card = tx.getAccountFrom().getCreditCard();
        dto.setTransaction_id(tx.getTransaction_no());
        dto.setTransaction_Amount(tx.getAmount());
        dto.setTransaction_Type("Bank Transfer");
        dto.setAccount_Balance(accountBalance);
        dto.setDevice_Type(tx.getDevice().getType());
        dto.setLocation(tx.getLocation());
        dto.setMerchant_Category("Transfer");
        dto.setIP_Address_Flag(0);
        dto.setPrevious_Fraudulent_Activity(FraudulentActivityExist(accountNo));
        dto.setDaily_Transaction_Count(getDailyTransactionCount(accountNo));
        dto.setAvg_Transaction_Amount_7d(transactionRepository.getAvgTransactionAmount7d(accountNo));
        dto.setCard_Type(card.getCardType());
        dto.setCard_Age(getCardAge(card));
        dto.setTransaction_Distance(0.0); // TODO
        dto.setAuthentication_Method("Password");
        Optional<CustomerActivity> optionalActivity = this.customerActivityService.getCustomerActivityByCustomer(customer, convertToLocalDate(tx.getDate()));
        if (optionalActivity.isPresent()){
           dto.setFailed_Transaction_Count_7d(optionalActivity.get().getFailed_attempts());
        }
        else{
            dto.setFailed_Transaction_Count_7d(0);
        }
        // Date Features
        Calendar cal = Calendar.getInstance();
        cal.setTime(tx.getDate());
        dto.setMinute(cal.get(Calendar.MINUTE));
        dto.setHour(cal.get(Calendar.HOUR_OF_DAY));
        dto.setDay(cal.get(Calendar.DAY_OF_MONTH));
        dto.setMonth(cal.get(Calendar.MONTH) + 1);

        // Derived Features
        dto.setIs_Weekend((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ? 1 : 0);
        double threshold = 138.28;
        dto.setAmount_Balance_Ratio(tx.getAmount() / accountBalance);
        dto.setHigh_transaction_amount(tx.getAmount() > threshold ? 1 : 0);

        dto.setTransactions_per_Hour(transactionRepository.getTransactionsPerHour(accountNo));
        dto.setAvg_Transactions_per_Hour_7d(transactionRepository.getAvgTransactionsPerHour7d(accountNo));
        dto.setHour_Activity_Ratio(dto.getTransactions_per_Hour() / Math.max(dto.getAvg_Transactions_per_Hour_7d(), 1.0));

        return dto;
    }
    public int FraudulentActivityExist(int accountNo){
       if(alertService.existsByAccount(accountNo))
           return 1;
       else
           return 0;

    }
    public int getDailyTransactionCount(int accountNo){
        Account account = accountService.getAccount(accountNo);
        return transactionRepository.countByAccountFromAndDate(account,new Date());
    }
    public int getCardAge(CreditCard card){
        LocalDate issuedLocalDate = card.getIssuedDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate today = LocalDate.now();
        return Period.between(issuedLocalDate, today).getYears();
    }
    public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}


