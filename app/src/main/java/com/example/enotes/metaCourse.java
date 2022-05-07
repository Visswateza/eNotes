package com.example.enotes;

public class metaCourse {
    public String courseID;
    public String title;
    public int status;

    public metaCourse(){

    }

    public metaCourse(String courseID, String title){
        this.courseID = courseID;
        this.title = title;
        this.status = 0;
    }

    public metaCourse(String courseID, String title, int status){
        this.courseID = courseID;
        this.title = title;
        this.status = status;
    }
}
