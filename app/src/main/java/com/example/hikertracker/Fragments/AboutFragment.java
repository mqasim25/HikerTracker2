package com.example.hikertracker.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hikertracker.BaseActivity;
import com.example.hikertracker.FragmentLifecycle;
import com.example.hikertracker.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AboutFragment extends Fragment implements FragmentLifecycle {

    Switch gameModeSwitch;
    Boolean trueArrow;

    RelativeLayout linearLayout1, linearLayout2;
    TextView textView;

    boolean isUp;
    public static  boolean trueFalseBoolSwitch = false;
    TextView gameMode, specialMode, versionTxt;
    ImageView arrowImg;

    Animation animation;

    String version;

    //SendData sendData;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    /*public interface SendData {
        void sendAllData(Boolean trueFalseBool, String minutesHours, int step, int min, int max, int progressValue);
    }*/

    @Override
    public void onPauseFragment() {

        Log.i("About", "onAboutFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {

        Log.i("About", "onAboutFragment()");
        //Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("About", "OnAttach");

        /*try {
            mCallback = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }*/

        /*try {
            sendData = (AboutFragment.SendData) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }*/

        trueFalseBoolSwitch = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);



        linearLayout1 = view.findViewById(R.id.li1a1ID);
        linearLayout2 = view.findViewById(R.id.lila2ID);
        textView = view.findViewById(R.id.tvID);
        arrowImg = view.findViewById(R.id.arrowIVID);

        linearLayout1.setVisibility(View.VISIBLE);

        versionTxt = view.findViewById(R.id.versionTxtID);

        try {
            PackageInfo pInfo = Objects.requireNonNull(getContext()).getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionTxt.setText("Version: " + version);

        //trueFalseBoolSwitch = false;


        trueArrow = false;

        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isUp = true;
        gameModeSwitch = view.findViewById(R.id.gameModeSwitchID);
        //gameModeSwitch.setVisibility(View.VISIBLE);

        //linearLayout2.setVisibility(View.GONE);

        //gameModeBool = false;

        //gameModeSwitch.setChecked(false);

        if (BaseActivity.sharedData.getBoolean("GameModeValue", true)) {
            gameModeSwitch.setChecked(true);
            trueFalseBoolSwitch = true;
        }

        gameModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gameModeSwitch.isChecked()) {
                    //do stuff when Switch is ON

                    trueFalseBoolSwitch = true;

                    BaseActivity.sharedEditor.putBoolean("GameModeValue", true);
                    BaseActivity.sharedEditor.apply();

                    //sendData.sendAllData(true, "minutes", 1, 1, 60, 4);

//                    if (GoogleService.mTimer != null) {
//
//                        GoogleService.mTimer.cancel();
//                    }

                    //slideUp(gameModeSwitch);

                    //editor.putString("IsGameModeChecked", String.valueOf(true));
                    //editor.apply();

                    linearLayout2.animate().translationY(0).start();
                    textView.animate().translationY(0).start();
                    //linearLayout2.setVisibility(View.GONE);

                    isUp = true;

                    if (trueArrow) {

                        RotateAnimation rotateAnim = new RotateAnimation(90, 0.0f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);
                        arrowImg.startAnimation(rotateAnim);

                        trueArrow = false;
                    } else {

                        RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);
                        arrowImg.startAnimation(rotateAnim);

                        trueArrow = true;
                    }

                    /*String minsStr = "minutes";

                    final int step = 1;
                    final int min = 1;
                    int max = 60;

                    TextView txtView = (TextView) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.hoursMinID);
                    txtView.setText(minsStr);

                    SeekBar seekBar = (SeekBar) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.seekbarID);
                    seekBar.setMax( (max - min) / step );
                    seekBar.setProgress(4);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            int value = min + (progress * step);
                            TextView txtView = (TextView) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.xTVID);
                            txtView.setText(String.valueOf(value));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });*/

                } else {
                    //do stuff when Switch if OFF

                    trueFalseBoolSwitch = false;

                    BaseActivity.sharedEditor.putBoolean("GameModeValue", false);
                    BaseActivity.sharedEditor.apply();

                    //sendData.sendAllData(false, "hours", 1, 1, 48, 23);

//                    if (GoogleService.mTimer != null) {
//
//                        GoogleService.mTimer.cancel();
//                    }

                    //slideUp(gameModeSwitch);

                    linearLayout2.animate().translationY(0).start();
                    textView.animate().translationY(0).start();
                    //linearLayout2.setVisibility(View.GONE);

                    isUp = true;

                    if (trueArrow) {

                        RotateAnimation rotateAnim = new RotateAnimation(90, 0.0f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);
                        arrowImg.startAnimation(rotateAnim);

                        trueArrow = false;
                    } else {

                        RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);
                        arrowImg.startAnimation(rotateAnim);

                        trueArrow = true;
                    }

                    /*final int step = new HomeFragment().step = 1;
                    final int min = new HomeFragment().min = 1;
                    int max = new HomeFragment().max = 48;

                    String hoursStr = "hours";

                    TextView txtView = (TextView) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.hoursMinID);
                    txtView.setText(hoursStr);

                    SeekBar seekBar = (SeekBar) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.seekbarID);
                    seekBar.setMax( (max - min) / step );
                    seekBar.setProgress(23);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            int value = min + (progress * step);
                            TextView txtView = (TextView) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.xTVID);
                            txtView.setText(String.valueOf(value));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });*/
                }
            }
        });

        if (trueFalseBoolSwitch) {

            gameModeSwitch.setChecked(true);
        } else {

            gameModeSwitch.setChecked(false);
        }

        /*if (gameModeSwitch.isChecked()) {

            trueFalseBoolSwitch = true;
        } else {

            trueFalseBoolSwitch = false;
        }*/

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gameModeSwitch.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);

                //Matrix matrix = new Matrix();
                //arrowImg.setScaleType(ImageView.ScaleType.MATRIX);   //required
                //matrix.postRotate((float) -90, 50, 50);
                //arrowImg.setImageMatrix(matrix);

                if (trueArrow) {

                    RotateAnimation rotateAnim = new RotateAnimation(90, 0.0f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);
                    arrowImg.startAnimation(rotateAnim);

                    trueArrow = false;
                } else {

                    RotateAnimation rotateAnim = new RotateAnimation(0.0f, 90,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);
                    arrowImg.startAnimation(rotateAnim);

                    trueArrow = true;
                }

                //Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_degree);
                //animation.setFillAfter(true);
                //arrowImg.setAnimation(animation);


                if (isUp) {
                    //slideDown(gameModeSwitch);

                    //linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout2.animate().translationY(100).start();
                    textView.animate().translationY(100).start();

                    //animation = AnimationUtils.loadAnimation(getContext(), R.anim.move_down);
                    //gameModeSwitch.setAnimation(animation);

                } else {
                    //slideUp(gameModeSwitch);

                    linearLayout2.animate().translationY(0).start();
                    textView.animate().translationY(0).start();
                    //linearLayout2.setVisibility(View.GONE);
                }
                isUp = !isUp;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("About", "OnActivityCreated");


    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("About", "OnStart");

    }


    // slide the view from below itself to the current position
    public void slideUp(Switch gameModeSwitch){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                gameModeSwitch.getHeight(),           // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        //animate.setInterpolator(new AccelerateInterpolator());
        gameModeSwitch.startAnimation(animate);

        //gameModeSwitch.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(Switch gameModeSwitch){

        gameModeSwitch.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                gameModeSwitch.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        //animate.setInterpolator(new AccelerateInterpolator());
        gameModeSwitch.startAnimation(animate);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();

        Log.e("About", "OnResume");

        //gameModeSwitch.setVisibility(View.VISIBLE);

        /*if (sharedPreferences.contains("IsGameModeChecked")) {

            gameModeSwitch.setChecked(true);
        }*/

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("About", "OnPause");
    }

}




