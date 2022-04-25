package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.model.LatLon;
import com.example.kidneyhealthapp.model.Patient;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    private static final String KEY_ID = "key_user_id";
    private static final String KEY_FIRST_NAME = "key_f_name";
    private static final String KEY_LAST_NAME = "key_l_name";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_AGE = "key_age";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_GENDER = "keyGender";
    private static final String KEY_ABOUT = "key_about";
    private static final String KEY_LOCATION_SET = "locationSet";
    private static final String KEY_ADDRESS = "keyaddress";
    private static final String KEY_DETAILS = "keydetails";
    private static final String KEY_CENTER_NAME = "keycname";
    private static final String KEY_CENTER_INFO = "keycinfo";
    private static final String KEY_CENTER_LOCATION = "keyclocation";
    private static final String KEY_CENTER_ID= "keycid";



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
    public void patientLogin(Patient patient) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, patient.getId());
        editor.putString(KEY_FIRST_NAME, patient.getFirstName());
        editor.putString(KEY_LAST_NAME, patient.getLastName());
        editor.putString(KEY_EMAIL, patient.getEmail());
        editor.putString(KEY_PHONE, patient.getPhone());
        editor.putString(KEY_ADDRESS, patient.getAddress());
        editor.putInt(KEY_AGE, patient.getAge());
        editor.putInt(KEY_GENDER, patient.getGender());
        editor.putInt(KEY_USER_TYPE, Constants.USER_TYPE_PATIENT);
        if(patient.getLocation() != null){
            setUserLocation(patient.getLocation().getLat(), patient.getLocation().getLon());
        }

        editor.apply();
    }
    public void patientUpdate(String fName, String lName, String address, String phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, fName);
        editor.putString(KEY_LAST_NAME, lName);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }
    //this method will give the logged in user
    public Patient getPatientData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Patient(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_LAST_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getInt(KEY_AGE, -1),
                sharedPreferences.getInt(KEY_GENDER, -1)
        );
    }
    public void doctorLogin(Doctor doctor) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, doctor.getId());
        editor.putString(KEY_FIRST_NAME, doctor.getFirstName());
        editor.putString(KEY_LAST_NAME, doctor.getLastName());
        editor.putString(KEY_EMAIL, doctor.getEmail());
        editor.putString(KEY_PHONE, doctor.getPhone());
        editor.putString(KEY_DETAILS, doctor.getDetails());
        editor.putInt(KEY_USER_TYPE, Constants.USER_TYPE_DOCTOR);

        editor.apply();
    }
    public Doctor getDoctorData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Doctor(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_LAST_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_DETAILS, null)
        );
    }
    public void centerSave(Center center) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CENTER_ID, center.getId());
        editor.putString(KEY_CENTER_NAME, center.getName());
        editor.putString(KEY_CENTER_INFO, center.getInfo());
        editor.putString(KEY_CENTER_LOCATION, center.getLocation());
        editor.putFloat(KEY_LAT, (float) center.getLat());
        editor.putFloat(KEY_LON, (float) center.getLon());

        editor.apply();
    }

    public Center getCenterData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Center(
                sharedPreferences.getInt(KEY_CENTER_ID, -1),
                sharedPreferences.getString(KEY_CENTER_NAME, null),
                (double)sharedPreferences.getFloat(KEY_LAT, 0),
                (double)sharedPreferences.getFloat(KEY_LON, 0),
                sharedPreferences.getString(KEY_CENTER_LOCATION, null),
                sharedPreferences.getString(KEY_CENTER_INFO, null)
                );
    }
    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }

    public void adminLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, -100);
        editor.putInt(KEY_USER_TYPE, Constants.USER_TYPE_ADMIN);

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



    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }





}
