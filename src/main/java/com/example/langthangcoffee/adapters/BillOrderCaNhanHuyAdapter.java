package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.fragments.ChiTietDatHangDaHuyFragment;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.LichSuOrder;

import java.util.List;

@SuppressWarnings("deprecation")
public class BillOrderCaNhanHuyAdapter extends RecyclerView.Adapter<BillOrderCaNhanHuyAdapter.FoodOrderViewHolder> {
    private List<DonHang> mBillOrder;
    MainActivity mainActivity;

    public BillOrderCaNhanHuyAdapter(List<DonHang> mBillOrder, MainActivity mainActivity) {
        this.mBillOrder = mBillOrder;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_su_order_dang_giao, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DonHang donHang = mBillOrder.get(position);
        if (donHang == null) {
            return;
        }
        holder.tvMaDonHang.setText("Mã đơn hàng: " + String.valueOf(donHang.getMaDonHang()));
        holder.tvTongTien.setText("Tổng tiền: " + String.valueOf(donHang.getSoTienThanhToan()) + " đ");
        holder.tvThoiGian.setText("Thời gian: " + donHang.getThoiGianDatHang());
        List<LichSuOrder> lichSuOrderList = donHang.getLichSuOrderList();
        String chiTietDonHang = "";
        for (int i = 0; i < lichSuOrderList.size(); i++) {
            String orderDetail = "";
            orderDetail = "x" + lichSuOrderList.get(i).getSoLuong() + " " + lichSuOrderList.get(i).getTenSanPham();
            if (i != lichSuOrderList.size() - 1) {
                orderDetail += ", ";
            }
            chiTietDonHang += orderDetail;
        }


        holder.tvDetail.setText("Chi tiết: " + chiTietDonHang);
        holder.lnItemDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChiTietDatHangDaHuyFragment chiTietDatHangDangChoFragment = new ChiTietDatHangDaHuyFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("donHang", donHang); // Put anything what you want
                chiTietDatHangDangChoFragment.setArguments(bundle);
                mainActivity.loadFragment(chiTietDatHangDangChoFragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mBillOrder != null) {
            return mBillOrder.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        ;
        private final TextView tvMaDonHang, tvDetail, tvTongTien, tvThoiGian;


        private  final LinearLayout lnItemDonHang;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang = itemView.findViewById(R.id.tv_ma_don_hang);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            tvTongTien = itemView.findViewById(R.id.tv_tong_tien);
            tvThoiGian = itemView.findViewById(R.id.tv_thoi_gian);
            lnItemDonHang = itemView.findViewById(R.id.ln_item_don_hang);


        }
    }
}

