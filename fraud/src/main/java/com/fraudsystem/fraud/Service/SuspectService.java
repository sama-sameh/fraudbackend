package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.SuspectCountPerDayDTO;
import com.fraudsystem.fraud.Entity.*;
import com.fraudsystem.fraud.Repository.SuspectRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
public class SuspectService {
    private SuspectRepository suspectRepository;
    private RuleService ruleService;
    private EntityManager entityManager;
    private ConditionService conditionService;
    CustomerService customerService;
    @Autowired
    public SuspectService(SuspectRepository suspectRepository, RuleService ruleService, EntityManager entityManager, ConditionService conditionService, CustomerService customerService) {
        this.suspectRepository = suspectRepository;
        this.ruleService = ruleService;
        this.entityManager = entityManager;
        this.conditionService = conditionService;
        this.customerService = customerService;

    }
    public List<Suspect> getAllSuspect() {
        List<Suspect> suspectList = suspectRepository.findAll();
        for (Suspect suspect : suspectList) {
            Date suspectDate = suspect.getDate();
            if (suspectDate != null) {
                LocalDate suspectLocalDate = suspectDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate now = LocalDate.now();

                int age = Period.between(suspectLocalDate, now).getDays();
                suspect.setSuspectAge(age);
            }
        }
        return suspectRepository.findAll();
    }
    public Optional<Suspect> getSuspectById(int id) {
        return suspectRepository.findById(id);
    }
    @Transactional
    public Suspect addSuspect(Suspect suspect) {
        return suspectRepository.save(suspect);
    }
    @Transactional
    public Suspect updateSuspect(Suspect suspect) {
        return suspectRepository.save(suspect);
    }
    @Transactional
    public void deleteSuspect(Suspect suspect) {
        suspectRepository.delete(suspect);
    }
    @Transactional
    public void evaluateTransaction(Transaction transaction) {

        List<Rule> rules = ruleService.getRulesStatus("Published");

        for (Rule rule : rules) {
            if (ViolateRule(transaction, rule)){
                List<Condition> conditions = conditionService.getConditionsByType(rule, transaction.getClass().getSimpleName().toLowerCase());
                Suspect suspect = new Suspect();
                suspect.setTransaction(transaction);
                suspect.setAccount(transaction.getAccountFrom());
                suspect.setRule(rule);
                String actionMessage = buildActionMessage(rule, transaction, conditions);
                suspect.setActionMassage(actionMessage);
                Customer customer = customerService.findCustomerById(transaction.getAccountFrom().getCustomer().getId());
                suspect.setCustomer(customer);
                suspect.setDate(new Date());
                addSuspect(suspect);

            }

        }

    }
    @Transactional
    public void evaluateActivity(CustomerActivity customerActivity) {
        List<Rule> rules = ruleService.getRulesStatus("Published");

        for (Rule rule : rules) {
            if (ViolateRule(customerActivity, rule)){
                List<Condition> conditions = conditionService.getConditionsByType(rule, customerActivity.getClass().getSimpleName().toLowerCase());
                Suspect suspect = new Suspect();
                suspect.setTransaction(null);
                suspect.setAccount(null);
                suspect.setRule(rule);
                String actionMessage = buildActionMessage(rule, customerActivity, conditions);
                suspect.setActionMassage(actionMessage);
                Customer customer = customerService.findCustomerByUserId(customerActivity.getUser().getId());
                suspect.setCustomer(customer);
                suspect.setDate(new Date());
                addSuspect(suspect);

            }

        }
    }
    public Boolean ViolateRule(Object entity,Rule rule) {
        List<Condition> conditions= conditionService.getConditionsByType(rule,entity.getClass().getSimpleName().toLowerCase());
        if (conditions.isEmpty()) return false;
        boolean result;
        Condition firstCondition = conditions.get(0);
        result = evaluateCondition(firstCondition, entity);
        for (int i = 1; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            boolean conditionResult = evaluateCondition(condition, entity);

            if ("AND".equals(condition.getLogicalConnector())) {
                result = result && conditionResult;
            } else if ("OR".equals(condition.getLogicalConnector())) {
                result = result || conditionResult;
            }
        }
        return  result;
    }
    public boolean evaluateCondition(Condition condition,Object entity) {
        try {
            if (condition.getAggregationFunction()!=null && condition.getTime_interval()!=null){
                return evaluateAggregationAndInterval(condition,entity);
            }
            else if(condition.getAggregationFunction()!=null){
                return evaluateAggregation(condition,entity);
            }
            Field entityField = entity.getClass().getDeclaredField(condition.getField());
            entityField.setAccessible(true);
            Object fieldValue = entityField.get(entity);
            Object rightSideValue = null;
            if ("static".equalsIgnoreCase(condition.getValueType())) {
                rightSideValue = castValue(condition.getValue(), entityField.getType());
            } else if ("dynamic".equalsIgnoreCase(condition.getValueType())) {
                rightSideValue = getDynamicValue(condition.getSource(), condition.getSourceField());
            }
            return compareValues(fieldValue, rightSideValue, condition.getOperator());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean evaluateAggregation(Condition condition, Object entity) {
        if (!(entity instanceof Transaction)) return false;
        Transaction transaction = (Transaction) entity;
        int account_no = transaction.getAccountFrom().getAccount_no();
        String aggregation = condition.getAggregationFunction();
        String value = condition.getValue();
        String type = condition.getType().substring(0, 1).toUpperCase() + condition.getType().substring(1);;
        String field  = condition.getField();
        String selectField;
        if (aggregation.equalsIgnoreCase("COUNT")) {
            selectField = "COUNT(*)";
        } else {
            selectField = aggregation + "(t." + field + ")";
        }
        String queryStr = "SELECT " + selectField + " FROM " + type + " t " +
                "WHERE t.accountFrom.account_no = :accountNo " +
                "AND t."+field + " = :fieldValue";

        long count = entityManager.createQuery(queryStr,Long.class)
                .setParameter("accountNo", account_no)
                .setParameter("fieldValue", transaction.getLocation())
                .getSingleResult();

        System.out.println(count);
       return count==Integer.parseInt(value);
    }

    private boolean evaluateAggregationAndInterval(Condition condition,Object entity) {
        if (!(entity instanceof Transaction)) return false;
        Transaction transaction = (Transaction) entity;
        int account_no = transaction.getAccountFrom().getAccount_no();
        LocalDateTime now =  LocalDateTime.now();
        LocalDateTime startTime = calculateStartTime(condition.getTime_interval());
        String aggregation = condition.getAggregationFunction();
        String type = condition.getType().substring(0, 1).toUpperCase() + condition.getType().substring(1);;
        String field  = condition.getField();
        String operator = condition.getOperator();
        String threshold = condition.getValue();
        String selectField;
        if (aggregation.equalsIgnoreCase("COUNT")) {
            selectField = "COUNT(*)";
        } else {
            selectField = aggregation + "(t." + field + ")";
        }
        String queryStr = "SELECT " + selectField + " FROM " + type + " t " +
                "WHERE t.accountFrom.account_no = :accountNo " +
                "AND t.date BETWEEN :startTime AND :now";

        Object result = entityManager.createQuery(queryStr)
                .setParameter("accountNo", account_no)
                .setParameter("startTime", startTime)
                .setParameter("now", now)
                .getSingleResult();
        double value = (result instanceof Long) ? (Long) result : (Double) result;

        return compareValues(value, threshold, operator);
    }

    private boolean compareValues(Object left, Object right, String operator) {
        if (left == null || right == null) return false;

        switch (operator.toUpperCase()) {
            case "=":
                return left.equals(right);

            case "!=":
                return !left.equals(right);

            case ">":
                return Double.parseDouble(left.toString()) > Double.parseDouble(right.toString());

            case "<":
                return Double.parseDouble(left.toString()) < Double.parseDouble(right.toString());

            case "IN":
                if (right instanceof Collection) {
                    return ((Collection<?>) right).contains(left);
                }
                return false;

            case "NOT IN":
                if (right instanceof Collection) {
                    return !((Collection<?>) right).contains(left);
                }
                return false;

            case "BETWEEN":
                if (right instanceof List && ((List<?>) right).size() == 2) {
                    double leftVal = Double.parseDouble(left.toString());
                    double min = Double.parseDouble(((List<?>) right).get(0).toString());
                    double max = Double.parseDouble(((List<?>) right).get(1).toString());
                    return leftVal >= min && leftVal <= max;
                }
                return false;

            case "LIKE":
                return left.toString().contains(right.toString());

            default:
                return false;
        }
    }


    public Object getDynamicValue(String sourceTable, String sourceField) {
        String query = "SELECT " + sourceField + " FROM " + sourceTable;
        List<?> values = entityManager.createNativeQuery(query).getResultList();
        return values;
    }
    private Object castValue(String value, Class<?> targetType) {
        if (value == null) return null;

        try {
            if (targetType == String.class) {
                return value;

            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);

            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);

            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value);

            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);

            } else if (targetType == java.util.Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(value);
            }

