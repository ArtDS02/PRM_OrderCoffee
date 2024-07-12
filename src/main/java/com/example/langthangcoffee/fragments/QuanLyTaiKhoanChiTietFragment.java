package com.example.langthangcoffee.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.langthangcoffee.models.QuyenHan;
import com.example.langthangcoffee.models.TaiKhoan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class QuanLyTaiKhoanChiTietFragment extends Fragment  {
    private ArrayAdapter<QuyenHan> quyenHanArrayAdapter;
    Button  btnCapNhat, btnXoaTaiKhoan;
    EditText edtHo, edtTen, edtDiaChi, edtSDT;
    ArrayList<QuyenHan> quyenHanArrayList;
    TaiKhoan taiKhoan;
    MainActivity mainActivity;
    private Spinner spinnerQuyenHan;

    public QuanLyTaiKhoanChiTietFragment(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_quan_ly_tai_khoan_chi_tiet, container, false);
        mainActivity = (MainActivity) getActivity();
        btnCapNhat = v.findViewById(R.id.btn_cap_nhat);
        btnXoaTaiKhoan = v.findViewById(R.id.btn_xoa);
        edtHo = v.findViewById(R.id.edt_hokh);
        edtTen = v.findViewById(R.id.edt_tenkh);
        edtDiaChi = v.findViewById(R.id.edt_diachi_giaohang);
        spinnerQuyenHan = v.findViewById(R.id.spinner_quyenhan);
        edtSDT = v.findViewById(R.id.edt_sdtkh);

        quyenHanArrayList = new ArrayList<>();

        quyenHanArrayList.add(new QuyenHan(1, "Khách hàng"));
        quyenHanArrayList.add(new QuyenHan(2, "Quản lý"));


        quyenHanArrayAdapter = new ArrayAdapter<QuyenHan>(getActivity(),
                R.layout.spinner_item,
                quyenHanArrayList);
        quyenHanArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerQuyenHan.setAdapter(quyenHanArrayAdapter);
        int indexPositionDefaultSpinner = -1;
        for (int i = 0; i < quyenHanArrayList.size(); i++) {
            if (taiKhoan.getMaQuyenHan() == quyenHanArrayList.get(i).getMaQuyenHan()) {
                indexPositionDefaultSpinner = i;
                break;
            }
        }
        spinnerQuyenHan.setSelection(indexPositionDefaultSpinner);
        updateView();
        edtHo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtHo.hasFocus()) {
                    taiKhoan.setHo(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        edtTen.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtTen.hasFocus()) {
                    taiKhoan.setTen(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        edtDiaChi.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtDiaChi.hasFocus()) {
                    taiKhoan.setDiaChiGiaoHang(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setCancelable(true);
                builder.setTitle("Xác nhận cập nhật tài khoản");
                builder.setMessage("Có chắc muốn cập nhật tài khoản này?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                capNhatTaiKhoan();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        btnXoaTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setCancelable(true);


                builder.setTitle("Xác nhận xóa tài khoản");
                builder.setMessage("Có chắc muốn xóa tài khoản này?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                xoaTaiKhoan();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });





        spinnerQuyenHan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        QuyenHan quyenHan = (QuyenHan) adapter.getItem(position);
        taiKhoan.setMaQuyenHan(quyenHan.getMaQuyenHan());
    }
    private void capNhatTaiKhoan() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/taikhoan/cap-nhat";

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            jsonBody.put("Ho", taiKhoan.getHo());
            jsonBody.put("Ten", taiKhoan.getTen());
            jsonBody.put("DiaChiGiaoHang", taiKhoan.getDiaChiGiaoHang());
            jsonBody.put("MaQuyenHan", taiKhoan.getMaQuyenHan());

            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
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

    private void xoaTaiKhoan() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/taikhoan/xoa-tai-khoan";
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
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();


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



    public void updateView() {
        if (taiKhoan != null) {
            edtDiaChi.setText(taiKhoan.getDiaChiGiaoHang());
            edtHo.setText(taiKhoan.getHo());
            edtTen.setText(taiKhoan.getTen());
            edtSDT.setText(taiKhoan.getSdtTaiKhoan());
            disableEditText(edtSDT);

        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgBack = (ImageView) view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }


}
