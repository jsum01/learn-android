package com.example.cproleprovider.model;

import android.database.Cursor;

public class GradeModel {

    public static final String TABLE = "student";

    int idx;
    String name;
    int grade;

    // no args constructure
    public GradeModel() {
    }

    // full args constructure
    public GradeModel(int idx, String name, int grade) {
        this.idx = idx;
        this.name = name;
        this.grade = grade;
    }

    //
    public static GradeModel newInstance(Cursor c) {
        // static 메서드는 객체 없이 생성되는데, GradeModel을 반환하고 있으니, 생성자 역할을 한다.
        int idx_pos = c.getColumnIndex("idx");
        int name_pos = c.getColumnIndex("name");
        int grade_pos = c.getColumnIndex("grade");
        return new GradeModel(c.getInt(idx_pos), c.getString(name_pos), c.getInt(grade_pos));
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
