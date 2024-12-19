package kr.ac.uc.order_checker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.uc.order_checker.model.OrderModel;

// order_mgm에서 소스코드 복사
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private TextView tvIdx, tvProduct, tvClient, tvAmount;
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
        notifyDataSetChanged(); // 데이터 갱신
    }

    @NonNull
    @Override
    /* viewHolder가 만들어져야 할 때 생성을 해야 하니까, itemLayout에서 inflate하는 건데,
    시험 때 완성이 안 되어 있을 수 있으니, 코드를 잘 살펴보길 바람 */
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Log.d("OrderAdapter", "Binding item at position " + position + " with idx: " + data.get(position).getIdx());
        // holder에 있는 position 값으로 값을 바인딩해달라는 뜻
        holder.bind(data.get(position)); // 이 position을 가지고 model에 있는 값을 찍어준다.
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // todo: item_layout의 view들을 가져와서 관리
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdx, tvProduct, tvClient, tvAmount;

        public OrderViewHolder(@NonNull View itemView) { // 클릭을 걸고 싶으면 아이템 전체에 클릭 기능을 달면 된다.
            super(itemView);
            tvIdx = itemView.findViewById(R.id.tvIdx);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvClient = itemView.findViewById(R.id.tvClient);
            tvAmount = itemView.findViewById(R.id.tvCount);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition(); // Adapter의 Position값을 읽어올 수 있음.
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(data.get(position).getIdx());
                }
            });
        }

        public void bind(OrderModel orderModel) {
            Log.d("OrderAdapter", "OrderViewHolder binding item with idx: " + orderModel.getIdx());
            this.tvIdx.setText("IDX: " + orderModel.getIdx());
            this.tvProduct.setText(orderModel.getProduct());
            this.tvClient.setText(orderModel.getClient());
            this.tvAmount.setText(String.format("%03d", orderModel.getAmount()).toString());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int idx);
    }
}
