package kr.ac.uc.final_client_a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.final_client_a.model.WordModel;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "http://183.104.147.15:18080/api/notification/last";
    ListFragment listFragment = new ListFragment();
    TextView tvNotice;
    TextView tvBatteryInfo;

    // Todo 4 : Broadcast Receiver 객체 생성 for ACTION_BATTERY_CHANGED
    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    // 배터리 상태를 감지하는 브로드캐스트 리시버
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = (level / (float) scale) * 100;
            tvBatteryInfo.setText("현재 충전량 : " + (int) batteryPct + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvNotice = findViewById(R.id.tvNotice);
        tvBatteryInfo = findViewById(R.id.tvBatteryInfo);

        // Todo 1 : ListFragment 를 표시.
        openList();

        // Todo 2 : Thread 를 사용하여 3초마다 API_URL 주소에서 데이터를 읽어오기 ( getNotification 메소드 사용할 것 )
        new Thread(() -> {
            while (true) {
                getNotification();
                SystemClock.sleep(3_000); // 3초마다 읽기
            }
        }).start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    void getNotification() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                in.close();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString()).getJSONObject("object");
                String datetime = jsonObject.getString("datetime");
                String title = jsonObject.getString("title");
                // Todo 3 : tvNotice 에 읽어온 title 과 datetiem 을 출력
                runOnUiThread(() -> {
                    tvNotice.setText(title + "\n " + datetime);
                });

            }

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void openList() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, listFragment);
        ft.commit();
    }

    public void openView(WordModel m) {
        ViewFragment editFragment = new ViewFragment(m);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, editFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // todo 5 : Broadcast Receiver 를 등록 ( registerReceiver 메소드 사용 )
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // todo 6 : Broadcast Receiver 를 등록 해제 ( unregisterReceiver 메소드 사용 )
        unregisterReceiver(batteryReceiver); // 브로드캐스트 리시버 해제
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }
}