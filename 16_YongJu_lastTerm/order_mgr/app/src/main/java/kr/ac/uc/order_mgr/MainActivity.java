package kr.ac.uc.order_mgr;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String API_URL = "http://183.104.147.15:18080/api/notification/last";
    ListFragment listFragment = new ListFragment();
    TimeService timeService = null;
    ConnectionClass serviceConnection = new ConnectionClass();
    TextView tvShowTimePass, tvNotice, tvBatteryLevel, tvBatteryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvShowTimePass = findViewById(R.id.tvShowTimePass);
        tvNotice = findViewById(R.id.tvNotice);
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);
        tvBatteryStatus = findViewById(R.id.tvBatteryStatus);

        // 배터리 상태를 감지하는 BroadcastReceiver 등록
        // IntentFilter: 여러가지 Action 중 배터리 정보만 골라와서 사용
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter); // 리시버 등록하는데 filter에 맞는 일만 하도록 등록


        Intent intent = new Intent(this, TimeService.class);
        boolean chk = isServiceRunning();
        if (!chk){
            startService(intent);
        }
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        openList(); // 앱이 열리자마자 listFragment가 바로 열리도록 함

        runBackgroundThread();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isServiceRunning() {
        // kr.ac.uc.order_mgr.TimeService
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        // 실제 실행중인 서비스를 받아와서 리스트에 저장
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : list){
            if (info.service.getClassName().equals("kr.ac.uc.order_mgr.TimeService")){
                return true;
            }
        }
        return false;
    }

    private void runBackgroundThread() {
        new Thread(() -> {
            while (true){
                getNotification(); // 실제 네트워크를 통해서 가져오는 데이터는 별도로 메소드를 만들어서 합시다.
                runOnUiThread(() -> {
                    if (timeService != null){
                        tvShowTimePass.setText(timeService.getPassedSec() + "초 지남");
                    }
                });
                SystemClock.sleep(5_000);
            }
        }).start();
    }

    private void getNotification() {
        // 매니페스트에 INTERNET 펄미션 추가 및 application에 android:usesCleartexTraffic="true" 꼭 넣어야함
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestMethod("GET"); // 통신방식
            connection.setDoInput(true); // 읽기 설정
            connection.setDoOutput(false);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder sb = new StringBuilder();

                while ((inputLine = br.readLine()) != null){
                    sb.append(inputLine);
                }
                br.close();

                JSONObject jsonObject = new JSONObject(sb.toString()).getJSONObject("object");
                String datetime = jsonObject.getString("datetime");
                String author = jsonObject.getString("author");
                String title = jsonObject.getString("title");

                runOnUiThread(() -> {
                    tvNotice.setText(author + "\n" + title + datetime + "\n");
                });
            }

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void openList(){
        // 여기에 백스택을 넣게되면 리스트 프래그먼트가 나타나기 전에 하면 즉, 빈화면에 또 나타날꺼니까 넣지말자
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, listFragment);
        ft.commit();
    }

    public void openEdit(int idx){
        EditFragment editFragment = new EditFragment(idx);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, editFragment);
        ft.addToBackStack(null); // 뒤로가기를 누르면 원래 앞에 있던 오픈리스트가 열려있는 상태로 돌아가게된다.
        ft.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver); // 브로드캐스트 리시버 해제
    }

    // 배터리 상태를 감지하는 브로드캐스트 리시버
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);  // 현재 배터리 레벨
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);  // 최대 배터리 레벨
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            // 퍼센트 계산
            float batteryPct = (level / (float)scale) * 100;

            // 배터리 상태 표시
            tvBatteryLevel.setText("배터리 상태 : " + (int) batteryPct + "%");

            switch (status){
                case 1 : tvBatteryStatus.setText("Battery Status : 알 수 없음"); break;
                case 2 : tvBatteryStatus.setText("Battery Status : 충전중"); break;
                case 3 : tvBatteryStatus.setText("Battery Status : Discharging"); break;
                case 4 : tvBatteryStatus.setText("Battery Status : 충전 중 아님"); break;
                case 5 : tvBatteryStatus.setText("Battery Status : 충전 완료"); break;
            }
        }
    };

    class ConnectionClass implements ServiceConnection { // 서비스 사용할 때 바인드하기 위한 커넥션

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimeService.LocalBinder binder = (TimeService.LocalBinder)service;
            timeService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timeService = null;
        }
    }
}