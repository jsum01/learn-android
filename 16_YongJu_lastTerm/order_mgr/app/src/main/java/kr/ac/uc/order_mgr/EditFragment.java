package kr.ac.uc.order_mgr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kr.ac.uc.order_mgr.model.OrderModel;


public class EditFragment extends Fragment {

    int idx;
    TextView tvIdx;
    EditText etProduct, etClient, etCount;
    Button btnSave, btnDelete;

    DBHelper dbHelper;
    public EditFragment() {
        // Required empty public constructor
    }

    public EditFragment(int idx) {
        this.idx = idx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit, container, false);
        tvIdx = view.findViewById(R.id.tvIdx);
        etProduct = view.findViewById(R.id.etProduct);
        etClient = view.findViewById(R.id.etClient);
        etCount = view.findViewById(R.id.etCount);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);

        initialzation(idx);

        btnSave.setOnClickListener(v -> doSave());
        btnDelete.setOnClickListener(v -> doDelete());

        return view;
    }

    private void doDelete() {
        if (idx != 0) dbHelper.remove(idx);

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void doSave() {
        String product = etProduct.getText().toString();
        String client = etClient.getText().toString();
        String amount = etCount.getText().toString();

        if (product.isEmpty() || client.isEmpty() || amount.isEmpty()) return;

        OrderModel m = new OrderModel(idx, product, client, Integer.parseInt(amount));

        if (idx == 0) dbHelper.insert(m);
        else dbHelper.modify(m);

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void initialzation(int idx) {
        if (idx == 0){ // 입력
            tvIdx.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }else { // 수정
            OrderModel m = dbHelper.getOrder(idx);
            if (m != null){
                tvIdx.setText("IDX : " + m.getIdx());
                etProduct.setText("" + m.getProduct());
                etClient.setText("" + m.getProduct());
                etCount.setText(String.valueOf(m.getAmount()));
                tvIdx.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }
    }
}