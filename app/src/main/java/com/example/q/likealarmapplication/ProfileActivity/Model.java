package com.example.q.likealarmapplication.ProfileActivity;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class Model {

    public static final int EDIT_NAME_TYPE=0;
    public static final int EDIT_SEX_TYPE=1;
    public static final int EDIT_AGE_TYPE=2;
    public static final int EDIT_HEIGHT_TYPE=3;
    public static final int EDIT_CHAR_TYPE=4;
    public static final int EDIT_ALCOHOL_TYPE=5;


    public int type;
    public String text;
    public String text2;
    public ArrayList<String> strings;
    public Bitmap bitmap;

    public Model(int type, String text, String text2, ArrayList<String> strings, Bitmap bitmap)
    {
        this.type=type;
        this.text=text;
        this.text2=text2;
        this.strings = strings;
        this.bitmap = bitmap;
    }
}