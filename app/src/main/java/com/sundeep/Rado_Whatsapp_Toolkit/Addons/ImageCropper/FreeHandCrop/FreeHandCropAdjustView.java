package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.FreeHandCrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class FreeHandCropAdjustView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint,alphaPaint,mergePaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas,colorCanvas,viewCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap,viewBitmap;

    public static Bitmap backgroundBitmap;
    //brush sizes
    private float brushSize, lastBrushSize;
    //erase flag
    private boolean erase=false;

    private boolean isFirstTime = false;

    public static Bitmap backgroundOverlay;


    public FreeHandCropAdjustView(Context context){
        super(context);
        //setBackgroundColor(Color.CYAN);
//        customFunction();
        setupDrawing();
        init();
    }


    //setup drawing
    private void setupDrawing(){

        //prepare for drawing and setup paint stroke properties
        brushSize = 50;
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        //canvasPaint.setColor(Color.GREEN);
    }

    private void init(){
        backgroundBitmap=Bitmap.createBitmap(FreeHandCropActivity.publicBitmap.getWidth(),FreeHandCropActivity.publicBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        colorCanvas = new Canvas(backgroundBitmap);
        colorCanvas.drawColor(Color.GREEN);
         alphaPaint=new Paint();
        alphaPaint.setAlpha(100);
        alphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvasBitmap=(FreeHandCropActivity.publicBitmap);
        colorCanvas.drawBitmap(canvasBitmap,0,0,alphaPaint);
        mergePaint=new Paint();
        mergePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        viewBitmap=Bitmap.createBitmap(FreeHandCropActivity.fullBitmap.getWidth(),FreeHandCropActivity.fullBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        viewCanvas=new Canvas(viewBitmap);
        viewCanvas.drawBitmap(FreeHandCropActivity.fullBitmap,0,0,new Paint());
//        viewCanvas.drawBitmap(canvasBitmap,0,0,mergePaint);
    }

    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasBitmap=(FreeHandCropActivity.publicBitmap);
//        Bitmap canvasBackGroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        canvasBackGroundBitmap = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.aa),h,w);
        //      drawCanvas = new Canvas(canvasBackGroundBitmap);
        //      drawCanvas.drawColor(Color.GREEN);
//        setBackgroundDrawable(new BitmapDrawable(canvasBackGroundBitmap));

//        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas=new Canvas(canvasBitmap);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {

        if(((FreeHandCropAdjust)getContext()).isAdd){
            setErase(false);
        }else{
            setErase(true);
        }

        colorCanvas.drawColor(Color.BLUE);
//        canvasBitmap=(FreeHandCropActivity.publicBitmap);
        colorCanvas.drawBitmap(canvasBitmap,0,0,alphaPaint);
//        Canvas alphaCanvas=new Canvas();
//        alphaCanvas.drawBitmap(backgroundBitmap, 0, 0, canvasPaint);
        canvas.drawBitmap(viewBitmap, 0, 0, canvasPaint);
        canvas.drawBitmap(backgroundBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                //drawPath.lineTo(touchX, touchY);
                //drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;
    }





    //update color
    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    //set brush size
    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    //get and set last brush size
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    //set erase true or false
    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else {
            drawPaint.setXfermode(null);
        }
    }

    //start new drawing
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
