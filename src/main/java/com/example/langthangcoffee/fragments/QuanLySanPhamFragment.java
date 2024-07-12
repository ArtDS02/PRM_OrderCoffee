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
import com.example.langthangcoffee.adapters.QuanLySanPhamAdapter;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.FoodOrder;
import com.example.langthangcoffee.models.KichThuocSanPham;
import com.example.langthangcoffee.models.ToppingSanPham;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class QuanLySanPhamFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuanLySanPhamAdapter quanLySanPhamAdapter;
    private EditText searchFoodOrder;
    private List<FoodOrder> filterList = new ArrayList<>();
    private List<FoodOrder> list = new ArrayList<>();
    private ColorStateList defText;
    private Button item1, item2, item3, item4, searchButton, priceButton, btnThemSanPham;
    private TextView tvQuantity, tvQuantityOrder;
    GridLayoutManager gridLayoutManager;
    ImageView imgBack;

    public MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        list.clear();

        // Khởi tạo View từ layout fragment_trang_quan_ly_san_pham
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_quan_ly_san_pham, container, false);

        // Ánh xạ RecyclerView từ layout
        recyclerView = v.findViewById(id.rcv_sanpham);

        // Khởi tạo GridLayoutManager với 1 cột
        gridLayoutManager = new GridLayoutManager(getContext(), 1);

        // Set LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(gridLayoutManager);

        // Khởi tạo adapter cho RecyclerView
        quanLySanPhamAdapter = new QuanLySanPhamAdapter(list);
        recyclerView.setAdapter(quanLySanPhamAdapter);

        // Gọi phương thức để lấy danh sách sản phẩm admin
        getListSanPhamAdmin();

        // Ánh xạ Button thêm sản phẩm từ layout
        btnThemSanPham = v.findViewById(id.btn_them_moi);

        // Ánh xạ ImageView từ layout
        imgBack = v.findViewById(id.img_back);

        // Sự kiện khi nhấn nút quay lại
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            // Quay lại fragment trước đó
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Sự kiện khi nhấn nút thêm sản phẩm
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemSanPhamFragment themSanPhamFragment = new ThemSanPhamFragment();
                mainActivity.loadFragment(themSanPhamFragment);
            }
        });
        return v;
    }

    // Phương thức lấy danh sách sản phẩm admin từ server
    private void getListSanPhamAdmin() {
        String url = getString(R.string.endpoint_server) + "/admin/sanpham/get-danh-sach";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Tạo request GET để lấy dữ liệu từ server
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        try {

                            // Chuyển response thành JSONObject
                            JSONObject obj = new JSONObject(response);

                            // Lấy mảng data từ JSONObject
                            JSONArray datasArray = obj.getJSONArray("data");

                            // Lặp qua các phần tử trong mảng data
                            for (int i = 0; i < datasArray.length(); i++) {

                                // Lấy từng JSONObject trong mảng data
                                JSONObject jsonObject = datasArray.getJSONObject(i);

                                // Tạo đối tượng FoodOrder và set các thuộc tính từ JSONObject
                                FoodOrder foodOrder = new FoodOrder();
                                int imageID = getActivity().getResources().getIdentifier(jsonObject.getString("HinhAnh"), "drawable", getActivity().getPackageName());
                                foodOrder.setImage(imageID);
                                foodOrder.setTenDanhMuc(jsonObject.getString("TenDanhMuc"));
                                foodOrder.setHinhAnh(jsonObject.getString("HinhAnh"));
                                foodOrder.setDesc(jsonObject.getString("MoTa"));
                                foodOrder.setName(jsonObject.getString("TenSanPham"));
                                foodOrder.setID(jsonObject.getInt("MaSanPham"));
                                foodOrder.setType(jsonObject.getInt("MaDanhMuc"));
                                foodOrder.setMaDanhMuc(jsonObject.getInt("MaDanhMuc"));
                                foodOrder.setPrice(jsonObject.getInt("GiaTien"));
                                List<KichThuocSanPham> kichThuocSanPhamList = new ArrayList<>();
                                JSONArray kichThuocArray = jsonObject.getJSONArray("kichThuoc");

                                // Lấy danh sách kích thước sản phẩm từ JSONObject
                                for (int j = 0; j < kichThuocArray.length(); j++) {
                                    JSONObject kichThuocObject = kichThuocArray.getJSONObject(j);
                                    KichThuocSanPham kichThuocSanPham = new KichThuocSanPham();
                                    kichThuocSanPham.setMaSanPham(kichThuocObject.getInt("MaSanPham"));
                                    kichThuocSanPham.setGiaTien(kichThuocObject.getInt("GiaTien"));
                                    kichThuocSanPham.setTenKichThuoc(kichThuocObject.getString("TenKichThuoc"));
                                    kichThuocSanPhamList.add(kichThuocSanPham);
                                }

                                // Lấy danh sách topping sản phẩm từ JSONObject
                                List<ToppingSanPham> toppingSanPhamList = new ArrayList<>();
                                JSONArray toppingArray = jsonObject.getJSONArray("topping");
                                for (int j = 0; j < toppingArray.length(); j++) {
                                    JSONObject toppingObject = toppingArray.getJSONObject(j);
                                    ToppingSanPham toppingSanPham = new ToppingSanPham();
                                    toppingSanPham.setMaSanPham(toppingObject.getInt("MaSanPham"));
                                    toppingSanPham.setGiaTien(toppingObject.getInt("GiaTien"));
                                    toppingSanPham.setTenTopping(toppingObject.getString("TenTopping"));
                                    toppingSanPhamList.add(toppingSanPham);
                                }

                                // Set danh sách kích thước và topping cho đối tượng FoodOrder
                                foodOrder.setKichThuocSanPhamList(kichThuocSanPhamList);
                                foodOrder.setToppingSanPhams(toppingSanPhamList);

                                // Thêm đối tượng FoodOrder vào danh sách
                                list.add(foodOrder);

                            }

                            // Cập nhật lại adapter với danh sách mới
                            quanLySanPhamAdapter.notifyDataSetChanged();

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



