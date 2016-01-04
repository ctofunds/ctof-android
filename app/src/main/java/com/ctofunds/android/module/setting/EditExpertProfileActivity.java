package com.ctofunds.android.module.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ctof.sms.api.Code;
import com.ctof.sms.api.Expert;
import com.ctof.sms.api.UpdateExpertRequest;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.event.ExpertProfileUpdatedEvent;
import com.ctofunds.android.exception.ImageUploaderException;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.utility.Environment;
import com.ctofunds.android.utility.ImageUploader;
import com.ctofunds.android.utility.ImageUtils;
import com.ctofunds.android.widget.CircleImageView;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qianhao.zhou on 12/30/15.
 */
public class EditExpertProfileActivity extends BaseActivity {

    private static final int SELECT_COVER_FROM_CAMERA = 1;
    private static final int SELECT_COVER_FROM_ALBUM = 2;
    private static final int SELECT_AVATAR_FROM_CAMERA = 3;
    private static final int SELECT_AVATAR_FROM_ALBUM = 4;

    private static final int GRID_ITEM_COUNT_PER_ROW = 4;

    private Expert expert;

    private String avatar;
    private String coverImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expert_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.complete_profile);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expert = SmsApplication.getAccountService().getExpertAccount();
        if (expert != null) {
            final NetworkImageView cover = (NetworkImageView) findViewById(R.id.cover);
            cover.getLayoutParams().height = com.ctofunds.android.utility.Environment.getInstance().screenWidthPixels() * 3 / 5;
            cover.getRootView().requestLayout();
            if (expert.getCoverImage() != null) {
                cover.setImageUrl(ImageUtils.getCoverUrl(expert.getCoverImage()), SmsApplication.getImageLoader());
            } else {
                cover.setDefaultImageResId(R.drawable.expert_cover);
            }
            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditExpertProfileActivity.this);
                    builder.setTitle(R.string.set_expert_cover).setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = getTempFile();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, SELECT_COVER_FROM_CAMERA);
                        }
                    }).setNegativeButton(R.string.album, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_COVER_FROM_ALBUM);
                        }
                    }).setCancelable(true).show();

                }
            });
            final BiMap<String, String> cities = HashBiMap.create(SmsApplication.getCodeService().getCity().getMapping());
            findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((EditText) findViewById(R.id.name)).getText().toString().trim();
                    if (name.length() == 0) {
                        showToast(R.string.missing_name);
                        return;
                    }
                    final UpdateExpertRequest updateExpertRequest = new UpdateExpertRequest();
                    updateExpertRequest.setAvatar(avatar);
                    updateExpertRequest.setCoverImage(coverImage);
                    updateExpertRequest.setName(name);
                    String company = ((EditText) findViewById(R.id.company)).getText().toString().trim();
                    if (company.length() > 0) {
                        updateExpertRequest.setCompany(company);
                    }
                    String position = ((EditText) findViewById(R.id.position)).getText().toString().trim();
                    if (position.length() > 0) {
                        updateExpertRequest.setPosition(position);
                    }
                    String location = ((EditText) findViewById(R.id.location)).getText().toString().trim();
                    if (location.length() > 0) {
                        String cityId = cities.inverse().get(location);
                        if (cityId != null) {
                            updateExpertRequest.setCity(Integer.parseInt(cityId));
                        }
                    }
                    ViewGroup expertiseContainer = (ViewGroup) findViewById(R.id.expertise);
                    ArrayList<Integer> expertise = Lists.newArrayList();
                    for (int i = 0, size = expertiseContainer.getChildCount(); i < size; ++i) {
                        View child = expertiseContainer.getChildAt(i);
                        if (child.isSelected()) {
                            expertise.add((Integer) child.getTag());
                        }
                    }
                    updateExpertRequest.setExpertise(expertise);
                    ViewGroup managementSkillContainer = (ViewGroup) findViewById(R.id.management_skill);
                    for (int i = 0, size = managementSkillContainer.getChildCount(); i < size; ++i) {
                        View child = managementSkillContainer.getChildAt(i);
                        if (child.isSelected()) {
                            updateExpertRequest.setManagementSkill((Integer) child.getTag());
                            break;
                        }
                    }
                    String description = ((EditText) findViewById(R.id.description)).getText().toString().trim();
                    if (description.length() > 0) {
                        updateExpertRequest.setDescription(description);
                    }
                    showProgressDialog(R.string.wait_tips);
                    ApiHandler.put(String.format(ApiConstants.EXPERT, expert.getId()), updateExpertRequest, Expert.class, new Response.Listener<Expert>() {
                        @Override
                        public void onResponse(Expert response) {
                            SmsApplication.getAccountService().setExpertAccount(response);
                            dismissProgressDialog();
                            showToast(R.string.setting_updated);
                            SmsApplication.getEventBus().post(new ExpertProfileUpdatedEvent(response));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissProgressDialog();
                        }
                    });
                }
            });
            if (expert.getAvatar() != null) {
                ((CircleImageView) findViewById(R.id.avatar)).setImageUrl(ImageUtils.getAvatarUrl(expert.getAvatar()), SmsApplication.getImageLoader());
            }
            findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditExpertProfileActivity.this);
                    builder.setTitle(R.string.set_avatar).setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = getTempFile();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, SELECT_AVATAR_FROM_CAMERA);
                        }
                    }).setNegativeButton(R.string.album, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_AVATAR_FROM_ALBUM);
                        }
                    }).setCancelable(true).show();
                }
            });
            ((EditText) findViewById(R.id.name)).setText(expert.getName());
            ((EditText) findViewById(R.id.company)).setText(expert.getCompany());
            ((EditText) findViewById(R.id.position)).setText(expert.getPosition());
            ((EditText) findViewById(R.id.description)).setText(expert.getDescription());

            LayoutInflater layoutInflater = getLayoutInflater();
            initCitySelector(expert);
            initExpertiseLayout(expert, layoutInflater);
            initManagementSkillLayout(expert, layoutInflater);
        }
    }

    private void initCitySelector(Expert expert) {
        final Map<String, String> cityBiMap = SmsApplication.getCodeService().getCity().getMapping();
        final CharSequence[] cities = Lists.newArrayList(cityBiMap.values()).toArray(new CharSequence[0]);
        final EditText locationEditor = (EditText) findViewById(R.id.location);
        if (expert.getCity() != null) {
            locationEditor.setText(cityBiMap.get(expert.getCity().toString()));
        }
        locationEditor.setFocusable(false);
        locationEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditExpertProfileActivity.this);
                builder.setTitle(R.string.location).setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String city = cities[which].toString();
                        locationEditor.setText(city);
                    }
                }).show();
            }
        });
    }

    private void initExpertiseLayout(Expert expert, LayoutInflater layoutInflater) {
        GridLayout expertiseContainer = (GridLayout) findViewById(R.id.expertise);
        expertiseContainer.setColumnCount(GRID_ITEM_COUNT_PER_ROW);
        Code domain = SmsApplication.getCodeService().getCategory();
        final int width = Environment.getInstance().screenWidthPixels() / GRID_ITEM_COUNT_PER_ROW;
        List<Integer> expertise = expert.getExpertise();
        for (Map.Entry<String, String> entry : domain.getMapping().entrySet()) {
            TextView view = (TextView) layoutInflater.inflate(R.layout.item_grid_selectable, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });
            Integer id = Integer.parseInt(entry.getKey());
            view.setSelected(expertise != null && expertise.contains(id));
            view.setTag(id);
            view.setText(entry.getValue());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = width;
            expertiseContainer.addView(view, params);
        }
        int toFill = GRID_ITEM_COUNT_PER_ROW - domain.getMapping().size() % GRID_ITEM_COUNT_PER_ROW;
        if (toFill < GRID_ITEM_COUNT_PER_ROW) {
            for (int i = 0; i < toFill; ++i) {
                TextView view = (TextView) layoutInflater.inflate(R.layout.item_grid_selectable, null);
                view.setEnabled(false);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = width;
                expertiseContainer.addView(view, params);
            }
        }
    }

    private void initManagementSkillLayout(Expert expert, LayoutInflater layoutInflater) {
        final GridLayout managementSkillContainer = (GridLayout) findViewById(R.id.management_skill);
        managementSkillContainer.setColumnCount(GRID_ITEM_COUNT_PER_ROW);
        Code domain = SmsApplication.getCodeService().getManagementSkill();
        final int width = Environment.getInstance().screenWidthPixels() / GRID_ITEM_COUNT_PER_ROW;
        for (Map.Entry<String, String> entry : domain.getMapping().entrySet()) {
            TextView view = (TextView) layoutInflater.inflate(R.layout.item_grid_selectable, null);
            Integer id = Integer.parseInt(entry.getKey());
            view.setSelected(id.equals(expert.getManagementSkill()));
            view.setTag(id);
            view.setText(entry.getValue());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = width;
            managementSkillContainer.addView(view, params);
        }
        final int size = managementSkillContainer.getChildCount();
        for (int i = 0; i < size; ++i) {
            View skill = managementSkillContainer.getChildAt(i);
            skill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!v.isSelected()) {
                        for (int j = 0; j < size; ++j) {
                            managementSkillContainer.getChildAt(j).setSelected(false);
                        }
                        v.setSelected(true);
                    }
                }
            });
        }

        int toFill = GRID_ITEM_COUNT_PER_ROW - domain.getMapping().size() % GRID_ITEM_COUNT_PER_ROW;
        if (toFill < GRID_ITEM_COUNT_PER_ROW) {
            for (int i = 0; i < toFill; ++i) {
                TextView view = (TextView) layoutInflater.inflate(R.layout.item_grid_selectable, null);
                view.setEnabled(false);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = width;
                managementSkillContainer.addView(view, params);
            }
        }
    }


    private static final File getTempFile() {
        return new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_COVER_FROM_CAMERA) {
                showProgressDialog(R.string.uploading);
                SmsApplication.getEventBus().post(new BackFromCameraEvent(getTempFile(), expert.getId(), COVER));
            } else if (requestCode == SELECT_COVER_FROM_ALBUM) {
                showProgressDialog(R.string.uploading);
                SmsApplication.getEventBus().post(new BackFromAlbumEvent(data.getData(), expert.getId(), COVER));
            } else if (requestCode == SELECT_AVATAR_FROM_ALBUM) {
                showProgressDialog(R.string.uploading);
                SmsApplication.getEventBus().post(new BackFromAlbumEvent(data.getData(), expert.getId(), AVATAR));
            } else if (requestCode == SELECT_AVATAR_FROM_CAMERA) {
                showProgressDialog(R.string.uploading);
                SmsApplication.getEventBus().post(new BackFromCameraEvent(getTempFile(), expert.getId(), COVER));
            }
        }
    }

    private static final int COVER = 1;
    private static final int AVATAR = 2;

    private static class PickEventFinish {
    }

    private static class BackFromAlbumEvent {
        private final Uri uri;
        private final long expertId;
        private final int type;

        private BackFromAlbumEvent(Uri uri, long expertId, int type) {
            this.uri = uri;
            this.expertId = expertId;
            this.type = type;
        }
    }

    private static class BackFromCameraEvent {
        private final File file;
        private final long expertId;
        private final int type;

        private BackFromCameraEvent(File file, long expertId, int type) {
            this.file = file;
            this.expertId = expertId;
            this.type = type;
        }
    }

    private static final class ImageUploadSuccessEvent {
        private final String url;
        private final int type;
        private final boolean succeed;

        public ImageUploadSuccessEvent(String url, int type, boolean succeed) {
            this.url = url;
            this.type = type;
            this.succeed = succeed;
        }
    }

    public void onEventMainThread(ImageUploadSuccessEvent imageUploadSuccessEvent) {
        if (imageUploadSuccessEvent.succeed) {
            if (imageUploadSuccessEvent.type == COVER) {
                ((NetworkImageView) findViewById(R.id.cover)).setImageUrl(ImageUtils.getCoverUrl(imageUploadSuccessEvent.url), SmsApplication.getImageLoader());
                coverImage = imageUploadSuccessEvent.url;
            } else if (imageUploadSuccessEvent.type == AVATAR) {
                ((CircleImageView) findViewById(R.id.avatar)).setImageUrl(ImageUtils.getAvatarUrl(imageUploadSuccessEvent.url), SmsApplication.getImageLoader());
                avatar = imageUploadSuccessEvent.url;
            }
        } else {
            showToast(R.string.upload_retry);
        }
    }

    public void onEventMainThread(PickEventFinish pickEventFinish) {
        dismissProgressDialog();
    }

    public void onEventAsync(final BackFromCameraEvent event) {
        File file = event.file;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
            byte[] imageData = baos.toByteArray();
            int length = imageData.length;
            Log.i(getTag(), file.getAbsolutePath() + " size:" + bitmap.getByteCount() + " compress to jpg size:" + length);
            ImageUploader.getInstance().upload(imageData,
                    ImageUploader.composeAvatarUrl(event.expertId, Bitmap.CompressFormat.JPEG.name().toLowerCase()),
                    new ImageUploader.Callback() {
                        @Override
                        public void onProgress(long current, long total) {
                        }

                        @Override
                        public void onSuccess(final String url) {
                            SmsApplication.getEventBus().post(new ImageUploadSuccessEvent(url, event.type, true));
                        }

                        @Override
                        public void onFailure(ImageUploaderException exception) {
                            SmsApplication.getEventBus().post(new ImageUploadSuccessEvent(null, event.type, false));
                        }
                    });
        } catch (Exception e) {
            Log.d(getTag(), "ImageCapturedEvent failed", e);
        } finally {
            SmsApplication.getEventBus().post(new PickEventFinish());
            try {
                file.delete();
            } catch (Exception e) {
                Log.e(getTag(), "error delete:" + file, e);
            }
        }
    }

    public void onEventAsync(final BackFromAlbumEvent event) {
        Cursor c = null;
        try {
            Log.i(getTag(), "async event type:" + event.getClass());
            final Uri selectedImage = event.uri;
            Log.d(getTag(), "image uri:" + selectedImage);
            String[] filePath = {MediaStore.Images.Media.DATA};
            c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            final String picturePath = c.getString(columnIndex);
            final Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
            byte[] imageData = baos.toByteArray();
            int length = imageData.length;
            Log.i(getTag(), picturePath + " size:" + bitmap.getByteCount() + " compress to jpg size:" + length);
            ImageUploader.getInstance().upload(imageData,
                    ImageUploader.composeAvatarUrl(event.expertId, Bitmap.CompressFormat.JPEG.name().toLowerCase()),
                    new ImageUploader.Callback() {
                        @Override
                        public void onProgress(long current, long total) {
                        }

                        @Override
                        public void onSuccess(final String url) {
                            SmsApplication.getEventBus().post(new ImageUploadSuccessEvent(url, event.type, true));
                        }

                        @Override
                        public void onFailure(ImageUploaderException exception) {
                            SmsApplication.getEventBus().post(new ImageUploadSuccessEvent(null, event.type, false));
                        }
                    });
        } finally {
            SmsApplication.getEventBus().post(new PickEventFinish());
            if (c != null) {
                c.close();
            }
        }

    }
}
