package com.emcsthai.emcslibrary.Model.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nakarin on 9/20/2016 AD.
 */

public class FileUtils {

    private static FileUtils instance = null;

    private Context mContext = null;

    private FileUtils() {
        mContext = Contextor.getInstance().getContext();
    }

    public static FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }
        return instance;
    }

    public String getResources(int resId) {
        return Contextor.getInstance().getContext().getResources().getString(resId);
    }

    public int neededRotation(File ff) {
        try {

            ExifInterface exif = new ExifInterface(ff.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            }
            return 0;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;

    }

    // saveBitmapToJPEG
    public void saveBitmapToJPEG(String path, String filename, Bitmap bmp) {
        Log.d("TAG", "saveBitmapToJPEG");
        FileOutputStream out = null;
        try {

            File file = new File(path, filename);
            Log.d("TAG", file.toString());
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public Bitmap decodeFile(File f) {
        Bitmap b = null;

        int IMAGE_MAX_SIZE = 1024;

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(
                    2,
                    (int) Math.ceil(Math.log(IMAGE_MAX_SIZE
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }

    public String getUTF8StringFromTxtRow(int rawId) {

        try {
            InputStream is = mContext.getResources().openRawResource(rawId);

            BufferedReader in = new BufferedReader(new InputStreamReader(is,
                    "UTF8"));

            String str;

            while ((str = in.readLine()) != null) {

                return str;
            }

        } catch (FileNotFoundException ex) {
             Log.e("getUTF8StringFromTxtRow", "Couldn't find the file " + " " + ex);
             return "";
        } catch (IOException ex) {
             Log.e("getUTF8StringFromTxtRow", "Error reading file " + " " + ex);
             return "";
        }
        return "";
    }
}
