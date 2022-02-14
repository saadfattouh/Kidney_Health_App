package com.example.kidneyhealthapp.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Validation {

    public static boolean validateInput(Context ctx, EditText...fields){
        for (EditText editText: fields) {
            if (editText.getText().toString().trim().isEmpty()){
                Toast.makeText(ctx, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
