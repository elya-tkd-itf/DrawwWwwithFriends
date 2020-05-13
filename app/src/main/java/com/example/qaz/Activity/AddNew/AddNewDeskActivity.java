package com.example.qaz.Activity.AddNew;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qaz.Helpers.Base64Converter;
import com.example.qaz.Helpers.MakeRequest;
import com.example.qaz.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNewDeskActivity extends AppCompatActivity {
    Button button;
    Integer id_g;
    TextView textView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_desk);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
    }
    protected void initViews(){
        id_g = getIntent().getExtras().getInt("id_g");
        textView = (TextView) findViewById(R.id.resp_new_desk);
        button = (Button) findViewById(R.id.but_add_new_desk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.name_of_new_desk);
                String name = editText.getText().toString();
                new AddNewDeskActivity.FetchItemTask().execute(name, id_g.toString());
            }
        });
    }
    protected void setupUpdate(String text){
        textView.setText(text);
    }
    private class FetchItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0], id_g_s = strings[1];
            int id_g = Integer.parseInt(id_g_s);
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Resources resources = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.head);
            Request request = maker.AddNewDesk(name, id_g, Base64Converter.BitmapToBase64(bitmap));
            Log.e("s", "s");
            String text = "";
            try {
                Response response = client.newCall(request).execute();
                text = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }
        @Override
        protected void onPostExecute(String text){
            setupUpdate(text);
        }
    }
}
