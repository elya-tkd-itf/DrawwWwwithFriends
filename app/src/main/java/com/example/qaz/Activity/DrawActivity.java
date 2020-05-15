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
        myThread = new MyThread();
        myThread.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myThread.setRunning(false);
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
                imageView.setnowPaint("red");
            }
        });
        green = (Button) findViewById(R.id.but_green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setnowPaint("green");
            }
        });
        yellow = (Button) findViewById(R.id.but_yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setnowPaint("yellow");
            }
        });
        blue = (Button) findViewById(R.id.but_blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setnowPaint("blue");
            }
        });
    }

    public class GetSetImage extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected synchronized Void doInBackground(Void... voids) {
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
            Bitmap muBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            //imageView.setImageBitmap(muBitmap);
            //imageView.setmBitmap(muBitmap);
            //imageView.setmCanvas();
            //imageView.canvasDrawPath();

            base64 = Base64Converter.BitmapToBase64(imageView.getmBitmap());
            request = maker.SetImage(base64, id_d);
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            updCan();
            return null;
        }
    }
    private boolean can = true;
    public void updCan(){can = !can;}
    class MyThread extends Thread{
        private volatile boolean running = true;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            can = true;
            while (running) {
                if (can){
                    updCan();
                    new DrawActivity.GetSetImage().execute();
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}
