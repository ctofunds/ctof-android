package com.ctofunds.android.service;

import com.ctof.sms.api.Code;

import java.util.List;

/**
 * Created by qianhao.zhou on 12/24/15.
 */
public interface CodeService {

    boolean isReady();

    void reload(List<Code> codes);

    Code getDomain();

    Code getRunningStatus();

    Code getInvestmentStatus();

    Code getReviewStatus();

    Code getCategory();

    Code getManagementSkill();

    Code getPermission();

    Code getPostStatus();

    Code getStaffMember();

    Code getCity();
}
