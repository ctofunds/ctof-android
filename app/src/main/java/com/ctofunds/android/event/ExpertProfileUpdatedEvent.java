package com.ctofunds.android.event;

import com.ctof.sms.api.Expert;

/**
 * Created by qianhao.zhou on 1/4/16.
 */
public final class ExpertProfileUpdatedEvent {

    private final Expert expert;

    public ExpertProfileUpdatedEvent(Expert expert) {
        this.expert = expert;
    }

    public Expert getExpert() {
        return expert;
    }
}
