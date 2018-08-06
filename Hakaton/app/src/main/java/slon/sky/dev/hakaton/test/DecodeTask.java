//package slon.sky.dev.hakaton.test;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.NotFoundException;
//import com.google.zxing.RGBLuminanceSource;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//import slon.sky.dev.hakaton.MainActivity;
//
///**
// * Created by Slon on 22.04.2017.
// */
//
//class DecodeTask extends AsyncTask<Uri, Void, Result>
//{
//
//    @Override
//    protected Result doInBackground(Uri... params) {
//
//        Uri uri = params[0];
//        String TAG = "mylog";
//
//        try
//        {
//
//            InputStream inputStream = MainActivity.this.getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            if (bitmap == null)
//            {
//                Log.e(TAG, "uri is not a bitmap," + uri.toString());
//                return null;
//            }
//            int width = bitmap.getWidth(), height = bitmap.getHeight();
//            int[] pixels = new int[width * height];
//            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//            bitmap.recycle();
//            bitmap = null;
//            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
//            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
//            MultiFormatReader reader = new MultiFormatReader();
//            try
//            {
//                Result result = reader.decode(bBitmap);
//                return result;
//            }
//            catch (NotFoundException e)
//            {
//                Log.e(TAG, "decode exception", e);
//                return null;
//            }
//        }
//        catch (FileNotFoundException e)
//        {
//            Log.e(TAG, "can not open file" + uri.toString(), e);
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(Result result) {
////        textView.setText(result.getText());
//    }
//}
