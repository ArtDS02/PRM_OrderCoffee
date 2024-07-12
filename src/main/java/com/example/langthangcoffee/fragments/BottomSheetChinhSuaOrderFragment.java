package com.example.langthangcoffee.fragments;

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
import com.example.langthangcoffee.adapters.FoodOrderSizeEditAdapter;
import com.example.langthangcoffee.adapters.FoodOrderToppingEditAdapter;
import com.example.langthangcoffee.clicklisteners.ItemClickListener;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.FoodOrderSize;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottomSheetChinhSuaOrderFragment extends BottomSheetDialogFragment implements FoodOrderToppingEditAdapter.EventListener {
    ImageView imgClose;
    private List<FoodOrderSize> listFoodOrderSize = new ArrayList<>();
    private List<FoodOrderTopping> listFoodOrderTopping = new ArrayList<>();

    BottomSheetChinhSuaOrderFragment bottomSheetChinhSuaOrderFragment;
    XacNhanDatHangFragment xacNhanDatHangFragment;
    TextView tvOrderName, tvQuantity;
    Button btnPriceDetail;
    EditText edtNote;
    ItemClickListener itemClickListener;
    private RecyclerView recyclerView, recyclerView2;
    private FoodOrderSizeEditAdapter foodOrderSizeEditAdapter;
    private FoodOrderToppingEditAdapter foodOrderToppingEditAdapter;
    GridLayoutManager gridLayoutManager, gridLayoutManager2;
    MainActivity mainActivity;
    DonHang donHang;
    LichSuOrder lichSuOrder;

    TextView tvQuantityOrder;
    Button btnPlusFood;
    Button btnSubtractFood;
    Button btnDeleteOrder;

    public BottomSheetChinhSuaOrderFragment(XacNhanDatHangFragment xacNhanDatHangFragment) {
        // Required empty public constructor
        bottomSheetChinhSuaOrderFragment = this;
        this.xacNhanDatHangFragment = xacNhanDatHangFragment;

    }

    public void setLichSuOrder(LichSuOrder lichSuOrder) {
        this.lichSuOrder = new LichSuOrder(lichSuOrder);

    }

    public void onUpdateLichSuOrder() {
        int thanhTien = lichSuOrder.getSoLuong() * (lichSuOrder.getGiaTienKichThuoc() + lichSuOrder.getGiaTienTopping());
        lichSuOrder.setThanhTien(thanhTien);
        btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_bottom_sheet_dialog_chinhsua_order, container, false);
        tvOrderName = v.findViewById(R.id.tv_order_name);
        tvQuantity = v.findViewById(R.id.tv_quantity);
        btnPriceDetail = v.findViewById(R.id.btn_price_detail);
        edtNote = v.findViewById(R.id.edt_note);
        btnDeleteOrder = v.findViewById(R.id.btn_delete_order);
        btnPlusFood = v.findViewById(R.id.btn_plus);
        btnSubtractFood = v.findViewById(R.id.btn_subtract);


        mainActivity = (MainActivity) getActivity();
        donHang = mainActivity.getDonHang();


        updateEditOrderView();

        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(JSONObject jsonObject) throws JSONException {
                // Notify adapter
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        foodOrderSizeEditAdapter.notifyDataSetChanged();
                    }
                });
                // Cập nhật size lịch sử order
                String tenKichThuoc = jsonObject.getString("tenKichThuoc");
                int giaTien = jsonObject.getInt("giaTien");
                int thanhTien = (lichSuOrder.getThanhTien() - lichSuOrder.getSoLuong() * lichSuOrder.getGiaTienKichThuoc()) + (lichSuOrder.getSoLuong() * giaTien);
                lichSuOrder.setKichThuoc(tenKichThuoc);
                lichSuOrder.setGiaTienKichThuoc(giaTien);
                lichSuOrder.setThanhTien(thanhTien);
                btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");


            }
        };
        recyclerView = v.findViewById(R.id.rcv_select_size);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        foodOrderSizeEditAdapter = new FoodOrderSizeEditAdapter(listFoodOrderSize, itemClickListener, lichSuOrder);
        recyclerView.setAdapter(foodOrderSizeEditAdapter);

        recyclerView2 = v.findViewById(R.id.rcv_select_topping);
        gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        foodOrderToppingEditAdapter = new FoodOrderToppingEditAdapter(listFoodOrderTopping, lichSuOrder, this);
        recyclerView2.setAdapter(foodOrderToppingEditAdapter);

        getFoodOrderDetail();


        btnPlusFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichSuOrder.setSoLuong(lichSuOrder.getSoLuong() + 1);
                tvQuantity.setText(String.valueOf(lichSuOrder.getSoLuong()));
                int thanhTien = lichSuOrder.getSoLuong() * (lichSuOrder.getGiaTienKichThuoc() + lichSuOrder.getGiaTienTopping());
                lichSuOrder.setThanhTien(thanhTien);
                btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");


            }
        });
        btnSubtractFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lichSuOrder.getSoLuong() > 0) {
                    lichSuOrder.setSoLuong(lichSuOrder.getSoLuong() - 1);
                    tvQuantity.setText(String.valueOf(lichSuOrder.getSoLuong()));
                    int thanhTien = lichSuOrder.getSoLuong() * (lichSuOrder.getGiaTienKichThuoc() + lichSuOrder.getGiaTienTopping());
                    lichSuOrder.setThanhTien(thanhTien);
                    btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");

                }
            }
        });

        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodOrderDetail();
            }
        });
        btnPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFoodOrderDetail();

            }
        });
        return v;
    }

    private void updateFoodOrderDetail() {
        try {
            String url = getString(R.string.endpoint_server) + "/donhang/update-lich-su-order";
            lichSuOrder.setGhiChu(edtNote.getText().toString());
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (FoodOrderTopping item : lichSuOrder.getFoodOrderToppingList()
            ) {
                JSONObject jsObj = new JSONObject();
                jsObj.put("TenTopping", item.getTenTopping());
                jsObj.put("GiaTien", item.getGiaTien());
                jsonArray.put(jsObj);
            }
            jsonBody.put("maLichSuOrder", lichSuOrder.getMaLichSuOrder());
            jsonBody.put("maDonHang", donHang.getMaDonHang());
            jsonBody.put("maSanPham", lichSuOrder.getMaSanPham());
            jsonBody.put("kichThuoc", lichSuOrder.getKichThuoc());
            jsonBody.put("giaTienKichThuoc", lichSuOrder.getGiaTienKichThuoc());
            jsonBody.put("ghiChu", lichSuOrder.getGhiChu());
            jsonBody.put("soLuong", lichSuOrder.getSoLuong());
            jsonBody.put("thanhTien", lichSuOrder.getThanhTien());
            jsonBody.put("topping", jsonArray);

            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completion

                            try {
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                donHang.setMaVoucher("");
                                donHang.setThanhTien(obj.getInt("thanhTienDonHang"));
                                donHang.setSoTienThanhToan(donHang.getThanhTien());
                                List<LichSuOrder> lichSuOrderDonHangList = donHang.getLichSuOrderList();
                                lichSuOrderDonHangList.removeIf(item -> item.getMaLichSuOrder() == lichSuOrder.getMaLichSuOrder());
                                lichSuOrderDonHangList.add(lichSuOrder);
                                // Sort LichSuOrder by MaLichSuOrder ASC
                                Collections.sort(lichSuOrderDonHangList);
// Update lich su order XacNhanDatHang
                                xacNhanDatHangFragment.updateLichSuOrderList(lichSuOrderDonHangList);

                                xacNhanDatHangFragment.updateDonHangView();

                                progressDialog.dismiss();
                                dismiss();

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

    private void deleteFoodOrderDetail() {
        try {
            String url = getString(R.string.endpoint_server) + "/donhang/delete-lich-su-order";

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maLichSuOrder", lichSuOrder.getMaLichSuOrder());
            jsonBody.put("maDonHang", donHang.getMaDonHang());
            final String requestBody = jsonBody.toString();
            //creating a string request to send request to the url
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //hiding the progressbar after completion

                            try {
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                donHang.setMaVoucher("");
                                List<LichSuOrder> lichSuOrderDonHangList = donHang.getLichSuOrderList();
                                lichSuOrderDonHangList.removeIf(item -> item.getMaLichSuOrder() == lichSuOrder.getMaLichSuOrder());
                                donHang.setLichSuOrderList(lichSuOrderDonHangList);
                                donHang.setThanhTien(obj.getInt("thanhTien"));
                                donHang.setSoTienThanhToan(donHang.getThanhTien() + donHang.getPhiGiaoHang());
                                xacNhanDatHangFragment.deleteLichSuOrderList(lichSuOrder.getMaLichSuOrder());
                                xacNhanDatHangFragment.updateDonHangView();

                                progressDialog.dismiss();
                                dismiss();

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

    private void getFoodOrderDetail() {
        try {
            String url = getString(R.string.endpoint_server) + "/sanpham/chitiet";

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maSanPham", lichSuOrder.getMaSanPham());
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
                                //we have the array named data  inside the object
                                //so here we are getting that json array

                                // Kich Thuoc
                                JSONArray dataArray = obj.getJSONArray("kichThuoc");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    FoodOrderSize foodOrderSize = new FoodOrderSize();
                                    foodOrderSize.setTenKichThuoc(jsonObject.getString("TenKichThuoc"));
                                    foodOrderSize.setGiaTien(jsonObject.getInt("GiaTien"));
                                    foodOrderSize.setMaSanPham(jsonObject.getInt("MaSanPham"));
                                    listFoodOrderSize.add(foodOrderSize);
                                }
                                foodOrderSizeEditAdapter.notifyDataSetChanged();


                                // Topping
                                dataArray = obj.getJSONArray("topping");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    FoodOrderTopping foodOrderTopping = new FoodOrderTopping();
                                    foodOrderTopping.setTenTopping(jsonObject.getString("TenTopping"));
                                    foodOrderTopping.setGiaTien(jsonObject.getInt("GiaTien"));
                                    foodOrderTopping.setMaSanPham(jsonObject.getInt("MaSanPham"));
                                    listFoodOrderTopping.add(foodOrderTopping);
                                }
                                foodOrderToppingEditAdapter.notifyDataSetChanged();

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

    private void updateEditOrderView() {
        tvOrderName.setText(lichSuOrder.getTenSanPham());
        tvQuantity.setText(String.valueOf(lichSuOrder.getSoLuong()));
        btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
        edtNote.setText(lichSuOrder.getGhiChu());

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgClose = view.findViewById(R.id.img_close_bottom_dialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetChinhSuaOrderFragment.dismiss();
            }


        });
    }
}
