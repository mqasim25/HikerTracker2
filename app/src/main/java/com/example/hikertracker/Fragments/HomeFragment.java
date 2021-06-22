package com.example.hikertracker.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikertracker.AppConstants;
import com.example.hikertracker.BaseActivity;
import com.example.hikertracker.FragmentLifecycle;
import com.example.hikertracker.GoogleService;
import com.example.hikertracker.GpsUtils;
import com.example.hikertracker.MainActivity;
import com.example.hikertracker.Models.Email;
import com.example.hikertracker.Models.Locations;
import com.example.hikertracker.R;

import com.example.hikertracker.yourActivityRunOnStartup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startForegroundService;

public class HomeFragment extends Fragment implements FragmentLifecycle {

    //BaseActivity baseActivity = new BaseActivity();

    //private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private boolean isContinue = false;
    private boolean isGPS;

    private double wayLatitude, wayLongitude;

    private StringBuilder stringBuilder;


    Button btn_start, emailBtn, yesBtn, noBtn;
    EditText emailAddressET;
    TextView tv_email, tv_uniqueIdentifier, tv_latitude, tv_longitude, xTV, hoursMinsTxt, contactICETxt;
    SeekBar simpleSeekBar;
    Switch aSwitch;
    Boolean switchTF, emailEntered;
    //LinearLayout contactLL;

    String emailStr;
    String uniqueID;

    SharedPreferences mPref, mPref1, sharedPreferences, latlonPrefs, mPref2;
    SharedPreferences.Editor editor, latlonEdit;
    SharedPreferences.Editor medit, medit1, medit2;

    Double latitude, longitude;
    Geocoder geocoder;

    Dialog dialog, contactDialog;

    FirebaseDatabase database;
    DatabaseReference reference;

    SimpleDateFormat dateFormat;
    String millisInString;

    Boolean trueFalseBoolean = false;
    String minsHours;
    int stepRec, minRec, maxRec, progValue;
    int value, valueRes;

    int step = 1;
    int max = 48;
    int min = 1;

    public static String str_rec = "servicetut.service.receiver";

    //SendMessage SM;

    Intent intenti, intent1;
    //Intent intent;

    Email email;


    //NetworkCheck networkCheck;
    boolean connection;

    //WifiState wifiState;

    //Switch gameModeSwitch;

