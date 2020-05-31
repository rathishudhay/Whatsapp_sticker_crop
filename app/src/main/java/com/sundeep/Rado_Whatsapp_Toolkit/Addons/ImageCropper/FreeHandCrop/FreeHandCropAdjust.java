package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.FreeHandCrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.MagicToolCrop.CutOut;
import com.sundeep.Rado_Whatsapp_Toolkit.Commons.BitmapTransform;
import com.sundeep.Rado_Whatsapp_Toolkit.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FreeHandCropAdjust extends AppCompatActivity {
    private Bitmap mBitmap;
    private SomeView mSomeView;
    private Uri imageUri;
    ToggleButton toggleButton1;
    Button saveImage;

    public boolean isAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_hand_crop_adjust);
        FreeHandCropAdjustView freeHandCropAdjustView=new FreeHandCropAdjustView(FreeHandCropAdjust.this);
        RelativeLayout rootLayout=(RelativeLayout) findViewById(R.id.activityMain);
        rootLayout.addView(freeHandCropAdjustView);
        toggleButton1=(ToggleButton)findViewById(R.id.toggleButton);
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdd=toggleButton1.getText().equals("On");
                Toast.makeText(FreeHandCropAdjust.this, ""+isAdd, Toast.LENGTH_SHORT).show();
            }
        });

        saveImage=(Button)findViewById(R.id.button);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageAndPassToIntent();
            }
        });

        SeekBar seekbar=(SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                freeHandCropAdjustView.setBrushSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void saveImageAndPassToIntent(){
        Bitmap background=FreeHandCropAdjustView.backgroundBitmap;
        Bitmap originalBitmap=FreeHandCropActivity.fullBitmap;
        Bitmap originalBitmapMutable=Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(originalBitmapMutable);
//        Paint p1=new Paint();

        canvas.drawBitmap(originalBitmap,0,0,new Paint());
        Paint paint=new Paint();
        Canvas backgroundCanvas=new Canvas();
        backgroundCanvas.setBitmap(background);
        Paint paintBackground=new Paint();
        paintBackground.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        backgroundCanvas.drawBitmap(background,0,0,paintBackground);
        backgroundCanvas.drawBitmap(background,0,0,paintBackground);
        backgroundCanvas.drawBitmap(background,0,0,paintBackground);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(background,0,0,paint);

        Bitmap clippedBitmap=BitmapTransform.trim(originalBitmapMutable);

        Uri uri=getImageUri(getApplicationContext(),clippedBitmap);
//        Intent intent = new Intent(getApplicationContext(), CropResultReceiver.class);
//        intent.putExtra("CroppedUri", uri);
////        Log.d("Intent","Intent");
//        startActivity(intent);
        CutOut.activity()
                .src(uri)
                .bordered()
                .noCrop()
                .start(this);


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        String root = Environment.getExternalStorageDirectory().getPath();
        String fname = "Image4.png";
        File file = new File (root, fname);
        FileOutputStream out=null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        mBitmap.setHasAlpha(true);

        inImage.compress(Bitmap.CompressFormat.PNG, 100, out);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}

