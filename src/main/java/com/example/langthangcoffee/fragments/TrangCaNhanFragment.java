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

// Định nghĩa Fragment cho trang cá nhân của người dùng
public class TrangCaNhanFragment extends Fragment {

    // Khai báo các thành phần UI sẽ được sử dụng trong Fragment
    ImageView imgBack;
    LinearLayout lnLichSuDonHang, lnThongTinCaNhan, lnThayDoiMatKhau, lnDangXuat, lnLienHe, lnUuDai, lnDanhSachYeuThich;
    MainActivity mainActivity;

    // Phương thức tạo và trả về giao diện cho Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout từ file XML và gán cho biến v
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_ca_nhan, container, false);

        // Ánh xạ các thành phần UI từ layout đã inflate
        imgBack =  v.findViewById(R.id.img_back);
        lnLichSuDonHang = v.findViewById(R.id.ln_lich_su_don_hang);
        lnThongTinCaNhan = v.findViewById(R.id.ln_thongtincanhan);
        lnThayDoiMatKhau = v.findViewById(R.id.ln_thaydoimatkhau);
        lnDangXuat = v.findViewById(R.id.ln_dangxuat);

        // Lấy tham chiếu đến MainActivity để sử dụng các phương thức của nó
        mainActivity = (MainActivity)getActivity();

        // Xử lý sự kiện click nút quay lại
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại Fragment trước đó trong stack
                getFragmentManager().popBackStack();
            }
        });

        // Xử lý sự kiện click vào mục "Lịch sử đơn hàng"
        lnLichSuDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và chuyển đến Fragment lịch sử đơn hàng // Xong
                LichSuBillCaNhanFragment lichSuBillCaNhanFragment = new LichSuBillCaNhanFragment();
                mainActivity.loadFragment(lichSuBillCaNhanFragment);
            }
        });


        // Xử lý sự kiện click vào mục "Thông tin cá nhân"
        lnThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và chuyển đến Fragment thông tin cá nhân // Xong
                ThongTinCaNhanFragment thongTinCaNhanFragment = new ThongTinCaNhanFragment();
                mainActivity.loadFragment(thongTinCaNhanFragment);
            }
        });

        // Xử lý sự kiện click vào mục "Thay đổi mật khẩu" // Xong
        lnThayDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và chuyển đến Fragment thay đổi mật khẩu
                ThayDoiMatKhauFragment thayDoiMatKhauFragment = new ThayDoiMatKhauFragment();
                mainActivity.loadFragment(thayDoiMatKhauFragment);
            }
        });

        // Xử lý sự kiện click vào mục "Đăng xuất"  // Xong
        lnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và chuyển đến Fragment đăng xuất
                DangXuatFragment dangXuatFragment = new DangXuatFragment();
                mainActivity.loadFragment(dangXuatFragment);
            }
        });

        // Trả về view đã được cấu hình
        return v;
    }

    // Phương thức này được gọi sau khi onCreateView() và view đã được tạo
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Có thể thêm logic bổ sung sau khi view đã được tạo
    }
}