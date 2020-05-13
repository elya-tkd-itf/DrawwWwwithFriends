package com.example.qaz.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.qaz.BasicClasses.Desk;
import com.example.qaz.Helpers.Base64Converter;
import com.example.qaz.Helpers.MakeRequest;
import com.example.qaz.Helpers.MyImageView;
import com.example.qaz.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DrawActivity extends AppCompatActivity {
    MyImageView imageView;
    Button red, green, yellow, blue;
    Bitmap image;
    int id_d;
    MyThread myThread;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
    }
    @Override
    protected void onPause() {
        super.onPause();
        myThread.setRunning(false);
    }
    @Override
    protected void onResume(){
        super.onResume();
        myThread = new MyThread();
        myThread.start();
    }

    protected void initViews() {
        id_d = Integer.parseInt(getIntent().getExtras().getString("id_d"));
        String image_base64 = (getIntent().getExtras().getString("image"));
        image = Base64Converter.Base64ToBitMap(image_base64);

        imageView = (MyImageView) findViewById(R.id.picture);
        imageView.setImageBitmap(image);
        imageView.setmBitmap(image);
        imageView.setId_d(id_d);

        red = (Button) findViewById(R.id.but_red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setmPaint("red");
            }
        });
        green = (Button) findViewById(R.id.but_green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setmPaint("green");
            }
        });
        yellow = (Button) findViewById(R.id.but_yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setmPaint("yellow");
            }
        });
        blue = (Button) findViewById(R.id.but_blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setmPaint("blue");
            }
        });

        //new DrawActivity.SetImage().execute();
        //new DrawActivity.GetImage().execute();
    }

    private class GetImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Request request = maker.GetImage(id_d);
            String base64 = "";
            try {
                Response response = client.newCall(request).execute();
                base64 = response.body().string();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Bitmap bitmap = Base64Converter.Base64ToBitMap(base64);
            imageView.setmBitmap(bitmap);
            return null;
        }
    }
    private class SetImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            @SuppressLint("WrongThread") Bitmap bitmap = imageView.getmBitmap();
            String base64 = Base64Converter.BitmapToBase64(bitmap);
            Request request = maker.SetImage(base64, id_d);
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    class MyThread extends Thread{
        private volatile boolean running = true;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                //new DrawActivity.GetImage().execute();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}
