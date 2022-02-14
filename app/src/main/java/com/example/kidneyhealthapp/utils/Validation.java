package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Validation {

    public static boolean validateInput(ArrayList<EditText> arrayList, Context ctx){
        for (EditText editText: arrayList) {
            if (editText.getText().toString().trim().isEmpty()){
                Toast.makeText(ctx, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
