package com.example.langthangcoffee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;

public class TrangQuanLyFragment extends Fragment {

    ImageView imgBack;
    LinearLayout lnDanhSachDonHang, lnDanhSachKhachHang, lnDanhSachSanPham;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_quan_ly, container, false);
        imgBack = v.findViewById(R.id.img_back);
        lnDanhSachDonHang = v.findViewById(R.id.ln_danhsachdonhang);
        lnDanhSachKhachHang = v.findViewById(R.id.ln_danhsachtaikhoan);
        lnDanhSachSanPham = v.findViewById(R.id.ln_danhsachsanpham);
        mainActivity = (MainActivity) getActivity();

        // Sự kiện khi bấm nút back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        // Chuyển trang khi bấm vào các mục // Xong
        lnDanhSachDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DanhSachDonHangAdminFragment danhSachDonHangAdminFragment = new DanhSachDonHangAdminFragment();
                mainActivity.loadFragment(danhSachDonHangAdminFragment);
            }
        });

        // Xong
        lnDanhSachKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuanLyTaiKhoanFragment quanLyTaiKhoanFragment = new QuanLyTaiKhoanFragment();
                mainActivity.loadFragment(quanLyTaiKhoanFragment);
            }
        });

        // Xong
        lnDanhSachSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuanLySanPhamFragment quanLySanPhamFragment = new QuanLySanPhamFragment();
                mainActivity.loadFragment(quanLySanPhamFragment);
            }
        });
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
