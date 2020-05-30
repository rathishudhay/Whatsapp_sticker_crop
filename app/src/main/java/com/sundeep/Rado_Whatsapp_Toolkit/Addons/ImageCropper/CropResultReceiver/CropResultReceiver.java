package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.CropResultReceiver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.sundeep.Rado_Whatsapp_Toolkit.R;



public class CropResultReceiver extends AppCompatActivity {
    String intentUriGetter="CroppedUri";

    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        imageUri = intent.getParcelableExtra(intentUriGetter);

        ImageView image=(ImageView)findViewById(R.id.imageView);
        Log.d("URI_TYPE", "onCreate: "+imageUri.getPath());
        image.setImageURI(imageUri);

    }




}