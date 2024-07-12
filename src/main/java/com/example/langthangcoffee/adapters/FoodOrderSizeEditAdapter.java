package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.clicklisteners.ItemClickListener;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.FoodOrderSize;
import com.example.langthangcoffee.models.LichSuOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings("deprecation")
public class FoodOrderSizeEditAdapter extends RecyclerView.Adapter<FoodOrderSizeEditAdapter.FoodOrderViewHolder> {
    private List<FoodOrderSize> mListFoodOrderSize;
    int selectedPosition = 0;
    LichSuOrder lichSuOrder;
    ItemClickListener itemClickListener;

    public FoodOrderSizeEditAdapter(List<FoodOrderSize> mListFoodOrderSize, ItemClickListener itemClickListener, LichSuOrder lichSuOrder) {
        this.mListFoodOrderSize = mListFoodOrderSize;
        this.itemClickListener = itemClickListener;
        this.lichSuOrder = lichSuOrder;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_order_size, parent, false);

        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final FoodOrderSize foodOrderSize = mListFoodOrderSize.get(position);
        if (foodOrderSize == null) {
            return;
        }

        for (int i = 0; i < mListFoodOrderSize.size(); i++) {
            if (mListFoodOrderSize.get(i).getTenKichThuoc().equals(lichSuOrder.getKichThuoc())) {
                selectedPosition = i;
                break;
            }
        }
        holder.rdoButtton.setChecked(position
                == selectedPosition);
        holder.tvSizeName.setText(foodOrderSize.getTenKichThuoc());
        holder.tvSizePrice.setText(String.valueOf(foodOrderSize.getGiaTien()) + " đ");
        holder.rdoButtton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton,
                            boolean b) {
                        // check condition
                        if (b) {
                            // When checked
                            // update selected position
                            selectedPosition
                                    = holder.getAdapterPosition();

                            JSONObject jsonBody = new JSONObject();
                            try {
                                jsonBody.put("tenKichThuoc", foodOrderSize.getTenKichThuoc());
                                jsonBody.put("giaTien", foodOrderSize.getGiaTien());
                                // Call listener
                                itemClickListener.onClick(
                                        jsonBody);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });

    }


    @Override
    public int getItemCount() {
        if (mListFoodOrderSize != null) {
            return mListFoodOrderSize.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton rdoButtton;
        private final TextView tvSizeName;
        private final TextView tvSizePrice;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            rdoButtton = itemView.findViewById(R.id.rb_select_size);

            tvSizeName = itemView.findViewById(R.id.tv_food_size_name);
            tvSizePrice = itemView.findViewById(R.id.tv_food_size_price);


        }
    }

}

