package com.vts.clientcenter.jobs;

import com.vts.clientcenter.domain.EmployeeEntity;
import com.vts.clientcenter.domain.EmployeeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeProcessor implements ItemProcessor<EmployeeRecord, EmployeeEntity> {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(EmployeeProcessor.class);

    public EmployeeEntity process(EmployeeRecord employee) throws Exception{
        LOGGER.info("Inserting employee '{}'", employee);

        EmployeeEntity rs = new EmployeeEntity();
        rs.setSourceId(employee.getSourceId());
        rs.setBirthDate( LocalDate.parse(employee.getBirthDate(), DateTimeFormatter.ofPattern("M/dd/yyyy")));
        rs.setCity(employee.getCity());
        rs.setEmailAddress(employee.getEmailAddress());
        rs.setFirstName(employee.getFirstName());
        rs.setLastName(employee.getLastName());
        rs.setMiddleInitial(employee.getMiddleInitial());
        rs.setPhoneNumber(employee.getPhoneNumber());
        rs.setSocialSecurityNumber(employee.getSsn());
        rs.setState(employee.getState());
        rs.setCity(employee.getCity());
        rs.setStreet(employee.getStreet());
        rs.setZipCode(employee.getZip());
        return rs;
    }

}
