package com.example.bigworks.http.UserData;

import com.example.bigworks.SlagoDB.UserData;
import com.example.bigworks.http.APIData;
import com.example.bigworks.http.Utils;
import com.example.bigworks.json.StatusResult;
import com.example.bigworks.utils.UserDataUtils;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http_setUserProfile {
    public static boolean push(String newProfile){
        boolean back=false;
        try{
            OkHttpClient client=new OkHttpClient();
            Request nameRq= Utils.SessionRequest(APIData.URL_MIPR+"setUserProfile?newProfile="+ newProfile);
            Gson gson=new Gson();
            Response nameRp = client.newCall(nameRq).execute();
            StatusResult rp=gson.fromJson(nameRp.body().string(), StatusResult.class);
            //更新数据库
            back=rp.result;
            if(back) {
                UserData userData = new UserData();
                userData.setProfile(newProfile);
                userData.updateAll("userid = ?", UserDataUtils.getUserid());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return back;
        }
    }
}