package com.truedev.neon.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.truedev.R;
import com.truedev.neon.Utils.Constants;
import com.truedev.neon.Utils.PhotoParams;

/**
 * Created by Lakshay on 10-08-2015.
 */
public class ReviewImageActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString(Constants.IMAGE_PATH);
        PhotoParams.CameraOrientation portrait = (PhotoParams.CameraOrientation) extras.getSerializable(Constants.ORIENTATION_REVIEW);

        if (portrait.equals(PhotoParams.CameraOrientation.PORTRAIT)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (portrait.equals(PhotoParams.CameraOrientation.LANDSCAPE)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        setContentView(R.layout.review_image_activity);
        ImageView ivReview = (ImageView) findViewById(R.id.ivReviewImage);
        ImageView bCancel = (ImageView) findViewById(R.id.bCancel);
        bCancel.setOnClickListener(this);
        ImageView bDone = (ImageView) findViewById(R.id.bDone);
        bDone.setOnClickListener(this);

        if (imagePath != null) {
            Glide.with(this).load("file://" + imagePath).into(ivReview);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bDone) {
            setResult(RESULT_OK);
            finish();
        } else if (id == R.id.bCancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
