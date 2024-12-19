package kr.ac.uc.order_mgr;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String API_URL = "http://183.104.147.15:18080/api/notification/last";
    ListFragment listFragment = new ListFragment();
    TimeService timeService = null;
    ConnectionClass serviceConnection = new ConnectionClass();
    TextView tvShowTimePass, tvNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvShowTimePass = findViewById(R.id.tvShowTimePass);
        tvNotice = findViewById(R.id.tvNotice);

        Intent intent = new Intent(this, TimeService.class);
        boolean checkServiceState = isServiceRunning();
        if (!checkServiceState) {
            startService(intent);
        }
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        // bind가 되었으니, bind 시점부터 timeService에 있는 메서드를 사용할 수 있게 된다

        openList();

        runBackgroundThread(); // 백그라운드에서 돌고 있는 쓰레드

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isServiceRunning() {
        // 서비스 주소를 알아야 서비스 상태를 확인할 수 있을 것 같다.
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list)
            if (info.service.getClassName().equals("kr.ac.uc.order_mgr.TimeService"))
                return true;
        return false;
    }

    private void runBackgroundThread() {
        new Thread(() -> {
            while (true) {
                getNotification();
                runOnUiThread(() -> {
                    if (timeService != null)
                        tvShowTimePass.setText(timeService.getPassedSec() + " 초 지남");
                });
//                SystemClock.sleep(5_000); // 5초마다 읽기 <- 여기서 굳이 해야하나 싶음(TimeService에서도 동일한 코드가 있는데)
            }
        }).start();
    }

    private void getNotification() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod("GET");         // 통신 방식
            connection.setDoInput(true);                // 읽기모드 지정
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
                String author = jsonObject.getString("author");
                String title = jsonObject.getString("title");
                runOnUiThread(() -> {
                    tvNotice.setText(author + "\n" + title + "\n" + datetime);
                }); // Thread에서 view에 직접 접근할 수 없다

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

    public void openEdit(int idx) {
        EditFragment editFragment = new EditFragment(idx);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, editFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // Fragment 교체 Function을 제작해서 재사용(다양한 프래그먼트로 대체 가능)
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null) // 뒤로가기 지원
                .commit();
    }

    // 서비스할 때 바인드하기 위한 것
    class ConnectionClass implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimeService.LocalBinder binder = (TimeService.LocalBinder) service;
            timeService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timeService = null;
        }
    }
}