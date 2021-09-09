package com.example.bigworks.http.Post;

import com.example.bigworks.http.APIData;
import com.example.bigworks.http.Utils;
import com.example.bigworks.json.getFindPosts;
import com.example.bigworks.json.getPostData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http_getFindPosts {
    public static List<String> fetch(){
        List<String> back=new ArrayList<>();
        try{
            OkHttpClient client=new OkHttpClient();
            Request nameRq= Utils.SessionRequest(APIData.URL_MIPR+"/getFindPosts?size=10");
            Gson gson=new Gson();
            Response nameRp = client.newCall(nameRq).execute();
            getFindPosts json=gson.fromJson(nameRp.body().string(), getFindPosts.class);
            if(json.result!=null){
                back=json.result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return back;
        }
    }
}
