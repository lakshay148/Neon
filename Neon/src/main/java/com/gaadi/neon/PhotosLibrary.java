package com.gaadi.neon;

import android.content.Context;
import android.content.Intent;

import com.gaadi.neon.activity.camera.NormalCameraActivityNeon;
import com.gaadi.neon.activity.gallery.GridFilesActivity;
import com.gaadi.neon.activity.gallery.GridFoldersActivity;
import com.gaadi.neon.activity.gallery.HorizontalFilesActivity;
import com.gaadi.neon.activity.neutral.NeonNeutralActivity;
import com.gaadi.neon.enumerations.LibraryMode;
import com.gaadi.neon.interfaces.ICameraParam;
import com.gaadi.neon.interfaces.IGalleryParam;
import com.gaadi.neon.interfaces.INeutralParam;
import com.gaadi.neon.interfaces.OnImageCollectionListener;
import com.gaadi.neon.model.PhotosMode;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.NeonException;
import com.gaadi.neon.util.NeonImagesHandler;

import java.util.List;

/**
 * @author lakshaygirdhar
 * @since 13-08-2016
 */
public class PhotosLibrary {

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

    private static void startCameraActivity(Context activity, PhotosMode photosMode) {
        ICameraParam cameraParams = (ICameraParam) photosMode.getParams();
        NeonImagesHandler.getSingleonInstance().setCameraParam(cameraParams);

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
