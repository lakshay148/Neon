package com.gaadi.neon;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.gaadi.neon.activity.camera.NormalCameraActivityNeon;
import com.gaadi.neon.activity.gallery.GridFilesActivity;
import com.gaadi.neon.activity.gallery.GridFoldersActivity;
import com.gaadi.neon.activity.gallery.HorizontalFilesActivity;
import com.gaadi.neon.activity.neutral.NeonNeutralActivity;
import com.gaadi.neon.enumerations.CameraFacing;
import com.gaadi.neon.enumerations.CameraOrientation;
import com.gaadi.neon.enumerations.CameraType;
import com.gaadi.neon.enumerations.LibraryMode;
import com.gaadi.neon.interfaces.ICameraParam;
import com.gaadi.neon.interfaces.IGalleryParam;
import com.gaadi.neon.interfaces.INeutralParam;
import com.gaadi.neon.interfaces.LivePhotosListener;
import com.gaadi.neon.interfaces.OnImageCollectionListener;
import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.model.NeonResponse;
import com.gaadi.neon.model.PhotosMode;
import com.gaadi.neon.util.CustomParameters;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.FindLocations;
import com.gaadi.neon.util.NeonException;
import com.gaadi.neon.util.NeonImagesHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lakshaygirdhar
 * @since 13-08-2016
 */
public class PhotosLibrary{

    public static void collectPhotos(Context activity, PhotosMode photosMode, OnImageCollectionListener listener) throws NullPointerException, NeonException {
        collectPhotos(activity,LibraryMode.Restrict,photosMode,listener);
    }

    public static void collectPhotos(Context activity, LibraryMode libraryMode, PhotosMode photosMode, OnImageCollectionListener listener) throws NullPointerException, NeonException {
        NeonImagesHandler.getSingleonInstance().setImageResultListener(listener);
        NeonImagesHandler.getSingleonInstance().setLibraryMode(libraryMode);
        validate(activity, photosMode,listener);
        List<FileInfo> alreadyAddedImages = photosMode.getParams().getAlreadyAddedImages();
        if(alreadyAddedImages != null) {
            NeonImagesHandler.getSingleonInstance().setImagesCollection(alreadyAddedImages);
        }
        if (photosMode.getParams() instanceof INeutralParam) {
            startNeutralActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof ICameraParam) {
            startCameraActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof IGalleryParam) {
            startGalleryActivity(activity, photosMode);
        }
    }





    public static void collectLivePhotos(final Context activity,final List<ImageTagModel> tagList, final OnImageCollectionListener imageCollectionListener, final LivePhotosListener listener) throws NullPointerException, NeonException {
        try {


            collectPhotos(activity, LibraryMode.Restrict, PhotosMode.setCameraMode().setParams(new ICameraParam() {


                @Override
                public CameraFacing getCameraFacing() {
                    return CameraFacing.back;
                }

                @Override
                public CameraOrientation getCameraOrientation() {
                    return CameraOrientation.portrait;
                }

                @Override
                public boolean getFlashEnabled() {
                    return false;
                }

                @Override
                public boolean getCameraSwitchingEnabled() {
                    return false;
                }

                @Override
                public CameraType getCameraViewType() {
                    return CameraType.normal_camera;
                }

                @Override
                public boolean cameraToGallerySwitchEnabled() {
                    return false;
                }

                @Override
                public boolean getVideoCaptureEnabled() {
                    return false;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 1;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    return tagList;
                }

                @Override
                public ArrayList<FileInfo> getAlreadyAddedImages() {
                    return null;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }

                @Override
                public CustomParameters getCustomParameters() {
                    return null;
                }
            }), imageCollectionListener ,listener);
        } catch (NeonException e) {
            e.printStackTrace();
        }
    }



    public static void collectPhotos(int requestCode, Context activity, LibraryMode libraryMode, PhotosMode photosMode, OnImageCollectionListener listener) throws NullPointerException, NeonException {
        NeonImagesHandler.getSingleonInstance().setImageResultListener(listener);
        NeonImagesHandler.getSingleonInstance().setLibraryMode(libraryMode);
        NeonImagesHandler.getSingleonInstance().setRequestCode(requestCode);
        validate(activity, photosMode,listener);
        List<FileInfo> alreadyAddedImages = photosMode.getParams().getAlreadyAddedImages();
        if(alreadyAddedImages != null) {
            NeonImagesHandler.getSingleonInstance().setImagesCollection(alreadyAddedImages);
        }
        if (photosMode.getParams() instanceof INeutralParam) {
            startNeutralActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof ICameraParam) {
            startCameraActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof IGalleryParam) {
            startGalleryActivity(activity, photosMode);
        }
    }

