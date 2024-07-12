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
import com.example.langthangcoffee.fragments.BottomSheetChiTietVoucherFragment;
import com.example.langthangcoffee.models.Voucher;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.FoodOrderViewHolder> {
    private List<Voucher> mListVoucher;
    private MainActivity mainActivity;

    public VoucherAdapter(List<Voucher> mListVoucher) {
        this.mListVoucher = mListVoucher;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        mainActivity = (MainActivity) parent.getContext();
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Voucher voucher = mListVoucher.get(position);
        if (voucher == null) {
            return;
        }
        holder.tvMaVoucher.setText(voucher.getMaVoucher());
        holder.tvTenVoucher.setText(voucher.getLoaiVoucher().getTenLoaiVoucher());
        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetChiTietVoucherFragment bottomSheetChiTietVoucherFragment = new BottomSheetChiTietVoucherFragment(voucher);
                bottomSheetChiTietVoucherFragment.show(mainActivity.getSupportFragmentManager(), bottomSheetChiTietVoucherFragment.getTag());
            }
        });
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateTimeNow = new Date();
            Date dateTimeHetHan = sf.parse(voucher.getThoiGianHetHan());
            long difference = dateTimeHetHan.getTime() - dateTimeNow.getTime();
            int seconds = (int) ((difference % (1000 * 60)) / 1000);
            int minutes = (int) ((difference % (1000 * 60 * 60)) / (1000 * 60));
            int hours   = (int) ((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int days   = (int) (difference / (1000 * 60 * 60 * 24));
            String thoiGianHetHan = "Hết hạn trong " + days + " ngày " +  hours + " giờ " + minutes + " phút nữa";
            holder.tvHetHan.setText(thoiGianHetHan);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get()
                .load(voucher.getLoaiVoucher().getHinhAnh())
                .fit()
                .into(holder.imgVoucher);


    }


    @Override
    public int getItemCount() {
        if (mListVoucher != null) {
            return mListVoucher.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgVoucher;
        private final TextView tvMaVoucher, tvTenVoucher, tvHetHan;
        private final LinearLayout lnItem;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            lnItem = itemView.findViewById(R.id.ln_item_voucher);
            imgVoucher = itemView.findViewById(R.id.img_voucher);
            tvMaVoucher = itemView.findViewById(R.id.tv_mavoucher);
            tvTenVoucher = itemView.findViewById(R.id.tv_tenvoucher);
            tvHetHan = itemView.findViewById(R.id.tv_thoigian_hethan);


        }
    }
}

