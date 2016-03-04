package com.truedev.neon.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.truedev.neon.Activity.CameraHolderActivity;
import com.truedev.neon.Activity.GalleryActivity;
import com.truedev.neon.Activity.ImageReviewActivity;
import com.truedev.neon.Adapters.CollectAndReviewAdapter;
import com.truedev.neon.Model.ImageModel;
import com.truedev.neon.Model.ImageTagsModel;
import com.truedev.R;
import com.truedev.neon.Utils.Constants;
import com.truedev.neon.Utils.FileInfo;

import java.util.ArrayList;

/**
 * Created by lakshaygirdhar on 24/12/15.
 */
public class CollectAndReviewFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 101;
    private static final int OPEN_IMAGE_VIEW_PAGER_SCREEN = 102;
    private static final String TAG = "CollectAndReviewFragmen";
    private CollectImagesListener collectImagesListener;
    private ArrayList<ImageTagsModel> mImageTags;
    private CollectAndReviewAdapter adapter;
    private ArrayList<FileInfo> resultImagesGallery;
    public ArrayList<ImageModel> collectedImages = new ArrayList<>();

    public CollectAndReviewAdapter getAdapter() {
        return adapter;
    }

    public enum ImageSource {
        CAMERA, GALLERY
    }

    public ArrayList<ImageModel> getCollectedImages() {
        return collectedImages;
    }

    public interface CollectImagesListener {
        public void onImagesCollected(ImageSource source, ArrayList<ImageModel> images);

        //public void onImagesWithTagsCollected(ImageSource source, HashMap<String, ArrayList<String>> images);
    }

    public static CollectAndReviewFragment getInstance(CollectImagesListener listener) {
        CollectAndReviewFragment fragment = new CollectAndReviewFragment();
        fragment.collectImagesListener = listener;
        return fragment;
    }

    public static CollectAndReviewFragment getInstance(CollectImagesListener listener, ArrayList<ImageTagsModel> imageTags) {
        CollectAndReviewFragment fragment = new CollectAndReviewFragment();
        fragment.collectImagesListener = listener;
        fragment.mImageTags = imageTags;
        return fragment;
    }

    public static CollectAndReviewFragment getInstance(CollectImagesListener listener, ArrayList<ImageTagsModel> imageTags,ArrayList<ImageModel> imagesList) {
        CollectAndReviewFragment fragment = new CollectAndReviewFragment();
        fragment.collectImagesListener = listener;
        fragment.mImageTags = imageTags;
        if(imagesList.size()>0){
            fragment.collectedImages=imagesList;
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collect_and_review, null);
        view.findViewById(R.id.tvTakeFromCamera).setOnClickListener(this);
        view.findViewById(R.id.tvTakeFromGallery).setOnClickListener(this);
        adapter = new CollectAndReviewAdapter(getActivity(), collectedImages);
        GridView gvImages = (GridView) view.findViewById(R.id.gvImages);
        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ImageReviewActivity.class);
                intent.putExtra(Constants.IMAGE_TAGS_FOR_REVIEW, mImageTags);
                intent.putExtra(Constants.IMAGE_MODEL_FOR__REVIEW, collectedImages);
                intent.putExtra(Constants.IMAGE_REVIEW_POSITION, position);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, OPEN_IMAGE_VIEW_PAGER_SCREEN);
            }
        });
        gvImages.setAdapter(adapter);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvTakeFromCamera) {
            Intent intent = new Intent(getActivity(), CameraHolderActivity.class);
            intent.putExtra(Constants.IMAGE_TAGS, mImageTags);
            startActivityForResult(intent, REQUEST_CAMERA);
        } else if (v.getId() == R.id.tvTakeFromGallery) {
            Intent intent = new Intent(getActivity(), GalleryActivity.class);
            if(mImageTags!=null && mImageTags.size()>0){
                intent.putExtra(GalleryActivity.MAX_COUNT,mImageTags.size()-adapter.getCount());
            }
            if( adapter.getImageModels().size()>0){
                intent.putExtra(GalleryActivity.ALREADY_SELECTED_FILES,adapter.getImageModels());
            }
            startActivityForResult(intent, REQUEST_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                Log.d(TAG, "onActivityResult:GalleryImage");
                ArrayList<ImageModel> imagesList  = (ArrayList<ImageModel>) data.getExtras().getSerializable(GalleryActivity.GALLERY_SELECTED_PHOTOS);
                adapter.setItems(imagesList);
            } else if (requestCode == OPEN_IMAGE_VIEW_PAGER_SCREEN) {
                Log.d(TAG, "onActivityResult: open image view pager");
                ArrayList<ImageModel> imagesList = (ArrayList<ImageModel>) data.getSerializableExtra(com.truedev.neon.Utils.Constants.IMAGE_MODEL_FOR__REVIEW);
//                collectedImages = imagesList;
                adapter.setItems(imagesList);
            } else if (requestCode == REQUEST_CAMERA) {
                Log.d(TAG, "onActivityResult: camera pics received");
                ArrayList<ImageModel> imageModels = (ArrayList<ImageModel>) data.getSerializableExtra(Constants.RESULT_IMAGES);
                adapter.addItems(imageModels);
            }
        }
    }

    public static ArrayList<ImageModel> convertToImageModels(ArrayList<FileInfo> resultImages) {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        for (FileInfo s : resultImages) {
            ImageModel model = new ImageModel();
            model.setImagePath(s.getFilePath());
            imageModels.add(model);
        }
        return imageModels;
    }
}
