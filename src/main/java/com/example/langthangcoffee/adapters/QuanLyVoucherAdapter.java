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
import com.example.langthangcoffee.models.Voucher;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class QuanLyVoucherAdapter extends RecyclerView.Adapter<QuanLyVoucherAdapter.FoodOrderViewHolder> {
    private List<Voucher> voucherList;
    private int mQuantity = 0;
    private MainActivity mainActivity;

    public QuanLyVoucherAdapter(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_admin, parent, false);
        mainActivity = (MainActivity) parent.getContext();
        return new FoodOrderViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Voucher voucher = voucherList.get(position);
        if (voucher == null) {
            return;
        }
        holder.tvTenVoucher.setText(voucher.getLoaiVoucher().getTenLoaiVoucher());
        holder.tvMaVoucher.setText(voucher.getMaVoucher());
        holder.tvSoDienThoai.setText("Số điện thoại: " + voucher.getSdtTaiKhoan());
        if (voucher.getSuDung() == 1) {
            holder.tvTinhTrangSuDung.setText("Đã sử dụng");
        } else {
            holder.tvTinhTrangSuDung.setText("Chưa sử dụng");
            holder.tvTinhTrangSuDung.setTextColor(R.color.red);
        }
        Picasso.get()
                .load(voucher.getLoaiVoucher().getHinhAnh())
                .fit()
                .centerInside()
                .into(holder.imgVoucher);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateTimeNow = new Date();
            Date dateTimeHetHan = sf.parse(voucher.getThoiGianHetHan());
            long difference = dateTimeHetHan.getTime() - dateTimeNow.getTime();
            int seconds = (int) ((difference % (1000 * 60)) / 1000);
            int minutes = (int) ((difference % (1000 * 60 * 60)) / (1000 * 60));
            int hours   = (int) ((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int days   = (int) (difference / (1000 * 60 * 60 * 24));
            String thoiGianHetHan;
            if (difference <=0) {
                thoiGianHetHan = "Đã hết hạn";
            } else {
                thoiGianHetHan = "Hết hạn trong " + days + " ngày " +  hours + " giờ " + minutes + " phút nữa";
            }
            holder.tvThoiGianHetHan.setText(thoiGianHetHan);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }



    @Override
    public int getItemCount() {
        if (voucherList != null) {
            return voucherList.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTenVoucher, tvMaVoucher, tvSoDienThoai, tvThoiGianHetHan, tvTinhTrangSuDung;
        private final LinearLayout lnItem;
        private final ImageView imgVoucher;



        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenVoucher = itemView.findViewById(R.id.tv_tenvoucher);
            tvMaVoucher = itemView.findViewById(R.id.tv_mavoucher);
            tvThoiGianHetHan = itemView.findViewById(R.id.tv_thoigian_hethan);
            tvTinhTrangSuDung = itemView.findViewById(R.id.tv_tinhtrangsudung);
            tvSoDienThoai = itemView.findViewById(R.id.tv_sdt);
            imgVoucher = itemView.findViewById(R.id.img_voucher);
            lnItem = itemView.findViewById(R.id.ln_item_voucher);


        }
    }
}

