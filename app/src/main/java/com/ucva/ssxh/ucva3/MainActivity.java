package com.ucva.ssxh.ucva3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Lenovo on 2018/1/10.
 */

public class MainActivity extends Activity {
    Mat src, src_gray;
    static int ACTION_MODE = 0;
    private final int SELECT_PHOTO = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sss = (Button)findViewById(R.id.sss);
        ImageView ivImage = (ImageView)findViewById(R.id.ivImage);
        ImageView ivImageProcessed = (ImageView)findViewById(R.id.ivImageProcessed);
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        Intent intent = getIntent();
       if(intent.hasExtra("ACTION_MODE")){
            ACTION_MODE = intent.getIntExtra("ACTION_MODE",0);
        }



    }
    private BaseLoaderCallback mOpenCVCallBack = new
            BaseLoaderCallback(this) {
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS:
                            //break;

                        dafault:
                            super.onManagerConnected(status);
                        break;
                    }
                }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_load_iamge ) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        switch(requestCode)
        {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream =
                                getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage =
                                BitmapFactory.decodeStream(imageStream);
                        src = new Mat(selectedImage.getHeight(),selectedImage.getWidth(), CvType.CV_8UC4);
                        Utils.bitmapToMat(selectedImage,src);

                        switch (ACTION_MODE){
                            case HomeActivity.GAUSSIAN_BLUR:
                                Imgproc.GaussianBlur(src, src, new Size(9, 9), 0);
                                break;
                            case HomeActivity.MEAN_BLUR:
                                Imgproc.blur(src, src, new Size(9, 9));
                                break;
                            case HomeActivity.MEDIAN_BLUR:
                                Imgproc.medianBlur(src, src, 9);
                                break;
                            case HomeActivity.SHARPEN:
                                Mat kernel = new Mat(3, 3, CvType.CV_16SC1);
                                //int[] values = {0, -1, 0, -1, 5, -1, 0, -1, 0};
                                Log.d("imageType", CvType.typeToString(src.type()) + "");
                                kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
                                Imgproc.filter2D(src, src, src_gray.depth(), kernel);
                                break;
                            case HomeActivity.DILATE:
                                Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.threshold(src_gray, src_gray, 100, 255, Imgproc.THRESH_BINARY);
                                Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
                                Imgproc.dilate(src_gray, src_gray, kernelDilate);
                                Imgproc.cvtColor(src_gray, src, Imgproc.COLOR_GRAY2RGBA, 4);
                                break;
                            case HomeActivity.ERODE:
                                Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.threshold(src_gray, src_gray, 100, 255, Imgproc.THRESH_BINARY);
                                Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
                                Imgproc.erode(src_gray, src_gray, kernelErode);
                                Imgproc.cvtColor(src_gray, src, Imgproc.COLOR_GRAY2RGBA, 4);
                                break;
                            case HomeActivity.THRESHOLD:
                                Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.threshold(src_gray, src_gray, 100, 255, Imgproc.THRESH_BINARY);
                                Imgproc.cvtColor(src_gray, src, Imgproc.COLOR_GRAY2RGBA, 4);
                                break;
                            case HomeActivity.ADAPTIVE_THRESHOLD:
                                Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.adaptiveThreshold(src_gray, src_gray, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 3, 0);
                                Imgproc.cvtColor(src_gray, src, Imgproc.COLOR_GRAY2RGBA, 4);
                                break;

                        }
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    protected void onResume(){
        super.onResume();
       OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11,this,mOpenCVCallBack);
    }
}