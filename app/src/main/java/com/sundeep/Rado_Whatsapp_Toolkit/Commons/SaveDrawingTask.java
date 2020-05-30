package com.sundeep.Rado_Whatsapp_Toolkit.Commons;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.MagicToolCrop.CutOut;
import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.MagicToolCrop.CutOutActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static android.view.View.VISIBLE;

public class SaveDrawingTask extends AsyncTask<Bitmap, Void, Pair<File, Exception>> {

    private static final String SAVED_IMAGE_FORMAT = "png";
    private static final String SAVED_IMAGE_NAME = "cutout_tmp";

    private final WeakReference<Activity> activityWeakReference;

    SaveDrawingTask(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        activityWeakReference.get().loadingModal.setVisibility(VISIBLE);
    }

    @Override
    protected Pair<File, Exception> doInBackground(Bitmap... bitmaps) {

        try {
            File file = File.createTempFile(SAVED_IMAGE_NAME, SAVED_IMAGE_FORMAT, activityWeakReference.get().getApplicationContext().getCacheDir());

            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 95, out);

                return new Pair<>(file, null);
            }
        } catch (IOException e) {
            return new Pair<>(null, e);
        }
    }

}