package com.example.enotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    EditText username,password;
    Button submit;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        SharedPreferences user = getSharedPreferences("users", 0);
        if(user.getString("username", null)!=null){
            Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(dashboard);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DocumentReference docRef = db.collection("faculty")
                            .document(username.getText().toString().trim());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Faculty faculty = (Faculty) document.toObject(Faculty.class);
                                    Log.d("Signin", "DocumentSnapshot data: " + faculty.password);
                                    if (password.getText().toString().trim().equals(faculty.password)) {
                                        SharedPreferences user = getSharedPreferences("users", 0);
                                        SharedPreferences.Editor editor = user.edit();
                                        editor.putString("username", faculty.username);
                                        editor.putString("fullname",faculty.fullname);
                                        editor.putInt("nCourses",faculty.nCourses);
                                        editor.apply();
                                        Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
                                        startActivity(dashboard);
                                    } else {
                                        DialogAlert DB = new DialogAlert("Login Failed", "Wrong Password\nTry again!");
                                        DB.show(getSupportFragmentManager(), "Login Failed");
                                    }
                                } else {
                                    DialogAlert DB = new DialogAlert("Login Failed", "User not found\nCreate a new Account!");
                                    DB.show(getSupportFragmentManager(), "Login Failed");
                                    Log.d("Signin", "No such document");
                                }
                            } else {
                                Log.d("Signin", "get failed with ", task.getException());
                                DialogAlert DB = new DialogAlert("Something went Wrong", "Please try again.");
                                DB.show(getSupportFragmentManager(), "Something went Wrong");
                            }
                        }
                    });
                }
                catch (Exception e){
                    Log.d("SignIn", e.toString());
                }
            }
        });

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });

    }
}