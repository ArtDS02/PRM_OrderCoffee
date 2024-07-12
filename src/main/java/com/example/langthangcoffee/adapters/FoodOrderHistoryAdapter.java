package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.clicklisteners.ItemClickListener;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.fragments.BottomSheetChinhSuaOrderFragment;
import com.example.langthangcoffee.fragments.XacNhanDatHangFragment;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;

import java.util.List;

@SuppressWarnings("deprecation")
public class FoodOrderHistoryAdapter extends RecyclerView.Adapter<FoodOrderHistoryAdapter.FoodOrderViewHolder> {
    private List<LichSuOrder> mLichSuOrderList;
    int selectedPosition = 0;
    ItemClickListener itemClickListener;
    XacNhanDatHangFragment xacNhanDatHangFragment;

    public FoodOrderHistoryAdapter(List<LichSuOrder> mLichSuOrderList,XacNhanDatHangFragment xacNhanDatHangFragment ) {
        this.mLichSuOrderList = mLichSuOrderList;
        this.xacNhanDatHangFragment = xacNhanDatHangFragment;

    }
    public void updateLichSuOrderList(List<LichSuOrder> lichSuOrderList) {
        mLichSuOrderList.clear();
        mLichSuOrderList.addAll(lichSuOrderList);
        this.notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_order_xacnhan, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final LichSuOrder lichSuOrder = mLichSuOrderList.get(position);
        if (lichSuOrder == null) {
            return;
        }

        holder.tvItemName.setText("x" + String.valueOf(lichSuOrder.getSoLuong()) + " " + lichSuOrder.getTenSanPham());
        holder.tvItemSize.setText(lichSuOrder.getKichThuoc());
        holder.tvItemNote.setText(lichSuOrder.getGhiChu());
        holder.tvItemThanhTien.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
        List<FoodOrderTopping> foodOrderToppingList = lichSuOrder.getFoodOrderToppingList();
        String getTopping = "";
        int p = 0;
        for (FoodOrderTopping foodOrderTopping :
                foodOrderToppingList
        ) {

            getTopping += foodOrderTopping.getTenTopping();
            if (p != foodOrderToppingList.size() - 1) {
                getTopping += ", ";
            }
            p++;

        }
        holder.tvItemTopping.setText(getTopping);

        if (getTopping.isEmpty()) {
            holder.tvItemTopping.setHeight(0);
        }
        if (lichSuOrder.getGhiChu().isEmpty()) {
            holder.tvItemNote.setHeight(0);
        }
        holder.lnChiTietOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetChinhSuaOrderFragment bottomSheetChinhSuaOrderFragment = new BottomSheetChinhSuaOrderFragment(xacNhanDatHangFragment);
                bottomSheetChinhSuaOrderFragment.setLichSuOrder(lichSuOrder);
                bottomSheetChinhSuaOrderFragment.show(xacNhanDatHangFragment.getParentFragmentManager(), bottomSheetChinhSuaOrderFragment.getTag());
            }
        });


    }


    @Override
    public int getItemCount() {
        if (mLichSuOrderList != null) {
            return mLichSuOrderList.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName;
        private final TextView tvItemSize;
        private final TextView tvItemThanhTien;
        private final TextView tvItemTopping;
        private final TextView tvItemNote;
        private final LinearLayout lnChiTietOrder;

        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemSize = itemView.findViewById(R.id.tv_item_size);
            tvItemThanhTien = itemView.findViewById(R.id.tv_item_thanhtien);
            tvItemTopping = itemView.findViewById(R.id.tv_item_topping);
            tvItemNote = itemView.findViewById(R.id.tv_item_note);
            lnChiTietOrder = itemView.findViewById(R.id.ln_chitiet_order);


        }
    }
}

