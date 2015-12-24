package com.ctofunds.android.service.impl;

import android.content.Context;

import com.ctof.sms.api.Code;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.serializer.Serializer;
import com.ctofunds.android.service.BaseService;
import com.ctofunds.android.service.CodeService;

import java.util.List;

/**
 * Created by qianhao.zhou on 12/24/15.
 */
public class CodeServiceImpl extends BaseService implements CodeService {

    public CodeServiceImpl(Context context) {
        super(context);
    }

    @Override
    protected Serializer getSerializer() {
        return SmsApplication.getSerializer();
    }

    @Override
    public boolean isReady() {
        return !isEmpty();
    }

    @Override
    public void reload(List<Code> codes) {
        for (Code code: codes) {
            putObject(code.getName(), code);
        }
    }

    @Override
    public Code getDomain() {
        return getCodeSystem("domain");
    }

    @Override
    public Code getRunningStatus() {
        return getCodeSystem("runningStatus");
    }

    @Override
    public Code getInvestmentStatus() {
        return getCodeSystem("investmentStatus");
    }

    @Override
    public Code getReviewStatus() {
        return getCodeSystem("reviewStatus");
    }

    @Override
    public Code getCategory() {
        return getCodeSystem("category");
    }

    @Override
    public Code getManagementSkill() {
        return getCodeSystem("managementSkill");
    }

    @Override
    public Code getPermission() {
        return getCodeSystem("permission");
    }

    @Override
    public Code getPostStatus() {
        return getCodeSystem("domain");
    }

    @Override
    public Code getStaffMember() {
        return getCodeSystem("postStatus");
    }

    @Override
    public Code getCity() {
        return getCodeSystem("city");
    }

    private Code getCodeSystem(String name) {
        return getObject(name, Code.class);
    }
}
