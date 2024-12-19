package kr.ac.uc.order_mgr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.order_mgr.model.OrderModel;

public class ListFragment extends Fragment {

    Button btnList, btnAdd;
    RecyclerView rvList;
    OrderAdapter orderAdapter;
    DBHelper dbHelper;

    private List<OrderModel> data = new ArrayList<>();


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo: 제일 먼저 데이터베이스에서 데이터를 불러와서 리스트에 표기해야 합니다. 이 일을 하는 게 어댑터
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 원래 return inflate~~~로 돼있는데, 아래와 같이 수정할 수 있어야 한다.
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // todo: 1. view 초기화
        rvList = view.findViewById(R.id.rvList);
        btnList = view.findViewById(R.id.btnList);
        btnAdd = view.findViewById(R.id.btnAdd);

        // todo: 2. 어댑터 설정
        orderAdapter = new OrderAdapter(getContext(), data);
        orderAdapter.setOnItemClickListener(idx -> {
            if (getActivity() instanceof MainActivity) { // 현재 Fragment가 속해있는 Activity가 Main이라면
                ((MainActivity) getActivity()).openEdit(idx);
            }
        });

        // todo: 3. RecyclerView 설정
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(orderAdapter);
        refresh();

        // todo: 4. 버튼 리스너 설정
        btnList.setOnClickListener(v -> refresh()); // 새로고침
        btnAdd.setOnClickListener(v -> { // 주문 추가
            Log.d("ListFragment", "Opening EditFragment with idx: 0");
            if (getActivity() instanceof MainActivity) { // 현재 Fragment가 속해있는 Activity가 Main이라면
                ((MainActivity) getActivity()).openEdit(0);
            }
        });

        return view;
    }

    private void refresh() {
        data = dbHelper.findAll();
        orderAdapter.setData(data);
    }


}