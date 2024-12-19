package kr.ac.uc.final_a;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.ac.uc.final_a.model.WordModel;

public class MainActivity extends AppCompatActivity {
    EditText etEngWord, etKorWord;
    Button btnSave;
    TextView tvWordCount;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etEngWord = findViewById(R.id.etEngWord);
        etKorWord = findViewById(R.id.etKorWord);
        tvWordCount = findViewById(R.id.tvWordCount);
        btnSave = findViewById(R.id.btnSave);

        // Todo 2 : 저장된 데이터의 개수를 tvWordCount에 표시하는 기능
        setCurrentWordCount();

        // Todo 3 : Thread 를 사용하여 3초마다 저장된 데이터의 개수를 표시하는 기능
        getWordCountThreeSeconds();

        /* Todo 4 : btnSave 버튼이 클릭 되면
                    etEngWord 와 etKorWord 에 입력된 값을 db 에 저장
                    저장된 데이터의 개수를 다시 표시
                    etEngWord 와 etKorWord 를 초기화 ( 입력된 글자 지워서 빈 값("") 으로 남김 )
         */
        btnSave.setOnClickListener(v -> saveInputData());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setCurrentWordCount() {
        tvWordCount.setText(dbHelper.getCount() + "개");
    }

    private void getWordCountThreeSeconds() {
        new Thread(() -> {
            while (true) {
                runOnUiThread(() -> {
                    setCurrentWordCount();
                });
                SystemClock.sleep(3_000); // 5초마다 읽기 <- 여기서 굳이 해야하나 싶음(TimeService에서도 동일한 코드가 있는데)
            }
        }).start();
    }

    private void saveInputData() {
        String eng = etEngWord.getText().toString();
        String kor = etKorWord.getText().toString();

        // 입력값 유효성 검사
        if (eng.isEmpty() || kor.isEmpty()) {
            return;
        }

        WordModel m = new WordModel(eng, kor);
        dbHelper.insert(m);

        // 저장된 데이터 다시 표시
        setCurrentWordCount();
    }
}