    @Override
    public void onPauseFragment() {

        Log.i("Home", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {

        Log.i("Home", "onResumeFragment()");
        //Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();

    }


//    public interface SendMessage {
//        void sendData(String message);
//    }

    /*DataPassListener mCallback;

    public interface DataPassListener{
        public void passData(String data);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("Home", "OnAttach");

        //fn_permission();

        /*try {
            mCallback = (DataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }*/

//        try {
//            SM = (SendMessage) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Error in retrieving data. Please try again");
//        }

        BaseActivity.sharedData = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        BaseActivity.sharedEditor = BaseActivity.sharedData.edit();


        if (!BaseActivity.sharedData.getBoolean("GameModeValue", false)) {
            BaseActivity.sharedEditor.putBoolean("GameModeValue", false);
            BaseActivity.sharedEditor.apply();
        }




    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("Home", "OnCreate");

        //networkCheck = new NetworkCheck();
        //wifiState = new WifiState(getContext());

        connection = false;


        isGPS = false;

        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        latlonPrefs = Objects.requireNonNull(getContext()).getSharedPreferences("MyPrefs", MODE_PRIVATE);
        latlonEdit = latlonPrefs.edit();

        getLocation();







        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;

                boolean_permission = isGPS;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        /*if (!isContinue) {
                            //tv_latitude.setText(String.format(Locale.US, "%s", wayLatitude));
                            //tv_longitude.setText(String.format(Locale.US, "%s", wayLongitude));
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                            //txtContinueLocation.setText(stringBuilder.toString());
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }*/

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.e("Home", "OnCreateView");

        btn_start = view.findViewById(R.id.btn_start);
        tv_email = view.findViewById(R.id.tv_email);
        tv_uniqueIdentifier = view.findViewById(R.id.tv_uniqueIdentifier);
        tv_latitude = view.findViewById(R.id.tv_latitude);
        tv_longitude = view.findViewById(R.id.tv_longitude);
        simpleSeekBar = view.findViewById(R.id.seekbarID);
        xTV = view.findViewById(R.id.xTVID);
        hoursMinsTxt = view.findViewById(R.id.hoursMinID);
        aSwitch = view.findViewById(R.id.contactSwitchID);
        //contactLL = view.findViewById(R.id.contactLLID);
        contactICETxt = view.findViewById(R.id.contactedICEID);

        dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.popup_email_layout);

        emailAddressET = dialog.findViewById(R.id.emailAddressETID);
        emailBtn = dialog.findViewById(R.id.saveEABtnID);

        email = new Email();

        //final View view1 = inflater.inflate(R.layout.fragment_about, container, false);
        //gameModeSwitch = view1.findViewById(R.id.gameModeSwitchID);

        //gameModeSwitch.findViewById(R.id.gameModeSwitchID);




        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailAddressET.getText().length() < 1) {

                    Toast.makeText(getContext(), "Email Address cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    emailEntered = true;
                    emailStr = emailAddressET.getText().toString();
                    tv_email.setText(emailStr);

                    uniqueID = Settings.Secure.getString(getContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    tv_uniqueIdentifier.setText(uniqueID);

                    editor.putString("Email", emailStr);
                    editor.putString("UniqueID", uniqueID);
                    editor.apply();

                    email.setEmailaddress(emailStr);

                    dialog.dismiss();

                    //SM.sendData(emailStr);

                    //mCallback.passData(emailStr);

                    //linearLayout.setVisibility(View.GONE);
                /*ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);*/

                    //statusCheck();
                }
            }
        });



        value = 23;
        //valueRes = 5;

        mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        medit = mPref.edit();

        Integer integer1 = value + 1;

        intenti = new Intent(getContext(), GoogleService.class);

        intenti.putExtra("Time", String.valueOf(integer1));
        intenti.putExtra("BoolTrue", false);

        /*Thread thread = new Thread() {
            @Override
            public void run() {

                try {

                    sleep(20000);

                } catch (Exception e) {

                    e.printStackTrace();

                } finally {


                }
            }
        };
        thread.start();*/


        if (isGPS) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (mPref.getString("service", "").matches("")) {
                    medit.putString("service", "service").commit();

                    Log.e("ServiceStarted", "True");

                    startForegroundService(Objects.requireNonNull(getContext()), intenti);
                    //Objects.requireNonNull(getContext()).startService(intenti);
                }

            } else {
                if (mPref.getString("service", "").matches("")) {
                    medit.putString("service", "service").commit();

                    Objects.requireNonNull(getContext()).startService(intenti);
                }
            }
        }




        contactDialog = new Dialog(Objects.requireNonNull(getContext()));
        contactDialog.setContentView(R.layout.popup_contact);

        yesBtn = contactDialog.findViewById(R.id.yesContactBtnID);
        noBtn = contactDialog.findViewById(R.id.noContactBtnID);




        //SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        //String name = sharedPreferences.getString("Email", "");

        String emailS = sharedPreferences1.getString("Email", "");

        //if (sharedPreferences1.contains("Email")) {
        //tv_email.setText(emailS);
        //}

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Check if we need to display email popup
        if (!defaultSharedPreferences.getBoolean("TrueFalseValueShowEmail", false)) {

            dialog.show();
            dialog.setCancelable(false);




            SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            sharedPreferencesEditor.putBoolean("TrueFalseValueShowEmail", true);
            sharedPreferencesEditor.apply();

        } else {

            dialog.dismiss();
            Log.e("Dismiss", "Dismiss");

            if (emailS == null) {

            } else {
                tv_email.setText(emailS);
                //SM.sendData(emailS);
            }

            if (sharedPreferences1.contains("UniqueID")) {
                tv_uniqueIdentifier.setText(sharedPreferences1.getString("UniqueID", ""));
            }
        }

        return view;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("Home", "OnViewCreated");


        //switchTF = false;
        //aSwitch.setChecked(switchTF);

        editor.putBoolean("ICEContactSwitchFalse", sharedPreferences.getBoolean("ICEContactSwitchFalse", false));
        editor.apply();



        emailEntered = false;
        simpleSeekBar.setMax( (max - min) / step );
        simpleSeekBar.setProgress(value);

        valueRes = 4;

        minsHours = "minutes";
        minRec = 1;
        maxRec = 60;
        stepRec = 1;
        //progValue = 4;

        //intenti.putExtra("Time", String.valueOf(value));
        //intenti.putExtra("BoolTrue", false);
        //Objects.requireNonNull(getContext()).startService(intenti);


        //linearLayout.setVisibility(View.VISIBLE);
        /*ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        btn_start.setVisibility(View.GONE);*/



        //int seekBarValue = simpleSeekBar.getProgress();
        //simpleSeekBar.setMax(48);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //   simpleSeekBar.setMin(1);
        //}


        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(aSwitch.isChecked()) {
                    //do stuff when Switch is ON

                    contactDialog.show();
                    contactDialog.setCancelable(false);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            switchTF = true;

                            editor.putBoolean("ICEContactSwitchFalse", true);
                            editor.apply();

                            //contactLL.setVisibility(View.VISIBLE);
                            contactICETxt.setTextColor(Color.RED);
                            contactDialog.dismiss();

                            editor.putString("ICEContactTrue", contactICETxt.getText().toString());
                            editor.apply();
                        }
                    });

                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //contactLL.setVisibility(View.GONE);
                            contactICETxt.setTextColor(Color.WHITE);
                            contactDialog.dismiss();
                            switchTF = false;
                            aSwitch.setChecked(false);

                            editor.putBoolean("ICEContactSwitchFalse", false);
                            editor.putString("ICEContactFalse", contactICETxt.getText().toString());
                            editor.apply();
                        }
                    });


                }
                else {
                    //do stuff when Switch if OFF
                    switchTF = false;
                    //contactLL.setVisibility(View.GONE);
                    contactICETxt.setTextColor(Color.WHITE);

                    editor.putBoolean("ICEContactSwitchFalse", false);
                    editor.putString("ICEContactFalse", contactICETxt.getText().toString());
                    editor.apply();
                }
            }
        });

        if (!sharedPreferences.getBoolean("ICEContactSwitchFalse", false)) {

            contactICETxt.setTextColor(Color.WHITE);
            contactDialog.dismiss();
            switchTF = false;
            aSwitch.setChecked(false);
        } else {

            switchTF = true;
            contactICETxt.setTextColor(Color.RED);
            aSwitch.setChecked(true);
        }


