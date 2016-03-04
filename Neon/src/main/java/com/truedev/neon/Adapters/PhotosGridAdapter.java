package com.truedev.neon.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.truedev.neon.Interfaces.UpdateSelection;
import com.truedev.R;
import com.truedev.neon.Utils.FileInfo;


import java.util.ArrayList;

/**
 * Created by Lakshay on 20-02-2015.
 */
public class PhotosGridAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = "PhotosGridAdapter";
    private Context context;
    private UpdateSelection updateSelection;
    private int imgLoadDefBig, imgLoadDefSmall;

    private ArrayList<FileInfo> mFiles;

    public PhotosGridAdapter(Context context,
                             ArrayList<FileInfo> fileInfos,
                             int columnCount,
                             UpdateSelection updateSelection,
                             int imgLoadDefBig,
                             int imgLoadDefSmall) {
//        super(context, fileInfos, columnCount);
        this.context = context;
        mFiles = fileInfos;
        this.updateSelection = updateSelection;
        this.imgLoadDefBig = imgLoadDefBig;
        this.imgLoadDefSmall = imgLoadDefSmall;
    }

    public ArrayList<FileInfo> getFiles() {
        return mFiles;
    }

    public void setFiles(ArrayList<FileInfo> mFiles) {
        this.mFiles = mFiles;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PhotosHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_images, null);
            holder = new PhotosHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.ivImageDisplay);
            holder.removeImage = (ImageView) convertView.findViewById(R.id.ivRemoveImage);
            holder.transparentView = convertView.findViewById(R.id.viewTransaparent);
            holder.tvProfile = (TextView) convertView.findViewById(R.id.tvProfilePhoto);
            convertView.setTag(holder);

            //holder.image.setBackgroundDrawable(context.getResources().getDrawable(imgLoadDefSmall > 0 ? imgLoadDefSmall : R.drawable.default_placeholder));

        } else {
            holder = (PhotosHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.tvProfile.setVisibility(View.VISIBLE);
            holder.transparentView.setVisibility(View.VISIBLE);
            holder.transparentView.setBackgroundColor(context.getResources().getColor(R.color.tranparent_black));
            holder.image.setBackgroundColor(context.getResources().getColor(R.color.transparent_white));

        } else {
            holder.tvProfile.setVisibility(View.GONE);
            holder.transparentView.setVisibility(View.GONE);
        }
        holder.removeImage.setTag(position);
        holder.removeImage.setOnClickListener(this);

        FileInfo fileInfo = (FileInfo) getItem(position);
        Glide.with(context).load(fileInfo.getFilePath())
                .crossFade()
                .placeholder((imgLoadDefSmall > 0) ? imgLoadDefSmall : R.drawable.default_placeholder)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        return convertView;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ivRemoveImage) {
            final int position = (int) v.getTag();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final AlertDialog alertDialog = builder
                    .setTitle(R.string.alert)
                    .setMessage(R.string.removeImage)
                    .setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    removeImage(position);
                                }
                            })
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();

                                }
                            }).setCancelable(false).create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

        }
    }

    private void removeImage(int position) {
        if(getCount()>0){
            FileInfo fileInfo = (FileInfo) getItem(position);
//            getItems().remove(position);
            this.updateSelection.updateSelected(fileInfo.getFilePath(), false);
            notifyDataSetChanged();
        }

    }

    private class PhotosHolder {
        ImageView image;
        ImageView removeImage;
        View transparentView;
        TextView tvProfile;
    }
}
