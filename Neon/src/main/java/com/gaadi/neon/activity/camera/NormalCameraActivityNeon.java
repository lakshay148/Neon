package com.gaadi.neon.activity.camera;

import com.gaadi.neon.PhotosLibrary;
import com.gaadi.neon.activity.ImageShow;
import com.gaadi.neon.enumerations.GalleryType;
import com.gaadi.neon.enumerations.ResponseCode;
import com.gaadi.neon.fragment.CameraFragment1;
import com.gaadi.neon.interfaces.ICameraParam;
import com.gaadi.neon.interfaces.IGalleryParam;
import com.gaadi.neon.interfaces.LivePhotoNextTagListener;
import com.gaadi.neon.interfaces.LivePhotosListener;
import com.gaadi.neon.interfaces.OnPermissionResultListener;
import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.model.NeonResponse;
import com.gaadi.neon.model.PhotosMode;
import com.gaadi.neon.util.AnimationUtils;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.ManifestPermission;
import com.gaadi.neon.util.NeonException;
import com.gaadi.neon.util.NeonImagesHandler;
import com.gaadi.neon.util.PermissionType;
import com.scanlibrary.R;
import com.scanlibrary.databinding.NormalCameraActivityLayoutBinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public class NormalCameraActivityNeon extends NeonBaseCameraActivity implements CameraFragment1.SetOnPictureTaken,LivePhotoNextTagListener {

    ICameraParam cameraParams;
    RelativeLayout tagsLayout;
    List<ImageTagModel> tagModels;
    int currentTag;
    NormalCameraActivityLayoutBinding binder;
    private TextView tvImageName, tvTag, tvNext, tvPrevious;
    private ImageView buttonGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindXml();
        cameraParams = NeonImagesHandler.getSingletonInstance().getCameraParam();
        if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
            binder.buttonDone.setVisibility(View.INVISIBLE);
        }
        else{
            binder.buttonDone.setVisibility(View.VISIBLE);
        }
        customize();
        bindCameraFragment();
        if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null)
        NeonImagesHandler.getSingletonInstance().setLivePhotoNextTagListener(this);
    }


    private void bindCameraFragment() {
        try {
            askForPermissionIfNeeded(PermissionType.write_external_storage, new OnPermissionResultListener() {
                @Override
                public void onResult(boolean permissionGranted) {
                    if (permissionGranted) {
                        try {
                            askForPermissionIfNeeded(PermissionType.camera, new OnPermissionResultListener() {
                                @Override
                                public void onResult(boolean permissionGranted) {
                                    if (permissionGranted) {
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    CameraFragment1 fragment = new CameraFragment1();
                                                    FragmentManager manager = getSupportFragmentManager();
                                                    manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(NormalCameraActivityNeon.this, R.string.permission_error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (ManifestPermission manifestPermission) {
                            manifestPermission.printStackTrace();
                        }
                    } else {
                        Toast.makeText(NormalCameraActivityNeon.this, R.string.permission_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (ManifestPermission manifestPermission) {
            manifestPermission.printStackTrace();
        }
    }

    private void bindXml() {
        binder = DataBindingUtil.setContentView(this, R.layout.normal_camera_activity_layout);
        tvImageName = binder.tvImageName;
        tvTag = binder.tvTag;
        tvNext = binder.tvSkip;
        tvPrevious = binder.tvPrev;
        buttonGallery = binder.buttonGallery;
        tagsLayout = binder.rlTags;
        binder.setHandlers(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonDone) {
            if (!NeonImagesHandler.getSingletonInstance().isNeutralEnabled()) {
                if (NeonImagesHandler.getSingletonInstance().getCameraParam().enableImageEditing()
                        || NeonImagesHandler.getSingletonInstance().getCameraParam().getTagEnabled()) {
                    Intent intent = new Intent(this, ImageShow.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (NeonImagesHandler.getSingletonInstance().validateNeonExit(this)) {
                        NeonImagesHandler.getSingletonInstance().sendImageCollectionAndFinish(this, ResponseCode.Success);
                        finish();
                    }
                }
            } else {
                setResult(RESULT_OK);
                finish();
            }

        } else if (id == R.id.buttonGallery) {
            try {
                IGalleryParam galleryParam = NeonImagesHandler.getSingletonInstance().getGalleryParam();
                if (galleryParam == null) {
                    galleryParam = new IGalleryParam() {
                        @Override
                        public boolean selectVideos() {
                            return false;
                        }

                        @Override
                        public GalleryType getGalleryViewType() {
                            return GalleryType.Grid_Structure;
                        }

                        @Override
                        public boolean enableFolderStructure() {
                            return true;
                        }

                        @Override
                        public boolean galleryToCameraSwitchEnabled() {
                            return true;
                        }

                        @Override
                        public boolean isRestrictedExtensionJpgPngEnabled() {
                            return true;
                        }

                        @Override
                        public int getNumberOfPhotos() {
                            return NeonImagesHandler.getSingletonInstance().getCameraParam().getNumberOfPhotos();
                        }

                        @Override
                        public boolean getTagEnabled() {
                            return NeonImagesHandler.getSingletonInstance().getCameraParam().getTagEnabled();
                        }

                        @Override
                        public List<ImageTagModel> getImageTagsModel() {
                            return NeonImagesHandler.getSingletonInstance().getCameraParam().getImageTagsModel();
                        }

                        @Override
                        public ArrayList<FileInfo> getAlreadyAddedImages() {
                            return null;
                        }

                        @Override
                        public boolean enableImageEditing() {
                            return NeonImagesHandler.getSingleonInstance().getCameraParam().enableImageEditing();
                        }

                    };
                }
                PhotosLibrary.collectPhotos(this, NeonImagesHandler.getSingleonInstance().getLibraryMode(), PhotosMode.setGalleryMode().setParams(galleryParam), NeonImagesHandler.getSingleonInstance().getImageResultListener());
                finish();
            } catch (NeonException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.tvSkip) {
            if (currentTag == tagModels.size() - 1) {
                if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
                    if (NeonImagesHandler.getSingletonInstance().validateNeonExit(this)) {
                        NeonImagesHandler.getSingletonInstance().sendImageCollectionAndFinish(this, ResponseCode.Success);
                    }
                }
                else {
                    onClick(binder.buttonDone);
               }

            } else {
                setTag(getNextTag(), true);
            }
        } else if (id == R.id.tvPrev) {
            setTag(getPreviousTag(), false);
        }
    }


    private boolean finishValidation() {
        if (NeonImagesHandler.getSingleonInstance().getCameraParam().getTagEnabled()) {
            for (int i = 0; i < tagModels.size(); i++) {
                if (tagModels.get(i).isMandatory() &&
                        !NeonImagesHandler.getSingleonInstance().checkImagesAvailableForTag(tagModels.get(i))) {
                    Toast.makeText(this, String.format(getString(R.string.tag_mandatory_error), tagModels.get(i).getTagName()),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            if (NeonImagesHandler.getSingleonInstance().getImagesCollection() == null ||
                    NeonImagesHandler.getSingleonInstance().getImagesCollection().size() <= 0) {
                Toast.makeText(this, R.string.no_images, Toast.LENGTH_SHORT).show();
                return false;
            } else if (NeonImagesHandler.getSingleonInstance().getImagesCollection().size() <
                    NeonImagesHandler.getSingleonInstance().getCameraParam().getNumberOfPhotos()) {
               /* Toast.makeText(this, NeonImagesHandler.getSingleonInstance().getCameraParam().getNumberOfPhotos() -
                        NeonImagesHandler.getSingleonInstance().getImagesCollection().size() + " more image required", Toast.LENGTH_SHORT).show();
                */
                Toast.makeText(this, getString(R.string.more_images, NeonImagesHandler.getSingleonInstance().getCameraParam().getNumberOfPhotos() -
                        NeonImagesHandler.getSingleonInstance().getImagesCollection().size()), Toast.LENGTH_SHORT).show();

                return false;
            }
        }
        return true;
    }

    public ImageTagModel getNextTag() {
       /* if (tagModels.get(currentTag).isMandatory() &&
                !NeonImagesHandler.getSingleonInstance().checkImagesAvailableForTag(tagModels.get(currentTag))) {
            Toast.makeText(this, String.format(getString(R.string.tag_mandatory_error), tagModels.get(currentTag).getTagName()),
                    Toast.LENGTH_SHORT).show();
        } else {
            currentTag++;
        }
        */
        currentTag++;

        if (currentTag == tagModels.size() - 1) {
            if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
                tvNext.setVisibility(View.INVISIBLE);
            }
            else {
                tvNext.setVisibility(View.VISIBLE);
                tvNext.setText(getString(R.string.finish));
            }
        }
        if (currentTag > 0) {
            tvPrevious.setVisibility(View.VISIBLE);
        }

        return tagModels.get(currentTag);
    }

    public ImageTagModel getPreviousTag() {
        if (currentTag > 0) {
            currentTag--;
        }
        if (currentTag != tagModels.size() - 1) {
            tvNext.setText(getString(R.string.next));
        }
        if (currentTag == 0) {
            tvPrevious.setVisibility(View.GONE);
        }
        return tagModels.get(currentTag);
    }

    public void setTag(ImageTagModel imageTagModel, boolean rightToLeft) {
        tvTag.setText(imageTagModel.isMandatory() ? "*" + imageTagModel.getTagName() : imageTagModel.getTagName());
        if (rightToLeft) {
            AnimationUtils.translateOnXAxis(tvTag, 200, 0);
        } else {
            AnimationUtils.translateOnXAxis(tvTag, -200, 0);
        }

    }

    private void customize() {
        if (cameraParams.getTagEnabled()) {
            tvImageName.setVisibility(View.GONE);
            tagsLayout.setVisibility(View.VISIBLE);
            tagModels = cameraParams.getImageTagsModel();
            initialiazeCurrentTag();
            setTag(tagModels.get(currentTag), true);
        } else {
            tagsLayout.setVisibility(View.GONE);
            findViewById(R.id.rlTags).setVisibility(View.GONE);
        }

        buttonGallery.setVisibility(cameraParams.cameraToGallerySwitchEnabled() ? View.VISIBLE : View.GONE);
    }

    private void initialiazeCurrentTag() {
        for (int i = 0; i < NeonImagesHandler.getSingletonInstance().getGenericParam().getImageTagsModel().size(); i++) {
            if (tagModels.get(i).isMandatory() &&
                    !NeonImagesHandler.getSingletonInstance().checkImagesAvailableForTag(tagModels.get(i))) {
                currentTag = i;
                break;
            }
        }
        if (currentTag == tagModels.size() - 1) {

            if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
                tvNext.setVisibility(View.INVISIBLE);
            }
            else {
                tvNext.setVisibility(View.VISIBLE);
                tvNext.setText(getString(R.string.finish));
            }
        }
        if (currentTag > 0) {
            tvPrevious.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (NeonImagesHandler.getSingletonInstance().isNeutralEnabled()) {
            super.onBackPressed();
        } else {
            if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
                NeonImagesHandler.getSingletonInstance().showBackOperationAlertIfNeededLive(this);
            }
            else {
                NeonImagesHandler.getSingletonInstance().showBackOperationAlertIfNeeded(this);
            }

        }
    }



    @Override
    public void onPictureTaken(String filePath) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(filePath);
        fileInfo.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
        fileInfo.setSource(FileInfo.SOURCE.PHONE_CAMERA);
        if (cameraParams.getTagEnabled()) {
            fileInfo.setFileTag(tagModels.get(currentTag));
        }
        NeonImagesHandler.getSingletonInstance().putInImageCollection(fileInfo, this);


        if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()==null){
            if (cameraParams.getTagEnabled()) {
                ImageTagModel imageTagModel = tagModels.get(currentTag);
                if (imageTagModel.getNumberOfPhotos() > 0 && NeonImagesHandler.getSingletonInstance().getNumberOfPhotosCollected(imageTagModel) >= imageTagModel.getNumberOfPhotos()) {
                    onClick(binder.tvSkip);
                }
            }
        }
    }


    @Override
    public void onNextTag() {
        if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener()!=null){
            if (cameraParams.getTagEnabled()) {
                ImageTagModel imageTagModel = tagModels.get(currentTag);
                if (imageTagModel.getNumberOfPhotos() > 0 && NeonImagesHandler.getSingletonInstance().getNumberOfPhotosCollected(imageTagModel) >= imageTagModel.getNumberOfPhotos()) {
                    onClick(binder.tvSkip);
                }
            }
        }
    }
}
