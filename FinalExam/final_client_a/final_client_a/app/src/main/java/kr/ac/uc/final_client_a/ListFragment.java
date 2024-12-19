package kr.ac.uc.final_client_a;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.final_client_a.model.WordModel;

public class ListFragment extends Fragment {

    private final Uri uri = Uri.parse("content://kr.ac.uc.word.provider");

    private List<WordModel> data = new ArrayList<>();
    WordAdapter wordAdapter;
    RecyclerView rvContainer;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // todo 7 : wordAdapter 객체 생성
        wordAdapter = new WordAdapter(getContext(), data);

        /* todo 8 : wordAdapter 에 OnItemClickListener 추가.
                    ViewFragment 를 표시한다.
                    ViewFragment 를 생성할 때 선택된 아이템을 전달한다.
         */
        wordAdapter.setOnItemClickListener((pos, idx) -> {
            ((MainActivity) getActivity()).openView(data.get(pos));
        });

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rvContainer = view.findViewById(R.id.rvContainer);

        // todo 9 : rvContainer의 레이아웃을 설정하고 위에서 생성한 adapter 를 등록
        rvContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContainer.setAdapter(wordAdapter);

        // todo 10 : final_a 의 content provider 에서 query 를 이용하여 data 를 가져와서 recycler view 에 출력
        getListFromProvider();
        // todo 11 : thread 를 사용하여 3초 마다 데이터를 content provider 에서 다시 가져와서 recycler view 에 출력
//        startUpdate3Seconds();


        return view;
    }

    private void getListFromProvider() {
        if (getActivity() == null) return;

        Cursor c = null;
        c = getActivity().getContentResolver().query(uri, null, null, null, "idx");
        if (c == null) {
            Log.d("ListFragment", "커서가 비었습닏.");
            return;
        }

        Log.d("ListFragment", "커서 개수: " + c.getCount());  // 데이터 개수 로깅

        data.clear();
        while (c.moveToNext()) {
            WordModel model = WordModel.newInstance(c);
            data.add(model);
        }
        wordAdapter.notifyDataSetChanged();
    }


    private void startUpdate3Seconds() {
        new Thread(() -> {
            while (true) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(this::getListFromProvider);
                }
                SystemClock.sleep(3_000);
            }
        }).start();
    }

}