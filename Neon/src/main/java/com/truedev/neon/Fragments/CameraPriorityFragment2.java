package com.truedev.neon.Fragments;

/**
 * Created by Lakshay on 02-09-2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.truedev.neon.Activity.CameraPriorityActivity;
import com.truedev.neon.Activity.GalleryActivity;
import com.truedev.neon.Activity.ReviewImageActivity;
import com.truedev.R;
import com.truedev.neon.Utils.Constants;
import com.truedev.neon.Utils.DrawingView;
import com.truedev.neon.Utils.FileInfo;
import com.truedev.neon.Utils.PhotoParams;
import com.truedev.neon.Utils.Preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraPriorityFragment2 extends Fragment implements View.OnClickListener, View.OnTouchListener, Camera.PictureCallback {

    protected static final String TAG = "CameraPriorityFragment";
    protected static final int REQUEST_REVIEW = 100;
    protected static final int GALLERY_PICK = 99;

    protected int FOCUS_AREA_SIZE = 200;
    protected Activity mActivity;
    protected ProgressBar progressBar;
    protected PhotoParams mPhotoParams;
    protected String imageName;
    protected int maxNumberOfImages;
    protected DrawingView drawingView;
    protected ImageView buttonCapture;
    protected ImageView buttonGallery;
    protected ImageView buttonDone;
    protected TextView tvImageName;
    protected LinearLayout scrollView;
    protected ArrayList<FileInfo> imagesList = new ArrayList<>();
    protected String capturedFilePath = "";
    protected Camera mCamera;
    protected boolean readyToTakePicture = false;
    protected FrameLayout mCameraLayout;
    protected View fragmentView;
    protected PictureTakenListener mPictureTakenListener;
    private Preview preview;
    private Display display;
    private SurfaceView surfaceView;

    public interface PictureTakenListener{
        public void onPictureTaken(String filePath);
        public void onPicturesCompleted();
    }


    public static CameraPriorityFragment2 getInstance(PhotoParams photoParams,PictureTakenListener pictureTakenListener) {
        CameraPriorityFragment2 fragment = new CameraPriorityFragment2();
        Bundle extras = new Bundle();
        extras.putSerializable(Constants.PHOTO_PARAMS, photoParams);
        fragment.mPictureTakenListener = pictureTakenListener;
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = LayoutInflater.from(mActivity).inflate(R.layout.camera_priority_fragment_2, null);
        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);

        mPhotoParams = (PhotoParams) getArguments().getSerializable(Constants.PHOTO_PARAMS);
        imageName = mPhotoParams.getImageName();
        maxNumberOfImages = mPhotoParams.getNoOfPhotos();
        PhotoParams.CameraOrientation orientation = mPhotoParams.getOrientation();

        //View to add rectangle on tap to focus
        drawingView = new DrawingView(mActivity);

        setOrientation(mActivity, orientation);
        surfaceView=(SurfaceView)fragmentView.findViewById(R.id.surfaceView);
        preview = new Preview(getActivity(),surfaceView );
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) fragmentView.findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
        buttonCapture = (ImageView) fragmentView.findViewById(R.id.buttonCapture);
        buttonGallery = (ImageView) fragmentView.findViewById(R.id.buttonGallery);
        buttonDone = (ImageView) fragmentView.findViewById(R.id.buttonDone);
        tvImageName = (TextView) fragmentView.findViewById(R.id.imageName);

        scrollView = (LinearLayout) fragmentView.findViewById(R.id.imageHolderView);

        //for handling screen orientation
        if (savedInstanceState != null) {
            Log.e(Constants.TAG, "savedInstanceState not null");
            imagesList = (ArrayList<FileInfo>) savedInstanceState.getSerializable(Constants.IMAGES_SELECTED);
            addInScrollView(imagesList);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewGroup.LayoutParams layoutParamsDrawing
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);

        mActivity.addContentView(drawingView, layoutParamsDrawing);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_PICK) {
                imagesList = (ArrayList<FileInfo>) data.getSerializableExtra(GalleryActivity.GALLERY_SELECTED_PHOTOS);
                if (imagesList != null) {
                    buttonCapture.setTag("done");
                    onClick(buttonCapture);
                }
            } else if (requestCode == REQUEST_REVIEW) {
                mPictureTakenListener.onPictureTaken(capturedFilePath);
            }
        } else {
            if (requestCode == REQUEST_REVIEW) {
                readyToTakePicture = true;
                buttonCapture.setEnabled(true);
            } else if (requestCode == GALLERY_PICK) {
                return;
            } else if (requestCode != 101) {
                mActivity.setResult(resultCode);
                mActivity.finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {
                int numCams = Camera.getNumberOfCameras();
                if(numCams > 0){
                    try{
                        mCamera = Camera.open(0);
                        mCamera.startPreview();
                        preview.setCamera(mCamera);
                    } catch (RuntimeException ex){
                        Toast.makeText(getActivity(), getString(R.string.takeFromCamera), Toast.LENGTH_LONG).show();
                    }
                }

                preview.setReadyListener(new Preview.ReadyToTakePicture() {
                    @Override
                    public void readyToTakePicture(boolean ready) {
                        readyToTakePicture = ready;
                    }
                });
                preview.setOnTouchListener(this);
                //To set hardware camera rotation
                setCameraRotation();



                mCameraLayout = (FrameLayout) fragmentView.findViewById(R.id.camera_preview);

                //set the screen layout to fullscreen
                mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                buttonCapture.setOnClickListener(this);
                enableDoneButton(false);
                buttonGallery.setOnClickListener(this);

            } catch (Exception e) {
                Log.e("Camera Open Exception", "" + e.getMessage());
            }

            //This was put in handler to make sure that name appears only after animation ends
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (maxNumberOfImages == 0) {
                        buttonDone.setVisibility(View.VISIBLE);
                        buttonDone.setOnClickListener(CameraPriorityFragment2.this);
                    }
                    if (mPhotoParams.getImageName() != null && !"".equals(mPhotoParams.getImageName())) {
                        tvImageName.setVisibility(View.VISIBLE);
                        tvImageName.setText(mPhotoParams.getImageName() + "");
                        tvImageName.setOnClickListener(CameraPriorityFragment2.this);
                    }
                }
            }, 1000);

        } else {
            enableDoneButton(false);
            Log.e(TAG, "camera not null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCamera != null) {
            mCamera.stopPreview();
            preview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            mCamera.setPreviewCallback(null);
           // mCameraPreview.getHolder().removeCallback(mCameraPreview);
            mCamera.stopPreview();
            mCameraLayout.removeAllViews();
            mCamera.release();
            mCamera = null;
            //mCameraPreview = null;
            releaseCameraAndPreview();
        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
        }

    }
    private void releaseCameraAndPreview() {
        preview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    public void onClick(View v) {

        //Toast.makeText(this, (v.getId()==R.id.bDone ? "done": "capture"), Toast.LENGTH_SHORT).show();
        if (v.getId() == R.id.buttonCapture) {
            if (readyToTakePicture) {
                mCamera.takePicture(null, null, this);
                readyToTakePicture = false;
                //llActionsCamera.setEnabled(false);
                buttonCapture.setEnabled(true);
            }
        } else if (v.getId() == R.id.buttonGallery) {
            //finish();
            Intent intent = new Intent(mActivity, GalleryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(GalleryActivity.MAX_COUNT, maxNumberOfImages);
            intent.putExtra(CameraPriorityActivity.FROM_PRIORITY_ACTIVITY, true);
            startActivityForResult(intent, GALLERY_PICK);

        } else if (v.getId() == R.id.buttonDone) {
            mPictureTakenListener.onPicturesCompleted();
        }
    }
    private void resetCam() {
        mCamera.startPreview();
        preview.setCamera(mCamera);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mCamera != null) {
//            Camera camera = mCamera.getCamera();
            mCamera.cancelAutoFocus();
            final Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);

            Camera.Parameters parameters = mCamera.getParameters();
            setCameraRotation();
            if (parameters.getFocusMode() != Camera.Parameters.FOCUS_MODE_AUTO) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                mylist.add(new Camera.Area(focusRect, 1000));
                parameters.setFocusAreas(mylist);
            }

            try {
                mCamera.cancelAutoFocus();
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                    }
                });

                drawingView.setHaveTouch(true, focusRect);
                drawingView.invalidate();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawingView.setHaveTouch(false, focusRect);
                        drawingView.invalidate();
                    }
                }, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        new ImagePostProcessing(mActivity, data).execute();
    }

    protected class ImagePostProcessing extends AsyncTask<Void, Void, File> {

        protected Context context;
        protected byte[] data;
        protected ProgressDialog progressDialog;

        public ImagePostProcessing(Context context, byte[] data) {
            this.context = context;
            this.data = data;
        }

        @Override
        protected File doInBackground(Void... params) {
            File pictureFile = Constants.getMediaOutputFile(Constants.TYPE_IMAGE);
            Log.e(TAG, pictureFile.getAbsolutePath());
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return null;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                Uri pictureFileUri = Uri.parse("file://" + pictureFile.getAbsolutePath());
                mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        pictureFileUri));
                mCamera.startPreview();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }


            return pictureFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, null, "Saving Picture", true);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();
                if (file != null) {
                    capturedFilePath = file.getPath();
                    Intent intent = new Intent(mActivity, ReviewImageActivity.class);
                    intent.putExtra(Constants.IMAGE_PATH, file.getPath());
                    intent.putExtra(Constants.ORIENTATION_REVIEW, mPhotoParams.getOrientation());
                    startActivityForResult(intent, REQUEST_REVIEW);
                    mCamera.startPreview();
                } else {
                    Toast.makeText(context, "Camera Error. Kindly try again", Toast.LENGTH_SHORT).show();
                    readyToTakePicture = true;
                    buttonCapture.setEnabled(true);
                    mCamera.startPreview();
                }
            }catch (Exception e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    }

    //updates the listview with the photos clicked by the camera
    protected void updateCapturedPhotos(File pictureFile) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(pictureFile.getAbsolutePath());
        fileInfo.setFileName(pictureFile.getAbsolutePath().substring(pictureFile.getAbsolutePath().lastIndexOf("/") + 1));
        fileInfo.setSource(FileInfo.SOURCE.PHONE_CAMERA);
        imagesList.add(fileInfo);
        if (maxNumberOfImages == 1) {
            buttonCapture.setTag("done");
            onClick(buttonCapture);
        } else {
            Log.e(Constants.TAG, "updateCapturedPhotos");
            if (imagesList.size() >= 1)
                scrollView.setVisibility(View.VISIBLE);
            else
                scrollView.setVisibility(View.GONE);
            addInScrollView(fileInfo);

            if (maxNumberOfImages > 0)
                updateView(imagesList.size() < maxNumberOfImages);
            mCamera.startPreview();
            readyToTakePicture = true;
            buttonCapture.setEnabled(true);
        }
    }


    protected void setCameraRotation() {
        //STEP #1: Get rotation degrees
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);
        mCamera.setParameters(params);
    }

    //It is called when configuration(orientation) of screen changes
    protected void addInScrollView(ArrayList<FileInfo> infos) {
        if (infos != null && infos.size() > 0) {
            for (FileInfo info : infos) {
                scrollView.addView(createImageView(info));
            }
            scrollView.setVisibility(View.VISIBLE);
        }
        Log.e(Constants.TAG, "Add multiple items in scroll ");
    }

    protected void addInScrollView(FileInfo info) {
        Log.e(Constants.TAG, " add in scroll View ");
        scrollView.addView(createImageView(info));
        scrollView.setVisibility(View.VISIBLE);
    }

    protected View createImageView(final FileInfo info) {
        final File file = new File(info.getFilePath());
        if (!file.exists())
            return null;
        final View outerView = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.camera_priority_overlay, null);
        outerView.findViewById(R.id.ivRemoveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.removeView(outerView);
                imagesList.remove(info);
                if (maxNumberOfImages > 0)
                    updateView(imagesList.size() < maxNumberOfImages);
                if (imagesList.size() < 1)
                    scrollView.setVisibility(View.GONE);
            }
        });

        Glide.with(this).load("file://" + info.getFilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .placeholder(R.drawable.image_load_default_small)
                .into((ImageView) outerView.findViewById(R.id.ivCaptured));/**/
        return outerView;
    }

    protected void setOrientation(Activity activity, PhotoParams.CameraOrientation orientation) {
        if (orientation != null) {
            if (orientation.equals(PhotoParams.CameraOrientation.LANDSCAPE)) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (orientation.equals(PhotoParams.CameraOrientation.PORTRAIT)) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            Log.e(Constants.TAG, "No orientation set");
        }
    }

    protected void updateView(boolean status) {
        enableDoneButton(!status);
        tvImageName.setText(status ? imageName : "Press Done");
    }

    protected void enableDoneButton(boolean enable) {
        buttonCapture.setImageResource(enable ? R.drawable.camera_ok : R.drawable.camera_click);
        buttonCapture.setTag(enable ? "done" : "capture");
    }

    protected Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(FOCUS_AREA_SIZE * coefficient).intValue();

        int left = clamp((int) x - areaSize / 2, 0, surfaceView.getWidth() - areaSize);
        int top = clamp((int) y - areaSize / 2, 0, surfaceView.getHeight() - areaSize);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
       // matrix.mapRect(rectF);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    protected int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }


}

