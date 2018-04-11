package com.ortiz.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.emcsthai.emcslibrary.R;

import java.util.ArrayList;

/**
 * Created by nakarin on 9/28/2016 AD.
 */

public class ViewPagerActivity extends AppCompatActivity {

    protected ViewPagerAdapter viewPagerAdapter;

    private ExtendedViewPager extendedViewPager;

    private Button btnClose;

    private ArrayList<String> stringArrayList = new ArrayList<>();

    protected int imageType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        imageType = IntentExtraManager.getInstance().getImageType(getIntent());
        if (imageType == 0) { // load from Internal Storage
            stringArrayList.add(IntentExtraManager.getInstance().getImageName(getIntent()));
        } else { // load from Web Service (Link & URL)
            stringArrayList.add(IntentExtraManager.getInstance().getImagePath(getIntent()));
        }

        // Method from this class
        initInstance();

        viewPagerAdapter = new ViewPagerAdapter(imageType, stringArrayList);
        extendedViewPager.setAdapter(viewPagerAdapter);
        btnClose.setOnClickListener(onClickListener);
    }

    private void initInstance() {
        // ViewPager
        extendedViewPager = (ExtendedViewPager) this.findViewById(R.id.view_pager);
        btnClose = (Button) this.findViewById(R.id.btn_close);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
