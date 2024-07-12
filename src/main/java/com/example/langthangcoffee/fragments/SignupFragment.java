package com.example.langthangcoffee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.langthangcoffee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignupFragment extends Fragment {
    ImageView imgBack, imgShowPassword;
    Button btnSignup;
    EditText edtHoKH, edtTenKH, edtSDTKH, edtPassKH;
    CheckBox chkChinhSach, chkChuongTrinhThanhVien;
    Boolean isShowPassword = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chkChinhSach = view.findViewById(R.id.chk_chinhsach);
        chkChuongTrinhThanhVien = view.findViewById(R.id.chk_chuongtrinhthanhvien);
        imgShowPassword = view.findViewById(R.id.img_showpassword);
        imgBack = view.findViewById(R.id.img_back);
        btnSignup = view.findViewById(R.id.btn_sign_up);
        edtHoKH = view.findViewById(R.id.edt_hokh);
        edtTenKH = view.findViewById(R.id.edt_tenkh);
        edtSDTKH = view.findViewById(R.id.edt_phone_number);
        edtPassKH = view.findViewById(R.id.edt_password);
        // Icon to back previous fragment
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowPassword) {
                    edtPassKH.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    edtPassKH.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                isShowPassword = !isShowPassword;
                int drawableId = isShowPassword ? R.drawable.hidepassword : R.drawable.showpassword;
                imgShowPassword.setImageResource(drawableId);
                edtPassKH.setSelection(edtPassKH.length());
            }
        });
        // Click sign up button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chkChinhSach.isChecked() || !chkChuongTrinhThanhVien.isChecked()) {
                    Toast.makeText(getActivity(), "Vui lòng chấp nhận các chính sách", Toast.LENGTH_SHORT).show();
                } else if (edtSDTKH.getText().toString().trim().length() != 10) {
                    Toast.makeText(getActivity(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (edtPassKH.getText().toString().trim().length() < 6) {
                    Toast.makeText(getActivity(), "Mật khẩu phải từ 6 kí tự trở lên", Toast.LENGTH_SHORT).show();
                } else if (edtHoKH.getText().toString().trim().length() < 2) {
                    Toast.makeText(getActivity(), "Họ phải từ 2 kí tự trở lên", Toast.LENGTH_SHORT).show();
                } else if (edtTenKH.getText().toString().trim().length() < 2) {
                    Toast.makeText(getActivity(), "Tên phải từ 2 kí tự trở lên", Toast.LENGTH_SHORT).show();
                } else {
                    signupTaiKhoan();
                }
            }

        });
    }

    private void signupTaiKhoan() {
        String url = getString(R.string.endpoint_server) + "/taikhoan/sign-up";
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", edtSDTKH.getText().toString());
            jsonBody.put("Ho", edtHoKH.getText().toString());
            jsonBody.put("Ten", edtTenKH.getText().toString());
            jsonBody.put("MatKhau", edtPassKH.getText().toString());

            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //hiding the progressbar after completion

                    try {
                        //getting the whole json object from the response
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
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
}
