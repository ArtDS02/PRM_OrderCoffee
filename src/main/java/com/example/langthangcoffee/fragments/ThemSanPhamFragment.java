package com.example.langthangcoffee.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.example.langthangcoffee.adapters.FoodOrderToppingAdapter;
import com.example.langthangcoffee.adapters.KichThuocSanPhamAdapter;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.adapters.ToppingSanPhamAdapter;
import com.example.langthangcoffee.utils.VolleyMultipartRequest;
import com.example.langthangcoffee.models.DanhMucSanPham;
import com.example.langthangcoffee.models.FoodOrder;
import com.example.langthangcoffee.models.KichThuocSanPham;
import com.example.langthangcoffee.models.LichSuOrder;
import com.example.langthangcoffee.models.ToppingSanPham;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemSanPhamFragment extends Fragment implements FoodOrderToppingAdapter.EventListener {
    private List<KichThuocSanPham> kichThuocSanPhamList = new ArrayList<>();
    private List<ToppingSanPham> toppingSanPhamList = new ArrayList<>();
    private int maSanPham = 0;
    private RecyclerView recyclerView, recyclerView2;
    private ToppingSanPhamAdapter toppingSanPhamAdapter;
    private KichThuocSanPhamAdapter kichThuocSanPhamAdapter;
    private ArrayAdapter<DanhMucSanPham> danhMucSanPhamArrayAdapter;
    GridLayoutManager gridLayoutManager, gridLayoutManager2;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private String filePath;
    ImageView imgFoodDetail;
    FirebaseStorage storage;
    StorageReference storageReference;
    Button btnPriceDetail, btnUploadHinhAnh, btnCapNhat, btnThemSanPham;
    EditText edtTenSanPham, edtMoTa;
    ArrayList<DanhMucSanPham> danhMucSanPhamArrayList;
    Button btnSubtractFood, btnThemSize, btnThemTopping;
    FoodOrder foodOrder = new FoodOrder();

    LichSuOrder lichSuOrder = null;
    MainActivity mainActivity;
    private Spinner spinnerDanhMuc;

    public ThemSanPhamFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_quan_ly_them_san_pham, container, false);
        mainActivity = (MainActivity) getActivity();
        imgFoodDetail = v.findViewById(R.id.img_info_detail);
        btnUploadHinhAnh = v.findViewById(R.id.btn_upload_anh);
        btnSubtractFood = v.findViewById(R.id.btn_subtract);
        edtTenSanPham = v.findViewById(R.id.edt_ten_san_pham);
        btnThemSanPham = v.findViewById(R.id.btn_them);
        edtMoTa = v.findViewById(R.id.edt_motasanpham);
        spinnerDanhMuc = v.findViewById(R.id.spinner_danhmucsanpham);
        btnThemSize = v.findViewById(R.id.btn_them_size);
        btnThemTopping = v.findViewById(R.id.btn_them_topping);

        recyclerView = v.findViewById(R.id.rcv_select_size);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        kichThuocSanPhamAdapter = new KichThuocSanPhamAdapter(kichThuocSanPhamList);
        recyclerView.setAdapter(kichThuocSanPhamAdapter);

        // display list food topping size
        recyclerView2 = v.findViewById(R.id.rcv_select_topping);
        gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        toppingSanPhamAdapter = new ToppingSanPhamAdapter(toppingSanPhamList);
        recyclerView2.setAdapter(toppingSanPhamAdapter);

        danhMucSanPhamArrayList = new ArrayList<>();

        danhMucSanPhamArrayList.add(new DanhMucSanPham(1, "Cà phê"));
        danhMucSanPhamArrayList.add(new DanhMucSanPham(2, "Soda"));
        danhMucSanPhamArrayList.add(new DanhMucSanPham(3, "Trà sữa"));
        danhMucSanPhamArrayList.add(new DanhMucSanPham(4, "Bánh ngọt"));

        danhMucSanPhamArrayAdapter = new ArrayAdapter<DanhMucSanPham>(getActivity(),
                R.layout.spinner_item,
                danhMucSanPhamArrayList);
        danhMucSanPhamArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerDanhMuc.setAdapter(danhMucSanPhamArrayAdapter);
        spinnerDanhMuc.setSelection(0);

        updateView();

        edtTenSanPham.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtTenSanPham.hasFocus()) {
                    foodOrder.setName(s.toString());
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
        edtMoTa.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (edtMoTa.hasFocus()) {
                    foodOrder.setDesc(s.toString());
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

        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setCancelable(true);
                builder.setTitle("Xác nhận thêm sản phẩm");
                builder.setMessage("Có chắc muốn thêm mới sản phẩm này?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                themSanPham();
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

        btnUploadHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
        spinnerDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnThemSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KichThuocSanPham kichThuocSanPham = new KichThuocSanPham();
                kichThuocSanPham.setMaSanPham(foodOrder.getID());
                kichThuocSanPham.setTenKichThuoc("");
                kichThuocSanPham.setGiaTien(0);
                kichThuocSanPhamList.add(kichThuocSanPham);
                kichThuocSanPhamAdapter.notifyDataSetChanged();
            }
        });
        btnThemTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toppingSanPhamList.add(new ToppingSanPham());
                toppingSanPhamAdapter.notifyDataSetChanged();
            }
        });
        v.setDrawingCacheEnabled(false);
        return v;
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        DanhMucSanPham danhMucSanPham = (DanhMucSanPham) adapter.getItem(position);
        foodOrder.setMaDanhMuc(danhMucSanPham.getMaDanhMuc());
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == mainActivity.RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            Uri filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                mainActivity.getContentResolver(),
                                filePath);
                uploadBitmap(bitmap);

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    private void themSanPham() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/sanpham/them-moi";

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("tenSanPham", foodOrder.getName());
            jsonBody.put("hinhAnh", foodOrder.getHinhAnh());
            jsonBody.put("moTa", foodOrder.getDesc());
            jsonBody.put("maDanhMuc", foodOrder.getMaDanhMuc());
            JSONArray sizeSanPhamArray = new JSONArray();
            for (KichThuocSanPham item : kichThuocSanPhamList
            ) {
                JSONObject kichThuocItemObj = new JSONObject();
                kichThuocItemObj.put("MaSanPham", item.getMaSanPham());
                kichThuocItemObj.put("TenKichThuoc", item.getTenKichThuoc());
                kichThuocItemObj.put("GiaTien", item.getGiaTien());
                sizeSanPhamArray.put(kichThuocItemObj);
            }

            JSONArray toppingSanPhamArray = new JSONArray();
            for (ToppingSanPham item : toppingSanPhamList
            ) {
                JSONObject toppingItemObj = new JSONObject();
                toppingItemObj.put("MaSanPham", item.getMaSanPham());
                toppingItemObj.put("TenTopping", item.getTenTopping());
                toppingItemObj.put("GiaTien", item.getGiaTien());
                toppingSanPhamArray.put(toppingItemObj);
            }
            jsonBody.put("kichThuoc", sizeSanPhamArray);
            jsonBody.put("topping", toppingSanPhamArray);


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

    private void xoaSanPham() {
        try {
            String url = getString(R.string.endpoint_server) + "/admin/sanpham/xoa-san-pham";
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maSanPham", foodOrder.getID());
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


    private void uploadBitmap(final Bitmap bitmap) {
        String url = getString(R.string.endpoint_server) + "/admin/up-anh";
        ProgressDialog progressDialog
                = new ProgressDialog(mainActivity);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(mainActivity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            foodOrder.setHinhAnh(obj.getString("link"));
                            Picasso.get()
                                    .load(foodOrder.getHinhAnh())
                                    .fit()
                                    .centerInside()
                                    .into(imgFoodDetail);
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

            //            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("tags", tags);
//                return params;
//            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding the request to volley
        Volley.newRequestQueue(mainActivity).add(volleyMultipartRequest);
    }


    // Cập nhật giá tiền khi chọn topping
    public void onUpdateLichSuOrder() {
        int thanhTien = lichSuOrder.getSoLuong() * (lichSuOrder.getGiaTienKichThuoc() + lichSuOrder.getGiaTienTopping());
        lichSuOrder.setThanhTien(thanhTien);
        btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
    }

    public void updateView() {
        if (foodOrder != null) {
            Picasso.get()
                    .load(foodOrder.getHinhAnh())
                    .fit()
                    .centerInside()
                    .into(imgFoodDetail);
            edtTenSanPham.setText(foodOrder.getName());
            edtMoTa.setText(foodOrder.getDesc());
            toppingSanPhamList.clear();
            toppingSanPhamList.addAll(foodOrder.getToppingSanPhams());
            kichThuocSanPhamList.clear();
            kichThuocSanPhamList.addAll(foodOrder.getKichThuocSanPhamList());
            toppingSanPhamAdapter.notifyDataSetChanged();
            kichThuocSanPhamAdapter.notifyDataSetChanged();
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
