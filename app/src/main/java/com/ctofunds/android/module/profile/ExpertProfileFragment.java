package com.ctofunds.android.module.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ctof.sms.api.Code;
import com.ctof.sms.api.Expert;
import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.widget.CircleImageView;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class ExpertProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Expert expertAccount = SmsApplication.getAccountService().getExpertAccount();
        Code city = SmsApplication.getCodeService().getCity();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_expert_profile, null);
        ((CircleImageView) root.findViewById(R.id.avatar)).setImageUrl(expertAccount.getAvatar(), SmsApplication.getImageLoader());
        ((TextView) root.findViewById(R.id.company)).setText(expertAccount.getCompany());
        ((TextView) root.findViewById(R.id.name)).setText(expertAccount.getName());
        ((TextView) root.findViewById(R.id.location)).setText(city.getMapping().get(expertAccount.getCity() + ""));
        return root;
    }
}
