package com.example.manco.googlemapp2;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;


public class Controller extends Context {

    public String groupId = null;
    public Hashtable<String, LatLng> stash = new Hashtable<String, LatLng>();
    private UserFragment userFragment;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private MainActivity activity;
    private GoogleMap myMap;
    private MapFragment map;
    private String[] members;
    private JSONMessages jsonMessages;
    MyConnectService mservice;
    private LatLng malmo;
    private LatLng fakePos;
    boolean serviceBound;
    boolean cheatOn = false;

    public Controller(MainActivity mainActivity, Bundle savedInstanceState) {
        this.activity = mainActivity;
        startBoundService();
        initializeDrawer();
        jsonMessages = new JSONMessages();
        map = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.mapFrag);
        initializeMap(map);
        userFragment = new UserFragment();
        userFragment.setController(this);
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver, new IntentFilter("my-event"));
    }


    public void initializeMap(MapFragment mapFragment) {
        myMap = mapFragment.getMap();
        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        malmo = new LatLng(55.59362448, 13.09414008);
        myMap.setMyLocationEnabled(true);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malmo, 8));
        LocationManager manager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 0, new GPSLyssnare());
    }


    public void initializeDrawer(){
        String[] drawerTitles = activity.getResources().getStringArray(R.array.drawer_choises);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) activity.findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, drawerTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }


    public void startBoundService(){
        Intent intent = new Intent(activity, MyConnectService.class);
        activity.bindService(intent, serviceConnection, activity.BIND_AUTO_CREATE);
        serviceBound = true;
        activity.startService(intent);
        Log.i("Controller", "bindServiceMethod() kördes, Serviceen är nu bunden till Controller.");
    }


    public void unBindService(){
        if(serviceBound){
            if(mservice!=null)
//            mservice.unbindService(serviceConnection);
                Log.i("Controller", "unBindServiceMethod() kördes, Serviceen är nu obunden.");
            serviceBound = false;
        }else {
            Log.i("Controller" ,"Error i unBindServiceMethod, försökte unbinda servicen när den inte var bound.");
        }
        mservice = null;
    }


    public void getGroups() {
        Log.i("Controller","getGroups():" + jsonMessages.getAllGroups());
        mservice.sendMessage(jsonMessages.getAllGroups());
    }


    public void registerNewGroup(String grpName, String userName){
        Log.i("Controller","registerNewGroup() kallades...");
        mservice.sendMessage(jsonMessages.registerGroup(grpName,userName));
        getGroups();
    }

    public void getMembers(String groupName) {
        mservice.sendMessage(jsonMessages.getMembers(groupName));
        Log.i("Controller","getMembers() kördes :" + groupName);
    }

    public String[] getMemberArray(){
        return members;
    }


    public void unregisterGroup(String myId) {
        Log.i("Controller","unregisterGroup() :" + myId);
        mservice.sendMessage(jsonMessages.unregister(myId));
    }

    public void putMarkerOnMap(String[] newPos) {
        LatLng latLng = new LatLng(Double.valueOf(newPos[2]), Double.valueOf(newPos[1]));
        stash.put(newPos[0], latLng);
        myMap.clear();
        Log.i("Controller","putMarkerOnMap() user:" + newPos[0]);
        if(members.length>0){
            for(int i=0;i<members.length;i++) {
                LatLng latLng1 = stash.get(members[i]);
                if(latLng1!=null)
                myMap.addMarker(new MarkerOptions().position(latLng1).title(members[i]));
            }
            if(cheatOn){
                myMap.addMarker(new MarkerOptions().position(fakePos).title("My fake-position"));
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyConnectService.LocalBinder localBinder = (MyConnectService.LocalBinder)service;
            mservice = localBinder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.i("Controller", "BroadcastReceiver onReceive():" + message);
            if (message.equals("groups")) {
                String[] array = intent.getStringArrayExtra("array");
                Log.i("Controller", "onReceive: " + message);
                userFragment.populateList(array);
                Toast.makeText(activity, R.string.toastUpdateGrp, Toast.LENGTH_SHORT).show();
            } else if (message.equals("register")) {
                String[] array = intent.getStringArrayExtra("array");
                groupId = array[0];
                userFragment.setId(groupId);
                Log.i("Controller", "onReceive(), Register , groupId:" + groupId);
                Toast.makeText(activity, R.string.toastJoinGrp, Toast.LENGTH_SHORT).show();
            } else if (message.equals("members")) {
                String[] array = intent.getStringArrayExtra("array");
                Log.i("Controller", "onReceive() members:" + message);
                members = array;
                userFragment.showMembersDialog();
            } else if (message.equals("locations")) {
                String[] array = intent.getStringArrayExtra("array");
                if ((!array[2].equals("NaN")) || (!array[1].equals("NaN"))) {
                    putMarkerOnMap(array);
                    Log.i("Controller", "onReceive() locations: " + Double.valueOf(array[2]) + ", " + Double.valueOf(array[1]) + " , " + array[0]);
                    Toast.makeText(activity, "Updated map-marker: " + array[0], Toast.LENGTH_SHORT).show();
                }
            } else if (message.equals("unregister")) {
                Toast.makeText(activity, R.string.toastUnsubscribe, Toast.LENGTH_SHORT).show();
                myMap.clear();
            } else if(message.equals("membersArray")){
                members = intent.getStringArrayExtra("array");
            }
        }
    };

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int resid) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return null;
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String name) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(String type) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String type) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String name, int mode) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteDatabase(String name) {
        return false;
    }

    @Override
    public File getDatabasePath(String name) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream data) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, Bundle options) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {

    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return null;
    }

    @Override
    public boolean stopService(Intent service) {
        return false;
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return false;
    }

    @Override
    public void unbindService(ServiceConnection conn) {

    }

    @Override
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        return false;
    }

    @Override
    public Object getSystemService(String name) {
        return null;
    }

    @Override
    public String getSystemServiceName(Class<?> serviceClass) {
        return null;
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return 0;
    }

    @Override
    public int checkCallingPermission(String permission) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfPermission(String permission) {
        return 0;
    }

    @Override
    public int checkSelfPermission(String permission) {
        return 0;
    }

    @Override
    public void enforcePermission(String permission, int pid, int uid, String message) {

    }

    @Override
    public void enforceCallingPermission(String permission, String message) {

    }

    @Override
    public void enforceCallingOrSelfPermission(String permission, String message) {

    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

    }

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        return null;
    }

    @Override
    public Context createDisplayContext(Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }


    private class GPSLyssnare implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            if (!cheatOn) {
                mservice.sendMessage(jsonMessages.sendPosition(lat, lng, groupId));
                Log.i("Controller", "GPSLyssnare,(NOT FAKE)  onLocationChanged sendPossition:" + lat + ", " + lng);
            } else {
                if (fakePos != null) {
                    mservice.sendMessage(jsonMessages.sendPosition(fakePos.latitude, fakePos.longitude, groupId));
                    Log.i("Controller", "GPSLyssnare,(FAKE)  onLocationChanged sendPossition:" + fakePos.latitude + ", " + fakePos.longitude);
                }
            }
            Toast.makeText(activity, R.string.toastPositionUpdate, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        private boolean isBackstckable = false;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawers();
            if (position == 1) {
                fragmentManager = activity.getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.fragment, userFragment);
                if(!isBackstckable)
                    ft.addToBackStack(null);
                ft.commit();
                isBackstckable = true;
                getGroups();
            }

            else if(position == 0){
                if(isBackstckable){
                    fragmentManager.popBackStackImmediate();
                    isBackstckable = false;
                }
            }
            else if(position == 2) {
                //Fusk på
                if (!cheatOn) {
                    myMap.addMarker(new MarkerOptions().position(malmo).title("Fake position!").draggable(true));
                    cheatOn = true;
                    myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            fakePos = marker.getPosition();
                            mservice.sendMessage(jsonMessages.sendPosition(fakePos.latitude, fakePos.longitude, groupId));
                        }
                    });
                }
            }
            else if(position == 3){
                if(cheatOn) {
                    myMap.clear();
                    cheatOn = false;
                }
            }
        }
    }
}