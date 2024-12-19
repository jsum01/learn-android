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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.uc.order_mgr.model.OrderModel;

public class ListFragment extends Fragment {

    Button btnList, btnAdd;
    RecyclerView rvList;
    OrderAdapter orderAdapter;
    DBHelper dbHelper;
    List<OrderModel> data = new ArrayList<>();


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false); // 이 부분도 누락될 수도 있음
        rvList = view.findViewById(R.id.rvList);
        btnList = view.findViewById(R.id.btnList);
        btnAdd = view.findViewById(R.id.btnAdd);

        orderAdapter = new OrderAdapter(getContext(), data); // getContext나 getActivity는 똑같음

        orderAdapter.setOnItemClickListener((idx) -> { // idx를 받아오기로 설정했으니 idx 사용
            ((MainActivity)getActivity()).openEdit(idx); // 이 부분은 수정
        });

        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(orderAdapter);
        refresh();

        btnList.setOnClickListener(v -> refresh());
        btnAdd.setOnClickListener(v -> {
            ((MainActivity)getActivity()).openEdit(0); // add는 새롭게 넣는거니까 0을 입력
        });

        return view;
    }

    private void refresh() {
        data = dbHelper.findAll();
        orderAdapter.setData(data);
        orderAdapter.notifyDataSetChanged();
    }

}