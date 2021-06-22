package com.example.hikertracker;

import android.graphics.Color;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MainActivity extends AppCompatActivity { //HomeFragment.SendMessage,  implements AboutFragment.SendData

    //private static final String TAG = "MainActivity";

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerTabAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToShow = (FragmentLifecycle) viewPagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) viewPagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPagerAdapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        /*Intent intenti = new Intent(this, GoogleService.class);

        intenti.putExtra("Time", String.valueOf(24));
        intenti.putExtra("BoolTrue", false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.e("ServiceStarted", "True");

            startForegroundService(intenti);
            //Objects.requireNonNull(getContext()).startService(intenti);

        } else {

            startService(intenti);
        }*/
    }

    /*@Override
    public void sendData(String message) {
        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        StatusFragment statusFragment = (StatusFragment) getSupportFragmentManager().findFragmentByTag(tag);
        assert statusFragment != null;
        statusFragment.displayReceivedData(message);
    }*/



    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initViews();
    }

    private void initViews() {
        initToolbar();
        initViewPager();
        initTabLayout();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private ViewPager mViewPager;
    private ViewPagerTabAdapter viewPagerTabAdapter;
    //private FrameLayout frameLayout;

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        //frameLayout = (FrameLayout) findViewById(R.id.frameLayoutID);
        List<String> tabNames = new ArrayList<String>();
        tabNames.add("Home");
        tabNames.add("Status");
        tabNames.add("About");
        viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(), getFragments(), tabNames);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(viewPagerTabAdapter);
    }


    private void initTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
    }


    private List<Fragment> mFragments;

    private List<Fragment> getFragments() {

        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new StatusFragment());
        mFragments.add(new AboutFragment());

        return mFragments;
    }*/

    /*@Override
    public void passData(String data) {

        Log.d(TAG, "data received to Activity... send to view pager");
        viewPagerTabAdapter.passData(data);
    }*/



    /*@Override
    public void onFragmentInteraction(String emailStr) {

        StatusFragment statusFragment = (StatusFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        assert statusFragment != null;
        statusFragment.emailReturned(emailStr);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));

    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(broadcastReceiver);
    }

    /*@Override
    public void sendAllData(Boolean trueFalseBool, String minutesHours, int step, int min, int max, int progressValue) {

        String tag = "android:switcher:" + R.id.viewPager + ":" + 0;
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
        assert homeFragment != null;
        homeFragment.displayReceivedData(trueFalseBool, minutesHours, step, min, max, progressValue);
    }*/

    public void jumpToPage(View view) {

        viewPager.setCurrentItem(getItem(), true);
    }

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

    /*@Override
    public void sendData(String message) {
        String tag = "android:switcher:" + R.id.viewpager + ":" + 1;
        StatusFragment statusFragment = (StatusFragment) getSupportFragmentManager().findFragmentByTag(tag);
        assert statusFragment != null;
        statusFragment.displayReceivedData(message);
    }*/

    /*@Override
    public void passData(String data) {
        StatusFragment statusFragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(StatusFragment.DATA_RECEIVE, data);
        statusFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, statusFragment)
                .commit();

                FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayoutID, statusFragment).commit();
    }*/
}


