package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.FreeHandCrop;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sundeep.Rado_Whatsapp_Toolkit.R;

import java.util.ArrayList;
import java.util.List;

public class SomeView extends View implements View.OnTouchListener {
    private Paint paint;
    private List<Point> points;

    Dialog freeHandCropDialog;
    int DIST = 2;
    boolean flgPathDraw = true;

    Point mfirstpoint = null;
    boolean bfirstpoint = false;

    Point mlastpoint = null;

    Bitmap bitmap,bitmapCopy;

    Context mContext;
//   Custom code
    private PointF zoomPos;
    private boolean zooming = false;
    private Matrix matrix;
    private Paint paint1;
    private BitmapShader shader;
    private int sizeOfMagnifier = 150;
    private Canvas bitmapCanvas;

//Custom code end


    public SomeView(Context c, Bitmap bitmap) {
        super(c);

        mContext = c;

        //this.bitmap = getResizedBitmap(bitmap,getScreenWidth(),getScreenWidth());
        this.bitmap=bitmap;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 3));
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(R.color.brightGreen));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();

        bfirstpoint = false;

//        Custom code
        init();
//        this.setDrawingCacheEnabled(true);
//        Custom code end

    }

    public SomeView(Context context, AttributeSet attrs) {
        super(context, attrs);


        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(getResources().getColor(R.color.brightGreen));

        points = new ArrayList<Point>();
        bfirstpoint = false;

        this.setOnTouchListener(this);

//        Custom code
        init();
//        Custom code end

    }

    private void init() {
        zoomPos = new PointF(0, 0);
        matrix = new Matrix();
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapCanvas=new Canvas();
        bitmapCopy=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        bitmapCanvas.setBitmap(bitmapCopy);
        bitmapCanvas.drawBitmap(bitmap,0,0,paint);
       // bitmapCanvas.drawBitmap(bitmap, null, new RectF(50, 0, 50, 0), paint);
    }

    public static int getScreenWidth() {


        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
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



    public void onDraw(Canvas canvas) {

            /*Rect dest = new Rect(0, 0, getWidth(), getHeight());

     paint.setFilterBitmap(true); canvas.drawBitmap(bitmap, null, dest, paint);*/
        bitmapCanvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawBitmap(bitmap, 0, 0, paint);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Path path = new Path();
        boolean first = true;

        for (int i = 0; i < points.size(); i ++) {
            Point point = points.get(i);
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            } else {
                mlastpoint = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        bitmapCanvas.drawPath(path,paint);
        canvas.drawPath(path, paint);
//        this.setDrawingCacheEnabled(true);
//        this.buildDrawingCache();
//        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
//        this.setDrawingCacheEnabled(false);

//        View v = new MyCanvas(getApplicationContext());
//        Bitmap bitmap = Bitmap.createBitmap(500/*width*/, 500/*height*/, Bitmap.Config.ARGB_8888);
//        Canvas canvas1 = new Canvas(bitmap);
//        v.draw(canvas);
//        ImageView iv = (ImageView) findViewById(R.id.iv);
//        iv.setImageBitmap(bitmap);

//        bitmap.recycle();

//Custom code
        if (zooming) {
//            bitmap = getDrawingCache();
            shader = new BitmapShader(bitmapCopy, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//            paint1 = new Paint();
            paint1.setShader(shader);
            matrix.reset();
            if(zoomPos.x<(Resources.getSystem().getDisplayMetrics().widthPixels/2)){
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                matrix.postScale(2f, 2f, zoomPos.x*2-width+150, zoomPos.y*2-150);
                paint1.getShader().setLocalMatrix(matrix);
//                Log.d("PosLog123",zoomPos.x+" "+zoomPos.y);
//            canvas.drawPath(path,paint1);
                Paint magnifierBorderPaint=new Paint();
                magnifierBorderPaint.setStyle(Paint.Style.STROKE);
                magnifierBorderPaint.setStrokeWidth(4);
                magnifierBorderPaint.setAntiAlias(true);
                canvas.drawCircle(width-150,150,sizeOfMagnifier+2,magnifierBorderPaint);

                canvas.drawCircle(width-150, 150, sizeOfMagnifier, paint1);
            }else{
                matrix.postScale(2f, 2f, zoomPos.x*2-150, zoomPos.y*2-150);
                paint1.getShader().setLocalMatrix(matrix);
                Log.d("PosLog123",zoomPos.x+" "+zoomPos.y);
//            canvas.drawPath(path,paint1);
                Paint magnifierBorderPaint=new Paint();
                magnifierBorderPaint.setStyle(Paint.Style.STROKE);
                magnifierBorderPaint.setStrokeWidth(4);
                magnifierBorderPaint.setAntiAlias(true);
                canvas.drawCircle(150,150,sizeOfMagnifier+2,magnifierBorderPaint);
                canvas.drawCircle(150, 150, sizeOfMagnifier, paint1);
            }

//            canvas.restore();
        }
//Custom code end
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();




        if (flgPathDraw) {

            if (bfirstpoint) {

                if (comparepoint(mfirstpoint, point)) {
                    // points.add(point);
                    points.add(mfirstpoint);
                    flgPathDraw = false;
                    showcropdialog();
                } else {
                    points.add(point);
                }
            } else {
                points.add(point);
            }

            if (!(bfirstpoint)) {

                mfirstpoint = point;
                bfirstpoint = true;
            }
        }

        invalidate();
        Log.e("Hi  ==>", "Size: " + point.x + " " + point.y);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("Action up*****~~>>>>", "called");
            mlastpoint = point;
            if (flgPathDraw) {
                if (points.size() > 12) {
                    if (!comparepoint(mfirstpoint, mlastpoint)) {
                        flgPathDraw = false;
                        points.add(mfirstpoint);
                        showcropdialog();
                    }
                }
            }
        }

//Custom code
        int action = event.getAction();

        zoomPos.x = event.getX();
        zoomPos.y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                zooming = true;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                zooming = false;
                this.invalidate();
                break;

            default:
                break;
        }


//Custom code end


        return true;
    }

    private boolean comparepoint(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public void fillinPartofPath() {
        Point point = new Point();
        point.x = points.get(0).x;
        point.y = points.get(0).y;

        points.add(point);
        invalidate();
    }

    public void resetView() {
        points.clear();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.brightGreen));

        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 3));
        paint.setStrokeWidth(3);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);


        points = new ArrayList<Point>();
        bfirstpoint = false;

        flgPathDraw = true;
        invalidate();
    }

    private void showcropdialog() {


        Bitmap croppedImageBitmap=((FreeHandCropActivity) mContext).getCroppedImageBitmap();

//
        freeHandCropDialog=new Dialog(getContext());
        freeHandCropDialog.setContentView(R.layout.dialog_free_hand_crop);

//
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        freeHandCropDialog.getWindow().setLayout(metrics.widthPixels*5/6,metrics.heightPixels/2);
        Button redo=(Button)freeHandCropDialog.findViewById(R.id.redo);
        Button adjust=(Button)freeHandCropDialog.findViewById(R.id.adjust);
        Button buttonContinue=(Button)freeHandCropDialog.findViewById(R.id.btn_continue);
        ImageView croppedImageView=(ImageView)freeHandCropDialog.findViewById(R.id.croppedImage);
        croppedImageView.setImageBitmap(croppedImageBitmap);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "redo", Toast.LENGTH_SHORT).show();
                resetView();
                freeHandCropDialog.dismiss();

            }
        });

        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "adjust", Toast.LENGTH_SHORT).show();
                freeHandCropDialog.dismiss();
                ((FreeHandCropActivity) mContext).makeIntentToAdjust(croppedImageBitmap);
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "continue", Toast.LENGTH_SHORT).show();
                ((FreeHandCropActivity) mContext).makeIntentToEditor(croppedImageBitmap);
            }
        });

        freeHandCropDialog.setCancelable(false);

        freeHandCropDialog.show();

//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent;
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        ((FreeHandCropActivity) mContext).cropImage();
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                            /*// No button clicked
//
//     intent = new Intent(mContext, DisplayCropActivity.class); intent.putExtra("crop", false); mContext.startActivity(intent);
//     bfirstpoint = false;*/  resetView();
//
//                        break;
//                }
//            }
//        };
//
//        LayoutInflater factory = LayoutInflater.from(getContext());
//        final View view = factory.inflate(R.layout.dialog_free_hand_crop, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setMessage("Do you Want to save Crop or Non-crop image?")
//                .setView(view)
//                .setPositiveButton("Crop", dialogClickListener)
//                .setNegativeButton("Non-crop", dialogClickListener).show()
//                .setCancelable(false);
    }

    public List<Point> getPoints() {
        return points;
    }
}