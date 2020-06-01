package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.CropIwaView;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.FreeHandCrop.FreeHandCropActivity;
import com.sundeep.Rado_Whatsapp_Toolkit.R;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.sample.config.CropViewConfigurator;
import com.yarolegovich.mp.MaterialPreferenceScreen;

public class CropActivity extends AppCompatActivity {

    private static final String EXTRA_URI = "https://pp.vk.me/c637119/v637119751/248d1/6dd4IPXWwzI.jpg";
    private Button freeHandCrop, rotateImage,flipHorizontal,flipVertical;



    public static Intent callingIntent(Context context, Uri imageUri) {

        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(EXTRA_URI, imageUri);
        return intent;
    }

    private CropIwaView cropView;
    private CropViewConfigurator configurator;
    private Uri imageUri;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.title_crop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imageUri = getIntent().getParcelableExtra(EXTRA_URI);
        cropView = (CropIwaView) findViewById(R.id.crop_view);
        cropView.setImageUri(imageUri);
        MaterialPreferenceScreen cropPrefScreen = (MaterialPreferenceScreen) findViewById(R.id.crop_preference_screen);
        configurator = new CropViewConfigurator(cropView, cropPrefScreen);
        cropPrefScreen.setStorageModule(configurator);
        Log.d("InsideCropActivity","SyncTest123");

        freeHandCrop=(Button)findViewById(R.id.free_hand_crop);
        rotateImage=(Button)findViewById(R.id.rotate_image);
        flipHorizontal=(Button)findViewById(R.id.flip_horizontal);
        flipVertical=(Button)findViewById(R.id.flip_vertical);

        freeHandCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FreeHandCrop","Success");
                Intent intent=new Intent(getApplicationContext(), FreeHandCropActivity.class);
                intent.putExtra("imgUri", imageUri);
                Log.d("Intent","Intent");
                startActivity(intent);
            }
        });

        rotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropView.RotateBitmap(90);
            }
        });

        flipHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropView.flipHorizontalBitmap();
            }
        });

        flipVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropView.flipVerticalBitmap();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_crop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            cropView.crop(configurator.getSelectedSaveConfig());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
