package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.drawable.custom_btn_deep_orange_radius_10dp;
import static com.example.langthangcoffee.R.drawable.custom_btn_pale_cream_radius_10dp;
import static com.example.langthangcoffee.R.layout;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.langthangcoffee.adapters.LichSuBillAdminAdapter;
import com.example.langthangcoffee.R;

import java.util.List;

@SuppressWarnings("deprecation")
public class DanhSachDonHangAdminFragment extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private Button btnWaitConfirm, btnDelivering, btnFinished, btnCacelled;
    private ColorStateList defText;
    private ImageView imgBack;
    LichSuBillAdminAdapter lichSuBillAdminAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_danh_sach_don_hang_admin, container, false);


        FragmentManager fragmentManager = getChildFragmentManager();

        // Lưu màu mặc định của text
        defText = ColorStateList.valueOf(getResources().getColor(R.color.black));

        // Khởi tạo ViewPager để chứa các fragment con
        viewPager = v.findViewById(R.id.vp_order_history);

        // Khởi tạo Adapter cho ViewPager
        lichSuBillAdminAdapter = new LichSuBillAdminAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // Set Adapter cho ViewPager
        viewPager.setAdapter(lichSuBillAdminAdapter);

        // Ánh xạ các button từ layout
        btnWaitConfirm = v.findViewById(R.id.btn_cho_xac_nhan);
        btnDelivering = v.findViewById(R.id.btn_dang_giao);
        btnFinished = v.findViewById(R.id.btn_hoan_tat);
        btnCacelled = v.findViewById(R.id.btn_da_huy);
        imgBack = v.findViewById(R.id.img_back);

        // Set sự kiện OnClickListener cho ImageView để quay lại fragment trước đó
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Set sự kiện OnClickListener cho từng button để thay đổi trạng thái ViewPager
        btnWaitConfirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // Thay đổi màu text và background của button khi được chọn
                btnWaitConfirm.setTextColor(R.color.white);
                btnDelivering.setTextColor(defText);
                btnFinished.setTextColor(defText);
                btnCacelled.setTextColor(defText);

                btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

                // Chuyển ViewPager đến trang tương ứng
                viewPager.setCurrentItem(0);
            }
        });

        btnDelivering.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnWaitConfirm.setTextColor(defText);
                btnDelivering.setTextColor(R.color.white);
                btnFinished.setTextColor(defText);
                btnCacelled.setTextColor(defText);

                btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

                viewPager.setCurrentItem(1);
            }
        });

        btnFinished.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnWaitConfirm.setTextColor(defText);
                btnDelivering.setTextColor(defText);
                btnFinished.setTextColor(R.color.white);
                btnCacelled.setTextColor(defText);

                btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                viewPager.setCurrentItem(2);
            }
        });

        btnCacelled.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btnWaitConfirm.setTextColor(defText);
                btnDelivering.setTextColor(defText);
                btnFinished.setTextColor(defText);
                btnCacelled.setTextColor(R.color.white);

                btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                viewPager.setCurrentItem(3);
            }
        });

        // cập nhật màu của các button khi click vào
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0: {

                        btnWaitConfirm.setTextColor(getResources().getColor(android.R.color.white));
                        btnDelivering.setTextColor(defText);
                        btnFinished.setTextColor(defText);
                        btnCacelled.setTextColor(defText);

                        btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                        btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

                    }
                    break;
                    case 1: {


                        btnWaitConfirm.setTextColor(defText);
                        btnDelivering.setTextColor(getResources().getColor(android.R.color.white));
                        btnFinished.setTextColor(defText);
                        btnCacelled.setTextColor(defText);

                        btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                        btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                    }
                    break;
                    case 2: {
                        btnWaitConfirm.setTextColor(defText);
                        btnDelivering.setTextColor(defText);
                        btnFinished.setTextColor(getResources().getColor(android.R.color.white));
                        btnCacelled.setTextColor(defText);

                        btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                        btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                    }
                    break;
                    case 3: {
                        btnWaitConfirm.setTextColor(defText);
                        btnDelivering.setTextColor(defText);
                        btnFinished.setTextColor(defText);
                        btnCacelled.setTextColor(getResources().getColor(android.R.color.white));

                        btnWaitConfirm.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnDelivering.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnFinished.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
                        btnCacelled.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
                    }
                    break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }


}
