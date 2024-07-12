package com.example.langthangcoffee.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.langthangcoffee.models.LoaiVoucher;
import com.example.langthangcoffee.models.TaiKhoan;
import com.example.langthangcoffee.models.Voucher;
import com.ozcanalasalvar.library.view.datePicker.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ThemVoucherFragment extends Fragment {

    private ArrayAdapter<TaiKhoan> taiKhoanArrayAdapter;
    ArrayList<TaiKhoan> taiKhoanArrayList;

    private ArrayAdapter<LoaiVoucher> loaiVoucherArrayAdapter;
    ArrayList<LoaiVoucher> loaiVoucherArrayList;
    Button btnThemVoucher, btnDatePicker, btnTimePicker;
    EditText edtMaVoucher;
    Voucher voucher = new Voucher();

    MainActivity mainActivity;
    private Spinner spinnerTaiKhoan, spinnerLoaiVoucher;
    CheckBox chkRandomVoucher, chkAllTaiKhoan;
    DatePicker datePicker;


    public ThemVoucherFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_quan_ly_them_voucher, container, false);
        voucher.setLoaiVoucher(new LoaiVoucher());
        mainActivity = (MainActivity) getActivity();
        datePicker = v.findViewById(R.id.datepicker);
        btnThemVoucher = v.findViewById(R.id.btn_them);
        edtMaVoucher = v.findViewById(R.id.edt_mavoucher);
        chkRandomVoucher = v.findViewById(R.id.chk_select_random_voucher);
        chkAllTaiKhoan = v.findViewById(R.id.chk_select_tatca_taikhoan);
        spinnerTaiKhoan = v.findViewById(R.id.spinner_taikhoan);
        spinnerLoaiVoucher = v.findViewById(R.id.spinner_loaivoucher);
        taiKhoanArrayList = new ArrayList<>();
        taiKhoanArrayAdapter = new ArrayAdapter<TaiKhoan>(getActivity(),
                R.layout.spinner_item,
                taiKhoanArrayList);
        taiKhoanArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTaiKhoan.setAdapter(taiKhoanArrayAdapter);

        loaiVoucherArrayList = new ArrayList<>();
        loaiVoucherArrayAdapter = new ArrayAdapter<LoaiVoucher>(getActivity(),
                R.layout.spinner_item,
                loaiVoucherArrayList);
        loaiVoucherArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerLoaiVoucher.setAdapter(loaiVoucherArrayAdapter);

        getListTaiKhoanAdmin();
        getListLoaiVoucherAdmin();

        datePicker.setDataSelectListener(new DatePicker.DataSelectListener() {
            @Override
            public void onDateSelected(long date, int day, int month, int year) {
                String thoiGianHetHan = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day) + " 00:00:00";
                Log.i("thoigianhethan", thoiGianHetHan);
                voucher.setThoiGianHetHan(thoiGianHetHan);
            }
        });

        edtMaVoucher.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtMaVoucher.hasFocus()) {
                    voucher.setMaVoucher(s.toString());
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
        btnThemVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setCancelable(true);
                builder.setTitle("Xác nhận thêm Voucher");
                builder.setMessage("Có chắc muốn thêm mới voucher này?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                themVoucher();
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
        spinnerTaiKhoan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                TaiKhoan taiKhoan = (TaiKhoan) adapter.getItem(position);
                voucher.setSdtTaiKhoan(taiKhoan.getSdtTaiKhoan());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerLoaiVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = parent.getAdapter();
                LoaiVoucher loaiVoucher = (LoaiVoucher) adapter.getItem(position);
                voucher.setLoaiVoucher(loaiVoucher);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void getListTaiKhoanAdmin() {
        String url = getString(R.string.endpoint_server) + "/admin/taikhoan/get-danh-sach";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named data  inside the object
                            //so here we are getting that json array
                            JSONArray datasArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < datasArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = datasArray.getJSONObject(i);
                                TaiKhoan taiKhoan = new TaiKhoan();
                                taiKhoan.setSdtTaiKhoan(jsonObject.getString("SDTTaiKhoan"));
                                taiKhoan.setHo(jsonObject.getString("Ho"));
                                taiKhoan.setTen(jsonObject.getString("Ten"));
                                taiKhoan.setDiaChiGiaoHang(jsonObject.getString("DiaChiGiaoHang"));
                                taiKhoan.setMaQuyenHan(jsonObject.getInt("MaQuyenHan"));
                                taiKhoan.setTenQuyenHan(jsonObject.getString("TenQuyenHan"));
                                taiKhoan.setThoiGianThamGia(jsonObject.getString("ThoiGianThamGia"));
                                taiKhoanArrayList.add(taiKhoan);
                            }
                            taiKhoanArrayAdapter.notifyDataSetChanged();

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

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    private void getListLoaiVoucherAdmin() {
        String url = getString(R.string.endpoint_server) + "/admin/loaivoucher/get-danh-sach";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named data  inside the object
                            //so here we are getting that json array
                            JSONArray datasArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < datasArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = datasArray.getJSONObject(i);
                                LoaiVoucher loaiVoucher = new LoaiVoucher();
                                loaiVoucher.setMaLoaiVoucher(jsonObject.getInt("MaLoaiVoucher"));
                                loaiVoucher.setTenLoaiVoucher(jsonObject.getString("TenLoaiVoucher"));
                                loaiVoucher.setMoTaVoucher(jsonObject.getString("MoTaLoaiVoucher"));
                                loaiVoucher.setHinhAnh(jsonObject.getString("MoTaLoaiVoucher"));
                                loaiVoucher.setPhiShip(jsonObject.getInt("PhiShip"));
                                loaiVoucher.setSoLuongToiThieu(jsonObject.getInt("SoLuongToiThieu"));
                                loaiVoucher.setTongTien(jsonObject.getInt("TongTien"));
                                loaiVoucherArrayList.add(loaiVoucher);
                            }
                            loaiVoucherArrayAdapter.notifyDataSetChanged();

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

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void themVoucher() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/voucher/them-moi";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("isRandomVoucher", chkRandomVoucher.isChecked());
            jsonBody.put("isAllTaiKhoan", chkAllTaiKhoan.isChecked());
            jsonBody.put("sdtTaiKhoan", voucher.getSdtTaiKhoan());
            jsonBody.put("maVoucher", voucher.getMaVoucher());
            jsonBody.put("maLoaiVoucher", voucher.getLoaiVoucher().getMaLoaiVoucher());
            jsonBody.put("thoiGianHetHan", voucher.getThoiGianHetHan());

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
