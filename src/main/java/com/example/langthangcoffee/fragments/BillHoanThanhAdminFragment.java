package com.example.langthangcoffee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.langthangcoffee.adapters.BillOrderAdminHoanThanhAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;
import com.example.langthangcoffee.models.TaiKhoan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class BillHoanThanhAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<DonHang> itemBillList;
    BillOrderAdminHoanThanhAdapter billOrderAdminHoanThanhAdapter;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_wait_for_confirmation, container, false);
        mainActivity = (MainActivity) getActivity();
        recyclerView = v.findViewById(R.id.rcv_wait_for_confirmation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemBillList = new ArrayList<>();
        billOrderAdminHoanThanhAdapter = new BillOrderAdminHoanThanhAdapter(itemBillList, mainActivity);
        recyclerView.setAdapter(billOrderAdminHoanThanhAdapter);
        getDonHangHoanThanh();
        return v;
    }

    public void getDonHangHoanThanh() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/donhang/get-don-hang-da-hoan-thanh";
            TaiKhoan taiKhoan = mainActivity.getTaiKhoan();

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completion

                            try {
                                //getting the whole json object from the response
                                JSONObject jsonObject = new JSONObject(response);
                                //we have the array named data  inside the object
                                //so here we are getting that json array

                                // get don hang info
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject donHangObject = dataArray.getJSONObject(i);
                                    DonHang donHang = new DonHang();
                                    donHang.setMaDonHang(donHangObject.getInt("MaDonHang"));
                                    donHang.setPhiGiaoHang(donHangObject.getInt("PhiGiaoHang"));
                                    donHang.setSoTienThanhToan(donHangObject.getInt("SoTienThanhToan"));
                                    donHang.setThanhTien(donHangObject.getInt("ThanhTien"));
                                    donHang.setTinhTrang(donHangObject.getInt("TinhTrang"));
                                    donHang.setDiaChiGiaoHang(donHangObject.getString("DiaChiGiaoHang"));

                                    donHang.setSdtKhachHang(donHangObject.getString("SDTTaiKhoan"));
                                    donHang.setMaVoucher(donHangObject.getString("MaVoucher"));
                                    donHang.setThoiGianTao(donHangObject.getString("ThoiGianTao"));
                                    donHang.setThoiGianDatHang(donHangObject.getString("ThoiGianDatHang"));
                                    donHang.setThoiGianCapNhat(donHangObject.getString("ThoiGianCapNhat"));
                                    // get lich su order cua don hang
                                    List<LichSuOrder> lichSuOrderList = new ArrayList<LichSuOrder>();
                                    JSONArray jsonArray = donHangObject.getJSONArray("lichSuOrder");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsobj = jsonArray.getJSONObject(j);
                                        LichSuOrder lichSuOrder = new LichSuOrder();
                                        lichSuOrder.setMaLichSuOrder(jsobj.getInt("MaLichSuOrder"));
                                        lichSuOrder.setSoLuong(jsobj.getInt("SoLuong"));
                                        lichSuOrder.setThanhTien(jsobj.getInt("ThanhTien"));
                                        lichSuOrder.setGhiChu(jsobj.getString("GhiChu"));
                                        lichSuOrder.setMaSanPham(jsobj.getInt("MaSanPham"));
                                        lichSuOrder.setMaDonHang(jsobj.getInt("MaDonHang"));
                                        lichSuOrder.setTenSanPham(jsobj.getString("TenSanPham"));
                                        lichSuOrder.setKichThuoc(jsobj.getString("KichThuoc"));
                                        lichSuOrder.setGiaTienKichThuoc(jsobj.getInt("GiaTienKichThuoc"));
                                        List<FoodOrderTopping> foodOrderToppingList = new ArrayList<FoodOrderTopping>();
                                        JSONArray toppingArray = jsobj.getJSONArray("Topping");
                                        for (int k = 0; k < toppingArray.length(); k++) {
                                            JSONObject toppingObject = toppingArray.getJSONObject(k);
                                            FoodOrderTopping foodOrderTopping = new FoodOrderTopping();
                                            foodOrderTopping.setTenTopping(toppingObject.getString("TenTopping"));
                                            foodOrderTopping.setGiaTien(toppingObject.getInt("GiaTien"));
                                            foodOrderTopping.setMaSanPham(toppingObject.getInt("MaSanPham"));
                                            foodOrderToppingList.add(foodOrderTopping);
                                        }
                                        lichSuOrder.setFoodOrderToppingList(foodOrderToppingList);


                                        lichSuOrderList.add(lichSuOrder);
                                    }
                                    donHang.setLichSuOrderList(lichSuOrderList);
                                    itemBillList.add(donHang);
                                    billOrderAdminHoanThanhAdapter.notifyDataSetChanged();
                                }


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

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

}
