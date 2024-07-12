package com.example.langthangcoffee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.langthangcoffee.adapters.DashBoardAdapter;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.viewmodels.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

// Định nghĩa DashBoardFragment, một Fragment chuyên dụng cho bảng điều khiển (dashboard)
public class DashBoardFragment extends Fragment {

    // Ghi đè phương thức onCreateView để tạo và trả về giao diện cho Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout từ file XML dashboard_fragment và gán cho biến v
        // false ở đây để không đính kèm view vào container ngay lập tức, Fragment sẽ tự quản lý việc này
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.dashboard_fragment, container, false);

        // Lấy FragmentManager từ Activity chứa Fragment này
        // FragmentManager được sử dụng để quản lý các Fragment con
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Tạo danh sách các Fragment sẽ được hiển thị trong ViewPager
        List<Fragment> list = new ArrayList<>();
        // Thêm FirstPageDashBoardFragment vào danh sách
        // Đây có thể là trang đầu tiên của dashboard
        list.add(new FirstPageDashBoardFragment());

        // Tạo adapter cho ViewPager, truyền vào FragmentManager và danh sách Fragment
        // DashBoardAdapter sẽ quản lý việc hiển thị các Fragment trong ViewPager
        DashBoardAdapter vPagerAdapter = new DashBoardAdapter(fragmentManager, list);

        // Tìm VerticalViewPager trong layout bằng ID
        // VerticalViewPager là một custom ViewPager cho phép cuộn dọc
        VerticalViewPager viewPager = v.findViewById(R.id.vp);

        // Gán adapter cho ViewPager
        // Điều này sẽ cho phép ViewPager hiển thị các Fragment trong danh sách
        viewPager.setAdapter(vPagerAdapter);

        return v;
    }
}