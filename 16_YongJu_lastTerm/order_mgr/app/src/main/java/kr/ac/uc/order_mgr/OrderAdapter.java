package kr.ac.uc.order_mgr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.uc.order_mgr.model.OrderModel;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private Context context;
    private List<OrderModel> data;
    private OnItemClickListener onItemClickListener;

    public OrderAdapter(Context context, List<OrderModel> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<OrderModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 이 부분도 비워져있을 수도 있음
        // 이 부분이 새롭게 뷰 홀더가 생성해야할 때 아이템 레이아웃에서 인플레이트하는 것
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    } // 이 코드는 비어있을 수도 있음 무조건 data의 전체 사이즈를 불러오는 코드가 있어야 함

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        // 인스턴스를 가지고와서 뷰 홀더란 객체에 저장하고 있는다
        // 재활용이 필요하다면 뷰 홀더에 있는걸 그대로 쓰고
        // 재활용이 필요없다면
        TextView tvIdx, tvProduct, tvClient, tvAmount;

        public OrderViewHolder(@NonNull View itemView) { // 이 코드가 동작함
            super(itemView);
            tvIdx = itemView.findViewById(R.id.tvIdx);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvClient = itemView.findViewById(R.id.tvClient);
            tvAmount = itemView.findViewById(R.id.tvCount);
            // 이 아이템들 전체에다가 클릭할 수 있는 기능을 달아주기 위해 아래의 코드를 추가함
            itemView.setOnClickListener(v -> {
                // 어떤 아이템이 선택되었는지 알기 위해 getAdapterPosition()를 사용
                int pos = getAdapterPosition(); // 포지션 값을 읽어옴
                if (pos != RecyclerView.NO_POSITION){
                    // 인덱스 값은 데이터 중에서 pos에 해당하면 모델 하다가 리턴된다. 그리고 여기서 getIdx()를 하게되면 인덱스 값을 가져온다
                    onItemClickListener.onItemClick(data.get(pos).getIdx());
                }
            });
        }

        public void bind(OrderModel m){
            this.tvIdx.setText("IDX : " + m.getIdx());
            this.tvProduct.setText(m.getProduct());
            this.tvClient.setText(m.getClient());
            this.tvAmount.setText(String.format("%3d", m.getAmount()).toString()); // 정수 앞에 총 3자리가 나타날 수 있게할 수 있는 코드
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int idx);
    }
}
