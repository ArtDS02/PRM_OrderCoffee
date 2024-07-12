package com.example.langthangcoffee.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.adapters.FoodOrderSizeEditAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.Voucher;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomSheetChiTietVoucherFragment extends BottomSheetDialogFragment {
    ImageView imgClose, imgVoucher;
    BottomSheetChiTietVoucherFragment bottomSheetChiTietVoucherFragment;
    MainActivity mainActivity;
    Voucher voucher;
    private RecyclerView recyclerView, recyclerView2;
    private FoodOrderSizeEditAdapter foodOrderSizeEditAdapter;
    private TextView maVoucher,moTaVoucher, tvSaoChep, tvHetHan;

    public BottomSheetChiTietVoucherFragment(Voucher voucher) {
        // Required empty public constructor
        bottomSheetChiTietVoucherFragment = this;
        this.voucher = voucher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_bottom_sheet_dialog_chi_tiet_voucher, container, false);

        mainActivity = (MainActivity) getActivity();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHetHan = view.findViewById(R.id.tv_thoigian_hethan);
        imgVoucher = view.findViewById(R.id.img_voucher);
        imgClose = view.findViewById(R.id.img_close_bottom_dialog);
        maVoucher = view.findViewById(R.id.tv_mavoucher);
        moTaVoucher = view.findViewById(R.id.tv_mota_voucher);
        maVoucher.setText(voucher.getMaVoucher());
        moTaVoucher.setText(voucher.getLoaiVoucher().getMoTaVoucher());
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetChiTietVoucherFragment.dismiss();
            }


        });
        tvSaoChep = view.findViewById(R.id.tv_saochep);
        tvSaoChep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Mã Voucher", voucher.getMaVoucher());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mainActivity, "Sao chép thành công", Toast.LENGTH_SHORT).show();

            }
        });

        // Display Image Voucher
        Picasso.get()
                .load(voucher.getLoaiVoucher().getHinhAnh())
                .fit()
                .into(imgVoucher);

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
            tvHetHan.setText(thoiGianHetHan);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
