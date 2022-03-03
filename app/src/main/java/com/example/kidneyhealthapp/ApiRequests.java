package com.example.kidneyhealthapp;

import com.example.kidneyhealthapp.model.User;

public interface ApiRequests {

    //======== All Users============

    /**
     *   Users Types :
     *    int USER_TYPE_DOCTOR = 1;
     *    int USER_TYPE_PATIENT = 2;
     *
     *   Gender :
     *    int MALE = 0;
     *    int FEMALE = 1;
     *   Status Types :
     *    String APPOINTMENT_PROCESSING = "processing";
     *    String APPOINTMENT_REJECTED = "rejected";
     *    String APPOINTMENT_APPROVED = "approved";
     */
    User login(String user_name, String password, int type);
    User register(String first_name, String last_name, String user_name, String password, int gender, String phone, String address, String age);


}
