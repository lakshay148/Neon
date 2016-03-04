package com.truedev.neon.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.truedev.neon.Model.ImageModel;
import com.truedev.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by lakshaygirdhar on 24/12/15.
 */
public class CollectAndReviewAdapter extends BaseAdapter {

    private ArrayList<ImageModel> imageModels;
    private Context mContext;

    public CollectAndReviewAdapter(Context context,ArrayList<ImageModel> imageModels) {
        this.imageModels = imageModels;
        this.mContext = context;
    }

    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    @Override
    public int getCount() {
        return this.imageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.review_grid_tuple,null);
            viewHolder.imageView= (SimpleDraweeView) convertView.findViewById(R.id.ivReview);
            viewHolder.tagView=(TextView)convertView.findViewById(R.id.tvTag);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if(imageModels.get(position).getTagsModel() == null){
//            viewHolder.tagView.setVisibility(View.GONE);
        } else {
            viewHolder.tagView.setVisibility(View.VISIBLE);
            viewHolder.tagView.setText(imageModels.get(position).getTagsModel().getTag_name());
        }
//        viewHolder.tagView.setText((imageModels.get(position).getTagsModel() == null) ? mContext.getString(R.string.select_tag) : imageModels.get(position).getTagsModel().getTag_name());
        viewHolder.imageView.setImageURI(Uri.fromFile(new File(imageModels.get(position).getImagePath())));

        //Glide.with(mContext).load(imageModels.get(position).getImagePath()).centerCrop().into(viewHolder.imageView);
        return convertView;
    }

    class ViewHolder{
        SimpleDraweeView imageView;
        TextView tagView;
    }

    public void setItems(ArrayList<ImageModel> listItems){
        if(imageModels!=null){
            imageModels.clear();
        }
        else{
            imageModels=new ArrayList<>();
        }
        imageModels.addAll(listItems);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<ImageModel> list){
        if(imageModels == null){
            imageModels=new ArrayList<>();
        }
        imageModels.addAll(list);
        notifyDataSetChanged();
    }
}
