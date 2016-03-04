package com.truedev.neon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.truedev.neon.Fragments.CameraPriorityFragment2;
import com.truedev.neon.Model.ImageModel;
import com.truedev.neon.Model.ImageTagsModel;
import com.truedev.R;
import com.truedev.neon.Utils.CommonUtils;
import com.truedev.neon.Utils.Constants;
import com.truedev.neon.Utils.PhotoParams;

import java.util.ArrayList;

/**
 * Created by lakshaygirdhar on 24/12/15.
 */
public class CameraHolderActivity extends BaseActivityGallery implements CameraPriorityFragment2.PictureTakenListener {

    private static final String TAG = "CameraHolderActivity";
    private TextView tvTag;
    private int currentTag = 0;
    private TranslateAnimation mAnimation;
    private TextView tvMessage;
    private ArrayList<ImageTagsModel> mTagsModels;
    private ArrayList<ImageModel> mImagesCollected = new ArrayList<>();
    private boolean withTags = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_holder);

        tvTag = (TextView) findViewById(R.id.tvTag);

        mTagsModels = (ArrayList<ImageTagsModel>) getIntent().getExtras().getSerializable(Constants.IMAGE_TAGS);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        if(mTagsModels == null || mTagsModels.size() == 0){
            withTags = false;
            findViewById(R.id.rlTagBackground).setVisibility(View.GONE);
            PhotoParams params = new PhotoParams();
            params.setOrientation(PhotoParams.CameraOrientation.LANDSCAPE);
            CameraPriorityFragment2 fragment = CameraPriorityFragment2.getInstance(params,this);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,fragment).commit();
        } else {
            withTags = true;
            findViewById(R.id.rlTagBackground).setVisibility(View.VISIBLE);
            tvTag.setText(mTagsModels.get(currentTag).getTag_name());

            mAnimation = new TranslateAnimation(CommonUtils.getScreenWidth(this),0,0,0);
            mAnimation.setDuration(Constants.STANDARD_ANIMATION_DURATION);

            PhotoParams params = new PhotoParams();
            params.setOrientation(PhotoParams.CameraOrientation.LANDSCAPE);
            CameraPriorityFragment2 fragment = CameraPriorityFragment2.getInstance(params,this);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,fragment).commit();
        }
    }

    @Override
    public void onPictureTaken(String filePath) {

        if(withTags){
            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(filePath);
            imageModel.setTagName(mTagsModels.get(currentTag).getTag_name());
            imageModel.setTagsModel(mTagsModels.get(currentTag));
            mImagesCollected.add(imageModel);
            if(currentTag < mTagsModels.size()-1)
                animateTag(++currentTag);
            else {
                Intent intent = new Intent();
                intent.putExtra(Constants.RESULT_IMAGES, mImagesCollected);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(filePath);
            mImagesCollected.add(imageModel);
        }
    }

    private void animateTag(int tag) {
        ImageTagsModel model = mTagsModels.get(tag);
        tvTag.setText(model.getTag_name());
        tvTag.startAnimation(mAnimation);
    }

    @Override
    public void onPicturesCompleted() {
        Intent intent = new Intent();
        intent.putExtra(Constants.RESULT_IMAGES, mImagesCollected);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_PERM_CAMERA:
                break;

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.RESULT_IMAGES, mImagesCollected);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
