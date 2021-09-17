package com.example.slago.persondata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.slago.R;
import com.example.slago.SlagoDB.UserData;
import com.example.slago.http.UserData.Http_setUserProfile;
import com.example.slago.utils.UserDataUtils;

public class PersonalSignatureActivity extends AppCompatActivity {
    private View back;
    private TextView titlebar_title;
    private EditText mySignature;
    private Button signatureButton;

    private void initElement() {
        //返回按钮back=(TextView)
        back = findViewById(R.id.layout_titlebar).findViewById(R.id.titlebar_combar_back);
        //获取标题栏标题
        titlebar_title = findViewById(R.id.titlebar_title);

        mySignature = (EditText) findViewById(R.id.set_my_signature);
        UserData userData= UserDataUtils.getAllUserData().get(0);//获取用户信息
        mySignature.setText(userData.getProfile());
    }

    private void bindActionForElement() {
        //设置标题栏文字
        titlebar_title.setText("个性签名");
        back.setClickable(true);//设置为textview可点击的
        //绑定标题栏内的返回按钮字体，作为返回上级事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//结束活动
            }
        });
    }

    private void saveButton(){
        signatureButton = (Button) findViewById(R.id.bt_signature);
        signatureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String editSignature = mySignature.getText().toString();
                new Thread(()->{
                    Http_setUserProfile.push(editSignature);
                }).start();
                finish();//结束活动
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_signature);
        initElement();
        bindActionForElement();
        saveButton();
    }
}