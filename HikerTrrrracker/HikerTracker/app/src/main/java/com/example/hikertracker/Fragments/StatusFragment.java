package com.example.hikertracker.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hikertracker.Adapter.MyRecyclerviewAdapter;
import com.example.hikertracker.Adapter.MyRecyclerviewHFAdapter;
import com.example.hikertracker.FragmentLifecycle;
import com.example.hikertracker.Models.Email;
import com.example.hikertracker.Models.Locations;
import com.example.hikertracker.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class StatusFragment extends Fragment implements FragmentLifecycle {

    //private static final String TAG = "StatusFragment";
    private RecyclerView recyclerView;
    private MyRecyclerviewAdapter myRecyclerviewAdapter;

    //private RecyclerView myRecyclerView;
    private MyRecyclerviewHFAdapter myRecyclerviewHFAdapter;

    FirebaseDatabase database;
    DatabaseReference reference;

    Email email;

    //String emailStr;

    String uniqueID;

    //LocationAdapter locationAdapter;

    //public final static String DATA_RECEIVE = "data_receive";



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("Status", "OnAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("Status", "OnCreate");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        Log.e("Status", "OnCreateView");

        //recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewID);


        /*SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", 0);
        String emailStr = prefs.getString("Email", "");
        Log.d("Email retreived", emailStr);*/

        //Retrieve email address from home fragment
        //assert getArguments() != null;
        //String emailAddressStr = getArguments().getString("Email");



        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("Status", "OnViewCreated");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewID);

//        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
//        if (animator instanceof DefaultItemAnimator) {
//            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
//        }


        /*SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", 0);
        String emailStr = prefs.getString("Email", "");
        Log.d("Email retreived", emailStr);*/

        //Retrieve email address from home fragment
        //assert getArguments() != null;
        //String emailAddressStr = getArguments().getString("Email");



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("Status", "OnActivityCreated");


    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e("Status", "OnStart");

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {

            //TextView txtView = (TextView) ((Activity) Objects.requireNonNull(getContext())).findViewById(R.id.tv_email);
            //txtView.setText(minsStr);

            //Log.d("Text", txtView.getText().toString());

            Log.e("Status", "SetUserVisibleHint");

//            if (emailStr == null) {
//
//                Log.e("Email", "Email is null");
//
//            } else {

                database = FirebaseDatabase.getInstance();
                //reference = database.getReference().child("Users").child(emailStr).child("Locations").push();
                //assert emailAddressStr != null;
                //Email email = new Email();

                //email = new Email();

                uniqueID = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                //Toast.makeText(getContext(), emailStr, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), email.getEmailaddress(), Toast.LENGTH_SHORT).show();

                reference = database.getReference().child("Users").child(uniqueID);
                //Query lastQuery = database.getReference().child("Users").child("Email").child("Locations").orderByKey().limitToLast(1);
                //String userId = reference.child("Locations").push().getKey();

                reference.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                /*for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(Objects.equals(data.getKey(), "latitude")){
                        String orderNumber = Objects.requireNonNull(data.getValue()).toString();
                        Log.e("Specific Node Value" , orderNumber);
                    }
                }*/

                        ArrayList<Locations> locationsArrayList = new ArrayList<>();

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {


                            //Locations locations = childDataSnapshot.getValue(Locations.class);

                            Double latitude = childDataSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = childDataSnapshot.child("longitude").getValue(Double.class);
                            String timeStamp = childDataSnapshot.child("timestamp").getValue(String.class);
                            Boolean contact = childDataSnapshot.child("contact").getValue(Boolean.class);
                            String type = childDataSnapshot.child("type").getValue(String.class);

                            //locationsArrayList.add(new Locations(latitude, longitude, timeStamp, contact, type));

                            /*assert locations != null;
                            locationsArrayList.add(new Locations(locations.getLatitude(),
                                    locations.getLongitude(),
                                    locations.getTimestamp(),
                                    locations.getContact(),
                                    locations.getType()));*/

//                            Collections.reverse(locationsArrayList);

                            locationsArrayList.add(0, new Locations(latitude, longitude, timeStamp, contact, type));

                            //locationAdapter = new LocationAdapter(LocationExpand.makeHeaders(timeStamp, locationsArrayList));

                            myRecyclerviewHFAdapter = new MyRecyclerviewHFAdapter(locationsArrayList, getContext());
                            recyclerView.setAdapter(myRecyclerviewHFAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

//        } else {
//
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("Status", "OnPause");
    }

    /*@Override
    public void passData(String data) {

        Log.d(TAG, data);

        emailStr = data;
        Toast.makeText(getContext(), emailStr, Toast.LENGTH_SHORT).show();

    }*/

    /*@Override
    public void onResume() {
        super.onResume();


    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            emailStr = args.getString(DATA_RECEIVE);
        }
    }*/

    /*public void displayReceivedData(String message) {

        emailStr = message;

        Log.e("Email", message);

    }*/

    @Override
    public void onPauseFragment() {

        Log.i("Status", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {

        Log.i("Status", "onResumeFragment()");
        //Toast.makeText(getActivity(), "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        locationAdapter.onSaveInstanceState(outState);
//    }

}






