package com.ctofunds.android.service;

import com.ctof.sms.api.Employee;
import com.ctof.sms.api.Expert;

/**
 * Created by qianhao.zhou on 12/22/15.
 */
public interface AccountService {

    String getToken();

    void setToken(String token);

    boolean hasLogin();

    void clearAccount();

    Expert getExpertAccount();

    void setExpertAccount(Expert expert);

    Employee getEmployeeAccount();

    void setEmployeeAccount(Employee employee);

}
