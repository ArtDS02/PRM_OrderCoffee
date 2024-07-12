package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.fragments.QuanLyTaiKhoanChiTietFragment;
import com.example.langthangcoffee.models.TaiKhoan;

import java.util.List;

@SuppressWarnings("deprecation")
public class QuanLyTaiKhoanAdapter extends RecyclerView.Adapter<QuanLyTaiKhoanAdapter.FoodOrderViewHolder> {
    private List<TaiKhoan> taiKhoanList;
    private int mQuantity = 0;
    private MainActivity mainActivity;

    public QuanLyTaiKhoanAdapter(List<TaiKhoan> taiKhoanList) {
        this.taiKhoanList = taiKhoanList;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tai_khoan, parent, false);
        mainActivity = (MainActivity) parent.getContext();
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TaiKhoan taiKhoan = taiKhoanList.get(position);
        if (taiKhoan == null) {
            return;
        }

        holder.tvHoTen.setText("Họ tên: " + taiKhoan.getHo() + " " + taiKhoan.getTen());
        holder.tvThoiGianThamGia.setText("Tham gia: " + taiKhoan.getThoiGianThamGia());
        holder.tvSoDienThoai.setText("Số điện thoại: " + taiKhoan.getSdtTaiKhoan());
        holder.tvTenQuyenHan.setText("Quyền hạn: " + taiKhoan.getTenQuyenHan());
        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuanLyTaiKhoanChiTietFragment quanLyTaiKhoanChiTietFragment = new QuanLyTaiKhoanChiTietFragment(taiKhoan);
                mainActivity.loadFragment(quanLyTaiKhoanChiTietFragment);

            }
        });

    }



    @Override
    public int getItemCount() {
        if (taiKhoanList != null) {
            return taiKhoanList.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvHoTen, tvSoDienThoai, tvThoiGianThamGia, tvTenQuyenHan;
        private final LinearLayout lnItem;



        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tv_hoten);
            tvSoDienThoai = itemView.findViewById(R.id.tv_sdt);
            tvThoiGianThamGia = itemView.findViewById(R.id.tv_thoigian_thamgia);
            tvTenQuyenHan = itemView.findViewById(R.id.tv_quyenhan);
            lnItem = itemView.findViewById(R.id.ln_item_san_pham);


        }
    }
}