//        if (sharedPreferences.contains("ICEContactSwitchTrue")) {
//            aSwitch.setChecked(true);
//            contactDialog.dismiss();
//            switchTF = true;
//        }
//
//        if (sharedPreferences.contains("ICEContactTrue")) {
//            contactICETxt.setText(sharedPreferences.getString("ICEContactTrue", ""));
//            contactICETxt.setTextColor(Color.RED);
//            switchTF = true;
//        }
//
//
//
//        if (sharedPreferences.contains("ICEContactSwitchFalse")) {
//            aSwitch.setChecked(false);
//            contactDialog.dismiss();
//            switchTF = false;
//        }
//
//        if (sharedPreferences.contains("ICEContactFalse")) {
//            contactICETxt.setText(sharedPreferences.getString("ICEContactFalse", ""));
//            contactICETxt.setTextColor(Color.WHITE);
//            switchTF = false;
//        }






        geocoder = new Geocoder(getContext(), Locale.getDefault());
//        mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
//        medit = mPref.edit();



        mPref1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        medit1 = mPref1.edit();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Objects.requireNonNull(getContext()), intent);
        } else {
            if (mPref1.getString("service1", "").matches("")) {
                medit1.putString("service1", "service1").commit();

                intent = new Intent(getContext(), GoogleService1.class);
                Objects.requireNonNull(getContext()).startService(intent);
            }
        }*/

        //if (trueFalseBoolean != null) {


        //Toast.makeText(getContext(), "Game mode: " + AboutFragment.trueFalseBoolSwitch, Toast.LENGTH_SHORT).show();


        //Toast.makeText(getContext(), "Shared Game mode: " + BaseActivity.sharedData.getBoolean("GameModeValue", false), Toast.LENGTH_SHORT).show();


            if (BaseActivity.sharedData.getBoolean("GameModeValue", false)) {

                BaseActivity.sharedEditor.putBoolean("GameModeValue", true);
                BaseActivity.sharedEditor.apply();



                if (GoogleService.mTimer != null) {

                    GoogleService.mTimer.cancel();
                }

                //Toast.makeText(getContext(), "2: Game mode: " + AboutFragment.trueFalseBoolSwitch, Toast.LENGTH_SHORT).show();

                //gameModeSwitch.setChecked(true);


                hoursMinsTxt.setText(minsHours);

                editor.putString("HoursMinsTrue", minsHours);
                editor.apply();

                simpleSeekBar.setMax( (maxRec - minRec) / stepRec );
                simpleSeekBar.post(new Runnable() {
                    @Override
                    public void run() {

                        valueRes = 4;

                        simpleSeekBar.setProgress(valueRes);
                    }
                });

                //editor.putString("ProgressValueTrue", String.valueOf(valueRes));
                //editor.apply();


                Log.e("ProgressValue", String.valueOf(valueRes));

                //Log.e("TrueFalseBoolTrue", trueFalseBoolean.toString());

                if (sharedPreferences.contains("ProgressValueTrue")) {

                    if (GoogleService.mTimer != null) {

                        GoogleService.mTimer.cancel();

                        intent1 = new Intent(getContext(), GoogleService.class);

                        Integer integer = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("ProgressValueTrue", "")));

                        intent1.putExtra("Time", String.valueOf(integer));
                        intent1.putExtra("BoolTrue", true);

                        simpleSeekBar.post(new Runnable() {
                            @Override
                            public void run() {

                                simpleSeekBar.setProgress(integer - 1);
                            }
                        });


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }

                            Log.e("ServiceStarted", "True");

                            startForegroundService(Objects.requireNonNull(getContext()), intent1);
                            //Objects.requireNonNull(getContext()).startService(intenti);
                            //}

                        } else {
                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }



                            Objects.requireNonNull(getContext()).startService(intent1);
                            //}
                        }

                    }
                } else {

                    if (GoogleService.mTimer != null) {

                        GoogleService.mTimer.cancel();

                        intent1 = new Intent(getContext(), GoogleService.class);

                        intent1.putExtra("Time", String.valueOf(valueRes + 1));
                        intent1.putExtra("BoolTrue", true);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }

                            Log.e("ServiceStarted", "True");

                            startForegroundService(Objects.requireNonNull(getContext()), intent1);
                            //Objects.requireNonNull(getContext()).startService(intenti);
                            //}

                        } else {
                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }



                            Objects.requireNonNull(getContext()).startService(intent1);
                            //}
                        }

                    }
                }



                if (maxRec == 60 && minRec == 1 && stepRec == 1) {

                /*hoursMinsTxt.setText(minsHours);

                simpleSeekBar.setMax( (maxRec - minRec) / stepRec );
                simpleSeekBar.setProgress(progValue);*/


                    //editor.putString("GameModeSwitchOn", "True");


                    Log.e("SwitchTrue", "True");

                    simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            valueRes = minRec + (progress * stepRec);
                            xTV.setText(String.valueOf(valueRes));

                            editor.putString("ProgressValueTrue", String.valueOf(valueRes));
                            editor.apply();

//                        intenti = new Intent(getContext(), GoogleService.class);
//                        intenti.putExtra("Time", String.valueOf(value));
//                        intenti.putExtra("BoolTrue", true);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                            //Objects.requireNonNull(getContext()).stopService(new Intent(getContext(), GoogleService.class));

//                            if (GoogleService.mTimer != null) {
//
//                                GoogleService.mTimer.cancel();
//
//                                Log.e("ServiceTimerStopped", "True");
//                            }


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                if (GoogleService.mTimer != null) {

                                    GoogleService.mTimer.cancel();

                                    Log.e("ServiceTimerStopped", "True");


                                }

                                intent1 = new Intent(getContext(), GoogleService.class);

                                intent1.putExtra("Time", String.valueOf(valueRes));
                                intent1.putExtra("BoolTrue", true);
                                startForegroundService(Objects.requireNonNull(getContext()), intent1);

                            } else {
                                //if (mPref.getString("service", "").matches("")) {
                                //medit.putString("service", "service").commit();

                                if (GoogleService.mTimer != null) {

                                    GoogleService.mTimer.cancel();
                                    //GoogleService.mTimer.purge();
                                    GoogleService.mTimer = null;

                                    Log.e("ServiceTimerStopped", "True");
                                }

                                intent1 = new Intent(getContext(), GoogleService.class);


                                intent1.putExtra("Time", String.valueOf(valueRes));
                                intent1.putExtra("BoolTrue", true);

                                Objects.requireNonNull(getContext()).startService(intent1);

                                Log.e("onStopTrackingTrue", "True");

                                //}
                            }
                        }
                    });
                } else {

                    Log.e("SeekBar", "Seekbar");
                }

            } else {

                BaseActivity.sharedEditor.putBoolean("GameModeValue", false);
                BaseActivity.sharedEditor.apply();

                if (GoogleService.mTimer != null) {

                    GoogleService.mTimer.cancel();
                }

                //gameModeSwitch.setChecked(true);

                minsHours = "hours";

                hoursMinsTxt.setText(minsHours);

                editor.putString("HoursMinsTrue", minsHours);
                editor.apply();

                simpleSeekBar.setMax( (max - min) / step );
                simpleSeekBar.post(new Runnable() {
                    @Override
                    public void run() {

                        value = 23;

                        simpleSeekBar.setProgress(value);
                    }
                });

                //editor.putString("ProgressValueTrue", String.valueOf(valueRes));
                //editor.apply();


                Log.e("ProgressValue", String.valueOf(value));

                //Log.e("TrueFalseBoolTrue", trueFalseBoolean.toString());

                if (sharedPreferences.contains("ProgressValueFalse")) {

                    if (GoogleService.mTimer != null) {

                        GoogleService.mTimer.cancel();

                        intent1 = new Intent(getContext(), GoogleService.class);

                        Integer integer = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("ProgressValueFalse", "")));

                        intent1.putExtra("Time", String.valueOf(integer));
                        intent1.putExtra("BoolTrue", false);

                        simpleSeekBar.post(new Runnable() {
                            @Override
                            public void run() {

                                simpleSeekBar.setProgress(integer - 1);
                            }
                        });


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }

                            Log.e("ServiceStarted", "True");

                            startForegroundService(Objects.requireNonNull(getContext()), intent1);
                            //Objects.requireNonNull(getContext()).startService(intenti);
                            //}

                        } else {
                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }



                            Objects.requireNonNull(getContext()).startService(intent1);
                            //}
                        }

                    }
                } else {

                    if (GoogleService.mTimer != null) {

                        GoogleService.mTimer.cancel();

                        intent1 = new Intent(getContext(), GoogleService.class);

                        intent1.putExtra("Time", String.valueOf(value + 1));
                        intent1.putExtra("BoolTrue", false);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }

                            Log.e("ServiceStarted", "True");

                            startForegroundService(Objects.requireNonNull(getContext()), intent1);
                            //Objects.requireNonNull(getContext()).startService(intenti);
                            //}

                        } else {
                            //if (mPref2.getString("service", "").matches("")) {
                            //medit2.putString("service", "service").commit();

                            if (GoogleService.mTimer != null) {

                                GoogleService.mTimer.cancel();

                                Log.e("ServiceTimerStopped", "True");
                            }



                            Objects.requireNonNull(getContext()).startService(intent1);
                            //}
                        }

                    }
                }



                if (max == 48 && min == 1 && step == 1) {

                /*hoursMinsTxt.setText(minsHours);

                simpleSeekBar.setMax( (maxRec - minRec) / stepRec );
                simpleSeekBar.setProgress(progValue);*/


                    //editor.putString("GameModeSwitchOn", "True");


                    Log.e("SwitchFalse", "False");

                    simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            value = min + (progress * step);
                            xTV.setText(String.valueOf(value));

                            editor.putString("ProgressValueFalse", String.valueOf(value));
                            editor.apply();

//                        intenti = new Intent(getContext(), GoogleService.class);
//                        intenti.putExtra("Time", String.valueOf(value));
//                        intenti.putExtra("BoolTrue", true);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                            //Objects.requireNonNull(getContext()).stopService(new Intent(getContext(), GoogleService.class));

//                            if (GoogleService.mTimer != null) {
//
//                                GoogleService.mTimer.cancel();
//
//                                Log.e("ServiceTimerStopped", "True");
//                            }


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                if (GoogleService.mTimer != null) {

                                    GoogleService.mTimer.cancel();

                                    Log.e("ServiceTimerStopped", "True");


                                }

                                intent1 = new Intent(getContext(), GoogleService.class);

                                intent1.putExtra("Time", String.valueOf(value));
                                intent1.putExtra("BoolTrue", false);
                                startForegroundService(Objects.requireNonNull(getContext()), intent1);

                            } else {
                                //if (mPref.getString("service", "").matches("")) {
                                //medit.putString("service", "service").commit();

                                if (GoogleService.mTimer != null) {

                                    GoogleService.mTimer.cancel();
                                    //GoogleService.mTimer.purge();
                                    GoogleService.mTimer = null;

                                    Log.e("ServiceTimerStopped", "True");
                                }

                                intent1 = new Intent(getContext(), GoogleService.class);


                                intent1.putExtra("Time", String.valueOf(value));
                                intent1.putExtra("BoolTrue", false);

                                Objects.requireNonNull(getContext()).startService(intent1);

                                Log.e("onStopTrackingTrue", "True");

                                //}
                            }
                        }
                    });
                } else {

                    Log.e("SeekBar", "Seekbar");
                }
            }
        //}

        /*if (sharedPreferences.getBoolean("GameModeTrueFalse", true)) {

            AboutFragment.trueFalseBoolSwitch = true;

            //Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
        }*/ /*else {

            AboutFragment.trueFalseBoolSwitch = true;

            Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
        }*/




        //if (isGPS) {





        //} else {

            //Toast.makeText(getContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
            //statusCheck();
        //}





        //database = FirebaseDatabase.getInstance();
        //reference = database.getReference().child("Users");







        /*Intent mIntent = new Intent("com.example.locationaccess");
        //Bundle mBundle = new Bundle();
        //mBundle.putString("Email", emailStr);
        //mBundle.putString("SwitchKey", String.valueOf(switchTF));
        mIntent.putExtra("Email", emailStr);
        mIntent.putExtra("SwitchKey", String.valueOf(switchTF));
        getContext().sendBroadcast(mIntent);*/

        //SM.sendData(emailStr);
        //mCallback.passData(emailStr);

        /*Bundle bundle = new Bundle();
        bundle.putString("Email", emailStr);

        StatusFragment fragment2 = new StatusFragment();
        fragment2.setArguments(bundle);

        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment2)
                .commit();*/


        /*if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = Double.valueOf(intent.getStringExtra("latitude"));
                    longitude = Double.valueOf(intent.getStringExtra("longitude"));

                    if (boolean_permission) {

                        Log.e("Receive", "Rec");
                        //firebaseDatabaseAdd("A");

                        tv_latitude.setText(String.valueOf(latitude));
                        tv_longitude.setText(String.valueOf(longitude));
                    }
                }
            };
        }
        Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("location_update"));*/


