package com.iwktd.rema;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.iwktd.rema.ui.activity.MainActivity;

import java.text.MessageFormat;
import java.util.Locale;


public class SignUpActivity extends AppCompatActivity {
    final int MAX_SIGN_UP = 5; // 最大登陆次数
    int cnt_sign_up;
    static boolean isChinese;
    static {
         isChinese = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // init
        cnt_sign_up = MAX_SIGN_UP;
        final TextView text_account = findViewById(R.id.text_account);
        final TextView text_pwd = findViewById(R.id.text_pwd);
        final Button button_sign_up = findViewById(R.id.button_sign_up);
        final Switch text_switch_lang = findViewById(R.id.switch_lang);
        final TextView tv_sign_in = findViewById(R.id.tv_sign_in);

        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击注册按钮 跳转sign_in页面（sign_in.xml ------------------未完成
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });


        text_switch_lang.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Log.d("SignUpActivity", "switch lang, with isChinese = " + String.valueOf(isChinese));
                        SwitchLang();
                    }
                }
        );
        button_sign_up.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String account = text_account.getText().toString();
                        String pwd = text_pwd.getText().toString();
                        if (!Check(account, pwd)){
                            cnt_sign_up -= 1;
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle(R.string.sign_up_fail_title)
                                    .setMessage(MessageFormat.format(getString(R.string.sign_up_fail_cont), cnt_sign_up))
                                    .setPositiveButton("OK", null)
                                    .show();
                            if (cnt_sign_up == 0){
                                button_sign_up.setEnabled(false);
                            }
                        }else{
                            cnt_sign_up = MAX_SIGN_UP;
                            int id = getAccountID(account, pwd);
                            saveUserInfoToSP();
                            switchToHomePage(id);
                        }
                    }
                }
        );

        this.isFirstTime();

    }
    // 2019-12
    // Check first time.
    void isFirstTime(){
        SharedPreferences sp = this.getSharedPreferences(ContentOperator.SP_INFO, MODE_PRIVATE);
        boolean is_first_time = sp.getBoolean("is_first_time", true);
        Log.d("Login", "Is First time?");
        if (is_first_time){
            // 自动建立表
            ModelUser db_user = new ModelUser(this, null, 1);
            ModelTeacher db_t = new ModelTeacher(this, null, 1);
            ModelCourse db_course = new ModelCourse(this, null, 1);
            ModelComments db_command = new ModelComments(this, null, 1);


            //sp.edit().putBoolean("is_first_time", false).apply();
            Log.d("login", "finish initialization");
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
        return account.equals(new String("account"))
                && pwd.equals(new String("123456"));
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
        if (SignUpActivity.isChinese){
            config.setLocale(Locale.ENGLISH);
            Log.d("SignUpActivity", "set ENG");
            SignUpActivity.isChinese = false;
        }else{
            config.setLocale(Locale.SIMPLIFIED_CHINESE);
            Log.d("SignUpActivity", "set SCN");
            SignUpActivity.isChinese = true;
        }
        Log.d("SignUpActivity", "sss");
        resources.updateConfiguration(config, metrics);

        //重新启动Activity
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    // use for testing.
    // TODO:
    void switchToHomePage(int userID){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_name", "nb666233");
        bundle.putInt("user_id", userID);
        bundle.putInt("user_stars", 666); // 666个赞
        bundle.putInt("user_course", 233); // 666个赞
        bundle.putInt("user_comments", 996); // 666个赞

        intent.putExtras(bundle);
        Log.d("SignUpActivity", "Start home page");
        startActivity(intent);
    }
}


