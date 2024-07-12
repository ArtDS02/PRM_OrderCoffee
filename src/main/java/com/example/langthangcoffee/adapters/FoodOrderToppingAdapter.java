package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class FoodOrderToppingAdapter extends RecyclerView.Adapter<FoodOrderToppingAdapter.FoodOrderViewHolder> {
    private List<FoodOrderTopping> mListFoodOrderTopping;
    LichSuOrder lichSuOrder = null;
    List<FoodOrderTopping> lichSuOrderTopping = new ArrayList<FoodOrderTopping>();
    static SparseBooleanArray itemStateArray = null;
    EventListener listener;

    public interface EventListener {
        void onUpdateLichSuOrder();
    }

    public FoodOrderToppingAdapter(List<FoodOrderTopping> mListFoodOrderTopping, LichSuOrder lichSuOrder, EventListener listener) {
        this.mListFoodOrderTopping = mListFoodOrderTopping;
        this.lichSuOrder = lichSuOrder;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_order_topping, parent, false);
        itemStateArray = new SparseBooleanArray();
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final FoodOrderTopping foodOrderTopping = mListFoodOrderTopping.get(position);
        if (foodOrderTopping == null) {
            return;
        }


        holder.tvToppingName.setText(foodOrderTopping.getTenTopping());
        holder.tvToppingPrice.setText(String.valueOf(foodOrderTopping.getGiaTien()) + " đ");

        holder.bind(position);
        holder.chkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemStateArray.get(position, false)) {
                    holder.chkButton.setChecked(true);
                    itemStateArray.put(position, true);
                    lichSuOrderTopping.add(foodOrderTopping);
                    lichSuOrder.setFoodOrderToppingList(lichSuOrderTopping);
                    // Cap nhat gia tien lich su order
                    listener.onUpdateLichSuOrder();
                    Log.i("topping", String.valueOf(lichSuOrderTopping.size()));
                } else {
                    holder.chkButton.setChecked(false);
                    itemStateArray.put(position, false);
                    lichSuOrderTopping.remove(foodOrderTopping);
                    lichSuOrder.setFoodOrderToppingList(lichSuOrderTopping);
                    // Cap nhat gia tien lich su order
                    listener.onUpdateLichSuOrder();
                    Log.i("topping", String.valueOf(lichSuOrderTopping.size()));
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        if (mListFoodOrderTopping != null) {
            return mListFoodOrderTopping.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox chkButton;
        private final TextView tvToppingName;
        private final TextView tvToppingPrice;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            chkButton = itemView.findViewById(R.id.chk_select_topping);
            tvToppingName = itemView.findViewById(R.id.tv_food_topping_name);
            tvToppingPrice = itemView.findViewById(R.id.tv_food_topping_price);


        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                chkButton.setChecked(false);
            } else {
                chkButton.setChecked(true);
            }
        }


    }
}

