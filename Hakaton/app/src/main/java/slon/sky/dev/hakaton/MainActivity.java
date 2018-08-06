package slon.sky.dev.hakaton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.bitmap;
import static slon.sky.dev.hakaton.R.id.imageView;

public class MainActivity extends AppCompatActivity{

    private static final int CAPTURE_REQUEST_CODE = 0x01;

    private Button btnMakePhoto;
    private Button btnAnalyze;
    private ImageView ivImage;

    private Bitmap photo;

    private boolean isCVLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ivImage = ((ImageView)findViewById(imageView));

        // temp
        photo = ((BitmapDrawable)ivImage.getDrawable()).getBitmap();

        btnAnalyze = (Button) findViewById(R.id.analyze);
        btnMakePhoto = (Button) findViewById(R.id.photo);

        btnMakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_REQUEST_CODE);
            }
        });

        btnAnalyze.setEnabled(false);
        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start analyzer
                // get result
                // start result activity with result

                if(photo == null) {
                    Toast.makeText(MainActivity.this, "No photo", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                           new Analyzer(ivImage).execute(photo);
                        }
                    }).start();
//                    new Thread(new CVRunnable(photo)).start();

//                    ivImage.post(new CVRunnable(((BitmapDrawable)ivImage.getDrawable()).getBitmap()));
                }
            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAPTURE_REQUEST_CODE:
                Bundle extras = data.getExtras();
                if(extras.get("data") != null) {
                    photo = (Bitmap) extras.get("data");
                    ivImage.setImageBitmap(photo);

                    btnAnalyze.setEnabled(isCVLoaded);
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case LoaderCallbackInterface.SUCCESS:
                    isCVLoaded = true;

                    btnAnalyze.setEnabled(true);
                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    class CVRunnable implements Runnable {

        Bitmap param;

        public CVRunnable(Bitmap param) {
            this.param = param;
        }

        @Override
        public void run() {
            String TAG = "CV-Analyzer";
            Mat image = new Mat();
            Utils.bitmapToMat(param, image);

            int height = param.getHeight();
            int width = param.getWidth();

            Mat gray = new Mat();
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

            Log.i(TAG, "grayed");

            Mat gradX = new Mat();
            Mat gradY = new Mat();
            Imgproc.Sobel(gray, gradX, CvType.CV_32F, 1, 0, -1, 1, 0);
            Imgproc.Sobel(gray, gradY, CvType.CV_32F, 0, 1, -1, 1, 0);


            Log.i(TAG, gradX.width() + " X width");
            Log.i(TAG, gradX.height() + " X heigth");

            Log.i(TAG, gradY.width() + " Y width");
            Log.i(TAG, gradY.height() + " Y heigth");


            Mat gradient = new Mat(height, width, gradX.type());

            Log.i(TAG, gradient.width() + " GRAD width");
            Log.i(TAG, gradient.height() + " GRAD heigth");

            for (int i = 0; i < gradX.height(); i++) {
                for(int j = 0; j < gradX.width(); i++) {
                    Log.i(TAG, gradX.get(i, j) + " X value");
                    Log.i(TAG, gradY.get(i, j) + " Y value");
                }
            }


            Core.subtract(gradX.clone(), gradY.clone(), gradient);
///*            for (int i = 0; i < gradient.height(); i++) {
//                for(int j = 0; j < gradient.width(); i++) {
//                    Log.i(TAG, gradient.get(i, j) + " GRAD value");
//
//                }
//            }*/
            Core.convertScaleAbs(gradient, gradient);
//
//            Log.i(TAG, "gradiented");
//
//            Mat morphology = new Mat();
//            Imgproc.morphologyEx(gradX, morphology, Imgproc.MORPH_CLOSE, Mat.ones(7,7, CvType.CV_8U));

//            Mat blurred = new Mat();
//            Imgproc.blur(morphology, blurred, new Size(3,3));
//
//            Mat thresh = new Mat();
//            Imgproc.threshold(blurred, thresh, 225, 255, Imgproc.THRESH_BINARY);
//
//            List<MatOfPoint> contours = new ArrayList<>();
//
//            Mat hierarchy = new Mat();
//            Imgproc.findContours(thresh.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//            Log.i(TAG, "contoured");
//            for(int i = 0; i < contours.size(); i++) {
//                if(contours.get(i).toArray().length < 10) {
//                    Imgproc.drawContours(thresh, contours, i, new Scalar(0,0,0), -1);
//                    continue;
//                }
//
//                if(Imgproc.contourArea(contours.get(i)) < 500) {
//                    Imgproc.drawContours(thresh, contours, i, new Scalar(0,0,0), -1);
//                    continue;
//                }
//
//                RotatedRect ellipse = Imgproc.fitEllipse(new MatOfPoint2f(contours.get(i).toArray()));
//                double ratio = Math.max(ellipse.size.width, ellipse.size.height)
//                               /Math.min(ellipse.size.width, ellipse.size.height);
//    //
//                if(ratio > 3) {
//                    Imgproc.drawContours(thresh, contours, i, new Scalar(0,0,0), -1);
//                }
//            }
//
//            Log.i(TAG, "filled");
//
//            contours = new ArrayList<>();
//            hierarchy = new Mat();
//            Imgproc.findContours(thresh.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//            ArrayList<Mat> segments = new ArrayList<>();
//            for(int i = 0; i < contours.size(); i++) {
//                Rect rect = Imgproc.boundingRect(contours.get(i));
//
//                double ratio = ((rect.width * rect.height * 1.0) / (width * height * 1.0));
//                double minArea = (width * height) * 0.1;
//                double area = rect.width * rect.height;
//                if(ratio < 0.95 && area > 2000 && area < minArea) {
//                    Imgproc.drawContours(thresh, contours, i, new Scalar(255, 255, 255), -1);
//                    Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0), 2);
//
//                    segments.add(image.submat(rect));
//                }
//
//            }
//
//            Log.i(TAG, "output");

            // temp
            Bitmap img = Bitmap.createBitmap(image.width(), image.height(), Bitmap.Config.ARGB_8888);
//        Mat out = new Mat();
//            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
//
            Utils.matToBitmap(gradY, img);
            ivImage.setImageBitmap(img);
//            Iterator<Mat> iter = segments.iterator();
//            while (iter.hasNext()){
//                Mat m = iter.next();
//                Bitmap bmp = Bitmap.createBitmap(m.width(), m.height(), param.getConfig());
//                Utils.matToBitmap(m, bmp);
//
//                String res = decodeQR(bmp);
//                if(res == null) {
////                    segments.remove();
//                    iter.remove();
//                    Log.i(TAG, "no QR");
//                } else {
//                    Log.i(TAG, "QR: " + res);
//                }
//            }
//            Log.i(TAG, "end");
        }

        private String decodeQR(Bitmap bitmap) {
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try
            {
                Result result = reader.decode(bBitmap);
                return result.getText();
            }
            catch (NotFoundException e)
            {
                return null;
            }
        }

    }

}
