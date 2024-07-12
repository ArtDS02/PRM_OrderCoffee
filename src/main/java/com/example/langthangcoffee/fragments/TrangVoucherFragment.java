package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.layout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.adapters.VoucherAdapter;
import com.example.langthangcoffee.models.LoaiVoucher;
import com.example.langthangcoffee.models.Voucher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TrangVoucherFragment extends Fragment {
    ImageView imgBack;
    GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> listVoucher = new ArrayList<>();
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_voucher, container, false);
        mainActivity = (MainActivity) getActivity();
        imgBack = v.findViewById(R.id.img_back);

        recyclerView = v.findViewById(R.id.rcv_voucher);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        voucherAdapter = new VoucherAdapter(listVoucher);
        recyclerView.setAdapter(voucherAdapter);

        getDanhSachVoucher();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return v;
    }

    private void getDanhSachVoucher() {
        try {
            String url = getString(R.string.endpoint_server) + "/voucher/get-danh-sach";

            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", mainActivity.getTaiKhoan().getSdtTaiKhoan());
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

                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    Voucher voucher = new Voucher();
                                    LoaiVoucher loaiVoucher = new LoaiVoucher();
                                    JSONObject itemVoucher = dataArray.getJSONObject(i);
                                    voucher.setMaVoucher(itemVoucher.getString("MaVoucher"));
                                    voucher.setSdtTaiKhoan(itemVoucher.getString("SDTTaiKhoan"));
                                    voucher.setSuDung(itemVoucher.getInt("SuDung"));
                                    voucher.setMaLoaiVoucher(itemVoucher.getInt("MaLoaiVoucher"));
                                    voucher.setThoiGianTao(itemVoucher.getString("ThoiGianTao"));
                                    voucher.setThoiGianHetHan(itemVoucher.getString("ThoiGianHetHan"));


                                    loaiVoucher.setMoTaVoucher(itemVoucher.getString("MoTaLoaiVoucher"));
                                    loaiVoucher.setTenLoaiVoucher(itemVoucher.getString("TenLoaiVoucher"));
                                    loaiVoucher.setHinhAnh(itemVoucher.getString("HinhAnh"));
                                    loaiVoucher.setMaLoaiVoucher(itemVoucher.getInt("MaLoaiVoucher"));
                                    loaiVoucher.setPhiShip(itemVoucher.getInt("PhiShip"));
                                    loaiVoucher.setSoLuongToiThieu(itemVoucher.getInt("SoLuongToiThieu"));
                                    loaiVoucher.setTongTien(itemVoucher.getInt("TongTien"));

                                    voucher.setLoaiVoucher(loaiVoucher);
                                    listVoucher.add(voucher);
                                    voucherAdapter.notifyDataSetChanged();


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
            RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }

    }


}
