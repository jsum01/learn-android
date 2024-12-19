package kr.ac.uc.final_a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import kr.ac.uc.final_a.model.WordModel;

public class DBHelper extends SQLiteOpenHelper {
    public static final String FILE_NAME = "words.db";
    public static final int VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ WordModel.TABLE_NAME+"("+
                "idx integer primary key autoincrement," +
                "engWord text not null,"+
                "korWord text not null )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    Todo 1 : 데이터를 추가하는 메소드
     */
    public long insert(WordModel m) {
        SQLiteDatabase conn = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WordModel.COL_KOR_WORD, m.getKorWord());
        cv.put(WordModel.COL_ENG_WORD, m.getEngWord());
        long rowNum = conn.insert(WordModel.TABLE_NAME, null, cv);
        conn.close();
        return rowNum;
    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT count(*) as cnt FROM "+WordModel.TABLE_NAME, null);
        c.moveToNext();
        return c.getInt(0);
    }
}
