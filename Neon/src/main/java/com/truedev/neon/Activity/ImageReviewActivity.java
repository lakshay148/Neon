package com.truedev.neon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.truedev.neon.Adapters.ImagesReviewViewPagerAdapter;
import com.truedev.neon.Interfaces.FragmentListener;
import com.truedev.neon.Model.ImageModel;
import com.truedev.neon.Model.ImageTagsModel;
import com.truedev.R;
import com.truedev.neon.Utils.Constants;
import com.truedev.neon.events.ImageEditEvent;

import java.util.ArrayList;


/**
 * Created by lakshaygirdhar on 25/12/15.
 */
public class ImageReviewActivity extends AppCompatActivity implements View.OnClickListener, FragmentListener {

    private ImagesReviewViewPagerAdapter mPagerAdapter;
    private ArrayList<ImageTagsModel> mImageTags;
    private ArrayList<ImageModel> imagesList;

    private TextView mDoneButton;
    private TextView mTitle;
    private ViewPager mPager;
    private Toolbar toolbar;
    private boolean isViewDirty=false;
    private ImageButton viewPagerRightBtn;
    private ImageButton viewPagerLeftBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_imagereview);
        toolbar = (Toolbar) findViewById(R.id.image_review_toolbar);
        setSupportActionBar(toolbar);
        mDoneButton = (TextView) findViewById(R.id.image_review_toolbar_doneBtn);
        mDoneButton.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        viewPagerLeftBtn=(ImageButton)findViewById(R.id.view_pager_leftbtn);
        viewPagerRightBtn=(ImageButton)findViewById(R.id.view_pager_rightbtn);
        viewPagerRightBtn.setOnClickListener(this);
        viewPagerLeftBtn.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.image_review_toolbar_title);
        getSupportActionBar().setTitle("Image Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        mImageTags = (ArrayList<ImageTagsModel>) intent.getSerializableExtra(Constants.IMAGE_TAGS_FOR_REVIEW);
        imagesList = (ArrayList<ImageModel>) intent.getSerializableExtra(Constants.IMAGE_MODEL_FOR__REVIEW);
        int position = intent.getIntExtra(Constants.IMAGE_REVIEW_POSITION, 0);
        if(position==0){
            viewPagerLeftBtn.setVisibility(View.GONE);
        }
        if(position==imagesList.size()-1){
            viewPagerRightBtn.setVisibility(View.GONE);
        }
        mPagerAdapter = new ImagesReviewViewPagerAdapter(getSupportFragmentManager(), imagesList, mImageTags);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(position);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0 || imagesList.size()==1){
                    viewPagerLeftBtn.setVisibility(View.GONE);
                }
                else{
                    viewPagerLeftBtn.setVisibility(View.VISIBLE);
                }
                if(position==imagesList.size()-1 || imagesList.size()==1){
                    viewPagerRightBtn.setVisibility(View.GONE);
                }
                else{
                    viewPagerRightBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getFragmentChanges(ImageEditEvent event) {
        if (event.getImageEventType() == ImageEditEvent.EVENT_DELETE) {
            isViewDirty=true;
            imagesList.remove(event.getPosition());
            mPagerAdapter.setPagerItems(imagesList);
            if(imagesList.size()==0){
                onBackPressed();
            }
        } else if (event.getImageEventType() == ImageEditEvent.EVENT_ROTATE) {
            isViewDirty=true;

        } else if (event.getImageEventType() == ImageEditEvent.EVENT_REPLACED_BY_CAM) {
            isViewDirty=true;
        } else if (event.getImageEventType() == ImageEditEvent.EVENT_REPLACED_BY_GALLERY) {
            isViewDirty=true;
        } else if (event.getImageEventType() == ImageEditEvent.EVENT_TAG_CHANGED) {
            imagesList.set(event.getPosition(), event.getModel());
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.image_review_toolbar_doneBtn) {
            Intent i = new Intent();
            i.putExtra(Constants.IMAGE_MODEL_FOR__REVIEW, imagesList);
            setResult(RESULT_OK, i);
            finish();
        }
        else if(id == R.id.view_pager_leftbtn){
            int position=mPager.getCurrentItem();
            if(position>0){
                position--;
                mPager.setCurrentItem(position);
            }


        }
        else if(id == R.id.view_pager_rightbtn){
            int position=mPager.getCurrentItem();
            if(position<imagesList.size()-1){
                position++;
                mPager.setCurrentItem(position);
            }


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_pager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        if(item.getItemId()== R.id.menu_done){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       /* if(isViewDirty){
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.clearCaches();
        }*/
        Intent i = new Intent();
        i.putExtra(Constants.IMAGE_MODEL_FOR__REVIEW, imagesList);
        setResult(RESULT_OK, i);
        //finish();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
