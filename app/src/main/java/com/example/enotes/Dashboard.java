package com.example.enotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    LinearLayout coursesLayout;
    Button add;
    ImageButton signOut;
    TextView name;
    Faculty faculty = new Faculty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        name = findViewById(R.id.nameDisplay);
        add = findViewById(R.id.addCourse);
        coursesLayout = findViewById(R.id.courses);
        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences user = getSharedPreferences("users", 0);
                SharedPreferences.Editor editor = user.edit();
                editor.putString("username", null);
                editor.putString("fullname", null);
                editor.apply();
//                DialogAlert DB = new DialogAlert("Log out", "You are successfully logged out.");
//                DB.show(getSupportFragmentManager(), "Log out");
                Toast.makeText(getApplicationContext(),"You are successfully logged out.",Toast.LENGTH_LONG).show();
                Intent signIn = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signIn);
            }
        });

        SharedPreferences user = getSharedPreferences("users", 0);
        if(user.getString("username",null)==null){
            Intent signin = new Intent(getApplicationContext(), SignIn.class);
            startActivity(signin);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            DocumentReference docRef = db.collection("faculty").document(user.getString("username", null));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            faculty = (Faculty) document.toObject(Faculty.class);
                            name.setText(faculty.fullname);
                            Log.d("number", faculty.courses.size() + "");
                            try {
                                for (Map.Entry<String, metaCourse> e : faculty.courses.entrySet()) {
                                    metaCourse m = e.getValue();
                                    if (m.status != -1) {
                                        addCourse(m);
                                    }
                                }
                            } catch (Exception e) {
                                Log.d("Dashboard", e.toString());
                            }
                        } else {
                            Log.d("Dashboard", "Error");
                        }
                    } else {
                        Log.d("Dashboard", "Failed");
                    }
                }
            });
        }
        catch (Exception e){
            Intent signin = new Intent(getApplicationContext(), SignIn.class);
            startActivity(signin);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faculty.nCourses++;
                metaCourse mc = new metaCourse(faculty.username+faculty.nCourses, "Untitled", 0);
                Course cs = new Course(mc);
                faculty.courses.put(mc.courseID,mc);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("courses").document(cs.meta.courseID)
                    .set(cs, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("faculty").document(faculty.username)
                                    .set(faculty, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            addCourse(mc);
                                            DialogAlert DB = new DialogAlert("Add new Course", "Coursed added Successfully");
                                            DB.show(getSupportFragmentManager(), "Add new Course");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            DialogAlert DB = new DialogAlert("Add new Course", "Something went wrong.");
                                            DB.show(getSupportFragmentManager(), "Add new Course");
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogAlert DB = new DialogAlert("Add new Course", "Something went wrong.");
                            DB.show(getSupportFragmentManager(), "Add new Course");
                        }
                    });
            }
        });
    }

    private void addCourse(metaCourse s) {
        final View course = getLayoutInflater().inflate(R.layout.course,null,false);

        TextView cid = (TextView) course.findViewById(R.id.courseId);
        cid.setText(s.courseID);
        TextView cname = (TextView) course.findViewById(R.id.courseName);
        cname.setText(s.title);
        ImageButton edit = (ImageButton) course.findViewById(R.id.edit);
        ImageButton status = (ImageButton) course.findViewById(R.id.status);
        ImageButton delete = (ImageButton) course.findViewById(R.id.delete);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCourse(cid.getText().toString());
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse(cid.getText().toString());
            }
        });

        coursesLayout.addView(course);

    }

    private void deleteCourse(String toString) {
    }

    private void editCourse(String cid) {
        Intent ec = new Intent(getApplicationContext(), EditCourse.class);
        Bundle b = new Bundle();
        b.putString("cid",cid);
        ec.putExtras(b);
        startActivity(ec);
    }

}