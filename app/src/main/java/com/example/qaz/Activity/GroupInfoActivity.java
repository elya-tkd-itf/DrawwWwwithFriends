package com.example.qaz.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.qaz.Activity.AddNew.AddNewUserActivity;
import com.example.qaz.BasicClasses.Desk;
import com.example.qaz.Helpers.Base64Converter;
import com.example.qaz.Helpers.MakeRequest;
import com.example.qaz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupInfoActivity extends AppCompatActivity {
    private int id_g;
    private String name;
    private TextView textView;
    private List<String> usernames = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private MyThread myThread;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);
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

    protected void initViews(){
        id_g = getIntent().getExtras().getInt("id_g");
        name = getIntent().getExtras().getString("name");

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(name);

        button = (FloatingActionButton) findViewById(R.id.but_group_add_user);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, AddNewUserActivity.class);
                intent.putExtra("id_g", id_g);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setupAdapter();
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public void setupAdapter(){
        recyclerView.setAdapter(new GroupInfoActivity.GroupAdapter(getApplicationContext(), usernames));
    }

    public class GroupAdapter extends RecyclerView.Adapter<GroupInfoActivity.GroupAdapter.ViewHolder> {
        private List<String> android_versions;
        private Context context;

        public GroupAdapter(Context context, List<String> android_versions) {
            this.context = context;
            this.android_versions = android_versions;

        }
        @Override
        public GroupInfoActivity.GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_info_item, viewGroup, false);
            return new GroupInfoActivity.GroupAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupInfoActivity.GroupAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.username.setText(android_versions.get(i));
        }

        @Override
        public int getItemCount() {
            return android_versions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            RadioButton username;
            public ViewHolder(View view) {
                super(view);
                username = view.findViewById(R.id.but_username);
            }
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Request request = maker.GetUsers(id_g);
            List<String> users = new ArrayList<>();
            try {
                Response response = client.newCall(request).execute();
                JSONArray users_s = new JSONArray(response.body().string());
                for (int i = 0; i < users_s.length(); i++)
                    users.add(users_s.getJSONArray(i).getString(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return users;
        }
        @Override
        protected void onPostExecute(List<String> users){
            usernames = users;
            setupAdapter();
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
                new GroupInfoActivity.FetchItemTask().execute();
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}


