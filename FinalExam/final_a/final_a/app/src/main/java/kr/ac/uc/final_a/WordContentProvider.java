package kr.ac.uc.final_a;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import kr.ac.uc.final_a.model.WordModel;

public class WordContentProvider extends ContentProvider {
    DBHelper dbHelper;

    public WordContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // todo 5 : delete 요청에 따라 DB 에 delete 처리
        return dbHelper.getWritableDatabase().delete(WordModel.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Todo 6 : 요청에 따라 DB 에 query 명령을 진행하고 그 결과를 Cursor 객체로 리턴
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                WordModel.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Log.d("Provider", cursor + "");
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}