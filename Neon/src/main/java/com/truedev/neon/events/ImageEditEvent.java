package com.truedev.neon.events;


import com.truedev.neon.Model.ImageModel;
import com.truedev.neon.Model.ImagesModel;

/**
 * Created by lakshaygirdhar on 4/12/15.
 */
public class ImageEditEvent extends GeneralEvent {
    public static final int EVENT_NONE=0;
    public static final int EVENT_DELETE=1;
    public static final int EVENT_ROTATE=2;
    public static final int EVENT_REPLACED_BY_CAM=3;
    public static final int EVENT_TAG_CHANGED=4;
    public static final int EVENT_REPLACED_BY_GALLERY=5;

    private ImageModel model;
    private int position;
    private int imageEventType;



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getImageEventType() {
        return imageEventType;
    }

    public void setImageEventType(int imageEventType) {
        this.imageEventType = imageEventType;
    }

    public ImageModel getModel() {
        return model;
    }

    public void setModel(ImageModel model) {
        this.model = model;
    }
}
