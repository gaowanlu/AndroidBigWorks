package com.example.bigworks.http.Post;

import android.util.Log;

import com.example.bigworks.SlagoDB.UserData;
import com.example.bigworks.http.APIData;
import com.example.bigworks.http.Utils;
import com.example.bigworks.json.getPostData;
import com.example.bigworks.json.getUserName;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http_getPostData {
    public static getPostData fetch(String postid){
        getPostData back=new getPostData();
        try{
            OkHttpClient client=new OkHttpClient();
            Request nameRq= Utils.SessionRequest(APIData.URL_MIPR+"/getPostData?postid="+ postid);
            Gson gson=new Gson();
            Response nameRp = client.newCall(nameRq).execute();
            String temp=nameRp.body().string();
            back=gson.fromJson(temp,getPostData.class);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return back;
        }
    }
}
