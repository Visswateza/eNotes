package com.example.enotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class EditCourse extends AppCompatActivity {

    LinearLayout contentsLayout;
    ImageButton addContent, addImage, addURL, save, signOut;
    TextView name;
    EditText title;
    Course course = new Course();
    Faculty faculty = new Faculty();

    Uri ima;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        name = findViewById(R.id.nameDisplay);
        addContent = findViewById(R.id.addContent);
        addImage = findViewById(R.id.addImage);
        addURL = findViewById(R.id.addLink);
        contentsLayout = findViewById(R.id.contents);
        signOut = findViewById(R.id.signOut);
        title = findViewById(R.id.ctitle);
        title.setEnabled(false);
        save = findViewById(R.id.saveCourse);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences user = getSharedPreferences("users", 0);
                SharedPreferences.Editor editor = user.edit();
                editor.putString("username", null);
                editor.putString("fullname", null);
                editor.apply();
                Toast.makeText(getApplicationContext(), "You are successfully logged out.", Toast.LENGTH_LONG).show();
                Intent signIn = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signIn);
            }
        });

        Log.d("EC", "Hello");
        Bundle b = getIntent().getExtras();
        String cid = b.getString("cid");
        Log.d("EC", b.getString("cid"));

        SharedPreferences user = getSharedPreferences("users", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("faculty").document(user.getString("username", null));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        faculty = (Faculty) document.toObject(Faculty.class);
                        name.setText(faculty.fullname);
                    }
                } else {
                }
            }
        });

        docRef = db.collection("courses").document(cid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        course = (Course) document.toObject(Course.class);
                        Log.d("Course Data", course.toString());
                        title.setText(course.meta.title);
                        try {
                            for (int i = 0; i < course.contents.size(); i++) {
                                addContent(course.contents.get(i));

                            }
                        } catch (Exception e) {
                            Log.d("Edit Course", e.toString());
                        }
                    } else {
                        Log.d("Edit Course", "Error");
                    }
                } else {
                    Log.d("Edit Course", "Failed");
                }
            }
        });

        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContent(new Content("Text", null, null, null, 18, false, false));
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContent(new Content("Image", null, null, null, 18, false, false));
            }
        });

        addURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContent(new Content("URL", null, null, null, 18, false, false));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course.contents.clear();
                for (int i = 0; i < contentsLayout.getChildCount(); i++) {
                    View con = contentsLayout.getChildAt(i);
                    TextView type = con.findViewById(R.id.type);
                    EditText text = con.findViewById(R.id.content);
                    EditText size = con.findViewById(R.id.fontSize);
                    ImageButton bold = con.findViewById(R.id.bold);
                    ImageButton italic = con.findViewById(R.id.italic);
                    Content c = new Content(type.getText().toString(), text.getText().toString(), null, "", Integer.parseInt(size.getText().toString()), bold.isActivated(), italic.isActivated());
                    course.contents.add(c);
                }
                Log.d("Save Course", course.toString());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("courses").document(course.meta.courseID)
                        .set(course)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DialogAlert DB = new DialogAlert("Save Changes", "Changes applied successfully");
                                DB.show(getSupportFragmentManager(), "Save Changes");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogAlert DB = new DialogAlert("Save Changes", "Something went wrong.");
                                DB.show(getSupportFragmentManager(), "Save Changes");
                            }
                        });
            }
        });
    }

    private void addContent(Content content) {
        final View con = getLayoutInflater().inflate(R.layout.content,null,false);

        TextView type = (TextView) con.findViewById(R.id.type);
        type.setText(content.type);
        EditText text = (EditText) con.findViewById(R.id.content);
        text.setText(content.content);
        ImageView image = (ImageView) con.findViewById(R.id.image);
        EditText title = (EditText) con.findViewById(R.id.title);
        title.setTextSize(18);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        EditText fontSize = (EditText) con.findViewById(R.id.fontSize);
        fontSize.setText(String.valueOf(content.size));
        text.setTextSize(Integer.parseInt(fontSize.getText().toString()));
        ImageButton bold = (ImageButton) con.findViewById(R.id.bold);
        ImageButton italic = (ImageButton) con.findViewById(R.id.italic);
        ImageButton dFont = (ImageButton) con.findViewById(R.id.dFont);
        ImageButton iFont = (ImageButton) con.findViewById(R.id.iFont);
        LinearLayout tools = (LinearLayout) con.findViewById(R.id.tools);

        bold.setActivated(content.isBold);
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), String.valueOf(bold.isActivated()), Toast.LENGTH_LONG).show();
                if(bold.isActivated()) {
                    text.setTypeface(null, Typeface.NORMAL);
                    bold.setActivated(false);
                }
                else{
                    text.setTypeface(null, Typeface.BOLD);
                    bold.setActivated(true);
                }
                if(italic.isActivated()){
                    text.setTypeface(null, Typeface.ITALIC);
                }
                if(italic.isActivated() && bold.isActivated()){
                    text.setTypeface(null, Typeface.BOLD_ITALIC);
                }
            }
        });

        italic.setActivated(content.isItalic);
        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(italic.isActivated()) {
                    text.setTypeface(null, Typeface.NORMAL);
                    italic.setActivated(false);
                }
                else{
                    text.setTypeface(null, Typeface.ITALIC);
                    italic.setActivated(true);
                }
                if(bold.isActivated()){
                    text.setTypeface(null, Typeface.BOLD);
                }
                if(italic.isActivated() && bold.isActivated()){
                    text.setTypeface(null, Typeface.BOLD_ITALIC);
                }
            }
        });

        iFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fs = Integer.parseInt(fontSize.getText().toString())+1;
                fontSize.setText(""+fs);
                text.setTextSize(fs);
            }
        });

        dFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fs = Integer.parseInt(fontSize.getText().toString())-1;
                fontSize.setText(""+fs);
                text.setTextSize(fs);
            }
        });

        if(content.type.equals("Text")){
            image.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
        }

        if(content.type.equals("Image")){
            text.setTextSize(0);
            text.setVisibility(View.INVISIBLE);

            Intent img = new Intent();
            img.setType("image/*");
            img.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(img, 2);
            image.setImageURI(ima);
            ima = null;
        }

        contentsLayout.addView(con);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            ima = data.getData();
        }
    }
}