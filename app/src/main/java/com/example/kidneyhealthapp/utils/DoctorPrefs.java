package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kidneyhealthapp.model.Doctor;


public class DoctorPrefs {

    private static final String SHARED_PREF_NAME = "generalFile";

    private static final String KEY_ID = "key_user_id";
    private static final String KEY_CENTER_ID = "center_id";
    private static final String KEY_FIRST_NAME = "key_f_name";
    private static final String KEY_LAST_NAME = "key_l_name";
    private static final String KEY_USERNAME = "key_user_name";
    private static final String KEY_PHONE = "key_doc_phone";
    private static final String KEY_ABOUT = "key_about";


    private static DoctorPrefs mInstance;
    private static Context context;

    private DoctorPrefs(Context context) {
        DoctorPrefs.context = context;
    }
    public static synchronized DoctorPrefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DoctorPrefs(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    //customer
    public void userLogin(Doctor doc) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, doc.getId());
        editor.putInt(KEY_CENTER_ID, doc.getCenterId());
        editor.putString(KEY_FIRST_NAME, doc.getFirstName());
        editor.putString(KEY_LAST_NAME, doc.getLastName());
        editor.putString(KEY_USERNAME, doc.getUserName());
        editor.putString(KEY_PHONE, doc.getPhone());
        editor.putString(KEY_ABOUT, doc.getAbout());

        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }



    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }



    //this method will give the logged in user
    public Doctor getUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Doctor(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_CENTER_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_LAST_NAME, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ABOUT, null)
                );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}
