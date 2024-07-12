package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.ToppingSanPham;

import java.util.List;

@SuppressWarnings("deprecation")
public class ToppingSanPhamAdapter extends RecyclerView.Adapter<ToppingSanPhamAdapter.FoodOrderViewHolder> {
    private List<ToppingSanPham> toppingSanPhamList;
    ToppingSanPhamAdapter toppingSanPhamAdapter;
    int selectedPosition = 0;

    public ToppingSanPhamAdapter(List<ToppingSanPham> toppingSanPhamList) {
        this.toppingSanPhamList = toppingSanPhamList;
        this.toppingSanPhamAdapter = this;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham_topping_admin, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ToppingSanPham toppingSanPham = toppingSanPhamList.get(position);
        if (toppingSanPham == null) {
            return;
        }

        holder.edtTitle.setText(toppingSanPham.getTenTopping());
        holder.edtPrice.setText(String.valueOf(toppingSanPham.getGiaTien()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toppingSanPhamList.remove(position);
                toppingSanPhamAdapter.notifyDataSetChanged();
            }
        });

        holder.edtTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.edtTitle.hasFocus()) {
                    toppingSanPham.setTenTopping(s.toString());

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        holder.edtPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.edtPrice.hasFocus()) {
                    toppingSanPham.setGiaTien(Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

    }


    @Override
    public int getItemCount() {
        if (toppingSanPhamList != null) {
            return toppingSanPhamList.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final Button btnDelete;
        private final EditText edtTitle;
        private final EditText edtPrice;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            btnDelete = itemView.findViewById(R.id.btn_subtract);
            edtTitle = itemView.findViewById(R.id.edt_food_title);
            edtPrice = itemView.findViewById(R.id.edt_food_price);


        }
    }
}

