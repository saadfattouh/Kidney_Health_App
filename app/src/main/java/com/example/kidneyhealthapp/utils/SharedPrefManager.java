package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kidneyhealthapp.model.User;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    private static final String KEY_ID = "keyid";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_USER_PASSWORD = "keyuserpassword";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ADDRESS = "keyaddress";
    private static final String KEY_USER_TYPE = "userType";


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
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putInt(KEY_USER_TYPE, user.getType());

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



    //this method will give the logged in user
    public User getUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_USER_PASSWORD, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getInt(KEY_USER_TYPE, -1)
                );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}
