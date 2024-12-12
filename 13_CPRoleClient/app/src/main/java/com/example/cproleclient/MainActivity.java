package com.example.cproleclient;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    final Uri uri = Uri.parse("content://com.example.cproleprovider");

    TextView tvText;
    EditText etName, etGrade, etIdx;
    Button btnInsert, btnSelect, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tvText);
        etName = findViewById(R.id.etName);
        etGrade = findViewById(R.id.etGrade);
        etIdx = findViewById(R.id.etIdx);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(v -> insertData());
        btnSelect.setOnClickListener(v -> selectAll());
        btnUpdate.setOnClickListener(v -> updateData());
        btnDelete.setOnClickListener(v -> deleteData());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * todo:
     * 1. 입력 예외처리
     * 2. 삽입
     */
    private void insertData() {

        String name = etName.getText().toString();
        String grade = etGrade.getText().toString();

        if (name.isEmpty() || grade.isEmpty()) {
            tvText.setText("입력되지 않은 필드가 존재합니다.");
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put("name", etName.getText().toString());
        cv.put("grade", Integer.parseInt(etGrade.getText().toString()));
        // ContentResolver contentResolver = getContentResolver(); // 여기에 CRUD 메소드가 다 들어있다. 또한, 한 번밖에 안 쓰니까 바로 아래 라인에서 호출한다.
        getContentResolver().insert(uri, cv);
        selectAll();
    }

    private void selectAll() {
        Cursor c = getContentResolver().query(uri, null, null, null, "idx");
        tvText.setText("");
        if (c == null) return;
        while (c.moveToNext()) {
            int idx_pos = c.getColumnIndex("idx");
            int name_pos = c.getColumnIndex("name");
            int grade_pos = c.getColumnIndex("grade");
            tvText.append("idx= " + c.getInt(idx_pos) + "\n");
            tvText.append("name= " + c.getString(name_pos) + "\n");
            tvText.append("grade= " + c.getInt(grade_pos) + "\n\n");
        }
    }

    /**
     * todo:
     * 1. 입력 예외처리
     * 2. 수정
     */
    private void updateData() {
        String idx = etIdx.getText().toString();
        String name = etName.getText().toString();
        String grade = etGrade.getText().toString();

        if (idx.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            tvText.setText("입력되지 않은 필드가 존재합니다.");
            return;
        }
        
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("grade", Integer.parseInt(grade));
        int rowsUpdated = getContentResolver().update(uri, cv, "idx = ?", new String[]{idx});

        if (rowsUpdated > 0) {
            tvText.setText("수정 완료!");
        } else {
            tvText.setText("해당 idx의 데이터를 찾을 수 없음...");
        }
        selectAll();
    }

    /**
     * todo:
     * 1. 입력 예외처리
     * 2. 삭제
     */
    private void deleteData() {
        String idx = etIdx.getText().toString();

        if (idx.isEmpty()) {
            tvText.setText("idx를 입력하세요.");
            return;
        }

        // 입력받은 idx의 데이터를 삭제
        int rowsDeleted = getContentResolver().delete(
                uri,
                "idx = ?",
                new String[]{idx}
        );

        if (rowsDeleted > 0) {
            tvText.setText("삭제되었습니다.");
        } else {
            tvText.setText("해당 idx의 데이터를 찾을 수 없음...");
        }
        selectAll();
    }
}