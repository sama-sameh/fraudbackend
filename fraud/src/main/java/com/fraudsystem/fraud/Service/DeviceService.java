package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.Entity.Device;
import com.fraudsystem.fraud.Repository.DeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    private DeviceRepository deviceRepository;
    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    public Device getDeviceById(int id) {
        return deviceRepository.findById(id).get();
    }
    @Transactional
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }
    @Transactional
    public void updateDevice(Device device) {
        deviceRepository.save(device);
    }
    @Transactional
    public void deleteDevice(int id) {
        deviceRepository.deleteById(id);
    }
    public Device getDeviceByIpAndType(Device device) {
        return deviceRepository.getDeviceByTypeAndIpAddress(device.getType(), device.getIpAddress());
    }

}
