package kr.ac.uc.final_client_a;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.final_client_a.model.WordModel;

public class ViewFragment extends Fragment {

    private final Uri uri = Uri.parse("content://kr.ac.uc.word.provider");

    WordModel model;
    TextView tvEngWord;
    TextView tvKorWord;
    Button btnDelete, btnBack;


    public ViewFragment() {
        // Required empty public constructor
    }

    public ViewFragment(WordModel m) {
        this.model = m;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        tvEngWord = view.findViewById(R.id.tvEngWord);
        tvKorWord = view.findViewById(R.id.tvKorWord);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnBack = view.findViewById(R.id.btnBack);

        // todo 12 : tvEngWord 와 tvKorWord 에 선택된 아이템(model)의 값을 출력
        tvEngWord.setText(model.getEngWord().toString());
        tvKorWord.setText(model.getKorWord().toString());

        // todo 13 : final_a 의 content provider의 delete 를 요청한 후 ListFragment 로 돌아감 (popBackStack 사용)
        deleteFromProvider();

        // todo 14 : ListFragment 로 돌아감 (popBackStack 사용)
        btnBack.setOnClickListener(v -> moveBack());

        return view;
    }

    private void deleteFromProvider() {
        String where = WordModel.COL_IDX + " = ?";
        String[] selectionArgs = {String.valueOf(model.getIdx())};

        // ContentResolver를 통해 삭제 실행
        getActivity().getContentResolver().delete(uri, where, selectionArgs);

        // 삭제 후 이전 화면으로 돌아가기
        moveBack();
    }

    private void moveBack() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


}