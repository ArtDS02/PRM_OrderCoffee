package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.layout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.langthangcoffee.models.TaiKhoan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

@SuppressWarnings("deprecation")
public class ThayDoiMatKhauFragment extends Fragment {
    ImageView imgBack;
    MainActivity mainActivity;
    TaiKhoan taiKhoan;
    EditText edtMatKhauHienTai, edtMatKhauMoi;
    TextView tvTenQuyenHan;
    Button btnCapNhat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_thay_doi_mat_khau, container, false);
        mainActivity = (MainActivity) getActivity();
        taiKhoan = mainActivity.getTaiKhoan();
        imgBack = v.findViewById(R.id.img_back);
        btnCapNhat = v.findViewById(R.id.btn_cap_nhat);
        edtMatKhauMoi = v.findViewById(R.id.edt_mat_khau_moi);
        edtMatKhauHienTai = v.findViewById(R.id.edt_mat_khau_hien_tai);
        tvTenQuyenHan = v.findViewById(R.id.tv_role_user);
        getThongTinTaiKhoan();

        // Cài sự kiện nếu bấm nút back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        // Cài sự kiện nếu bấm nút cập nhật
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Kiểm tra điều kiện cho mật khẩu
                if (edtMatKhauHienTai.getText().toString().trim().length() < 6 || edtMatKhauMoi.getText().toString().trim().length() < 6) {
                    Toast.makeText(getActivity(), "Mật khẩu phải từ 6 kí tự trở lên", Toast.LENGTH_SHORT).show();

                } else if (edtMatKhauHienTai.getText().toString().trim().equals(edtMatKhauMoi.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Hai mật khẩu không được trùng nhau", Toast.LENGTH_SHORT).show();
                } else {
                    updateMatKhau();
                }
            }
        });


        return v;
    }

    // Get thông tin tài khoản từ API
    public void getThongTinTaiKhoan() {
        try {
            String url = getString(R.string.endpoint_server) + "/taikhoan/";
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

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                taiKhoan.setSdtTaiKhoan(jsonObjectData.getString("SDTTaiKhoan"));
                                taiKhoan.setHo(jsonObjectData.getString("Ho"));
                                taiKhoan.setTen(jsonObjectData.getString("Ten"));
                                taiKhoan.setDiaChiGiaoHang(jsonObjectData.getString("DiaChiGiaoHang"));
                                taiKhoan.setMaQuyenHan(jsonObjectData.getInt("MaQuyenHan"));
                                taiKhoan.setTenQuyenHan(jsonObjectData.getString("TenQuyenHan"));
                                taiKhoan.setDiaChiGiaoHang(jsonObjectData.getString("DiaChiGiaoHang"));
                                hienThiThongTin();
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
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                    //displaying the error in toast if occur
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    // Dùng API để cập nhật mật khẩu mới
    public void updateMatKhau() {
        try {
            String url = getString(R.string.endpoint_server) + "/taikhoan/update-mat-khau";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("MatKhauCu", edtMatKhauHienTai.getText().toString());
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            jsonBody.put("MatKhauMoi", edtMatKhauMoi.getText().toString());

            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completion

                            try {
                                JSONObject jsonObject = new JSONObject(response);


                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


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
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                    //displaying the error in toast if occur
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //adding the string request to request queue
            requestQueue.add(stringRequest);

        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    // Phương thức hiển thị tên role UI
    public void hienThiThongTin() {

        tvTenQuyenHan.setText(taiKhoan.getTenQuyenHan());

    }

}
