package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.domain.Employee;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.repository.EmployeeExtensionRepository;
import com.vts.clientcenter.service.UserClientService;
import com.vts.clientcenter.service.dto.EmployeeDTO;
import com.vts.clientcenter.service.dto.TResult;
import com.vts.clientcenter.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserClientServiceImpl implements UserClientService {
    public static final String ERR_EMAIL_EMPTY = "ERR_EMAIL_EMPTY";
    public static final String ERR_EMAIL_EXISTED = "ERR_EMAIL_EXISTED";

    @Autowired
    private Environment environment;

    @Autowired
    private EmployeeExtensionRepository employeeRepository;

    @Override
    public User createAccount(EmployeeDTO employeeDTO) {
        return null;
    }

    @Override
    public TResult<String> generateUniversalLink(EmployeeDTO employeeDTO) {
        // save information employee for retry send email

        //validate existed by email
        if (StringUtils.isEmpty(employeeDTO.getEmailAddress())) {
            throw new BadRequestAlertException("Email can not be null.", "EmployeeDto", ERR_EMAIL_EMPTY);
        }

        Optional<Employee> employeeOptional = employeeRepository.findByEmailAddress(employeeDTO.getEmailAddress());
        if (employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Email has existed.", "EmployeeDto", ERR_EMAIL_EXISTED);
        }
        // generate universal link
        return null;
    }
}
