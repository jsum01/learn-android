package kr.ac.uc.order_checker;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.order_checker.model.OrderModel;

public class MainActivity extends AppCompatActivity {
    // 아래의 주소는 order_mgr의 매니페스트 android:authorities="kr.ac.uc.order.provider" 이 부분
    public final Uri uri = Uri.parse("content://kr.ac.uc.order.provider");
    RecyclerView rvList;
    OrderAdapter orderAdapter;
    List<OrderModel> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvList);

        orderAdapter = new OrderAdapter(this, data);
        orderAdapter.setOnItemClickListener(idx -> deleteFromProvider(idx));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(orderAdapter);
        // 여기까지 어댑터 셋팅

        getListFromProvider();

        new Thread(() -> {
            while (true){
                SystemClock.sleep(3_000);
                runOnUiThread(() -> {
                    getListFromProvider();
                });
            }
        }).start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void deleteFromProvider(int idx) {
        getContentResolver().delete(uri, "idx=?", new String[]{String.valueOf(idx)});
        getListFromProvider();
    }

    private void getListFromProvider() {
        Cursor c = getContentResolver().query(uri, null, null, null, "idx");
        if (c == null) return;
        data.clear();
        while (c.moveToNext()){
            data.add(OrderModel.newInstance(c));
        }
        orderAdapter.notifyDataSetChanged();
    }

}