            // Add support for other types if needed (e.g., LocalDate, BigDecimal)
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private String buildActionMessage(Rule rule, Object entity, List<Condition> conditions) {
        String template = rule.getMessageTemplate();
        Map<String, String> placeholders = new HashMap<>();

        for (Condition condition : conditions) {
            try {
                String field = condition.getField();

                Field entityField = entity.getClass().getDeclaredField(field);
                entityField.setAccessible(true);
                Object fieldValue = entityField.get(entity);

                placeholders.put(field, String.valueOf(fieldValue));

                if ("static".equalsIgnoreCase(condition.getValueType())) {
                    placeholders.put("limit", condition.getValue());
                    placeholders.put("failed_attempts", condition.getValue());
                }
                if (condition.getTime_interval() != null) {
                    placeholders.put("interval", condition.getTime_interval());
                }

                if (condition.getAggregationFunction() != null) {
                    placeholders.put("aggregationFunction", condition.getAggregationFunction());
                }

                if (condition.getSourceField() != null) {
                    placeholders.put("sourceField", condition.getSourceField());
                }

            } catch (Exception e) {
                continue;
            }
        }
        if (entity instanceof Transaction tx) {
            placeholders.put("amount", String.valueOf(tx.getAmount()));
            placeholders.put("location", tx.getLocation());
            placeholders.put("time", tx.getDate().toString());
            placeholders.put("date", tx.getDate().toString());

            if (tx.getAccountFrom() != null) {
                placeholders.put("accountNumber", String.valueOf(tx.getAccountFrom().getAccount_no()));
            }

            if (tx.getAccountTo() != null) {
                placeholders.put("toAccount",String.valueOf( tx.getAccountTo().getAccount_no()));
            }

            if (tx.getDevice() != null) {
                placeholders.put("device", tx.getDevice().getIpAddress());
            }
        }
        else if (entity instanceof CustomerActivity ca) {
            placeholders.put("interval", String.valueOf(ca.getDate()));
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return template;
    }
    private LocalDateTime calculateStartTime(String interval) {
        LocalDateTime now = LocalDateTime.now();

        switch (interval) {
            case "1 Minute":
                return now.minusMinutes(1);
            case "5 Minutes":
                return now.minusMinutes(5);
            case "10 Minutes":
                return now.minusMinutes(10);
            case "15 Minutes":
                return now.minusMinutes(15);
            case "30 Minutes":
                return now.minusMinutes(30);
            case "60 Minutes":
                return now.minusMinutes(60);
            default:
                return now.minusMinutes(10); // Default fallback
        }
    }
    public List<SuspectCountPerDayDTO> getSuspectsCountPerNDays(LocalDate start){
        List<SuspectCountPerDayDTO> dtos = new ArrayList<>();
        Date endDate = java.sql.Timestamp.valueOf(start.atStartOfDay().plusDays(1));
        Date startDate = java.sql.Timestamp.valueOf(start.minusDays(15).atStartOfDay());
        System.out.println(startDate);
        System.out.println(endDate);
        for (Object[] row : suspectRepository.getSuspectCountBetweenDates(startDate,endDate)) {
            java.sql.Date date = (java.sql.Date) row[0];
            Long count = (Long) row[1];
            LocalDate localDate = ((java.sql.Date) date).toLocalDate();

            dtos.add(new SuspectCountPerDayDTO(localDate, count));
        }
        return dtos;
    }
    public Map<String, Long> getCountAlertsPerRule(){

        List<Object[]> results = suspectRepository.countAlertsPerRule();
        Map<String, Long> resultMap = new HashMap<>();
        for (Object[] row : results) {
            resultMap.put((String) row[0], (Long) row[1]);
        }
        return resultMap;
    }
    boolean existsByTransaction(Transaction tx) {
        return suspectRepository.existsByTransaction(tx);
    }
    public Long getNoOfSuspects() {
        return suspectRepository.count();
    }
    public Map<Integer, Long> getCountAlertsPerAccount(){
        Pageable topTen = PageRequest.of(0, 5);
        List<Object[]> results = suspectRepository.countTop10AlertsPerAccount(topTen);

        Map<Integer, Long> resultMap = new HashMap<>();
        for (Object[] row : results) {
            if(row[0]!=null){
                resultMap.put((Integer) row[0], (Long) row[1]);
            }
        }
        return resultMap;
    }
}
