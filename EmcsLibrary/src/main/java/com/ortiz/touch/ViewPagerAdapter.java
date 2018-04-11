package com.ortiz.touch;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by nakarin on 9/28/2016 AD.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private int imageType = 0;
    private ArrayList<String> imageArray;

    public ViewPagerAdapter(int imageType, ArrayList<String> imageArray) {
        this.imageType = imageType;
        this.imageArray = imageArray;
    }

    @Override
    public int getCount() {
        if (imageArray != null) {
            return imageArray.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TouchImageView imageView = new TouchImageView(container.getContext());

        if (imageType == 0) { // load from Internal Storage
//            File imgFile = PhotoManager.getInstance().getImageByFileName(imageArray.get(position));
//
//            if (imgFile.exists()) {
//                try {
//                    Picasso.with(container.getContext())
//                            .load(imgFile)
//                            .error(R.drawable.no_media)
//                            .skipMemoryCache()
//                            .into(imageView);
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//                }
//            }

        } else { // load from Web Service (Link & URL)
            Timber.i("URL : " + imageArray.get(position));

            Picasso.with(container.getContext()).load(imageArray.get(position)).into(imageView);

//            Uri uri = new Uri.Builder().appendPath(imageArray.get(position)).build();
//            Glide.with(container.getContext())
//                    .load(uri)
//                    .into(imageView);
        }

        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}