    public static void collectPhotos(Context activity, LibraryMode libraryMode, PhotosMode photosMode, OnImageCollectionListener listener,LivePhotosListener livePhotosListener) throws NullPointerException, NeonException {
        NeonImagesHandler.getSingletonInstance().setImageResultListener(listener);
        NeonImagesHandler.getSingletonInstance().setLivePhotosListener(livePhotosListener);

        NeonImagesHandler.getSingletonInstance().setLibraryMode(libraryMode);
        validate(activity, photosMode,listener);
        validate(activity, photosMode,livePhotosListener);
        List<FileInfo> alreadyAddedImages = photosMode.getParams().getAlreadyAddedImages();
        if(alreadyAddedImages != null) {
            NeonImagesHandler.getSingletonInstance().setImagesCollection(alreadyAddedImages);
        }
        if (photosMode.getParams() instanceof INeutralParam) {
            startNeutralActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof ICameraParam) {
            startCameraActivity(activity, photosMode);
        } else if (photosMode.getParams() instanceof IGalleryParam) {
            startGalleryActivity(activity, photosMode);
        }
    }



    private static void validate(Context activity, PhotosMode photosMode,OnImageCollectionListener listener) throws NullPointerException, NeonException {
        if (activity == null) {
            throw new NullPointerException("Activity instance cannot be null");
        } else if (photosMode == null) {
            throw new NullPointerException("PhotosMode instance cannot be null");
        } else if ((photosMode.getParams().getTagEnabled()) &&
                (photosMode.getParams().getImageTagsModel() == null || photosMode.getParams().getImageTagsModel().size() <= 0)) {
            throw new NeonException("Tags enabled but list is empty or null");
        }else if(listener == null){
            throw new NullPointerException("'OnImageCollectionListener' cannot be null");
        }
    }


    private static void validate(Context activity, PhotosMode photosMode,LivePhotosListener listener) throws NullPointerException, NeonException {
        if (activity == null) {
            throw new NullPointerException("Activity instance cannot be null");
        } else if (photosMode == null) {
            throw new NullPointerException("PhotosMode instance cannot be null");
        } else if ((photosMode.getParams().getTagEnabled()) &&
                (photosMode.getParams().getImageTagsModel() == null || photosMode.getParams().getImageTagsModel().size() <= 0)) {
            throw new NeonException("Tags enabled but list is empty or null");
        }else if(listener == null){
            throw new NullPointerException("'OnImageCollectionListener' cannot be null");
        }
    }

    private static void startCameraActivity(Context activity, PhotosMode photosMode) {
        ICameraParam cameraParams = (ICameraParam) photosMode.getParams();
        NeonImagesHandler.getSingletonInstance().setCameraParam(cameraParams);

        switch (cameraParams.getCameraViewType()) {

            case normal_camera:
                Intent intent = new Intent(activity, NormalCameraActivityNeon.class);
                activity.startActivity(intent);
                break;

        }
    }

    private static void startGalleryActivity(Context activity, PhotosMode photosMode) {
        IGalleryParam galleryParams = (IGalleryParam) photosMode.getParams();
        NeonImagesHandler.getSingleonInstance().setGalleryParam(galleryParams);

        switch (galleryParams.getGalleryViewType()) {

            case Grid_Structure:
                Intent gridGalleryIntent;
                if(galleryParams.enableFolderStructure()){
                    gridGalleryIntent = new Intent(activity, GridFoldersActivity.class);
                }else{
                    gridGalleryIntent = new Intent(activity, GridFilesActivity.class);
                }
                activity.startActivity(gridGalleryIntent);
                break;

            case Horizontal_Structure:
                Intent horizontalGalleryIntent;
                if(galleryParams.enableFolderStructure()){
                    horizontalGalleryIntent = new Intent(activity, GridFoldersActivity.class);
                }else{
                    horizontalGalleryIntent = new Intent(activity, HorizontalFilesActivity.class);
                }
                activity.startActivity(horizontalGalleryIntent);
                break;

        }
    }

    private static void startNeutralActivity(Context activity, PhotosMode photosMode) {
        NeonImagesHandler.getSingletonInstance().setNeutralEnabled(true);

        INeutralParam neutralParamParams = (INeutralParam) photosMode.getParams();
        NeonImagesHandler.getSingletonInstance().setNeutralParam(neutralParamParams);

        Intent neutralIntent = new Intent(activity, NeonNeutralActivity.class);
        activity.startActivity(neutralIntent);

    }
}
