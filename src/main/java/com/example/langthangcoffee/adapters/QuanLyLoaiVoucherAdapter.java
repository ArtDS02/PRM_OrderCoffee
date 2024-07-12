package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.models.LoaiVoucher;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class QuanLyLoaiVoucherAdapter extends RecyclerView.Adapter<QuanLyLoaiVoucherAdapter.FoodOrderViewHolder> {
    private List<LoaiVoucher> loaiVoucherList;
    private int mQuantity = 0;
    private MainActivity mainActivity;

    public QuanLyLoaiVoucherAdapter(List<LoaiVoucher> loaiVoucherList) {
        this.loaiVoucherList = loaiVoucherList;
    }
    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_voucher, parent, false);
        mainActivity = (MainActivity) parent.getContext();
        return new FoodOrderViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final LoaiVoucher loaiVoucher = loaiVoucherList.get(position);
        if (loaiVoucher == null) {
            return;
        }
        holder.tvTenLoaiVoucher.setText(loaiVoucher.getTenLoaiVoucher());
        holder.tvTongTien.setText("Giảm thành tiền: " + String.valueOf(loaiVoucher.getTongTien()) + "%");
        holder.tvPhiShip.setText("Giảm phí giao hàng: " + String.valueOf(loaiVoucher.getPhiShip()) + "%");
        holder.tvSoLuongToiThieu.setText("Số lượng món tối thiểu: " + String.valueOf(loaiVoucher.getSoLuongToiThieu()));
        Picasso.get()
                .load(loaiVoucher.getHinhAnh())
                .fit()
                .centerInside()
                .into(holder.imgLoaiVoucher);

        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    @Override
    public int getItemCount() {
        if (loaiVoucherList != null) {
            return loaiVoucherList.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTenLoaiVoucher, tvTongTien, tvPhiShip, tvSoLuongToiThieu;
        private final LinearLayout lnItem;
        private final ImageView imgLoaiVoucher;



        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLoaiVoucher = itemView.findViewById(R.id.tv_tenloaivoucher);
            tvTongTien = itemView.findViewById(R.id.tv_tongtien);
            tvPhiShip = itemView.findViewById(R.id.tv_phiship);
            tvSoLuongToiThieu = itemView.findViewById(R.id.tv_soluongtoithieu);
            imgLoaiVoucher = itemView.findViewById(R.id.img_voucher);
            lnItem = itemView.findViewById(R.id.ln_item);


        }
    }
}

