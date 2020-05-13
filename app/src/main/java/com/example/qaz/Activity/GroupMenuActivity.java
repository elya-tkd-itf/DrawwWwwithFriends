package com.example.qaz.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qaz.Activity.AddNew.AddNewDeskActivity;
import com.example.qaz.BasicClasses.Desk;
import com.example.qaz.BasicClasses.Group;
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

public class GroupMenuActivity extends AppCompatActivity {
    private Group group;
    private TextView nameGroup;
    private FloatingActionButton button_i, button_d;
    private RecyclerView recyclerView;
    private List<Desk> myDesks = new ArrayList<>();
    private MyThread myThread;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_menu);
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
        group = new Group();
        int[] id_u = getIntent().getExtras().getIntArray("id_u");
        group.setId_u(id_u);
        int[] id_d = getIntent().getExtras().getIntArray("id_d");
        group.setId_d(id_d);
        int id_g = getIntent().getExtras().getInt("id_g");
        group.setId_g(id_g);
        String name = getIntent().getExtras().getString("name");
        group.setName(name);

        nameGroup = (TextView) findViewById(R.id.textView);
        nameGroup.setText(name);

        button_i = (FloatingActionButton) findViewById(R.id.but_group_add_user);
        button_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupMenuActivity.this, GroupInfoActivity.class);
                intent.putExtra("id_g", group.getId_g());
                intent.putExtra("name", group.getName());
                startActivity(intent);
            }
        });
        button_d = (FloatingActionButton) findViewById(R.id.but_group_add_desk);
        button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupMenuActivity.this, AddNewDeskActivity.class);
                intent.putExtra("id_g", group.getId_g());
                intent.putExtra("name", group.getName());
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_desks);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        new GroupMenuActivity.FetchItemTask().execute();
        setupAdapter();
    }

    private void setupAdapter(){
        recyclerView.setAdapter(new GroupMenuActivity.GroupAdapter(getApplicationContext(), myDesks));
    }

    public class GroupAdapter extends RecyclerView.Adapter<GroupMenuActivity.GroupAdapter.ViewHolder> {
        private List<Desk> android_versions;
        private Context context;

        public GroupAdapter(Context context, List<Desk> android_versions) {
            this.context = context;
            this.android_versions = android_versions;

        }
        @Override
        public GroupMenuActivity.GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_menu_item, viewGroup, false);
            return new GroupMenuActivity.GroupAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupMenuActivity.GroupAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.imageLabel.setText(android_versions.get(i).getName());
            viewHolder.imageButton.setImageBitmap(android_versions.get(i).getImage());
            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupMenuActivity.this, DrawActivity.class);
                    intent.putExtra("id_d", String.valueOf(android_versions.get(i).getId_d()));
                    intent.putExtra("image", Base64Converter.BitmapToBase64(android_versions.get(i).getImage()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return android_versions.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView imageLabel;
            ImageButton imageButton;
            public ViewHolder(View view) {
                super(view);
                imageLabel = view.findViewById(R.id.image_label);
                imageButton = view.findViewById(R.id.image);
            }
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<Desk>> {

        @Override
        protected List<Desk> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            MakeRequest maker = new MakeRequest();
            Request request = maker.GetDesks(group.getId_g());
            List<Desk> desks = new ArrayList<>();
            try {
                Response response = client.newCall(request).execute();
                JSONObject all_desks = new JSONObject(response.body().string());
                for (Iterator<String> it = all_desks.keys(); it.hasNext(); ) {
                    String id_d_s = it.next();
                    int id_d = Integer.parseInt(id_d_s);

                    JSONArray all_info = all_desks.getJSONArray(id_d_s);

                    String name = all_info.getString(0);
                    String base64_image = all_info.getString(1);
                    Bitmap image = Base64Converter.Base64ToBitMap(base64_image);

                    Desk desk = new Desk();
                    desk.setName(name);
                    desk.setImage(image);
                    desk.setId_d(id_d);
                    desks.add(desk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return desks;
        }
        @Override
        protected void onPostExecute(List<Desk> desks){
            myDesks = desks;
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
                new GroupMenuActivity.FetchItemTask().execute();
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }
}
