package com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.NormalCrop.image;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import com.sundeep.Rado_Whatsapp_Toolkit.Addons.ImageCropper.CropResultReceiver.CropResultReceiver;


/**
 * @author yarolegovich
 * 25.02.2017.
 */
public class CropIwaResultReceiver extends BroadcastReceiver {
    private static final String ACTION_CROP_COMPLETED = "cropIwa_action_crop_completed";
    private static final String EXTRA_ERROR = "extra_error";
    private static final String EXTRA_URI = "extra_uri";

    public static void onCropCompleted(Context context, Uri croppedImageUri) {
//        Intent intent = new Intent(context,EditImageActivity.class);
//        intent.putExtra(EXTRA_URI, croppedImageUri);
//        Log.d("Intent","Intent");
//        context.startActivity(intent);
        Intent intent = new Intent(context, CropResultReceiver.class);
        intent.putExtra("CroppedUri", croppedImageUri);
//        Log.d("Intent","Intent");
        context.startActivity(intent);

    }

    public static void onCropFailed(Context context, Throwable e) {
        Intent intent = new Intent(ACTION_CROP_COMPLETED);
        intent.putExtra(EXTRA_ERROR, e);
        context.sendBroadcast(intent);
    }

    private Listener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (listener != null) {
            if (extras.containsKey(EXTRA_ERROR)) {
                listener.onCropFailed((Throwable) extras.getSerializable(EXTRA_ERROR));
            } else if (extras.containsKey(EXTRA_URI)) {
                listener.onCropSuccess((Uri) extras.getParcelable(EXTRA_URI));
            }
        }
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ACTION_CROP_COMPLETED);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onCropSuccess(Uri croppedUri);

        void onCropFailed(Throwable e);
    }
}
