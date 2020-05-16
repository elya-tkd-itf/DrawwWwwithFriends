package com.example.qaz.Helpers;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MakeRequest {
    private String base = "https://0a86ca77.ngrok.io";
    public Request LogIn(String username, String password){
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(base+"/login")
                .post(formBody)
                .build();
        return request;
    }
    public Request Register(String username, String password){
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(base+"/registration")
                .post(formBody)
                .build();
        return request;
    }
    public Request GetGroups(Integer id_u){
        RequestBody formBody = new FormBody.Builder()
                .add("id_u", String.valueOf(id_u))
                .build();
        Request request = new Request.Builder()
                .url(base+"/getgroups")
                .post(formBody)
                .build();
        return request;
    }
    public Request GetDesks(Integer id_g){
        RequestBody formBody = new FormBody.Builder()
                .add("id_g", String.valueOf(id_g))
                .build();
        Request request = new Request.Builder()
                .url(base+"/getdesks")
                .post(formBody)
                .build();
        return request;
    }
    public Request GetUsers(Integer id_g){
        RequestBody formBody = new FormBody.Builder()
                .add("id_g", String.valueOf(id_g))
                .build();
        Request request = new Request.Builder()
                .url(base+"/getusers")
                .post(formBody)
                .build();
        return request;
    }
    public Request GetImage(Integer id_d){
        RequestBody formBody = new FormBody.Builder()
                .add("id_d", String.valueOf(id_d))
                .build();
        Request request = new Request.Builder()
                .url(base+"/getimage")
                .post(formBody)
                .build();
        return request;
    }
    public Request SetImage(String image, int id_d){
        RequestBody formBody = new FormBody.Builder()
                .add("image", image)
                .add("id_d", String.valueOf(id_d))
                .build();
        Request request = new Request.Builder()
                .url(base+"/setimage")
                .post(formBody)
                .build();
        return request;
    }
    public Request AddNewGroup(String name, Integer id_u){
        RequestBody formBody = new FormBody.Builder()
                .add("id_u", String.valueOf(id_u))
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url(base+"/addnewgroup")
                .post(formBody)
                .build();
        return request;
    }
    public Request AddNewUser(String name, Integer id_g){
        RequestBody formBody = new FormBody.Builder()
                .add("id_g", String.valueOf(id_g))
                .add("login", name)
                .build();
        Request request = new Request.Builder()
                .url(base+"/addnewuser")
                .post(formBody)
                .build();
        return request;
    }
    public Request AddNewDesk(String name, Integer id_g, String image){
        RequestBody formBody = new FormBody.Builder()
                .add("id_g", String.valueOf(id_g))
                .add("name", name)
                .add("image", image)
                .build();
        Request request = new Request.Builder()
                .url(base+"/addnewdesk")
                .post(formBody)
                .build();
        return request;
    }
    public Request Empty(){
        Request request = new Request.Builder()
                .url(base)
                .get()
                .build();
        return request;
    }
}
