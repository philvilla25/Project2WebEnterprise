package com.example.sliderapp;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    TextInputEditText editTextUser, editTextPassword;
    Button buttonReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        editTextUser = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user,password;
                user = String.valueOf(editTextUser.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(user)){
                    Toast enterUsername = Toast.makeText(Register.this, "Enter Username", Toast.LENGTH_SHORT);
                    enterUsername.show();
                }

                if (TextUtils.isEmpty(password)){
                    Toast enterPassword = Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT);
                    enterPassword.show();
                }


            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}