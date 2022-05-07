package com.example.enotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Course{

    public ArrayList<Content> contents = new ArrayList<Content>();
    public metaCourse meta = null;

    public Course(){

    }

    public Course(metaCourse meta){
        this.meta = meta;
    }

    public Course(metaCourse meta, ArrayList<Content> contents){
        this.meta = meta;
        this.contents = contents;
    }

}