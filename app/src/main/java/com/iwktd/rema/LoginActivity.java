package com.iwktd.rema;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.iwktd.rema.Models.ModelComments;
import com.iwktd.rema.Models.ModelCourse;
import com.iwktd.rema.Models.ModelTeacher;
import com.iwktd.rema.Models.ModelUser;
import com.iwktd.rema.Network.NetworkInit;
import com.iwktd.rema.Network.SessionOperation;
import com.iwktd.rema.ui.activity.MainActivity;

import java.util.Locale;

import static com.iwktd.rema.ContentOperator.networkInit;
import static com.iwktd.rema.ContentOperator.sessionOperation;


public class LoginActivity extends AppCompatActivity {
    // 2019
    final Handler handler = null;
    final Handler postHandler = new Handler();

    static boolean isChinese;

    public Handler loginHandler;
    public ProgressDialog dialog;

    static {
         isChinese = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // init
        final TextView text_account = findViewById(R.id.text_account);
        final TextView text_pwd = findViewById(R.id.text_pwd);
        final Button button_sign_up = findViewById(R.id.button_sign_up);
        final Switch text_switch_lang = findViewById(R.id.switch_lang);
        final TextView tv_sign_in = findViewById(R.id.tv_sign_in);
        this.isFirstTime();
        loginHandler = new Handler();

        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击注册按钮 跳转sign_in页面（sign_in.xml ------------------未完成
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        text_switch_lang.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Log.d("LoginActivity", "switch lang, with isChinese = " + String.valueOf(isChinese));
                        SwitchLang();
                    }
                }
        );
        // 2019-12
        // 登陆
        button_sign_up.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String account = text_account.getText().toString();
                        String pwd = text_pwd.getText().toString();
                        // 先看看是否登录了
                        boolean is_logined = ContentOperator
                                .getGlobalContext()
                                .getSharedPreferences(ContentOperator.SP_INFO, MODE_PRIVATE)
                                .getBoolean(ContentOperator.IS_LOGINED, false);
                        //int id = getAccountID(account, pwd);
                        networkInit = new NetworkInit(account, pwd, loginHandler, LoginActivity.this);
                        if (!is_logined){
                            Log.d("Login", "try to login.");
                            dialog = ProgressDialog.show(LoginActivity.this, "",
                                    "Loading. Please wait...", true);
                            networkInit.start();
                        }else{
                            Log.d("mmm", "wuwuwu");
                            switchToHomePage();
                        }
                    }
                }
        );
    }

    public void loginFailed(){
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(R.string.sign_up_fail_title)
                .setPositiveButton("OK", null)
                .show();
    }
    // 2019-12
    // Check first time.
    void isFirstTime(){
        SharedPreferences sp = this.getSharedPreferences(ContentOperator.SP_INFO, MODE_PRIVATE);
        boolean is_first_time = sp.getBoolean("is_first_time", true);
        Log.d("Login", "Is First time?" + is_first_time);
        if (is_first_time){
            // 自动建立表
            ContentOperator.zinit(this);
            //sp.edit().putBoolean("is_first_time", false).apply();
            Log.d("Login", "finish initialization");
        }
    }

    // 2019-12
    void saveUserInfoToSP(){
        SharedPreferences sp = this.getSharedPreferences(ContentOperator.SP_INFO, MODE_PRIVATE);
        sp.edit()
                .putString("username", "admin")
                .putInt("uid", 300).apply();
    }

    boolean Check(String account, String pwd){
        // send request to server !

        return true;
    }

    // TODO: Check SP. If didn't sign up, send request to Server.
    int getAccountID(String account, String pwd){
        if (account.equals(new String("account")) &&
                pwd.equals(new String("123456"))){
            return 0;
        }else{
            return -1;
        }
    }

    void SwitchLang(){
        // 获得res资源对象
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
        if (LoginActivity.isChinese){
            config.setLocale(Locale.ENGLISH);
            Log.d("LoginActivity", "set ENG");
            LoginActivity.isChinese = false;
        }else{
            config.setLocale(Locale.SIMPLIFIED_CHINESE);
            Log.d("LoginActivity", "set SCN");
            LoginActivity.isChinese = true;
        }
        Log.d("LoginActivity", "sss");
        resources.updateConfiguration(config, metrics);

        //重新启动Activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    // use for testing.
    // TODO:
    public void switchToHomePage(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //Bundle bundle = new Bundle();
        //bundle.putString("user_name", "nb666233");
        //bundle.putInt("user_id", userID);
        //bundle.putInt("user_stars", 666); // 666个赞
        //bundle.putInt("user_course", 233); // 666个赞
        //bundle.putInt("user_comments", 996); // 666个赞

        if (ContentOperator.getCurrentHash() == "000000"){
            sessionOperation = new SessionOperation(networkInit.session);
        }
        else{
            sessionOperation = new SessionOperation(networkInit.session, ContentOperator.getCurrentHash());
        }
        Log.v("LoginActivity", "Hash = " + ContentOperator.getCurrentHash());
        sessionOperation.update_db();
        saveUserInfoToSP();

        //intent.putExtras(bundle);
        Log.d("LoginActivity", "Start home page");
        startActivity(intent);
    }
    @Override
    protected void onStart(){
        super.onStart();
        ContentOperator.setGlobalContext(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ContentOperator.setGlobalContext(this);
    }
}


