package com.example.langthangcoffee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.adapters.BillOrderAdminDangGiaoAdapter;
import com.example.langthangcoffee.adapters.FoodOrderHistoryLichSuDonHangAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.LichSuOrder;

import java.util.ArrayList;
import java.util.List;

public class ChiTietDatHangAdminDangGiaoFragment extends Fragment {
    MainActivity mainActivity;
    BillOrderAdminDangGiaoAdapter billOrderAdminDangGiaoAdapter;
    LinearLayout lnGiamGia;
    ImageView imgBack;
    Button btnThayDoiDiaChi, btnConfirmOrder, btnHoanTat, btnHuyDonHang;
    private RecyclerView recyclerView;
    private FoodOrderHistoryLichSuDonHangAdapter foodOrderHistoryLichSuDonHangAdapter;
    GridLayoutManager gridLayoutManager;
    DonHang donHang = null;
    private List<LichSuOrder> lichSuOrderList = new ArrayList<>();
    ChiTietDatHangAdminDangGiaoFragment chiTietDatHangDangChoFragment;
    TextView tvInfoShip, tvItemThanhTien, tvItemPhiGiaoHang, tvItemThanhToan, tvVoucher, tvMaDonHang, tvTaiKhoan;

    public ChiTietDatHangAdminDangGiaoFragment(BillOrderAdminDangGiaoAdapter billOrderAdminDangGiaoAdapter) {
        this.billOrderAdminDangGiaoAdapter = billOrderAdminDangGiaoAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chiTietDatHangDangChoFragment = this;
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_thong_tin_bill_dang_giao_admin, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
                donHang = (DonHang) bundle.getSerializable("donHang");
        }
        tvTaiKhoan = v.findViewById(R.id.tv_taikhoan_don_hang);
        tvMaDonHang = v.findViewById(R.id.tv_ma_don_hang);
        imgBack = v.findViewById(R.id.img_back);
        btnHuyDonHang = v.findViewById(R.id.btn_huy_don_hang);
        tvInfoShip = v.findViewById(R.id.tv_info_ship_order);
        tvItemThanhTien = v.findViewById(R.id.tv_item_thanhtien);
        tvItemPhiGiaoHang = v.findViewById(R.id.tv_item_phigiaohang);
        tvItemThanhToan = v.findViewById(R.id.tv_item_thanhtoan);
        tvVoucher = v.findViewById(R.id.tv_voucher_donhang);
        btnHoanTat = v.findViewById(R.id.btn_hoan_tat);
        mainActivity = (MainActivity) getActivity();

        recyclerView = v.findViewById(R.id.rcv_order_history);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        foodOrderHistoryLichSuDonHangAdapter = new FoodOrderHistoryLichSuDonHangAdapter(lichSuOrderList);
        recyclerView.setAdapter(foodOrderHistoryLichSuDonHangAdapter);
        updateDonHangView();
        btnHuyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billOrderAdminDangGiaoAdapter.huyDonHangDangGiao(donHang.getMaDonHang());
                imgBack.performClick();
            }
        });
        btnHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billOrderAdminDangGiaoAdapter.hoanTatDonHangDangGiao(donHang.getMaDonHang());
                imgBack.performClick();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return v;
    }


    public void updateDonHangView() {
        if (donHang != null) {
            tvTaiKhoan.setText(donHang.getSdtKhachHang());
            tvMaDonHang.setText(String.valueOf(donHang.getMaDonHang()));
            tvInfoShip.setText(donHang.getDiaChiGiaoHang());
            tvItemThanhToan.setText(String.valueOf(donHang.getSoTienThanhToan()) + " đ");
            tvItemPhiGiaoHang.setText(String.valueOf(donHang.getPhiGiaoHang()) + " đ");
            tvItemThanhTien.setText(String.valueOf(donHang.getThanhTien()) + " đ");
            tvVoucher.setText(donHang.getMaVoucher().isEmpty() || donHang.getMaVoucher() == null || donHang.getMaVoucher().equals("null") ? "Không có" : donHang.getMaVoucher());
            lichSuOrderList.clear();
            lichSuOrderList.addAll(donHang.getLichSuOrderList());
            foodOrderHistoryLichSuDonHangAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
