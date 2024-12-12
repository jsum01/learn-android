package com.example.cproleprovider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.cproleprovider.model.GradeModel;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String FILE_NAME = "student.db";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, FILE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + GradeModel.TABLE + " (" +
                "idx INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "grade INTEGER NOT NULL)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GradeModel.TABLE);
        onCreate(db);
    }


    public List<GradeModel> findAll() {
        ArrayList<GradeModel> data = new ArrayList<>();
        SQLiteDatabase conn = this.getWritableDatabase();
        Cursor c = conn.query(
                GradeModel.TABLE,
                null, // 다 가져올 거면 null
                null,
                null,
                null,
                null,
                "idx"
        );
        while(c.moveToNext()) {
            // GradeModel.newInstance(c)의 결과로
            data.add(GradeModel.newInstance(c));

        }
        conn.close();
        return data;
    }
}
