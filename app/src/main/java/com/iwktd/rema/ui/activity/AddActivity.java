package com.iwktd.rema.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;
import com.iwktd.rema.ContentOperator;
import com.iwktd.rema.ModelCourse;
import com.iwktd.rema.ui.adapter.PhotoFiltersAdapter;
import com.iwktd.rema.ui.view.RevealBackgroundView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import com.iwktd.rema.R;
import com.iwktd.rema.Utils;

// 创建课程的视图
public class AddActivity extends BaseActivity
         {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final int STATE_TAKE_PHOTO = 0;
    private static final int STATE_SETUP_PHOTO = 1;

    @BindView(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;

    private boolean pendingIntro; // ?
    private int currentState;
    private File photoPath;

    // 2019-12
    private  Button btnRegister = null;

    public static void startCameraFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, AddActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        updateStatusBarColor();
//        updateState(STATE_TAKE_PHOTO);
        setupRevealBackground(savedInstanceState);
//        setupPhotoFilters();

        int uid = ContentOperator.getUid(this);
        if (uid < 0){
            Log.w("AddActivity", "Didn't get uid!");
        }

        // 2019-12
        btnRegister = findViewById(R.id.btRegister);
        btnRegister.setEnabled(true);
        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cname = ((EditText) findViewById(R.id.et_lessonname)).getText().toString();
                        String tname = ((EditText) findViewById(R.id.et_teacher)).getText().toString();
                        String intro = ((EditText) findViewById(R.id.et_intro)).getText().toString();

                        if (createNewCourse(cname, tname, intro, uid)){
                            Log.d("AddActivity", "Success");
                            finish();
                        }else{
                            Log.e("AddActivity", "Failed to add course");
                        }
                    }
                }
        );

    }

    boolean createNewCourse(String cname, String tname, String intro, int uid){
        ModelCourse tb = new ModelCourse(this, null, 1);
        ContentValues cnt = new ContentValues();
        cnt.put(ModelCourse.tname, tname);
        cnt.put(ModelCourse.cname, cname);
        cnt.put(ModelCourse.intro, intro);
        cnt.put(ModelCourse.uid, String.valueOf(uid));
        int id = (int) tb.getWritableDatabase().insert(
                ModelCourse.tblName,
                null,
                cnt
        );

        if (id >= 0){
            Log.d("AddActivity", "insert success: id = " + id);
        }else{
            Log.e("AddActivity", "insert failed.");
        }

        return id >= 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor() {
        if (Utils.isAndroid5()) {
            getWindow().setStatusBarColor(0xff111111);
        }
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setFillPaintColor(0xffffffff);
//        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }



    class MyCameraHost extends SimpleCameraHost {

        private Camera.Size previewSize;

        public MyCameraHost(Context ctxt) {
            super(ctxt);
        }

        @Override
        public boolean useFullBleedPreview() {
            return true;
        }

        @Override
        public Camera.Size getPictureSize(PictureTransaction xact, Camera.Parameters parameters) {
            return previewSize;
        }

        @Override
        public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
            Camera.Parameters parameters1 = super.adjustPreviewParameters(parameters);
            previewSize = parameters1.getPreviewSize();
            return parameters1;
        }



        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {
            super.saveImage(xact, image);
            photoPath = getPhotoPath();
        }
    }


}
