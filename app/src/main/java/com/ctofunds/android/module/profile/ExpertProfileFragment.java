package com.ctofunds.android.module.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ctof.sms.api.Code;
import com.ctof.sms.api.Expert;
import com.ctof.sms.api.Startup;
import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.service.CodeService;
import com.ctofunds.android.utility.Environment;
import com.ctofunds.android.widget.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class ExpertProfileFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        long id = getArguments().getLong("id", -1);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_expert_profile, null);
        if (id > 0L) {
            ApiHandler.get(String.format(ApiConstants.EXPERT, id), Expert.class, new Response.Listener<Expert>() {
                @Override
                public void onResponse(Expert response) {
                    updateInfo(inflater, root, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToast(error.getMessage());
                }
            });
        } else {
            showToast(R.string.invalid_user);
        }
        return root;
    }

    private void updateInfo(LayoutInflater inflater, ViewGroup root, Expert expertAccount) {
        CodeService codeService = SmsApplication.getCodeService();
        Code cityCode = codeService.getCity();
        Code expertiseCode = codeService.getCategory();
        ((CircleImageView) root.findViewById(R.id.avatar)).setImageUrl(expertAccount.getAvatar(), SmsApplication.getImageLoader());
        NetworkImageView cover = (NetworkImageView) root.findViewById(R.id.cover);
        cover.getLayoutParams().height = Environment.getInstance().screenWidthPixels() * 3 / 5;
        root.requestLayout();
        if (expertAccount.getCoverImage() != null) {
            cover.setImageUrl(expertAccount.getCoverImage(), SmsApplication.getImageLoader());
        } else {
            cover.setDefaultImageResId(R.drawable.expert_cover);
        }
        ((TextView) root.findViewById(R.id.company)).setText(expertAccount.getCompany());
        ((TextView) root.findViewById(R.id.name)).setText(expertAccount.getName());
        ((TextView) root.findViewById(R.id.location)).setText(cityCode.getMapping().get(expertAccount.getCity() + ""));
        ((TextView) root.findViewById(R.id.cto_coins)).setText(expertAccount.getCtoCoins() + "");
        ((TextView) root.findViewById(R.id.reputation)).setText(expertAccount.getReputation() + "");
        ((TextView) root.findViewById(R.id.review_count)).setText(expertAccount.getViewCount() + "");
        ((TextView) root.findViewById(R.id.replied_topic_count)).setText(expertAccount.getReplyCount() + "");
        ((TextView) root.findViewById(R.id.accepted_topic_count)).setText(expertAccount.getAcceptedReplyCount() + "");
        ((TextView) root.findViewById(R.id.name_label)).setText(expertAccount.getName());
        ((TextView) root.findViewById(R.id.description)).setText(expertAccount.getDescription());

        LinearLayout expertiseContainer = (LinearLayout) root.findViewById(R.id.expertise_container);
        Map<String, String> expertiseMapping = expertiseCode.getMapping();
        List<Integer> expertiseList = expertAccount.getExpertise();
        final int expertiseCount = expertiseList.size();
        for (int i = 0; i < expertiseCount; i++) {
            String expertise = expertiseMapping.get(expertiseList.get(i).toString());
            inflater.inflate(R.layout.item_expertise, expertiseContainer, true);
            ((TextView) expertiseContainer.getChildAt(i)).setText(expertise);
        }

        initStartupList(inflater, root, codeService, expertAccount);
    }

    private void initStartupList(LayoutInflater inflater, ViewGroup root, CodeService codeService, Expert expertAccount) {
        List<Startup> startups = expertAccount.getHelpedStartups();
        LinearLayout startupContainer = (LinearLayout) root.findViewById(R.id.startup_list);
        Code domain = codeService.getDomain();
        Code investmentStatus = codeService.getInvestmentStatus();
        if (startups != null) {
            ((TextView) root.findViewById(R.id.count)).setText(startups.size() + "");
            for (int i = 0; i < startups.size(); i++) {
                Startup startup = startups.get(i);
                inflater.inflate(R.layout.item_startup, startupContainer, true);
                ViewGroup startupView = (ViewGroup) startupContainer.getChildAt(i);
                NetworkImageView logo = (NetworkImageView) startupView.findViewById(R.id.startup_logo);
                logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
                logo.setDefaultImageResId(R.drawable.default_startup_logo);
                if (startup.getLogo() != null) {
                    logo.setImageUrl(startup.getLogo(), SmsApplication.getImageLoader());
                }
                ((TextView) startupView.findViewById(R.id.startup_name)).setText(startup.getName());
                ((TextView) startupView.findViewById(R.id.domain)).setText(domain.getMapping().get(startup.getDomain().toString()));
                ((TextView) startupView.findViewById(R.id.investment_status)).setText(investmentStatus.getMapping().get(startup.getInvestmentStatus().toString()));
                ((TextView) startupView.findViewById(R.id.startup_location)).setText(codeService.getCity().getMapping().get(startup.getCity().toString()));
            }
        }
    }
}
