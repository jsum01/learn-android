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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.order_checker.model.OrderModel;

public class MainActivity extends AppCompatActivity {
    private final Uri uri = Uri.parse("content://kr.ac.uc.order.provider");
    RecyclerView rvList;
    OrderAdapter orderAdapter;

    private List<OrderModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvList);
        orderAdapter = new OrderAdapter(this, data);
        orderAdapter.setOnItemClickListener(idx -> {
            // TODO: resolver에서 dataProvider를 사용해서 지우는 방식
            deleteFromProvider(idx);
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(orderAdapter);
        getListFromProvider();

        new Thread(() -> {
            while (true) {
                SystemClock.sleep(3_000);
                runOnUiThread(this::getListFromProvider);
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
        getListFromProvider(); // 지우고 다시 리스트를 가져와야 하니까
    }

    // TODO: 1. resolver가 필요하다
    private void getListFromProvider() {
        Cursor c = getContentResolver().query(uri, null, null, null, "idx");
        if (c == null) return;
        data.clear();
        // 데이터를 교체시켜주는 게 아니라, ArrayList의 element를 다 지우고 다시 채우는 과정으로 해보겠다.
        while (c.moveToNext()) { // data.addAll(c), data.set(newData)하는 방법도 있음
            data.add(OrderModel.newInstance(c));
        }
        orderAdapter.notifyDataSetChanged();
    }

}