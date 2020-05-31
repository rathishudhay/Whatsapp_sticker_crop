package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.FreeHandCrop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.MagicToolCrop.CutOut;
import com.sundeep.Rado_Whatsapp_Toolkit.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class FreeHandCropActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private SomeView mSomeView;
    private Uri imageUri;
    public static Bitmap publicBitmap,fullBitmap;
    double POINTS_DISTANCE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_hand_crop);

        getIntentData();

        if(mBitmap==null){
            Bitmap tempBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.bb);
            float width=tempBitmap.getWidth();
            float height=tempBitmap.getHeight();
            float bitmapRatio=width/height;

            mBitmap = getResizedBitmap(tempBitmap,getScreenWidth(),getScreenWidth()/bitmapRatio);
        }
        mSomeView = new SomeView(this, mBitmap);
        LinearLayout layout = findViewById(R.id.layout);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(mSomeView, lp);
    }

    void getIntentData(){
        Intent intent = getIntent();
        imageUri=null;
        if (intent != null) {
            imageUri = intent.getParcelableExtra("imgUri");
            if (imageUri != null) {
                Log.d("EditImageActivity","Success");
//                    source.setImageURI(imageUri);
            }
        }
        try {
            Bitmap tempBitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            float width=tempBitmap.getWidth();
            float height=tempBitmap.getHeight();
            float bitmapRatio=width/height;
            mBitmap = getResizedBitmap(tempBitmap,getScreenWidth(),getScreenWidth()/bitmapRatio);

//            mPhotoEditor.addImage(bitmap);
        }catch(Exception e){
            Log.d("EditImageActivity",e.toString());
        }
    }

    //new
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Bitmap getResizedBitmap(Bitmap bm, float newWidth, float newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    //new

    public Bitmap getCroppedImageBitmap() {
//        setContentView(R.layout.activity_picture_preview);
//        ImageView imageView = findViewById(R.id.image);
//
//        Bitmap fullScreenBitmap =
//                Bitmap.createBitmap(mSomeView.getWidth(), mSomeView.getHeight(), mBitmap.getConfig());
//
//        Canvas canvas = new Canvas(fullScreenBitmap);
//
//        Path path = new Path();
//        List<Point> points = mSomeView.getPoints();
//        for (int i = 0; i < points.size(); i++) {
//            path.lineTo(points.get(i).x, points.get(i).y);
//        }
//
//        // Cut out the selected portion of the image...
//        Paint paint = new Paint();
//        canvas.drawPath(path, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(mBitmap, 0, 0, paint);
//
//        // Frame the cut out portion...
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(0.001f);
//        canvas.drawPath(path, paint);
//
//        // Create a bitmap with just the cropped area.
//        Region region = new Region();
//        Region clip = new Region(0, 0, fullScreenBitmap.getWidth(), fullScreenBitmap.getHeight());
//        region.setPath(path, clip);
//        Rect bounds = region.getBounds();
//        Bitmap croppedBitmap =
//                Bitmap.createBitmap(fullScreenBitmap, bounds.left, bounds.top,
//                        bounds.width(), bounds.height());

//        setContentView(R.layout.activity_picture_preview);

//        ImageView imageView = findViewById(R.id.image);

        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
        }
        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        Bitmap bitmap2 = mBitmap;
        fullBitmap=mBitmap;
//    Uri uri=getImageUri(getApplicationContext(),mBitmap);

        Bitmap resultingImage = Bitmap.createBitmap(widthOfscreen,
                heightOfScreen, Bitmap.Config.ARGB_8888);

        resultingImage.setHasAlpha(true);

        Canvas canvas = new Canvas(resultingImage);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.lightRed));
        Path path = new Path();

        List<Point> points = mSomeView.getPoints();

        Log.d("pointsSize",""+points.size());

//        draw path
        Point prevPoint=points.get(0);
        path.lineTo(prevPoint.x,prevPoint.y);

        for (int i = 0; i < points.size(); i++) {
            if(getDistanceBetweenPoints(prevPoint,points.get(i))>POINTS_DISTANCE){
                path.lineTo(points.get(i).x, points.get(i).y);
                prevPoint=points.get(i);
            }
        }
        path.lineTo(points.get(points.size()-1).x, points.get(points.size()-1).y);

        canvas.drawPath(path, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        publicBitmap=resultingImage;
        canvas.drawBitmap(bitmap2, 0, 0, paint);
//         Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, resultingImage.getWidth(), resultingImage.getHeight());
        region.setPath(path, clip);
        Rect bounds = region.getBounds();
        Bitmap croppedBitmap =
                Bitmap.createBitmap(resultingImage, bounds.left, bounds.top,
                        bounds.width(), bounds.height());
//        publicBitmap=resultingImage;
        return croppedBitmap;
    }



    public void makeIntentToEditor(Bitmap croppedBitmap){
//        Intent intent = new Intent(getApplicationContext(), CropResultReceiver.class);
//        intent.putExtra("CroppedUri", getImageUri(getApplicationContext(),croppedBitmap));
//        startActivity(intent);
        CutOut.activity()
                .src(getImageUri(getApplicationContext(),croppedBitmap))
                .bordered()
                .noCrop()
                .start(this);

    }


    public void makeIntentToAdjust(Bitmap croppedBitmap){
        Intent intent = new Intent(getApplicationContext(), FreeHandCropAdjust.class);
        intent.putExtra("CroppedUri", getImageUri(getApplicationContext(),croppedBitmap));
        intent.putExtra("OriginalImageUri",imageUri);
        startActivity(intent);
//        setContentView(R.layout.activity_free_hand_crop_adjust);
//        FreeHandCropAdjustView freeHandCropAdjustView=new FreeHandCropAdjustView(FreeHandCropActivity.this);
//        RelativeLayout rootLayout=(RelativeLayout) findViewById(R.id.activityMain);
//        rootLayout.addView(freeHandCropAdjustView);
    }





    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

//        String root = Environment.getExternalStorageDirectory().getPath();
        try {
            File file = File.createTempFile("cutout_tmp", "png", Environment.getExternalStorageDirectory());
            FileOutputStream out = new FileOutputStream(file);
            inImage.compress(Bitmap.CompressFormat.PNG, 95, out);

        }catch(IOException e){
            e.printStackTrace();
        }
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
//
//        String root = Environment.getExternalStorageDirectory().getPath();
//        String fname = "Image4.png";
//        File file = new File (root, fname);
//        FileOutputStream out=null;
//        try {
//            out = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        mBitmap.setHasAlpha(true);
//
//        inImage.compress(Bitmap.CompressFormat.PNG, 100, out);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);

    }


    public double getDistanceBetweenPoints(Point point1,Point point2){
        double distance=Math.sqrt((point2.x-point1.x)*(point2.x-point1.x) + (point2.y-point1.y)*(point2.y-point1.y));
        return distance;
    }



}

