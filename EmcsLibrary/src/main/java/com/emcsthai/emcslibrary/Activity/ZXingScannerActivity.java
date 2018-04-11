package com.emcsthai.emcslibrary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by nakarin on 3/29/2017 AD.
 */

public class ZXingScannerActivity extends AppCompatActivity {

    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        zXingScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        zXingScannerView.setAutoFocus(true);
        setContentView(zXingScannerView);                // Set the scanner view as the content view
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(resultHandler); // Register ourselves as a handler for scan results.
        zXingScannerView.startCamera();          // Start camera on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();           // Stop camera on pause
    }

    /**********************************************************************************
     * ********************************* Listener **************************************
     **********************************************************************************/

    private final ZXingScannerView.ResultHandler resultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            String jsonResult = result.getText();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", jsonResult);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

//            ToastUtils.getInstance().showText("jsonResult : " + jsonResult);
        }
    };
}