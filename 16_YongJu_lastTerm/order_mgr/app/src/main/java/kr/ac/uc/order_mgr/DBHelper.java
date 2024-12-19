package kr.ac.uc.order_mgr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.order_mgr.model.OrderModel;

public class DBHelper extends SQLiteOpenHelper {
    public static final String FILE_NAME = "order.db";
    public static final int VERSION = 1;


    public DBHelper(@Nullable Context context) {
        super(context, FILE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE order_tbl (" +
                "idx integer primary key autoincrement, "+
                "product text not null," +
                "client text not null," +
                "amount int not null )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(OrderModel m){ // 이 부분 비워질 수 있음
        SQLiteDatabase conn = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(OrderModel.COL_PRODUCT, m.getProduct());
        cv.put(OrderModel.COL_CLIENT, m.getClient());
        cv.put(OrderModel.COL_AMOUNT, m.getAmount());
        long rowNum =  conn.insert(OrderModel.TABLE_NAME, null, cv);
        conn.close();
        return rowNum;
    }

    public int modify(OrderModel m){ // 이 부분 비워질 수 있음
        SQLiteDatabase conn = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(OrderModel.COL_PRODUCT, m.getProduct());
        cv.put(OrderModel.COL_CLIENT, m.getClient());
        cv.put(OrderModel.COL_AMOUNT, m.getAmount());
        int rowNum = conn.update(OrderModel.TABLE_NAME, cv, "idx=?", new String[]{String.valueOf(m.getIdx())});
        conn.close();
        return rowNum;
    }

    public int remove(int idx){
        SQLiteDatabase conn = this.getWritableDatabase();
        int rowNum = conn.delete(OrderModel.TABLE_NAME, "idx=?", new String[]{String.valueOf(idx)});
        conn.close();
        return rowNum;
    }

    public List<OrderModel> findAll(){
        ArrayList<OrderModel> list = new ArrayList<>();
        SQLiteDatabase conn = this.getWritableDatabase();
        Cursor c = conn.query(OrderModel.TABLE_NAME, null, null, null, null, null, "idx");
        while(c.moveToNext()){
            list.add(OrderModel.newInstance(c));
        }
        conn.close();
        return list;
    }

    public OrderModel getOrder(int idx){
        SQLiteDatabase conn = this.getWritableDatabase();
        Cursor c = conn.query(OrderModel.TABLE_NAME, null, "idx=?", new String[]{String.valueOf(idx)}, null, null, null, null);
        OrderModel m = null;
        if (c.moveToNext()){ // DB의 테이블에서 가져온 값이 있다면?
            m = OrderModel.newInstance(c); // OrderModel의 인스턴스를 만들어서 m에다가 값 할당
        }
        conn.close();
        return m;
    }
}
