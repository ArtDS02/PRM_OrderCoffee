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
import com.example.langthangcoffee.models.KichThuocSanPham;

import java.util.List;

@SuppressWarnings("deprecation")
public class KichThuocSanPhamAdapter extends RecyclerView.Adapter<KichThuocSanPhamAdapter.FoodOrderViewHolder> {
    private List<KichThuocSanPham> kichThuocSanPhamList;
    KichThuocSanPhamAdapter kichThuocSanPhamAdapter;
    int selectedPosition = 0;
    public KichThuocSanPhamAdapter(List<KichThuocSanPham> kichThuocSanPhamList) {
        this.kichThuocSanPhamList = kichThuocSanPhamList;
        this.kichThuocSanPhamAdapter = this;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham_topping_admin, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final KichThuocSanPham kichThuocSanPham = kichThuocSanPhamList.get(position);
        if (kichThuocSanPham == null) {
            return;
        }

        holder.edtTitle.setText(kichThuocSanPham.getTenKichThuoc());
        holder.edtPrice.setText(String.valueOf(kichThuocSanPham.getGiaTien()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kichThuocSanPhamList.remove(position);
                kichThuocSanPhamAdapter.notifyDataSetChanged();
            }
        });

        holder.edtTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.edtTitle.hasFocus()) {
                    kichThuocSanPham.setTenKichThuoc(s.toString());

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
                    kichThuocSanPham.setGiaTien(Integer.valueOf(s.toString()));
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
        if (kichThuocSanPhamList != null) {
            return kichThuocSanPhamList.size();
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

