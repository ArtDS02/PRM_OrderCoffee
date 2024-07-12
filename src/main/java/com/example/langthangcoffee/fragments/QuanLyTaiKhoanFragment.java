package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.drawable;
import static com.example.langthangcoffee.R.id;
import static com.example.langthangcoffee.R.layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.adapters.QuanLyTaiKhoanAdapter;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.TaiKhoan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class QuanLyTaiKhoanFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuanLyTaiKhoanAdapter quanLyTaiKhoanAdapter;
    private EditText edtSearch;
    private List<TaiKhoan> filterList = new ArrayList<>();
    private List<TaiKhoan> list = new ArrayList<>();
    private ColorStateList defText;
    private Button item1, item2, item3, item4, searchButton, priceButton, btnThemTaiKhoan;
    private TextView tvQuantity, tvQuantityOrder;
    GridLayoutManager gridLayoutManager;
    ImageView imgBack;

    public MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        list.clear();

        // Khởi tạo View từ layout fragment_trang_quan_ly_tai_khoan
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_quan_ly_tai_khoan, container, false);

        // Ánh xạ RecyclerView từ layout
        recyclerView = v.findViewById(id.rcv_taikhoan);
        imgBack = v.findViewById(id.img_back);

        // Khởi tạo GridLayoutManager với 1 cột
        gridLayoutManager = new GridLayoutManager(getContext(), 1);

        // Set LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(gridLayoutManager);

        // Khởi tạo adapter cho RecyclerView
        quanLyTaiKhoanAdapter = new QuanLyTaiKhoanAdapter(list);
        recyclerView.setAdapter(quanLyTaiKhoanAdapter);

        // Lấy danh sách tài khoản admin
        getListTaiKhoanAdmin();

        // Ánh xạ Button thêm tài khoản từ layout
        btnThemTaiKhoan = v.findViewById(id.btn_them_moi);

        // Sự kiện khi nhấn nút back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Sự kiện khi nhấn nút thêm tài khoản
        btnThemTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tạo fragment thêm tài khoản mới
                ThemTaiKhoanFragment themTaiKhoanFragment = new ThemTaiKhoanFragment();

                // Thay thế fragment hiện tại bằng fragment thêm tài khoản
                mainActivity.loadFragment(themTaiKhoanFragment);
            }
        });
        return v;
    }

    // Phương thức lấy danh sách tài khoản admin từ server
    private void getListTaiKhoanAdmin() {
        String url = getString(R.string.endpoint_server) + "/admin/taikhoan/get-danh-sach";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Tạo request GET để lấy dữ liệu từ server
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Ẩn the progressbar khi hoàn thành

                        try {

                            // Chuyển response thành JSONObject
                            JSONObject obj = new JSONObject(response);

                            // Lấy mảng data từ JSONObject
                            JSONArray datasArray = obj.getJSONArray("data");

                            // Lặp qua các phần tử trong mảng data
                            for (int i = 0; i < datasArray.length(); i++) {

                                // Lấy từng JSONObject trong mảng data
                                JSONObject jsonObject = datasArray.getJSONObject(i);

                                // Tạo đối tượng TaiKhoan và set các thuộc tính từ JSONObject
                                TaiKhoan taiKhoan = new TaiKhoan();
                                taiKhoan.setSdtTaiKhoan(jsonObject.getString("SDTTaiKhoan"));
                                taiKhoan.setHo(jsonObject.getString("Ho"));
                                taiKhoan.setTen(jsonObject.getString("Ten"));
                                taiKhoan.setDiaChiGiaoHang(jsonObject.getString("DiaChiGiaoHang"));
                                taiKhoan.setMaQuyenHan(jsonObject.getInt("MaQuyenHan"));
                                taiKhoan.setTenQuyenHan(jsonObject.getString("TenQuyenHan"));
                                taiKhoan.setThoiGianThamGia(jsonObject.getString("ThoiGianThamGia"));

                                // Thêm đối tượng TaiKhoan vào danh sách
                                list.add(taiKhoan);
                            }

                            // Cập nhật lại adapter với danh sách mới
                            quanLyTaiKhoanAdapter.notifyDataSetChanged();

                            // Ẩn ProgressDialog
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }


                        progressDialog.dismiss();
                    }
                });

        // Tạo request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Thêm request vào request queue
        requestQueue.add(stringRequest);

    }
}



