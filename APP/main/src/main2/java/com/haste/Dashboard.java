package com.haste;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.digi.xbee.api.exceptions.XBeeException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.haste.BaseClass.BaseXbeeActivity;
import com.haste.ReportFragments.Constant;
import com.haste.ReportFragments.IncidentType;
import com.haste.ReportObject.IncidentReport;
import com.haste.User.Account;


public class Dashboard extends BaseXbeeActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        IncidentType.OnFragmentInteractionListener,
        Information.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        InformationListView.OnFragmentInteractionListener,
        MapReportInfo.OnFragmentInteractionListener,
        Account.OnFragmentInteractionListener{

    public Fragment fragment;
    public static IncidentReport incidentReport = new IncidentReport("bla");
    public static Fragment incidentType;
    protected GoogleApiClient googleApiClient;
    public static Location location;
    private FirebaseUser currentUser;

    final static String TAG = "Dashboard";

    private XBeeManager xbeeManager;
    private boolean connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setupUserInfoInNavigationDrawer();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Code for enabling the admin mode in the Navigation Drawer
        navigationView.getMenu().findItem(R.id.nav_admin_mode).setVisible(false);
        /*String fireBaseToken = FirebaseInstanceId.getInstance().getToken();

        if(fireBaseToken != null) {
            if (fireBaseToken.equals(Constant.ADMIN))
                navigationView.getMenu().findItem(R.id.nav_admin_mode).setVisible(true);
        }*/

        if(currentUser != null){
            if(currentUser.getEmail().equals("admin@haste.com")){
                navigationView.getMenu().findItem(R.id.nav_admin_mode).setVisible(true);
            }
        }

        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            // fragment = new ChooseAction();
            fragment = new TabFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(Dashboard.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Open XBee connection
        xbeeManager = XBeeManagerApplication.getInstance().getXBeeManager();
        if (connecting)
            return;
        xbeeManager.createXBeeDevice(9600);
        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                connecting = true;
                try {
                    //showToastMessage("opening connection");
                    xbeeManager.openConnection();
                    //Log.d(TAG, xbeeManager.getLocalXBeeDevice().isOpen()+" ");
                } catch (XBeeException e) {
                    // showToastMessage("error: " +
                    Log.d(TAG, e.toString());
                }
                connecting = false;
            }
        });
        connectThread.start();


    }

    public void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Google api client implementation
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nearbyIncident) {
            fragment = new TabFragment();
        } else if (id == R.id.nav_subscription) {
            fragment = new SubscriptionFragment();
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
        } else if (id == R.id.nav_account) {
            fragment = new Account();
        } else if (id == R.id.nav_sos_mode){  //sos mode
            fragment = new SOSFragment();
        } else if (id == R.id.nav_create_report){
            fragment = Dashboard.incidentType;
            if (fragment == null) {
                Dashboard.incidentType = new IncidentType();
                fragment = Dashboard.incidentType;
            }
        } else if (id == R.id.nav_subscription_setting) {
            Intent intent = new Intent(this, NotificationSetting.class);
            startActivity(intent);
        } else if( id == R.id.nav_chat){
            fragment = new ChatViewFragment();
        }

        // Add admin mode here
        else if (id == R.id.nav_admin_mode){
                fragment = new AdminMode();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do Nothing
    }

    public void showEditDialog(String title, String info, String location) {
        FragmentManager fm = getSupportFragmentManager();
        MapReportInfo editNameDialogFragment = MapReportInfo.newInstance(title, info, location);
        editNameDialogFragment.show(fm, "fragment_map_report_info");
    }

    public void setupUserInfoInNavigationDrawer(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        View headerView = navigationView.getHeaderView(0);

        TextView username = (TextView) headerView.findViewById(R.id.nav_username);
        TextView email = (TextView) headerView.findViewById(R.id.nav_email);
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.nav_userImage);

        if(currentUser != null) {
            username.setText(currentUser.getDisplayName());
            email.setText(currentUser.getEmail());
        } else {
            username.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            profileImage.setVisibility(View.INVISIBLE);
        }

    }

}
