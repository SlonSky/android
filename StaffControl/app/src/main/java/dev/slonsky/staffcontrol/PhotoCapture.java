package dev.slonsky.staffcontrol;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Slon on 13.08.2017.
 */

public class PhotoCapture {

    private Camera camera;
    private Camera.PictureCallback picCallback;

    public PhotoCapture(Camera.PictureCallback callback) throws Exception {


        this.picCallback = callback;

        Camera.CameraInfo info = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for(int camI = 0; camI < cameraCount; camI++) {
            Camera.getCameraInfo(camI, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    SurfaceTexture surface = new SurfaceTexture(10);
                    camera = Camera.open(camI);
                    camera.setPreviewTexture(surface);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (camera == null) {
            throw new Exception("Camera not found!");
        }
    }

    public File capturePhoto(String name, String action) {
        File photoFile = null;
        try {
           photoFile = makePhotoFile(makePhotoName(name, action));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            camera.takePicture(null, null, picCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoFile;
    }

    private File makePhotoFile(String filename) throws Exception {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(pictures == null) {
            Log.d("DEBUG", "External storage not available");
            throw new Exception("External storage not available");
        }
        return new File(pictures, filename);

    }

    private String makePhotoName(String name, String action) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return (name + " " + action + " " + day + "-" + month + "-" + year + ".jpg");
    }

    public void releaseCam() {
        camera.release();
    }
}
