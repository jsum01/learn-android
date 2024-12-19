package kr.ac.uc.final_client_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.uc.final_client_a.model.WordModel;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private Context context;
    private List<WordModel> data;
    private OnItemClickListener onItemClickListener;

    public WordAdapter(Context context, List<WordModel> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<WordModel> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvEngWord;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEngWord = itemView.findViewById(R.id.tvEngWord);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    int idx = data.get(pos).getIdx();
                    onItemClickListener.onItemClick(pos, idx);
                }
            });
        }

        public void bind(WordModel m){
            tvEngWord.setText(m.getEngWord());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos, int idx);
    }
}
