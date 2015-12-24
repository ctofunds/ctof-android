package com.ctofunds.android.service.impl;

import android.content.Context;

import com.ctof.sms.api.Employee;
import com.ctof.sms.api.Expert;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.serializer.Serializer;
import com.ctofunds.android.service.AccountService;
import com.ctofunds.android.service.BaseService;

/**
 * Created by qianhao.zhou on 12/22/15.
 */
public class AccountServiceImpl extends BaseService implements AccountService {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPERT = "expert";
    private static final String KEY_EMPLOYEE = "employee";

    public AccountServiceImpl(Context context) {
        super(context);
    }

    @Override
    protected Serializer getSerializer() {
        return SmsApplication.getSerializer();
    }

    @Override
    public String getToken() {
        return get(KEY_TOKEN);
    }

    @Override
    public void setToken(String token) {
        put(KEY_TOKEN, token);
    }

    @Override
    public boolean hasLogin() {
        return getToken() != null;
    }

    @Override
    public void clearAccount() {
        removeAll();
    }

    @Override
    public Expert getExpertAccount() {
        return getObject(KEY_EXPERT, Expert.class);
    }

    @Override
    public void setExpertAccount(Expert expert) {
        putObject(KEY_EXPERT, expert);
    }

    @Override
    public Employee getEmployeeAccount() {
        return getObject(KEY_EMPLOYEE, Employee.class);
    }

    @Override
    public void setEmployeeAccount(Employee employee) {
        putObject(KEY_EMPLOYEE, employee);
    }

}
