package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kidneyhealthapp.model.LatLon;
import com.example.kidneyhealthapp.model.User;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    private static final String KEY_ID = "key_user_id";
    private static final String KEY_FIRST_NAME = "key_f_name";
    private static final String KEY_LAST_NAME = "key_l_name";
    private static final String KEY_USERNAME = "key_user_name";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_AGE = "key_age";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_GENDER = "keyGender";
    private static final String KEY_ABOUT = "key_about";
    private static final String KEY_LOCATION_SET = "locationSet";


    private static SharedPrefManager mInstance;
    private static Context context;

    private SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    //customer
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putInt(KEY_AGE, user.getAge());
        editor.putInt(KEY_GENDER, user.getGender());
        if(user.getLocation() != null){
            setUserLocation(user.getLocation().getLat(), user.getLocation().getLon());
        }

        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }

    public void setUserType(int type){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_TYPE, type);
        editor.apply();
    }

    public int getUserType(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_TYPE, -1);
    }

    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }


    public void setUserLocation(double lat, double lon){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAT, String.valueOf(lat));
        editor.putString(KEY_LON, String.valueOf(lon));
        editor.putBoolean(KEY_LOCATION_SET, true);
        editor.apply();
    }

    public boolean isUserLocationSet(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_LOCATION_SET, false);
    }

    public LatLon getUserLocation(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LatLon(Double.parseDouble(sharedPreferences.getString(KEY_LAT, "200")), Double.parseDouble(sharedPreferences.getString(KEY_LON, "200")));
    }


    //this method will give the logged in user
    public User getUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_LAST_NAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getInt(KEY_AGE, -1),
                sharedPreferences.getInt(KEY_GENDER, -1)
        );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}
