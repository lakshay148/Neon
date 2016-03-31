package com.truedev.neonsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.truedev.neonsample.adapter.BaseRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvItems = (RecyclerView) findViewById(R.id.rcvItems);

//        BaseRecyclerAdapter


    }
}
