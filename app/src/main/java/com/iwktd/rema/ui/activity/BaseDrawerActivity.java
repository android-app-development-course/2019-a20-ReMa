package com.iwktd.rema.ui.activity;

import android.content.Intent;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.view.MenuItem;

import com.iwktd.rema.MyCourseActivity;
import com.iwktd.rema.PersonalInfo;
import com.iwktd.rema.PreviewHistory;
import com.iwktd.rema.myCollectionActivity;
import com.iwktd.rema.myViewHistoryActivity;
import com.iwktd.rema.ui.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import com.iwktd.rema.R;

/**
 * Created by Miroslaw Stanek on 15.07.15.
 */
public class BaseDrawerActivity extends BaseActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;
    private Button button_my_info;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();

    }




    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void setupHeader() {
        View headerView = vNavigation.getHeaderView(0);



        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });





        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_main:
                        Intent intent0 = new Intent();
                        intent0.setClass(BaseDrawerActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.button_personal_info:
                        Intent intent1 = new Intent();
                        intent1.setClass(BaseDrawerActivity.this, PersonalInfo.class);
                        startActivity(intent1);
                        break;
                    case R.id.button_my_comment:
                        Intent intent2 = new Intent();
                        intent2.setClass(BaseDrawerActivity.this, com.iwktd.rema.ui.myComment.MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.button_my_collection:
                        Intent intent3 = new Intent();
                        intent3.setClass(BaseDrawerActivity.this, myCollectionActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.button_add:
                        Intent intent4 = new Intent();
                        intent4.setClass(BaseDrawerActivity.this, com.iwktd.rema.myIssueActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.button_view_history:
                        Intent intent5 = new Intent();
                        intent5.setClass(BaseDrawerActivity.this, myViewHistoryActivity.class);
                        startActivity(intent5);
                        break;


                }
                return true;
            }
            });






        Picasso.with(this)
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this);
                overridePendingTransition(0, 0);
            }
        }, 200);
    }

}