//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10 * 1000); // 10 seconds
//        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
//
//        new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
//            @Override
//            public void gpsStatus(boolean isGPSEnable) {
//                // turn on GPS
//                isGPS = isGPSEnable;
//
//                boolean_permission = isGPS;
//            }
//        });
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        wayLatitude = location.getLatitude();
//                        wayLongitude = location.getLongitude();
//                        /*if (!isContinue) {
//                            //tv_latitude.setText(String.format(Locale.US, "%s", wayLatitude));
//                            //tv_longitude.setText(String.format(Locale.US, "%s", wayLongitude));
//                        } else {
//                            stringBuilder.append(wayLatitude);
//                            stringBuilder.append("-");
//                            stringBuilder.append(wayLongitude);
//                            stringBuilder.append("\n\n");
//                            //txtContinueLocation.setText(stringBuilder.toString());
//                        }
//                        if (!isContinue && mFusedLocationClient != null) {
//                            mFusedLocationClient.removeLocationUpdates(locationCallback);
//                        }*/
//
//                        if (mFusedLocationClient != null) {
//                            mFusedLocationClient.removeLocationUpdates(locationCallback);
//                        }
//                    }
//                }
//            }
//        };


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (isGPS) {

                        getLocation();

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                //tv_latitude.setText(String.format(Locale.US, "%s", wayLatitude));
                                //tv_longitude.setText(String.format(Locale.US, "%s", wayLongitude));

                                tv_latitude.setText(String.valueOf(wayLatitude));
                                tv_longitude.setText(String.valueOf(wayLongitude));

                                latlonEdit.putString("Latitude", String.valueOf(wayLatitude));
                                latlonEdit.putString("Longitude", String.valueOf(wayLongitude));
                                latlonEdit.apply();



                                //connection = networkCheck.checkNow(getContext());
                                //connection = wifiState.haveNetworkConnection();

                                ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);


                                NetworkInfo[] netInfo = connectivityManager != null ? connectivityManager.getAllNetworkInfo() : new NetworkInfo[0];

                                for (NetworkInfo ni : netInfo) {
                                    if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                                        if (ni.isConnected())
                                            connection = true;
                                }

                                if (connection) {
                                // user is on wifi
                                    String manual = "M";
                                    firebaseDatabaseAdd(manual);

                                    ((MainActivity) getActivity()).jumpToPage(view);
                                }else{
                                // user is on mobile data
                                    String manual = "M";
                                    firebaseDatabaseAdd(manual);

                                    ((MainActivity) getActivity()).jumpToPage(view);
                                }

                                /*if (connection) {

                                    String manual = "M";
                                    firebaseDatabaseAdd(manual);

                                    ((MainActivity) getActivity()).jumpToPage(view);
                                } else {
                                    Toast.makeText(getContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                                }*/


                                //ViewPager viewPager = new ViewPager(Objects.requireNonNull(getContext()));
                                //viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);




                                isGPS = true;
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                                //isGPS = false;
                            }
                        });

                    } else {

                        Toast.makeText(getContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();

                        isGPS = false;


//                        String manual = "M";
//                        firebaseDatabaseAdd(manual);


                        //tv_latitude.setText(String.valueOf(latitude));
                        //tv_longitude.setText(String.valueOf(longitude));
                    }







//                if (boolean_permission) {
//
//                    //Intent intent = new Intent(getContext(), GoogleService1.class);
//                    //intenti.putExtra("BoolTrue", true);
//                    //Objects.requireNonNull(getContext()).startService(intent);
//
//                    String manual = "M";
//                    //firebaseDatabaseAdd(manual);
//
//                    tv_latitude.setText(String.valueOf(latitude));
//                    tv_longitude.setText(String.valueOf(longitude));
//
//
//                    /*if (aSwitch != null) {
//                        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                if(isChecked) {
//                                    //do stuff when Switch is ON
//                                    switchTF = true;
//                                } else {
//                                    //do stuff when Switch if OFF
//                                    switchTF = false;
//                                }
//                            }
//                        });
//                    }
//
//                    Email email = new Email(emailStr);
//                    reference.setValue(email);
//
//                    reference = database.getReference().child("Users").child("Email").child("Locations").push();
//
//                    tv_latitude.setText(String.valueOf(latitude));
//                    tv_longitude.setText(String.valueOf(longitude));
//
//                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    millisInString = dateFormat.format(new Date());
//
//                    Locations locations = new Locations(latitude, longitude, millisInString, switchTF, "M");
//
//                    reference.setValue(locations);
//
//                    reference = database.getReference().child("Users").child("Email").child("UDID");
//                    //uniqueID = UUID.randomUUID().toString();
//                    uniqueID = Settings.Secure.getString(getContext().getContentResolver(),
//                            Settings.Secure.ANDROID_ID);
//                    //tv_uniqueIdentifier.setText(uniqueID);
//                    tv_uniqueIdentifier.setText(uniqueID);
//                    reference.setValue(uniqueID);*/
//
//                    /*Locations locations = new Locations();
//                    locations.setLatitude(latitude);
//                    locations.setLongitude(longitude);
//                    locations.setTimestamp(millisInString);
//                    locations.setContact(switchTF);
//                    locations.setType("M");*/
//
//                } else {
//                    Toast.makeText(getContext(), "Please enable the gps", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        //sending email to status fragment
        //Put the value
        //StatusFragment statusFragment = new StatusFragment();
        //Bundle args = new Bundle();
        //args.putString("EmailAddress", emailStr);
        //statusFragment.setArguments(args);

        //Inflate the fragment
        //assert getFragmentManager() != null;
        //getFragmentManager().beginTransaction().add(R.id.tv_email, statusFragment).commit();

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//                Intent serviceIntent = new Intent(context, GoogleService.class);
//                serviceIntent.putExtra("Time", String.valueOf(1));
//                serviceIntent.putExtra("BoolTrue", true);
//                context.startService(serviceIntent);
//            }

            /*if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent serviceIntent = new Intent(context, GoogleService.class);
                serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                serviceIntent.putExtra("Time", String.valueOf(1));
                serviceIntent.putExtra("BoolTrue", false);
                context.startService(serviceIntent);
            }*/

            /*if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent i = new Intent(context, GoogleService.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Time", String.valueOf(1));
                i.putExtra("BoolTrue", false);
                context.startActivity(i);

                Log.e("After Rebooted", "After Rebooted");

            }*/

            wayLatitude = Double.valueOf(intent.getStringExtra("latitude"));
            wayLongitude = Double.valueOf(intent.getStringExtra("longitude"));

            Log.e("BroadcastedData", "Received");

            //connection = networkCheck.checkNow(getContext());
            //connection = wifiState.haveNetworkConnection();

            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);


            NetworkInfo[] netInfo = connectivityManager != null ? connectivityManager.getAllNetworkInfo() : new NetworkInfo[0];

            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        connection = true;
            }

            if (connection) {
                String auto = "A";
                firebaseDatabaseAdd(auto);
            } else {
                String auto = "A";
                firebaseDatabaseAdd(auto);

                //Toast.makeText(getContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
            }

            tv_latitude.setText(wayLatitude + "");
            tv_longitude.setText(wayLongitude + "");

            latlonEdit.putString("Latitude", String.valueOf(wayLatitude));
            latlonEdit.putString("Longitude", String.valueOf(wayLongitude));
            latlonEdit.apply();


        }
    };

    /*private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            wayLatitude = Double.valueOf(intent.getStringExtra("latitude"));
            wayLongitude = Double.valueOf(intent.getStringExtra("longitude"));

            String auto = "A";
            firebaseDatabaseAdd(auto);

            tv_latitude.setText(wayLatitude + "");
            tv_longitude.setText(wayLongitude + "");

            latlonEdit.putString("Latitude", String.valueOf(wayLatitude));
            latlonEdit.putString("Longitude", String.valueOf(wayLongitude));
            latlonEdit.apply();

        }
    };*/



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

            isGPS = true;

        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    if (isContinue) {
//                        //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        //tv_latitude.setText(String.format(Locale.US, "%s", wayLatitude));
                        //tv_longitude.setText(String.format(Locale.US, "%s", wayLongitude));
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
                //}

                boolean_permission = true;

            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                boolean_permission = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location

                boolean_permission = true;
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("Home", "OnActivityCreated");


    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("Home", "OnStart");
    }




    public void firebaseDatabaseAdd(String autoManual) {

//        if (aSwitch != null) {
//            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked) {
//                        //do stuff when Switch is ON
//                        switchTF = true;
//                    } else {
//                        //do stuff when Switch if OFF
//                        switchTF = false;
//                    }
//                }
//            });
//        }

        //SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        //String name = sharedPreferences.getString("Email", "");

        String emailS = sharedPreferences1.getString("Email", "");

        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");

        //uniqueID = UUID.randomUUID().toString();
        uniqueID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //tv_uniqueIdentifier.setText(uniqueID);
        tv_uniqueIdentifier.setText(uniqueID);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Email", emailS);

        reference = database.getReference().child("Users").child(uniqueID);
        reference.updateChildren(map);



//        reference = database.getReference().child("Users").child(uniqueID).child("Locations").push();
        reference = database.getReference().child("Users").child(uniqueID).child("Locations");


        //String latLength = String.valueOf(latitude).substring(0, 10);
        //String lonLength = String.valueOf(longitude).substring(0, 10);

        tv_latitude.setText(String.valueOf(wayLatitude));
        tv_longitude.setText(String.valueOf(wayLongitude));

        Double latStr = Double.valueOf(tv_latitude.getText().toString());
        Double lonStr = Double.valueOf(tv_longitude.getText().toString());

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString = dateFormat.format(new Date());

        Locations locations = new Locations(latStr, lonStr, millisInString, switchTF, autoManual);
        /*Map<String, Object> locationMap = new HashMap<String, Object>();
        locationMap.put("Latitude", wayLatitude);
        locationMap.put("Longitude", wayLongitude);
        locationMap.put("Type", autoManual);
        locationMap.put("Contact", switchTF);
        locationMap.put("Timestamp", millisInString);*/


        reference.push().setValue(locations);




//        //reference.setValue(email);
//
//        reference = database.getReference().child("Users").child(uniqueID);
//        reference.setValue(uniqueID);
//
////        reference = database.getReference().child("Users").child(uniqueID).child("Locations").push();
//        reference = database.getReference().child("Users").child(uniqueID).child("Locations");
//
//
//        //String latLength = String.valueOf(latitude).substring(0, 10);
//        //String lonLength = String.valueOf(longitude).substring(0, 10);
//
//        tv_latitude.setText(String.valueOf(wayLatitude));
//        tv_longitude.setText(String.valueOf(wayLongitude));
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        millisInString = dateFormat.format(new Date());
//
//        Locations locations = new Locations(wayLatitude, wayLongitude, millisInString, switchTF, autoManual);
//
//        reference.setValue(locations);
//
//        reference = database.getReference().child("Users").child(uniqueID).child("Email");
//
//        reference.setValue(emailS);
    }

    /*public class MyServiceReceiverOnStartup extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("BootReceived", "BootReceived");

            if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent i = new Intent(context, StartupService.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }


//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent serviceIntent = new Intent(context, GoogleService.class);
//            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            serviceIntent.putExtra("Time", String.valueOf(1));
//            serviceIntent.putExtra("BoolTrue", false);
//            context.startService(serviceIntent);
//        }


        }

    }*/

    /*@Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            //mCallback = (OnImageClickListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement OnImageClickListener");
        }
    }*/


    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fn_permission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);

                /*if ((ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION))) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                            },
                            REQUEST_PERMISSIONS);

                }*/

       /*         boolean_permission = true;

            }
        } else {

            statusCheck();
        }
        boolean_permission = false;
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                fn_permission();
                Toast.makeText(getContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void statusCheck() {
        final LocationManager manager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        //boolean_permission = true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }*/

    /*private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            /*List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //String cityName = addresses.get(0).getAddressLine(0);
                //String stateName = addresses.get(0).getAddressLine(1);
                //String countryName = addresses.get(0).getAddressLine(2);

                /*tv_area.setText(addresses.get(0).getAdminArea());
                tv_locality.setText(stateName);
                tv_address.setText(countryName);


            } catch (IOException e1) {
                e1.printStackTrace();
            }


            tv_latitude.setText(String.valueOf(latitude));
            tv_longitude.setText(String.valueOf(longitude));
            //tv_address.getText();


        }
    };*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
        }

        if (sharedPreferences.contains("Email")) {
            tv_email.setText(sharedPreferences.getString("Email", ""));
        }*/

        //Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));

        //Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver1, new IntentFilter(GoogleService1.str_receiver));

        contactDialog.dismiss();


        if (sharedPreferences.contains("Email")) {
            tv_email.setText(sharedPreferences.getString("Email", ""));
        }

        if (sharedPreferences.contains("UniqueID")) {
            tv_uniqueIdentifier.setText(sharedPreferences.getString("UniqueID", ""));
        }

        if (latlonPrefs.contains("Latitude") && latlonPrefs.contains("Longitude")) {
            tv_latitude.setText(latlonPrefs.getString("Latitude", ""));
            tv_longitude.setText(latlonPrefs.getString("Longitude", ""));
        }

        if (switchTF) {

            if (sharedPreferences.contains("ICEContactSwitchTrue")) {
                aSwitch.setChecked(true);
                contactDialog.dismiss();
            }

            if (sharedPreferences.contains("ICEContactTrue")) {
                contactICETxt.setText(sharedPreferences.getString("ICEContactTrue", ""));
                contactICETxt.setTextColor(Color.RED);
            }
        } else {

            if (sharedPreferences.contains("ICEContactSwitchFalse")) {
                aSwitch.setChecked(false);
                contactDialog.dismiss();
            }

            if (sharedPreferences.contains("ICEContactFalse")) {
                contactICETxt.setText(sharedPreferences.getString("ICEContactFalse", ""));
                contactICETxt.setTextColor(Color.WHITE);
            }
        }

        Log.e("Home", "OnResume");

        if (sharedPreferences.getBoolean("GameModeTrueFalse", true)) {

            AboutFragment.trueFalseBoolSwitch = true;

            //Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
        }

        if (BaseActivity.sharedData.getBoolean("GameModeValue", false)) {

            //Toast.makeText(getContext(), "3: Game mode: " + AboutFragment.trueFalseBoolSwitch, Toast.LENGTH_SHORT).show();

            if (sharedPreferences.contains("ProgressValueTrue")) {



                simpleSeekBar.setProgress(Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("ProgressValueTrue", ""))) - 1);
                //xTV.setText(sharedPreferences.getString("ProgressValueTrue", ""));
            }
        } else {

            if (sharedPreferences.contains("ProgressValueFalse")) {

                simpleSeekBar.setProgress(Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("ProgressValueFalse", ""))) - 1);
            }
        }

        if (sharedPreferences.contains("HoursMinsTrue")) {

            hoursMinsTxt.setText(sharedPreferences.getString("HoursMinsTrue", ""));
        }

        if (sharedPreferences.contains("HoursMinsFalse")) {

            hoursMinsTxt.setText(sharedPreferences.getString("HoursMinsFalse", ""));
        }

        /*if (sharedPreferences.contains("GameModeSwitchOn")) {

            AboutFragment aboutFragment = new AboutFragment();

            new AboutFragment().gameModeSwitch.setChecked(true);
        }*/

        Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onPause() {
        super.onPause();

        Log.e("Home", "OnPause");

        //if (broadcastReceiver != null) {
            Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));

            //Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver1, new IntentFilter(GoogleService1.str_receiver));
        //}

        if (sharedPreferences.contains("Email")) {
            tv_email.setText(sharedPreferences.getString("Email", ""));
        }

        if (sharedPreferences.contains("UniqueID")) {
            tv_uniqueIdentifier.setText(sharedPreferences.getString("UniqueID", ""));
        }

        if (latlonPrefs.contains("Latitude") && latlonPrefs.contains("Longitude")) {
            tv_latitude.setText(latlonPrefs.getString("Latitude", ""));
            tv_longitude.setText(latlonPrefs.getString("Longitude", ""));
        }

        if (switchTF) {

            if (sharedPreferences.contains("ICEContactSwitchTrue")) {
                aSwitch.setChecked(true);
                contactDialog.dismiss();
            }

            if (sharedPreferences.contains("ICEContactTrue")) {
                contactICETxt.setText(sharedPreferences.getString("ICEContactTrue", ""));
                contactICETxt.setTextColor(Color.RED);
            }
        } else {

            if (sharedPreferences.contains("ICEContactSwitchFalse")) {
                aSwitch.setChecked(false);
                contactDialog.dismiss();
            }

            if (sharedPreferences.contains("ICEContactFalse")) {
                contactICETxt.setText(sharedPreferences.getString("ICEContactFalse", ""));
                contactICETxt.setTextColor(Color.WHITE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroy() {
        super.onDestroy();

        //if (broadcastReceiver != null) {
            Objects.requireNonNull(getContext()).unregisterReceiver(broadcastReceiver);
            //Objects.requireNonNull(getContext()).unregisterReceiver(broadcastReceiver1);
        //}

        Objects.requireNonNull(getContext()).stopService(new Intent(getContext(), GoogleService.class));
        //getContext().stopForeground(true);
        //getContext().stopSelf();

        killBroadCastReceiver();
    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }*/

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }*/

    /*public void displayReceivedData(Boolean trueFalseBool, String minutesHours, int step, int min, int max, int progressValue) {

        trueFalseBoolean = trueFalseBool;
        minsHours = minutesHours;
        stepRec = step;
        minRec = min;
        maxRec = max;
        progValue = progressValue;

        //editor.putString("HoursMinsFalse", minsHours);
        //editor.apply();

        //editor.putString("ProgressValueTrue", String.valueOf(progValue));
        //editor.putString("ProgressValueFalse", String.valueOf(progValue));
        //editor.apply();

        Log.e("Bool", String.valueOf(trueFalseBoolean));
        Log.e("HoursMin", minsHours);
        Log.e("Step", String.valueOf(stepRec));
        Log.e("Min", String.valueOf(minRec));
        Log.e("Max", String.valueOf(maxRec));
        Log.e("ProgressValue", String.valueOf(progValue));
    }*/

    private void killBroadCastReceiver() {
        PackageManager pm = getContext().getPackageManager();
        ComponentName componentName = new ComponentName(getContext(), yourActivityRunOnStartup.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //Toast.makeText(getContext(), "BroadCast Receiver Killed", Toast.LENGTH_LONG).show();
    }
}

