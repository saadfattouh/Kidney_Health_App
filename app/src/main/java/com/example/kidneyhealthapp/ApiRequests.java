package com.example.kidneyhealthapp;

import com.example.kidneyhealthapp.model.Appointment;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.model.Instruction;
import com.example.kidneyhealthapp.model.Patient;

import java.util.ArrayList;

public interface ApiRequests {
    /**
     * Users Types :
     * int USER_TYPE_DOCTOR = 1;
     * int USER_TYPE_PATIENT = 2;
     //////
     * Gender :
     * int MALE = 0;
     * int FEMALE = 1;
     * /////////
     * Status Types :
     *int APPOINTMENT_PROCESSING = 1;
     * int APPOINTMENT_REJECTED = 0;
     * int APPOINTMENT_APPROVED = 2;
     */
    /**login
     *
     * @param String email
     * @param String password
     * @param int type
     *
     * @return patient or doctor
     *
     */

    /**
     * register_patient
     * @param String first_name
     * @param String last_name
     * @param String email
     * @param string password
     * @param int gender
     * @param String phone
     * @param String address
     * @param int age
     *
     * @return  patient
     */

    /**register_doctor
     *
     * @param String first_name
     * @param String last_name
     * @param String email
     * @param String password
     * @param String phone
     * @param String details
     *
     * @return doctor
     */

    /**
     * get_doctor_cneter
     * @param int doctor_id
     * @return center
     *  * Center
     *    int id;
     *    String name;
     *    double lat;
     *    double lon;
     *    String location;
     *    String info;
     */
    /**
     * get_centers : get all centers from database
     * @return list of centers each object has these info:
     *      * int id;
     *      * String name;
     *      * String doctorName; // using doctor_id
     *      * double lat;
     *      * double lon;
     *      * String info;
     */

    /**update_patient
     *
     * @param String first_name
     * @param String last_name
     * @param String address
     * @param String phone
     *
     * @return patient
     */

    /**update_doctor
     *
     * @param String first_name
     * @param String last_name
     * @param String address
     * @param String phone
     *
     * @return doctor
     */

    /**make_appointment
     *
     * @param patient_id
     * @param center_id
     *
     * @return status of api request
     */

    /**insert_daily_info
     *
     * @param int patient_id
     * @param String water
     * @param String medicine
     *
     * @return status of api request
     */


    /**get_my_appointments
     *return list of appointment made by this patient
     * each item has these info
     *
     * int id;
     * String doctorName;
     * String centerName;
     * int patientId;
     * String date;
     * int status;
     * String resultInfo;
     * String patientStatus;
     */

    /**get_instructions
     * @param patient_id
     * @param doctor_id
     *
     *     int id;
     *     String date;
     *     String instruction;
     */


    /**get_my_daily_info
     * @param patient_id
     * @return list of daily_ifo
     * each item has these info
     * int id
     * String water
     * String medicine
     * date created_at
     *
     */

    /**delete_daily_info
     * @param patient_id
     * @param daily_id // record id
     *
     * @return status of api request
     */

    /**get_doctors_list
     *
     * @return list of doctors
     * each item has these info
     *  int id;
     *  String firstName;
     *  String lastName;
     *  String email;
     *  String phone;
     *  String details;
     */

    /**start_chat
     * //this will start a chat by creating a new chat record only if it is not created yet
     * if chat is already created return chat info
     *
     * @param patient_id
     * @param doctor_id
     *
     * @return status of api request //or chat info
     *
     */

    /**patient_get_chat_list
     * @param patient_id
     *
     * @return list of chats which has this patient as a member
     * each item has these info
     *
     * int id
     * object doctor
     */

    /**doctor_get_chat_list
     * @param doctor_id
     *
     * @return list of chats which has this doctor as a member
     * each item has these info
     *
     * int id
     * object patient
     */

    /**get_chat_content
     *
     * @param chat_id
     *
     * @return list of chat content "conversation"
     * each item has these info:
     *
     * int id
     * int sender_type //TODO add sender type to database  we need it in because id is coming from two different table
     * String content
     * date created_at
     *
     */

    /**send_massage
     *int chat_id
     *int sender_type
     *String content
     *
     */

    /**update_center
     * @param int center_id
     * @param String name
     * @param String info
     * @param String location
     * @param double lat
     * @param double lon
     *
     */

    /**get_patients
     * @reurn lst of patients
     * each item has these info
     * int id;
     * String firstName;
     * String lastName;
     * String email;
     * String phone;
     * String address;
     * int age;
     * int gender
     */

    /**get_patient_daily_info
     * @param patient_id
     *
     * @return list of daily_info
     * each item has these info
     *  int id
     *  String water
     *  String medicine
     *  date created_at
     *
     */

    /**get_appointment_requests
     * @param int center_id
     *
     * @return list of appointments for this center
     * each item has these info
     * int id
     * int patient_id
     * int status
     * int created_at
     * String patient_status
     * String resultInfo
     * Object patient
     */

    /**change_appointment_status
     * int appointment_id
     * int status

     *
     */
    /**update_appointment
     * int appointment_id
     *String resultInfo
     *String patientStatus
     *
     */

    /**get_Center_patients
     * @param center_id
     * @return list of patients who made an appointmt to this center
     */

    //Admin//
    /**delete_center
     * @param center_id
     */
    /**link_doctor_to center
     * note: add doctor_id to center record
     * int center_id
     * int doctor_id
     *
     */

    /**delete_doctor
     * int doctor_id
     *
     */
    /**delete_patient
     * int patient_id
     *
     */


}
