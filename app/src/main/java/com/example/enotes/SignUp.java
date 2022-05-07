package com.example.enotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SignUp extends AppCompatActivity {

    EditText fullname, email, username, password, conPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        conPassword = findViewById(R.id.cnfpassword);

        register = findViewById(R.id.submit);

        Faculty faculty = new Faculty();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullname.getText().toString().trim().equals("")){
                    DialogAlert DB = new DialogAlert("Caution","Full Name must not be empty");
                    DB.show(getSupportFragmentManager(), "Caution");
                }
                else if(email.getText().toString().trim().equals("")){
                    DialogAlert DB = new DialogAlert("Caution","Email must not be empty");
                    DB.show(getSupportFragmentManager(), "Caution");
                }
                else if(username.getText().toString().trim().equals("")){
                    DialogAlert DB = new DialogAlert("Caution","Username must not be empty");
                    DB.show(getSupportFragmentManager(), "Caution");
                }
                else if(password.getText().toString().trim().equals("")){
                    DialogAlert DB = new DialogAlert("Caution","Password must not be empty");
                    DB.show(getSupportFragmentManager(), "Caution");
                }
                else if(!conPassword.getText().toString().trim().equals(password.getText().toString().trim())){
                    DialogAlert DB = new DialogAlert("Caution","Password mis-match");
                    DB.show(getSupportFragmentManager(), "Caution");
                }
                else{
                    faculty.fullname = fullname.getText().toString().trim();
                    faculty.email = email.getText().toString().trim();
                    faculty.username = username.getText().toString().trim();
                    faculty.password = password.getText().toString().trim();

                    db.collection("faculty").document(faculty.username)
                            .set(faculty, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Signup", "DocumentSnapshot successfully written!");
                                    DialogAlert DB = new DialogAlert("Registration Successful", "Redirect to Login page");
                                    DB.show(getSupportFragmentManager(), "Registration Successful");
                                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Signup", "Error writing document", e);
                                    DialogAlert DB = new DialogAlert("Registration Failed", "Please try again");
                                    DB.show(getSupportFragmentManager(), "Registration Failed");
                                }
                            });
                }
            }
        });
    }
}