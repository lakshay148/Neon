package com.truedev.neon.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.truedev.neon.Interfaces.UpdateSelection;
import com.truedev.neon.Model.ImageModel;
import com.truedev.R;
import com.truedev.neon.Utils.FileInfo;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Lakshay on 02-03-2015.
 */
public class SelectFilesAdapter extends CursorAdapter implements View.OnClickListener{


    private static final String TAG = "SelectFilesAdapter";
    public HashSet<String> selectedArr = new HashSet<>();
    private ArrayList<FileInfo> files;
    private Context context;
    private UpdateSelection updateSelection;
    private Cursor mCursor;
    private FilesHolder filesHolder;
    private boolean stopSelection = false;
    private ArrayList<ImageModel> previouslySelectedImages;

    public SelectFilesAdapter(Context context, Cursor c, int flags, UpdateSelection updateSelection) {
        super(context, c, flags);
        mCursor = c;
        this.updateSelection = updateSelection;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlSelectFiles) {
            actionSelectUnSelect(v);
        }
    }

    private void actionSelectUnSelect(View v) {
        FilesHolder holder = (FilesHolder) v.getTag();
        String imagePath = (String) holder.transparentLayer.getTag();
        int position = (int) holder.selection_view.getTag();
//        if(FolderFiles.selectedFilesOnlyAdapter!= null && FolderFiles.selectedFilesOnlyAdapter.contains(imagePath))
        if (View.VISIBLE == holder.transparentLayer.getVisibility()) {
            holder.transparentLayer.setVisibility(View.INVISIBLE);
            holder.selection_view.setVisibility(View.INVISIBLE);
//            FolderFiles.selectedFilesOnlyAdapter.remove(imagePath);
            selectedArr.remove(position + "");
            this.updateSelection.updateSelected(imagePath, false);
        } else if (stopSelection) {
            return;
        } else {
            holder.transparentLayer.setVisibility(View.VISIBLE);
            holder.selection_view.setVisibility(View.VISIBLE);
            holder.selectedImage.setBackgroundColor(context.getResources().getColor(R.color.transparent_white));
            this.updateSelection.updateSelected(imagePath, true);
//            FolderFiles.selectedFilesOnlyAdapter.add(imagePath);
            selectedArr.add(position + "");
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.select_files,null);
        filesHolder = new FilesHolder();
        filesHolder.selectedImage = (ImageView) convertView.findViewById(R.id.imageSelected);
        filesHolder.selection_view = (ImageView) convertView.findViewById(R.id.selection_view);
        filesHolder.transparentLayer = (ImageView)convertView.findViewById(R.id.vTransparentLayer);
        convertView.setTag( filesHolder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        filesHolder = (FilesHolder) view.getTag();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        view.setOnClickListener(this);
//        filesHolder.selectedImage.setTag(path);

        filesHolder.transparentLayer.setTag(path);
        filesHolder.selection_view.setTag(cursor.getPosition());

        Glide.with(context).load(path)
                .placeholder(R.drawable.default_placeholder)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(filesHolder.selectedImage);

        boolean isFound=false;
        if(previouslySelectedImages != null )
        {
            for (ImageModel model:previouslySelectedImages
                 ) {
                if(model.getImagePath().equals(path)){
                    isFound=true;
                    filesHolder.transparentLayer.setVisibility(View.VISIBLE);
                    filesHolder.selection_view.setVisibility(View.VISIBLE);
                }
            }


        }
        if(!isFound) {
            filesHolder.transparentLayer.setVisibility(View.INVISIBLE);
            filesHolder.selection_view.setVisibility(View.INVISIBLE);
        }

        if (selectedArr.contains(cursor.getPosition() + "")) {
            filesHolder.transparentLayer.setVisibility(View.VISIBLE);
            filesHolder.selection_view.setVisibility(View.VISIBLE);
        }
//        else {
//            filesHolder.transparentLayer.setVisibility(View.INVISIBLE);
//            filesHolder.selection_view.setVisibility(View.INVISIBLE);
//
//        }
//        if(fileInfo != null)
//        {
//            if(fileInfo.getSelected())
//            {
//                filesHolder.transparentLayer.setVisibility(View.VISIBLE);
//                filesHolder.selection_view.setVisibility(View.VISIBLE);
//                // filesHolder.selectedCheckBox.setChecked(true);
//            }
//            else
//            {
//                filesHolder.transparentLayer.setVisibility(View.INVISIBLE);
//                filesHolder.selection_view.setVisibility(View.INVISIBLE);
//                // filesHolder.selectedCheckBox.setChecked(false);
//            }
//        }
    }

    public void setStopSelection(boolean stopSelection) {
        this.stopSelection = stopSelection;
    }

    public void setPreviouslySelectedImages(ArrayList<ImageModel> previouslySelectedImages) {
        this.previouslySelectedImages = previouslySelectedImages;
    }

    public class FilesHolder {
        ImageView selectedImage;
        ImageView transparentLayer;
        ImageView selection_view;
    }
}
