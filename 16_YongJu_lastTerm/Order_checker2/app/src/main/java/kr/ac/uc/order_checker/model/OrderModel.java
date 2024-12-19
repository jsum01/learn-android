package kr.ac.uc.order_checker.model;

import android.database.Cursor;

public class OrderModel {
    public static final String COL_IDX = "idx";
    public static final String COL_PRODUCT = "product";
    public static final String COL_CLIENT = "client";
    public static final String COL_AMOUNT = "amount";
    int idx;
    String product;
    String client;
    int amount;

    public static OrderModel newInstance(Cursor c){
        OrderModel m = new OrderModel();
        int col_idx = c.getColumnIndex(COL_IDX);
        int col_product = c.getColumnIndex(COL_PRODUCT);
        int col_client = c.getColumnIndex(COL_CLIENT);
        int col_amount = c.getColumnIndex(COL_AMOUNT);
        m.setIdx(c.getInt(col_idx));
        m.setProduct(c.getString(col_product));
        m.setClient(c.getString(col_client));
        m.setAmount(c.getInt(col_amount));
        return m;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
