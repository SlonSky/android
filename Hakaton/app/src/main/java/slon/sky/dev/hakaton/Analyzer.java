package slon.sky.dev.hakaton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Slon on 22.04.2017.
 */

public class Analyzer extends AsyncTask<Bitmap, Void, List<Boolean>>{

    private ImageView imageView;
    private Bitmap img;

    public Analyzer(ImageView imageView) {
        this.imageView = imageView;

    }


    String TAG = "CV-Analyzer";

    @Override
    protected List<Boolean> doInBackground(Bitmap... params) {

        Mat image = new Mat();
        Utils.bitmapToMat(params[0], image);

        int height = image.height();
        int width = image.width();

        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        Log.i(TAG, "grayed");

//        String s = "gradients: w " + gray.width() + "\nh " + gray.height() +
//                "\nch " + gray.channels() + "\ndump "+gray.dump();
//        Log.i(TAG, s);
        Mat gradX = new Mat();
        Mat gradY = new Mat();
        Imgproc.Sobel(gray, gradX, CvType.CV_32F, 1, 0, -1, 1, 0);
        Imgproc.Sobel(gray, gradY, CvType.CV_32F, 0, 1, -1, 1, 0);

        Mat gradient = new Mat(gradX.size(), gradX.type());
//        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
//        Core.absdiff(gradX, gradY, gradient);

        Core.subtract(gradX, gradY, gradient);
        Core.convertScaleAbs(gradient, gradient);


        Log.i(TAG, "gradiented");
//
//        Mat morphology = new Mat();
//        Imgproc.morphologyEx(gradient, morphology, Imgproc.MORPH_CLOSE, Mat.ones(7,7, CvType.CV_8U));
//
//        Mat blurred = new Mat();
//        Imgproc.blur(morphology, blurred, new Size(3,3));
//
//        Mat thresh = new Mat();
//        Imgproc.threshold(blurred, thresh, 225, 255, Imgproc.THRESH_BINARY);
//
//        List<MatOfPoint> contours = new ArrayList<>();
//
//        Mat hierarchy = new Mat();
//        Imgproc.findContours(thresh.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        Log.i(TAG, "contoured");
//        Imgproc.drawContours(image, contours, -1, new Scalar(0,0,255), 3);
//        for(int i = 0; i < contours.size(); i++) {
//            if(contours.get(i).toArray().length < 5) {
//                continue;
//            }
//            RotatedRect ellipse = Imgproc.fitEllipse(new MatOfPoint2f(contours.get(i).toArray()));
//            double ratio = Math.max(ellipse.size.width, ellipse.size.height)
//                           /Math.min(ellipse.size.width, ellipse.size.height);
//
//            if(contours.get(i).size().width < 10
//                    || Imgproc.contourArea(contours.get(i)) < 500
//                    || ratio > 3) {
//                Imgproc.drawContours(thresh, contours, i, new Scalar(0,0,0), -1);
//            }
//        }

//        Log.i(TAG, "filled");
//
//        contours = new ArrayList<>();
//        hierarchy = new Mat();
//        Imgproc.findContours(thresh.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        for(int i = 0; i < contours.size(); i++) {
//            Rect rect = Imgproc.boundingRect(contours.get(i));
//
//            double ratio = ((rect.width * rect.height * 1.0) / (width * height * 1.0));
//            double minArea = (width * height) * 0.1;
//            double area = rect.width * rect.height;
//
//            if(ratio < 0.95 && area > 2000 && area < minArea) {
//                Imgproc.drawContours(thresh, contours, i, new Scalar(255, 255, 255), -1);
//                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0), 2);
//            }
//
//
//        }

        Log.i(TAG, "output");

        // temp
        img = Bitmap.createBitmap(image.width(), image.height(), params[0].getConfig());
//        Mat out = new Mat();
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(gradient, img);

        return null;
    }

    @Override
    protected void onPostExecute(List<Boolean> booleen) {
//        super.onPostExecute(booleen);
        imageView.setImageBitmap(img);

        Log.i(TAG, "image set");
    }
}
