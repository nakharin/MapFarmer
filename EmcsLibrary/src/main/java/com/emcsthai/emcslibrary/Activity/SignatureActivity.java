package com.emcsthai.emcslibrary.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.emcsthai.emcslibrary.Model.Utils.ToastUtils;
import com.emcsthai.emcslibrary.R;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;

/**
 * Created by nakarin on 3/27/2017 AD.
 */

public class SignatureActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SignatureView signatureView;

    public static final int SIGNATURE_CODE = 5555;
    public static final String SIGNATURE_NAME = "SIGNATURE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        //Method From this class
        initInstances();
    }

    private void initInstances() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.str_sign);
        setSupportActionBar(toolbar);

        signatureView = (SignatureView) findViewById(R.id.signature_view);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private byte[] getSignature() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signature, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        } else if (i == R.id.action_save) {
            Intent intent = new Intent();
            intent.putExtra(SIGNATURE_NAME, getSignature());
            setResult(RESULT_OK, intent);
            ToastUtils.getInstance().showText("บันทึกเรียบร้อย");
            finish();
            return true;
        } else if (i == R.id.action_clear) {
            signatureView.clearCanvas();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }
}

