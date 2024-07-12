package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.fragments.ChiTietDatHangAdminDangChoFragment;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.LichSuOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

@SuppressWarnings("deprecation")
public class BillOrderAdminDangChoAdapter extends RecyclerView.Adapter<BillOrderAdminDangChoAdapter.FoodOrderViewHolder> {
    private List<DonHang> mBillOrder;
    MainActivity mainActivity;
    BillOrderAdminDangChoAdapter billOrderAdminDangChoAdapter;


    public BillOrderAdminDangChoAdapter(List<DonHang> mBillOrder, MainActivity mainActivity) {
        this.mBillOrder = mBillOrder;
        this.mainActivity = mainActivity;
        billOrderAdminDangChoAdapter = this;
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_su_don_hang_dang_cho_admin, parent, false);
        return new FoodOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DonHang donHang = mBillOrder.get(position);
        if (donHang == null) {
            return;
        }
        holder.tvTaiKhoan.setText("Số điện thoại: " + (donHang.getSdtKhachHang()));
        holder.tvMaDonHang.setText("Mã đơn hàng: " + String.valueOf(donHang.getMaDonHang()));
        holder.tvTongTien.setText("Tổng tiền: " + String.valueOf(donHang.getSoTienThanhToan()) + " đ");
        holder.tvThoiGian.setText("Thời gian: " + donHang.getThoiGianDatHang());
        List<LichSuOrder> lichSuOrderList = donHang.getLichSuOrderList();
        String chiTietDonHang = "";
        for (int i = 0; i < lichSuOrderList.size(); i++) {
            String orderDetail = "";
            orderDetail = "x" + lichSuOrderList.get(i).getSoLuong() + " " + lichSuOrderList.get(i).getTenSanPham();
            if (i != lichSuOrderList.size() - 1) {
                orderDetail += ", ";
            }
            chiTietDonHang += orderDetail;
        }
        holder.tvDetail.setText("Chi tiết: " + chiTietDonHang);

        holder.btnHuyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huyDonHangDangCho(donHang.getMaDonHang());

            }
        });
        holder.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xacNhanDonHangDangCho(donHang.getMaDonHang());



            }
        });
        holder.lnItemDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChiTietDatHangAdminDangChoFragment chiTietDatHangAdminDangChoFragment = new ChiTietDatHangAdminDangChoFragment(billOrderAdminDangChoAdapter);
                Bundle bundle = new Bundle();
                bundle.putSerializable("donHang", donHang); // Put anything what you want
                chiTietDatHangAdminDangChoFragment.setArguments(bundle);
                mainActivity.loadFragment(chiTietDatHangAdminDangChoFragment);
            }
        });

    }

    public void huyDonHangDangCho(int maDonHang) {
        try {
            String url = mainActivity.getString(R.string.endpoint_server) + "/admin/donhang/huy-don-hang-dang-cho";

            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDonHang", maDonHang);
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
                                Toast.makeText(mainActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                mBillOrder.removeIf(item -> item.getMaDonHang() == maDonHang);
                                billOrderAdminDangChoAdapter.notifyDataSetChanged();

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
                                    Toast.makeText(mainActivity, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    public void xacNhanDonHangDangCho(int maDonHang) {
        try {
            String url = mainActivity.getString(R.string.endpoint_server) + "/admin/donhang/xac-nhan-don-hang-dang-cho";

            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDonHang", maDonHang);
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
                                Toast.makeText(mainActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                mBillOrder.removeIf(item -> item.getMaDonHang() == maDonHang);
                                billOrderAdminDangChoAdapter.notifyDataSetChanged();

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
                                    Toast.makeText(mainActivity, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mBillOrder != null) {
            return mBillOrder.size();
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        ;
        private final TextView tvMaDonHang, tvDetail, tvTongTien, tvThoiGian, tvTaiKhoan;

        private final Button btnHuyDonHang, btnXacNhan;
        private final LinearLayout lnItemDonHang;


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);


            tvMaDonHang = itemView.findViewById(R.id.tv_ma_don_hang);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            tvTongTien = itemView.findViewById(R.id.tv_tong_tien);
            tvThoiGian = itemView.findViewById(R.id.tv_thoi_gian);
            btnHuyDonHang = itemView.findViewById(R.id.btn_cancel_an_order);
            lnItemDonHang = itemView.findViewById(R.id.ln_item_don_hang);
            btnXacNhan = itemView.findViewById(R.id.btn_confirm_bill);
            tvTaiKhoan = itemView.findViewById(R.id.tv_sdt_don_hang);


        }
    }
}

