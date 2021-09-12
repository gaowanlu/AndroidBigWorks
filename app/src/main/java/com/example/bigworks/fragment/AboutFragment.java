package com.example.bigworks.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bigworks.R;

import com.example.bigworks.SlagoDB.UserData;
import com.example.bigworks.http.Post.Http_getAboutPosts;
import com.example.bigworks.http.Post.Http_getFindPosts;
import com.example.bigworks.http.Post.Http_getPostData;
import com.example.bigworks.json.getPostData;
import com.example.bigworks.recyclerView.Adapter.Post;
import com.example.bigworks.recyclerView.Adapter.PostAdapter;
import com.example.bigworks.uploadpost.UploadPostActivity;
import com.example.bigworks.utils.UserDataUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bigworks.R;

import com.example.bigworks.SlagoDB.UserData;
import com.example.bigworks.http.Post.Http_getAboutPosts;
import com.example.bigworks.http.Post.Http_getFindPosts;
import com.example.bigworks.http.Post.Http_getPostData;
import com.example.bigworks.json.getPostData;
import com.example.bigworks.recyclerView.Adapter.Post;
import com.example.bigworks.recyclerView.Adapter.PostAdapter;
import com.example.bigworks.uploadpost.UploadPostActivity;
import com.example.bigworks.utils.UserDataUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {
    private View mview;
    private SmartRefreshLayout refreshLayout;
    private ImageView uploadpostbutton;
    private RecyclerView postlist;
    private List<Post> postlistData=new ArrayList<>();
    private PostAdapter postAdapter;
    private RefreshLayout refreshlayouttop;
    private RefreshLayout refreshlayoutbootom;//底部加载更多

    Handler HANDLER=new Handler((Message msg) -> {
        UserData userData= UserDataUtils.getAllUserData().get(0);//获取用户信息
        if(null==userData){ return false;}
        switch (msg.what){
            case 1:
                postAdapter.notifyDataSetChanged();
                if(refreshlayouttop!=null)
                    refreshlayouttop.finishRefresh(0);
                if(refreshlayoutbootom!=null)
                    refreshlayoutbootom.finishLoadMore(0);
                break;
            default:;
        }
        return true;
    });

    private void binActionForElement(View view){
        uploadpostbutton=view.findViewById(R.id.fragment_about_upload);
        //发帖子按钮点击事件
        uploadpostbutton.setOnClickListener(v->{
            Intent intent= new Intent(getActivity(), UploadPostActivity.class);
            startActivity(intent);
        });
        refreshLayout.setOnRefreshListener((RefreshLayout refreshlayout)-> {
            this.refreshlayouttop=refreshlayout;
            reloadPost(true);
        });

        //SmartRefreshLayout控件的加载
        refreshLayout.setOnLoadMoreListener((RefreshLayout refreshlayout) ->{
            this.refreshlayoutbootom=refreshlayout;
            reloadPost(false);
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int flag=0;
        if(mview==null) {
            mview = inflater.inflate(R.layout.fragment_about, container, false);
            initElement(mview);
            //为内部组件绑定事件
            binActionForElement(mview);
            initList();
            //mGlide= Glide.with(getContext());
        }
        return mview;
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //初始化recylerview
    private void initList() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        StaggeredGridLayoutManager layoutManager1=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        postlist.setLayoutManager(layoutManager1);
        postAdapter=new PostAdapter(postlistData);
        postlist.setAdapter(postAdapter);
        //初始化列表数据
        refreshLayout.autoRefresh();
    }

    private void reloadPost(boolean clear){
        new Thread(()->{
            //获取推荐postids
            List<String> postids= Http_getAboutPosts.fetch();
            if(clear) {
                postlistData.clear();
            }
            for(int i=0;i<postids.size();i++){
                String postid=postids.get(i);
                    Log.e("postid",postid);
                    if(checkExist(postid)==false) {
                        loadingPostData(postid);
                    }
            }
            Message message=new Message();
            message.what=1;
            HANDLER.sendMessage(message);
        }).start();
    }

    private void loadingPostData(String postid){
        getPostData data= Http_getPostData.fetch(postid);
        Post post=new Post();
        post.content=data.posttext;
        post.headimg=R.drawable.tempheadimg;
        post.collectioned=data.collectioned;
        post.liked=data.liked;
        post.collectionNum=data.collectionNum;
        post.likeNum=data.likeNum;
        post.userid=data.userid;
        post.imgs=data.imgs;
        post.postdate=data.postdate;
        post.commentNum=data.commentNum;
        post.postid=postid;
        postlistData.add(post);//推进
    }


    //获得view节点
    private void initElement(View view) {
        postlist=view.findViewById(R.id.about_recyclerview);
        refreshLayout=view.findViewById(R.id.about_refreshLayout);
    }

    //检查帖子是否已经存在
    private boolean checkExist(String postid){
        if(postlistData==null||postlistData.size()==0){return false;}
        boolean flag=false;
        for(int i=0;i<postlistData.size();i++){
            if(postid.equals(postlistData.get(i).postid)){
                flag=true;
            }
        }
        return flag;
    }

}

