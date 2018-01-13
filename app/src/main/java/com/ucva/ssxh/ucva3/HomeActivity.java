package com.ucva.ssxh.ucva3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

    final static int MEAN_BLUR = 1;
    final static int SHARPEN = 2;
    final static int MEDIAN_BLUR = 3;
    final static int DILATE = 4;
    final static int ERODE = 5;
    final static int THRESHOLD = 6;
    final static int ADAPTIVE_THRESHOLD = 7;
    final static int GAUSSIAN_BLUR = 8;
    final static int TEST = 9;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button bMean = (Button)findViewById(R.id.bMean);
        Button bGaussian = (Button)findViewById(R.id.bGaussian);
        Button bMedian = (Button)findViewById(R.id.bMedian);
        Button bTest = (Button)findViewById(R.id.bTest);
        bMean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("ACTION_MODE",MEAN_BLUR);
                startActivity(i);
            }
        });
        bGaussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("ACTION_MODE",GAUSSIAN_BLUR);
                startActivity(i);
            }
        });
        bMedian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("ACTION_MODE",MEDIAN_BLUR);
                startActivity(i);
            }
        });
        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Test.class);
                i.putExtra("ACTION_MODE",TEST);
                startActivity(i);
            }
        });


    }

}
