package com.example.qaz.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qaz.Activity.AddNew.AddNewGroupActivity;
import com.example.qaz.BasicClasses.Group;
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

public class MyGroupsActivity extends AppCompatActivity {
    private List<Group> myGroups = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private int id_u;
    private MyThread myThread;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_groups_menu);
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
        id_u = Integer.parseInt(getIntent().getExtras().getString("id_u"));
        actionButton = findViewById(R.id.add_new_group);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGroupsActivity.this, AddNewGroupActivity.class);
                intent.putExtra("id_u", String.valueOf(id_u));
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_groups);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new FetchItemTask().execute();
        setupAdapter();
    }

    private void setupAdapter(){
        recyclerView.setAdapter(new GroupAdapter(getApplicationContext(), myGroups));
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<Group>> {

        @Override
        protected List<Group> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Request request = maker.GetGroups(id_u);
            List<Group> groups = new ArrayList<>();
            try {
                Response response = client.newCall(request).execute();
                JSONObject all_groups = new JSONObject(response.body().string());
                Log.e("all_groups", all_groups.toString());
                for (Iterator<String> it = all_groups.keys(); it.hasNext(); ) {
                    List<Integer> id_u = new ArrayList<>(), id_d = new ArrayList<>();
                    String id_g_s = it.next();
                    int id_g = Integer.parseInt(id_g_s);
                    JSONArray all_info = all_groups.getJSONArray(id_g_s);
                    Log.e("all_info", all_info.toString());

                    JSONArray id_u_j  = all_info.getJSONArray(0);
                    for (int i = 0; i<id_u_j.length(); i++) id_u.add(id_u_j.getInt(i));
                    Log.e("id_u", id_u.toString());

                    String name = all_info.getString(1);

                    JSONArray id_d_j = all_info.getJSONArray(2);
                    for (int i = 0; i<id_d_j.length(); i++) id_d.add(id_d_j.getInt(i));
                    Log.e("id_d", id_d.toString());

                    Group group = new Group();
                    group.setId_g(id_g);
                    group.setId_u(id_u);
                    group.setName(name);
                    group.setId_d(id_d);
                    groups.add(group);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("groups", String.valueOf(groups.size()));
            return groups;
        }
        @Override
        protected void onPostExecute(List<Group> groups){
            myGroups = groups;
            setupAdapter();
        }
    }

    public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
        private List<Group> android_versions;
        private Context context;

        public GroupAdapter(Context context, List<Group> android_versions) {
            this.context = context;
            this.android_versions = android_versions;

        }
        @Override
        public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_groups_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.button.setText(android_versions.get(i).getName());
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyGroupsActivity.this, GroupMenuActivity.class);
                    intent.putExtra("id_u", android_versions.get(i).getId_u());
                    intent.putExtra("id_d", android_versions.get(i).getId_d());
                    intent.putExtra("id_g", android_versions.get(i).getId_g());
                    intent.putExtra("name", android_versions.get(i).getName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return android_versions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            Button button;
            public ViewHolder(View view) {
                super(view);
                button = view.findViewById(R.id.but_group);
            }
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
                new MyGroupsActivity.FetchItemTask().execute();
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}
