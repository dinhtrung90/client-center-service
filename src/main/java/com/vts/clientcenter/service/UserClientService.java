package com.vts.clientcenter.service;

import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.service.dto.EmployeeDTO;
import com.vts.clientcenter.service.dto.TResult;

public interface UserClientService {
    User createAccount(EmployeeDTO employeeDTO);
    TResult<String> generateUniversalLink(EmployeeDTO employeeDTO);
}
