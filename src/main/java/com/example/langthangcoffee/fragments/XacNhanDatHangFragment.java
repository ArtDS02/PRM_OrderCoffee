package com.example.langthangcoffee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.langthangcoffee.adapters.FoodOrderHistoryAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class XacNhanDatHangFragment extends Fragment {
    MainActivity mainActivity;
    LinearLayout lnGiamGia;
    ImageView imgBack;
    Button btnThayDoiDiaChi, btnConfirmOrder, btnAddOrder;
    private RecyclerView recyclerView;
    private FoodOrderHistoryAdapter foodOrderHistoryAdapter;
    GridLayoutManager gridLayoutManager;
    DonHang donHang = null;
    private List<LichSuOrder> lichSuOrderList = new ArrayList<>();
    XacNhanDatHangFragment xacNhanDatHangFragment;
    TextView tvInfoShip, tvItemThanhTien, tvItemPhiGiaoHang, tvItemThanhToan;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        xacNhanDatHangFragment = this;
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_xac_nhan_order, container, false);
        imgBack = v.findViewById(R.id.img_back);
        btnAddOrder = v.findViewById(R.id.btn_add_item);
        tvInfoShip = v.findViewById(R.id.tv_info_ship_order);
        tvItemThanhTien = v.findViewById(R.id.tv_item_thanhtien);
        tvItemPhiGiaoHang = v.findViewById(R.id.tv_item_phigiaohang);
        tvItemThanhToan = v.findViewById(R.id.tv_item_thanhtoan);
        btnConfirmOrder = v.findViewById(R.id.btn_confirm_order);
        mainActivity = (MainActivity) getActivity();
        donHang = mainActivity.getDonHang();
        if (donHang != null) {
            donHang.setMaVoucher("");
            donHang.setSoTienThanhToan(donHang.getThanhTien() + donHang.getPhiGiaoHang());
        }
        updateDonHangView();
        recyclerView = v.findViewById(R.id.rcv_order_history);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        foodOrderHistoryAdapter = new FoodOrderHistoryAdapter(lichSuOrderList, xacNhanDatHangFragment);
        recyclerView.setAdapter(foodOrderHistoryAdapter);
        getFoodOrderHistory();


        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lichSuOrderList.size() == 0) {
                    Toast.makeText(getActivity(), "Vui lòng order ít nhất một món", Toast.LENGTH_SHORT).show();
                } else if (donHang.getDiaChiGiaoHang() == null || donHang.getDiaChiGiaoHang().isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng điền địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                } else {
                    updateTinhTrangDonHang();
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private void updateTinhTrangDonHang() {
        try {
            String url = getString(R.string.endpoint_server) + "/donhang/update-tinh-trang";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDonHang", donHang.getMaDonHang());
            // Update tình trạng đơn hàng từ khởi tạo -> chờ xác nhận
            jsonBody.put("tinhTrang", 1);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                // Remove đơn hàng hiện tại
                                donHang = null;
                                // Xóa list lịch sử order
                                lichSuOrderList.clear();
                                foodOrderHistoryAdapter.notifyDataSetChanged();
                                // Tạo đơn hàng trống
                                mainActivity.getDonHangMoiNhat();
                                // Quay lại trang đặt hàng
                                imgBack.performClick();


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
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }


    public void deleteLichSuOrderList(int maLichSuOrder) {
        lichSuOrderList.removeIf(item -> item.getMaLichSuOrder() == maLichSuOrder);
        foodOrderHistoryAdapter.notifyDataSetChanged();
    }

    public void updateLichSuOrderList(List<LichSuOrder> lichSuOrderList2) {
        this.lichSuOrderList = lichSuOrderList2;
        foodOrderHistoryAdapter = new FoodOrderHistoryAdapter(lichSuOrderList, xacNhanDatHangFragment);
        recyclerView.setAdapter(foodOrderHistoryAdapter);
    }

    public void updateDonHangView() {
        if (donHang != null) {
            tvInfoShip.setText(donHang.getDiaChiGiaoHang());
            tvItemThanhToan.setText(String.valueOf(donHang.getSoTienThanhToan()) + " đ");
            tvItemPhiGiaoHang.setText(String.valueOf(donHang.getPhiGiaoHang()) + " đ");
            tvItemThanhTien.setText(String.valueOf(donHang.getThanhTien()) + " đ");
        }

    }


    private void getFoodOrderHistory() {
        try {
            String url = getString(R.string.endpoint_server) + "/lichsuorder";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDonHang", mainActivity.getDonHang().getMaDonHang());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                // Kich Thuoc
                                JSONArray dataArray = obj.getJSONArray("lichSuOrder");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    LichSuOrder lichSuOrder = new LichSuOrder();
                                    lichSuOrder.setMaLichSuOrder(jsonObject.getInt("MaLichSuOrder"));
                                    lichSuOrder.setSoLuong(jsonObject.getInt("SoLuong"));
                                    lichSuOrder.setThanhTien(jsonObject.getInt("ThanhTien"));
                                    lichSuOrder.setGhiChu(jsonObject.getString("GhiChu"));
                                    lichSuOrder.setMaSanPham(jsonObject.getInt("MaSanPham"));
                                    lichSuOrder.setMaDonHang(jsonObject.getInt("MaDonHang"));
                                    lichSuOrder.setTenSanPham(jsonObject.getString("TenSanPham"));
                                    lichSuOrder.setKichThuoc(jsonObject.getString("KichThuoc"));
                                    lichSuOrder.setGiaTienKichThuoc(jsonObject.getInt("GiaTienKichThuoc"));
                                    List<FoodOrderTopping> foodOrderToppingList = new ArrayList<FoodOrderTopping>();
                                    JSONArray toppingArray = jsonObject.getJSONArray("Topping");
                                    for (int j = 0; j < toppingArray.length(); j++) {
                                        JSONObject toppingObject = toppingArray.getJSONObject(j);
                                        FoodOrderTopping foodOrderTopping = new FoodOrderTopping();
                                        foodOrderTopping.setTenTopping(toppingObject.getString("TenTopping"));
                                        foodOrderTopping.setGiaTien(toppingObject.getInt("GiaTien"));
                                        foodOrderTopping.setMaSanPham(toppingObject.getInt("MaSanPham"));
                                        foodOrderToppingList.add(foodOrderTopping);
                                    }
                                    lichSuOrder.setFoodOrderToppingList(foodOrderToppingList);
                                    lichSuOrderList.add(lichSuOrder);
                                }
                                foodOrderHistoryAdapter.notifyDataSetChanged();
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
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnGiamGia = view.findViewById(R.id.ln_magiamgia_xacnhan_dathang);
        btnThayDoiDiaChi = view.findViewById(R.id.btn_change_info_ship_order);
        lnGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetMaGiamGiaFragment bottomSheetMaGiamGiaFragment = new BottomSheetMaGiamGiaFragment(xacNhanDatHangFragment);
                bottomSheetMaGiamGiaFragment.show(getParentFragmentManager(), bottomSheetMaGiamGiaFragment.getTag());
            }
        });
        btnThayDoiDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDiaChiGiaoHangFragment bottomSheetDiaChiGiaoHangFragment = new BottomSheetDiaChiGiaoHangFragment(xacNhanDatHangFragment);
                bottomSheetDiaChiGiaoHangFragment.show(getParentFragmentManager(), bottomSheetDiaChiGiaoHangFragment.getTag());
            }
        });

    }
}
