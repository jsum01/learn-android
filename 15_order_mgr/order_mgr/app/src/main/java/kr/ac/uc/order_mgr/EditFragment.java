package kr.ac.uc.order_mgr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.uc.order_mgr.model.OrderModel;


public class EditFragment extends Fragment {

    int idx; // 0인지 아닌지에 따라 열리는 방식을 다르게 하자
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
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        tvIdx = view.findViewById(R.id.tvIdx);
        etProduct = view.findViewById(R.id.etProduct);
        etClient = view.findViewById(R.id.etClient);
        etCount = view.findViewById(R.id.etCount);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);

        Log.d("EditFragment", "Before initialize() call");
        initialize();
        Log.d("EditFragment", "After initialize() call");

        btnSave.setOnClickListener(v -> save());

        btnDelete.setOnClickListener(v -> delete());

        return view;
    }

    // OrderModel.java에서 getter 메서드들이 제대로 구현되어 있는지
// getIdx()와 getAmount()가 실제로 int를 반환하는지
// m 객체가 제대로 초기화되었는지

    // EditFragment에서 시도해볼 수 있는 디버깅 코드:
    private void initialize() {
        if (idx == 0) {
            tvIdx.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        } else {
            OrderModel m = dbHelper.getOrder(idx);
            if (m != null) {
                tvIdx.setText("IDX: " + m.getIdx());
                etProduct.setText(m.getProduct());
                etClient.setText(m.getClient());
                etCount.setText(String.valueOf(m.getAmount()));

                tvIdx.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    private void save() {
        String product = etProduct.getText().toString();
        String client = etClient.getText().toString();
        String amount = etCount.getText().toString();

        // 입력값 유효성 검사
        if (product.isEmpty() || client.isEmpty() || amount.isEmpty()) {
            // 에러 처리 (필요에 따라 사용자에게 알림)
            Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderModel m = new OrderModel(idx, product, client, Integer.parseInt(amount));
        if (idx == 0) {
            dbHelper.insert(m);
        } else {
            dbHelper.modify(m);
        }
        moveBack();
    }

    private void delete() {
        // delete 할 때는 index값만 알면 된다.
        if (idx != 0) {
            dbHelper.remove(idx);
        }
        moveBack();
    }

    private void moveBack() {
        getActivity().getSupportFragmentManager().popBackStack(); // 저장 후 뒤로가기(ListFragment)
    }
}