package com.iwktd.rema;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
                            // Display alert window.
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle(R.string.sign_up_fail_title)
                                    .setMessage(MessageFormat.format(getString(R.string.sign_up_fail_cont), cnt_sign_up))
                                    .setPositiveButton("OK", null)
                                    .show();
                            if (cnt_sign_up == 0){
                                // Disable button_sign_up.
                                button_sign_up.setEnabled(false);
                            }
                        }else{
                            cnt_sign_up = MAX_SIGN_UP;
                            // Jump to welcome page.
                            /*
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle(getString(R.string.sign_up_success))
                                    //.setMessage("用户名或密码不正确， 剩余登陆次数: " + String.valueOf(cnt_sign_up))
                                    .setPositiveButton("OK", null)
                                    .show();
                            */
                            int id = getAccountID(account, pwd);
                            switchToHomePage(id);
                        }
                    }
                }
        );

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


