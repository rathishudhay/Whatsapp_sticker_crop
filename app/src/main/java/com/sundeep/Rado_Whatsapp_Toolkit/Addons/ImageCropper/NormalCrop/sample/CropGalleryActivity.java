package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sundeep.Rado_Whatsapp_Toolkit.R;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.image.CropIwaResultReceiver;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.sample.adapter.CropGalleryAdapter;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.sample.util.Permissions;

public class CropGalleryActivity extends AppCompatActivity implements CropIwaResultReceiver.Listener {

    private static final String TAG_CHOOSE_IMAGE_FRAGMENT = "choose_image";
    private static final String TAG_CONFIRM_DELETE_IMAGE = "confirm_delete";

    private CropIwaResultReceiver cropResultReceiver;
    private CropGalleryAdapter cropGalleryAdapter;

    private static final int REQUEST_CHOOSE_PHOTO = 1101;
    private static final int REQUEST_STORAGE_PERMISSION = 9;


    private View container;
    Button addNewImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_gallery);

//        ***** My Code ******
        addNewImageButton=(Button) findViewById(R.id.AddNewImage);

        addNewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ClickButton","Success");
                startGalleryApp();
            }
        });
    }


// ***** Clone Code Start *****//
    @SuppressLint("NewApi")
    private void startGalleryApp() {
        if (Permissions.isGranted(CropGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent = Intent.createChooser(intent, getString(R.string.title_choose_image));
            startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryApp();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            startCropActivity(data.getData());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startCropActivity(Uri uri) {
        startActivity(CropActivity.callingIntent(getApplicationContext(),uri));

//        dismiss();
    }

//    Clone Code End

    @Override
    public void onCropSuccess(Uri croppedUri) {
        cropGalleryAdapter.addImage(croppedUri);
        Log.d("Received123","Rec");
    }


    @Override
    public void onCropFailed(Throwable e) {
        Snackbar.make(container,
                getString(R.string.msg_crop_failed, e.getMessage()),
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cropResultReceiver.unregister(this);
    }
}
