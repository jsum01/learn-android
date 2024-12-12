package com.example.cproleprovider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cproleprovider.model.GradeModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);

    TextView tvText;
    Button btnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tvText);
        btnSelect = findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(v -> selectData());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void selectData() {
        List<GradeModel> data = dbHelper.findAll();
        tvText.setText("");
        for(GradeModel m : data) {
            tvText.append("IDX= " + m.getIdx() + "\n");
            tvText.append("NAME= " + m.getName() + "\n");
            tvText.append("GRADE= " + m.getGrade() + "\n\n");
        }
    }
}