package com.ctofunds.android.module.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.Code;
import com.ctof.sms.api.Expert;
import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.widget.CircleImageView;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class ExpertProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        long id = getArguments().getLong("id");
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_expert_profile, null);
        ApiHandler.get(String.format(ApiConstants.EXPERT, id), Expert.class, new Response.Listener<Expert>() {
            @Override
            public void onResponse(Expert response) {
                updateInfo(root, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.getMessage());
            }
        });
        return root;
    }

    private void updateInfo(ViewGroup root, Expert expertAccount) {
        Code city = SmsApplication.getCodeService().getCity();
        ((CircleImageView) root.findViewById(R.id.avatar)).setImageUrl(expertAccount.getAvatar(), SmsApplication.getImageLoader());
        ((TextView) root.findViewById(R.id.company)).setText(expertAccount.getCompany());
        ((TextView) root.findViewById(R.id.name)).setText(expertAccount.getName());
        ((TextView) root.findViewById(R.id.location)).setText(city.getMapping().get(expertAccount.getCity() + ""));
        ((TextView) root.findViewById(R.id.cto_coins)).setText(expertAccount.getCtoCoins() + "");
        ((TextView) root.findViewById(R.id.reputation)).setText(expertAccount.getReputation() + "");
        ((TextView) root.findViewById(R.id.review_count)).setText(expertAccount.getViewCount() + "");
    }
}
