package com.example.qaz.Activity.AddNew;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qaz.Helpers.MakeRequest;
import com.example.qaz.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNewGroupActivity extends AppCompatActivity {
    private Button add;
    private EditText editName;
    private int id_u;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_group);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
    }
    protected void initViews(){
        id_u = Integer.parseInt(getIntent().getExtras().getString("id_u"));
        add = (Button) findViewById(R.id.but_add_new_desk);
        editName = (EditText) findViewById(R.id.name_of_new_desk);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(editName.getText());
                new FetchItemTask().execute(name, String.valueOf(id_u));
            }
        });
    }
    protected void finish_activity(String s){
        if (s.equals("Ok!")) finish();
    }
    private class FetchItemTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            String name = strings[0], id_u_s = strings[1], text="";
            Request request = maker.AddNewGroup(name, Integer.parseInt(id_u_s));
            try {
                Response response = client.newCall(request).execute();
                text = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }
        @Override
        protected void onPostExecute(String s){
            finish_activity(s);
        }
    }
}
