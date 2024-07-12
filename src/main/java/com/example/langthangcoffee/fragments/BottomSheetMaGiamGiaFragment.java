package com.example.langthangcoffee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.langthangcoffee.adapters.FoodOrderSizeEditAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class BottomSheetMaGiamGiaFragment extends BottomSheetDialogFragment {
    ImageView imgClose;
    BottomSheetMaGiamGiaFragment bottomSheetMaGiamGiaFragment;
    XacNhanDatHangFragment xacNhanDatHangFragment;
    EditText edtVoucher;
    Button btnApplyVoucher;
    MainActivity mainActivity;
    DonHang donHang;
    private RecyclerView recyclerView, recyclerView2;
    private FoodOrderSizeEditAdapter foodOrderSizeEditAdapter;

    public BottomSheetMaGiamGiaFragment(XacNhanDatHangFragment xacNhanDatHangFragment) {
        // Required empty public constructor
        bottomSheetMaGiamGiaFragment = this;
        this.xacNhanDatHangFragment = xacNhanDatHangFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_bottom_sheet_dialog_ma_giam_gia, container, false);
        edtVoucher = v.findViewById(R.id.edt_magiamgia_donhang);
        btnApplyVoucher = v.findViewById(R.id.btn_apply_voucher);
        mainActivity = (MainActivity) getActivity();
        donHang = mainActivity.getDonHang();
        btnApplyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVoucherDonHang();
            }
        });
        return v;
    }
    private void updateVoucherDonHang() {
        try {
            String url = getString(R.string.endpoint_server) + "/donhang/update-voucher";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDonHang", donHang.getMaDonHang());
            jsonBody.put("maVoucher", edtVoucher.getText().toString());
            jsonBody.put("sdtTaiKhoan", mainActivity.getTaiKhoan().getSdtTaiKhoan());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject donHangObject = obj.getJSONObject("donHang");
                                donHang.setMaVoucher(donHangObject.getString("MaVoucher"));
                                donHang.setPhiGiaoHangGiam(donHangObject.getInt("PhiGiaoHangGiam"));
                                donHang.setThanhTienGiam(donHangObject.getInt("ThanhTienGiam"));
                                donHang.setSoTienThanhToan(donHangObject.getInt("SoTienThanhToan"));
                                xacNhanDatHangFragment.updateDonHangView();

                                progressDialog.dismiss();
                                bottomSheetMaGiamGiaFragment.dismiss();
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
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                    //displaying the error in toast if occur
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    donHang.setMaVoucher("");
                                    donHang.setSoTienThanhToan(donHang.getThanhTien() + donHang.getPhiGiaoHang());
                                    xacNhanDatHangFragment.updateDonHangView();
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
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
        imgClose = view.findViewById(R.id.img_close_bottom_dialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetMaGiamGiaFragment.dismiss();
            }


        });
    }
}
