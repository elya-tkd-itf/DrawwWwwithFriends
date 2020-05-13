package com.example.qaz.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qaz.Helpers.MakeRequest;
import com.example.qaz.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LogInMenuActivity extends AppCompatActivity{
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
    }
    protected void initViews(){
        Button but_login = (Button) findViewById(R.id.but_login);
        but_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.edit_login);
                EditText pass = (EditText) findViewById(R.id.edit_pass);
                String username = login.getText().toString();
                String password = pass.getText().toString();
                new FetchItemTask().execute(username, password, "1");
            }
        });
        Button but_reg = (Button) findViewById(R.id.but_reg);
        but_reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText login = (EditText) findViewById(R.id.edit_login);
                EditText pass = (EditText) findViewById(R.id.edit_pass);
                String username = login.getText().toString();
                String password = pass.getText().toString();
                new FetchItemTask().execute(username, password, "2");
            }
        });
    }


    public void setupUpdate(List<String> list){
        String text = list.get(0), state = list.get(1);
        TextView textView = (TextView) findViewById(R.id.textView);
        switch (state){
            case "1": if (text.equals("Invalid username") || text.equals("Invalid password"))
                          textView.setText(text);
                      else{
                          Intent intent = new Intent(LogInMenuActivity.this, MyGroupsActivity.class);
                          intent.putExtra("id_u", text);
                          startActivity(intent);
                      } break;
            case "2": textView.setText(text); break;
            case "3": break;
        }
    }

    private class FetchItemTask extends AsyncTask<String, Void, List<String>> {

        private Integer[] integers;

        @Override
        protected List<String> doInBackground(String... strings){
            String username = strings[0], password = strings[1], state = strings[2];
            String text = "";
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Request request = maker.Empty();
            switch (state){
                case "1": request = maker.LogIn(username, password); break;
                case "2": request = maker.Register(username, password); break;
            }
            try {
                Response response = client.newCall(request).execute();
                text = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> list = new ArrayList<>();
            list.add(text); list.add(state);
            return list;
        }

        @Override
        protected void onPostExecute(List<String> list){
            setupUpdate(list);
        }
    }
}
