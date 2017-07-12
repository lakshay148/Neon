package com.customise.gaadi.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gaadi.neon.enumerations.CameraFacing;
import com.gaadi.neon.enumerations.CameraOrientation;
import com.gaadi.neon.enumerations.CameraType;
import com.gaadi.neon.enumerations.GalleryType;
import com.gaadi.neon.enumerations.LibraryMode;
import com.gaadi.neon.enumerations.ResponseCode;
import com.gaadi.neon.PhotosLibrary;
import com.gaadi.neon.interfaces.ICameraParam;
import com.gaadi.neon.interfaces.IGalleryParam;
import com.gaadi.neon.interfaces.INeutralParam;
import com.gaadi.neon.interfaces.LivePhotosListener;
import com.gaadi.neon.interfaces.OnImageCollectionListener;
import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.model.NeonResponse;
import com.gaadi.neon.model.PhotosMode;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.NeonException;
import com.gaadi.neon.util.NeonImagesHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnImageCollectionListener {

    private static final String TAG = "MainActivity";
    private int numberOfTags = 5;
    List<FileInfo> allreadyImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void cameraPriorityClicked(View view) {
        try {

            PhotosLibrary.collectPhotos(this, NeonImagesHandler.getSingleonInstance().getLibraryMode(), PhotosMode.setCameraMode().setParams(new ICameraParam() {
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
                    return true;
                }

                @Override
                public boolean getCameraSwitchingEnabled() {
                    return true;
                }

                @Override
                public boolean getVideoCaptureEnabled() {
                    return false;
                }

                @Override
                public CameraType getCameraViewType() {
                    return CameraType.normal_camera;
                }

                @Override
                public boolean cameraToGallerySwitchEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 3;
                }

                @Override
                public boolean getTagEnabled() {
                    return false;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }


            }),this);
        } catch (NullPointerException e) {

        } catch (NeonException e) {

        }

    }

    public void cameraOnlyClicked(View view) {
        try {
            PhotosLibrary.collectPhotos(this, NeonImagesHandler.getSingleonInstance().getLibraryMode(),PhotosMode.setCameraMode().setParams(new ICameraParam() {
                @Override
                public CameraFacing getCameraFacing() {
                    return CameraFacing.front;
                }

                @Override
                public CameraOrientation getCameraOrientation() {
                    return CameraOrientation.landscape;
                }

                @Override
                public boolean getFlashEnabled() {
                    return true;
                }

                @Override
                public boolean getCameraSwitchingEnabled() {
                    return true;
                }

                @Override
                public boolean getVideoCaptureEnabled() {
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
                public int getNumberOfPhotos() {
                    return 0;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }


            }),this);
        } catch (NullPointerException e) {

        } catch (NeonException e) {

        }

    }

    public void neutralClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this, LibraryMode.Relax,PhotosMode.setNeutralMode().setParams(new INeutralParam() {
                @Override
                public CameraFacing getCameraFacing() {
                    return CameraFacing.front;
                }

                @Override
                public CameraOrientation getCameraOrientation() {
                    return CameraOrientation.portrait;
                }

                @Override
                public boolean getFlashEnabled() {
                    return true;
                }

                @Override
                public boolean getCameraSwitchingEnabled() {
                    return true;
                }

                @Override
                public boolean getVideoCaptureEnabled() {
                    return false;
                }

                @Override
                public CameraType getCameraViewType() {
                    return CameraType.normal_camera;
                }

                @Override
                public boolean cameraToGallerySwitchEnabled() {
                    return true;
                }

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
                    return 0;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        } catch (NeonException e) {
            e.printStackTrace();
        }
    }

    public void livePhotoClick(View view){

        try {
            ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
            for (int i = 0; i < numberOfTags; i++) {
                list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
            }
            PhotosLibrary.collectLivePhotos(this, list, new OnImageCollectionListener() {
                @Override
                public void imageCollection(NeonResponse neonResponse) {

                }
            }, new LivePhotosListener() {
                @Override
                public void onLivePhotoCollected(NeonResponse neonResponse) {
                    Toast.makeText(MainActivity.this, neonResponse.getImageCollection().get(0).getFileTag().getTagName(), Toast.LENGTH_SHORT).show();

                }
            });
        }catch (NeonException e){
            e.printStackTrace();
        }
    }


    public void gridOnlyFolderClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,NeonImagesHandler.getSingleonInstance().getLibraryMode(),PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
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
                    return false;
                }

                @Override
                public boolean isRestrictedExtensionJpgPngEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void gridPriorityFolderClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,NeonImagesHandler.getSingleonInstance().getLibraryMode(),PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
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
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return false;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void gridOnlyFilesClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,NeonImagesHandler.getSingleonInstance().getLibraryMode(),PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
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
                    return false;
                }

                @Override
                public boolean galleryToCameraSwitchEnabled() {
                    return false;
                }

                @Override
                public boolean isRestrictedExtensionJpgPngEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void gridPriorityFilesClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
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
                    return false;
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
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }



    public void horizontalOnlyFolderClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
                @Override
                public boolean selectVideos() {
                    return false;
                }

                @Override
                public GalleryType getGalleryViewType() {
                    return GalleryType.Horizontal_Structure;
                }

                @Override
                public boolean enableFolderStructure() {
                    return true;
                }

                @Override
                public boolean galleryToCameraSwitchEnabled() {
                    return false;
                }

                @Override
                public boolean isRestrictedExtensionJpgPngEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void horizontalPriorityFolderClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
                @Override
                public boolean selectVideos() {
                    return false;
                }

                @Override
                public GalleryType getGalleryViewType() {
                    return GalleryType.Horizontal_Structure;
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
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void horizontalOnlyFilesClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
                @Override
                public boolean selectVideos() {
                    return false;
                }

                @Override
                public GalleryType getGalleryViewType() {
                    return GalleryType.Horizontal_Structure;
                }

                @Override
                public boolean enableFolderStructure() {
                    return false;
                }

                @Override
                public boolean galleryToCameraSwitchEnabled() {
                    return false;
                }

                @Override
                public boolean isRestrictedExtensionJpgPngEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }

    public void horizontalPrioriyFilesClicked(View view){
        try {
            PhotosLibrary.collectPhotos(this,PhotosMode.setGalleryMode().setParams(new IGalleryParam() {
                @Override
                public boolean selectVideos() {
                    return false;
                }

                @Override
                public GalleryType getGalleryViewType() {
                    return GalleryType.Horizontal_Structure;
                }

                @Override
                public boolean enableFolderStructure() {
                    return false;
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
                    return 5;
                }

                @Override
                public boolean getTagEnabled() {
                    return true;
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    ArrayList<ImageTagModel> list = new ArrayList<ImageTagModel>();
                    for (int i = 0; i < numberOfTags; i++) {
                        if(i%2==0) {
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), true,1));
                        }else{
                            list.add(new ImageTagModel("Tag" + i, String.valueOf(i), false,1));
                        }
                    }
                    return list;
                }

                @Override
                public List<FileInfo> getAlreadyAddedImages() {
                    return allreadyImages;
                }

                @Override
                public boolean enableImageEditing() {
                    return false;
                }
            }),this);
        }catch (Exception e){

        }
    }






    @Override
    public void imageCollection(NeonResponse neonResponse) {
        if(neonResponse.getImageCollection() != null && neonResponse.getImageCollection().size()>0){
            allreadyImages = neonResponse.getImageCollection();
            Toast.makeText(this,"Got collection with size " + neonResponse.getImageCollection().size(),Toast.LENGTH_SHORT).show();
        }
    }
}
