package com.truedev.neon.Activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.truedev.neon.Adapters.SelectFilesAdapter;
import com.truedev.neon.Fragments.CollectAndReviewFragment;
import com.truedev.neon.Interfaces.UpdateSelection;
import com.truedev.neon.Model.ImageModel;
import com.truedev.R;
import com.truedev.neon.Utils.CommonUtils;
import com.truedev.neon.Utils.FileInfo;

import java.util.ArrayList;

/**
 * Created by Lakshay on 02-03-2015.
 */
public class FolderFiles extends BaseActivityGallery implements View.OnClickListener, UpdateSelection ,LoaderManager.LoaderCallbacks{

    public static final int RESULT_SKIP_FOLDERS = 10;
    public static final int RESULT_DONT_SKIP_FOLDERS = 11;
    public static final String SELECTED_FILES = "selectedFiles";

    private ImageView doneButton;
    private ArrayList<FileInfo> files;
    private SelectFilesAdapter adapter;
    private TextView tvFolderName;
    private ArrayList<FileInfo> folderSelectedFiles;
    private ArrayList<FileInfo> deletedFiles , addedFiles;
    private Cursor mCursor;
    private GridView folderFiles;
//    private Toolbar mToolbar;
    private MenuItem textViewDone;

    private String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME +" =? and "+MediaStore.Images.Media.SIZE+" >?";
    private Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] mProjection = {
            "_data",
            "_id",
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };
    private String[] selectionArgs;
    private String order = MediaStore.Images.Media.DATE_TAKEN +" DESC";
    private int maxCount = 0;
    private ArrayList<ImageModel> previouslySelectedImages;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        selectedFilesOnlyAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.folder_files,frameLayout);

        setUpActionBar();
        folderSelectedFiles = new ArrayList<>();
        deletedFiles = new ArrayList<>();
        addedFiles = new ArrayList<>();

        String folderName = getIntent().getStringExtra(GalleryActivity.FOLDER_NAME);
        if (getIntent().getExtras().containsKey(GalleryActivity.MAX_COUNT))
            maxCount = getIntent().getExtras().getInt(GalleryActivity.MAX_COUNT);
        if (maxCount > 0) {

        }
        previouslySelectedImages= (ArrayList<ImageModel>) getIntent().getSerializableExtra(GalleryActivity.ALREADY_SELECTED_FILES);

        if (getIntent().getExtras().containsKey(CameraPriorityActivity.FROM_PRIORITY_ACTIVITY)) {

        }
        selectionArgs = new String[]{folderName,String.valueOf(0)};

        if(folderName!= null && folderName.equals(".thumbnails"))
        {
            mUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            selection = null;
            selectionArgs = null;
            mProjection = null;
            order = MediaStore.Images.Thumbnails.IMAGE_ID + " DESC";
        }

        mCursor = getContentResolver().query(mUri, mProjection , selection, selectionArgs, order);
        mCursor.moveToFirst();

        adapter = new SelectFilesAdapter(this, mCursor , 0,this);
        adapter.setPreviouslySelectedImages(previouslySelectedImages);
        folderFiles = (GridView) findViewById(R.id.gvFolderPhotos);
        folderFiles.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setUpActionBar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String folderName = extras.getString(GalleryActivity.FOLDER_NAME);
            getSupportActionBar().setTitle(folderName);
            toolbar.setTitle(folderName);
        }

        /**/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id==android.R.id.home)
        {
            if(folderSelectedFiles != null && folderSelectedFiles.size()>0)
            {
                folderSelectedFiles = null;
            }
            finish();
        }else if (id==R.id.menu_next)
        {
            folderSelectedFiles.addAll(addedFiles);
            CommonUtils.removeFileInfo(folderSelectedFiles , deletedFiles);
           // if (maxCount == 0) {
                if (previouslySelectedImages == null)
                    previouslySelectedImages = new ArrayList<>();
                addFilesToSelected(previouslySelectedImages, CollectAndReviewFragment.convertToImageModels(addedFiles));
                removeFilesFromSelected(previouslySelectedImages, deletedFiles);
                CommonUtils.removeFileInfo(previouslySelectedImages, deletedFiles, false);
            //}
            Intent intent = new Intent();
            intent.putExtra(SELECTED_FILES, previouslySelectedImages);
            setResult(RESULT_SKIP_FOLDERS, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void collectImagesAndSendToPreviousPage(int reqId){
        folderSelectedFiles.addAll(addedFiles);
        CommonUtils.removeFileInfo(folderSelectedFiles , deletedFiles);
        // if (maxCount == 0) {
        if (previouslySelectedImages == null)
            previouslySelectedImages = new ArrayList<>();
        addFilesToSelected(previouslySelectedImages, CollectAndReviewFragment.convertToImageModels(addedFiles));
        removeFilesFromSelected(previouslySelectedImages, deletedFiles);
        CommonUtils.removeFileInfo(previouslySelectedImages, deletedFiles, false);
        //}
        Intent intent = new Intent();
        intent.putExtra(SELECTED_FILES, previouslySelectedImages);
        setResult(reqId, intent);
    }

    @Override
    public void onBackPressed() {
        collectImagesAndSendToPreviousPage(RESULT_DONT_SKIP_FOLDERS);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
//        if(v.getId()== R.id.tvDone)
//        {
//            folderSelectedFiles.addAll(addedFiles);
//            CommonUtils.removeFileInfo(folderSelectedFiles , deletedFiles);
//            if (maxCount == 0) {
//                if (ApplicationController.selectedFiles == null)
//                    ApplicationController.selectedFiles = new ArrayList<>();
//                addFilesToSelected(ApplicationController.selectedFiles, addedFiles);
//                removeFilesFromSelected(ApplicationController.selectedFiles, deletedFiles);
//                CommonUtils.removeFileInfo(ApplicationController.selectedFiles, deletedFiles, false);
//            }
//            Intent intent = new Intent();
//            intent.putExtra(SELECTED_FILES , folderSelectedFiles);
//            setResult(RESULT_SKIP_FOLDERS, intent);
//            finish();
//        }
    }

    @Override
    public void updateSelected(String imagePath, Boolean selected) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(imagePath);
        if(selected)
        {
            textViewDone.setVisible(true);
            CommonUtils.addFileInfo(addedFiles, fileInfo);
            CommonUtils.removeFileInfo(deletedFiles , fileInfo);
        }
        else
        {
            SelectFilesAdapter adapter = (SelectFilesAdapter) folderFiles.getAdapter();
            if(adapter.selectedArr.size()==0){
                textViewDone.setVisible(true);
            }
            CommonUtils.addFileInfo(deletedFiles, fileInfo);
        }
        if (maxCount > 0)
            adapter.setStopSelection((addedFiles.size() - deletedFiles.size()) == maxCount);
    }

    public void addFilesToSelected(ArrayList<ImageModel> list , ArrayList<ImageModel> list1)
    {
        if(list1!=null)
        {
            for(int i =0 ; i<list1.size();i++)
            {
                list.add(list1.get(i));
            }
        }
    }
    public void removeFilesFromSelected(ArrayList<ImageModel> list , ArrayList<FileInfo> list1)
    {
        if(list1!=null && list !=null)
        {
            for(int i =0 ; i< list1.size();i++)
            {
                FileInfo info= list1.get(i);
                for(int j=0;j<list.size();j++){
                    ImageModel model=list.get(j);
                    if(info.getFilePath().equals(model.getImagePath())){
                        list.remove(j);
                        break;
                    }

                }
                //list.remove(list1.get(i).getFilePath());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder_file,menu);
        textViewDone=  menu.findItem(R.id.menu_next);
        textViewDone.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, mUri, mProjection, selection, selectionArgs, order);
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
