package com.example.cproleprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.cproleprovider.model.GradeModel;

import java.net.URI;

public class MyContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.cproleprovider");
    DBHelper dbHelper;

    // todo: DBHelper와 연동
    public MyContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase conn = dbHelper.getWritableDatabase();
        int deletedRows = conn.delete(GradeModel.TABLE, selection, selectionArgs);
        if (deletedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null); // 알림
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase conn = dbHelper.getWritableDatabase();
        long id = conn.insert(GradeModel.TABLE, null, values);
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase conn = dbHelper.getWritableDatabase();
        return conn.query(GradeModel.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase conn = dbHelper.getWritableDatabase();
        return conn.update(GradeModel.TABLE, values, selection, selectionArgs);
    }
}