package com.example.kidneyhealthapp.utils;

public class Urls {

    //MAIN REQUESTS
//    public static final String BASE_URL = "http://std.scit.co/kidney-health/public/api/";
    public static final String BASE_URL = "http://192.168.43.130/std-kidney-health/public/api/";
    public static final String LOGIN = BASE_URL +"login";
    public static final String RESET_PASSWORD = BASE_URL + "";
    public static final String UPDATE_PATIENT_ACC = BASE_URL + "update_patient";
    public static final String UPDATE_DOCTOR_ACC = BASE_URL + "update_doctor";

    //Patient
    public static final String REGISTER_PATIENT = BASE_URL + "register_patient";
    public static final String GET_CENTERS = BASE_URL + "get_centers";
    public static final String MAKE_APPOINTMENT = BASE_URL + "make_appointment";
    public static final String GET_MY_APPOINTMENT = BASE_URL + "get_my_appointments";

    //Doctor
    public static final String REGISTER_DOCTOR = BASE_URL + "register_doctor";
    public static final String GET_PATIENTS_LIST = BASE_URL + "";
    public static final String GET_DOCTOR_CENTER = BASE_URL + "get_doctor_center";

    public static final String GET_MESSAGES_LIST = BASE_URL + "";
    public static final String GET_MESSAGES_WITH = BASE_URL + "";
    public static final String INSERT_INFO = BASE_URL + "insert_daily_info";
    public static final String PATIENT_GET_CHAT_LIST = BASE_URL + "patient_get_chat_list";
    public static final String GET_CHAT_CONTENT =  BASE_URL + "get_chat_content" ;
    public static final String SEND_MESSAGE = BASE_URL + "send_massage";
    public static final String GET_DOCTORS = BASE_URL + "get_doctors_list";
    public static final String START_CHAT = BASE_URL + "start_chat";
    public static final String GET_INSTRUCTIONS = BASE_URL + "get_instructions";
    public static final String GET_DAILY_INFO =  BASE_URL + "get_patient_daily_info";
    public static final String DELETE_DAILY =  BASE_URL + "delete_daily_info";
    public static final String UPDATE_CENTER =  BASE_URL + "update_center";


    public static final String GET_CENTER_PATIENTS =  BASE_URL + "get_Center_patients";
    public static final String DOCTOR_GET_CHAT_LIST = BASE_URL + "doctor_get_chat_list";
    public static final String GET_APPOINTMENT_REQUESTS = BASE_URL + "get_appointment_requests";
    public static final String CHANGE_APPOINTMENT_STATUS = BASE_URL + "change_appointment_status";
    public static final String UPDATE_APPOINTMENT = BASE_URL + "update_appointment";
    public static final String ADD_INSTRUCTION = BASE_URL + "insert_instructions";
    public static final String DELETE_CENTER = BASE_URL + "delete_center";
    public static final String DELETE_DOCTOR = BASE_URL + "delete_doctor";
    public static final String DELETE_PATIENT = BASE_URL + "delete_patient";
    public static final String ADD_CENTER = BASE_URL + "insert_center";
    public static final String GET_PATIENTS = BASE_URL + "get_patients";
    public static final String LINK_DOCTOR_TO_CENTER = BASE_URL + "link_doctor_to_center";
}

