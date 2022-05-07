package com.example.enotes;

import java.util.ArrayList;
import java.util.HashMap;

public class Faculty {

    public String fullname, email, username, password;
    public HashMap<String, metaCourse> courses = new HashMap<String, metaCourse>();
    public int nCourses=0;

    public Faculty(){}

    public Faculty(String fullname, String email, String username, String password, int nCourses){
        this.fullname = fullname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.nCourses = nCourses;
    }

    public Faculty(String fullname, String email, String username, String password, int nCourses, HashMap<String, metaCourse> courses){
        this.fullname = fullname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.courses = courses;
        this.nCourses = nCourses;
    }
}
