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
import com.example.langthangcoffee.adapters.FoodOrderSizeAdapter;
import com.example.langthangcoffee.adapters.FoodOrderToppingAdapter;
import com.example.langthangcoffee.clicklisteners.ItemClickListener;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.FoodOrder;
import com.example.langthangcoffee.models.FoodOrderSize;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DatHangChiTietFragment extends Fragment implements FoodOrderToppingAdapter.EventListener {
    private List<FoodOrderSize> listFoodOrderSize = new ArrayList<>();
    private List<FoodOrderTopping> listFoodOrderTopping = new ArrayList<>();
    private int maSanPham = 0;
    private RecyclerView recyclerView, recyclerView2;
    private FoodOrderSizeAdapter foodOrderSizeAdapter;
    private FoodOrderToppingAdapter foodOrderToppingAdapter;
    GridLayoutManager gridLayoutManager, gridLayoutManager2;
    ItemClickListener itemClickListener;
    ImageView imgFoodDetail;
    TextView tvFoodDetailName;
    TextView tvFoodDetailPrice;
    TextView tvFoodDetailDesc;
    Button btnPriceDetail;
    EditText edtGhiChu;
    TextView tvQuantity;
    TextView tvQuantityOrder;
    Button btnPlusFood;
    Button btnSubtractFood, btnFavorite;


    LichSuOrder lichSuOrder = null;
    MainActivity mainActivity;
    FoodOrder foodOrder = new FoodOrder();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_trang_order_chitiet, container, false);
        mainActivity = (MainActivity) getActivity();

        // Liên kết các view từ layout với code
        imgFoodDetail = v.findViewById(R.id.img_info_detail);
        tvFoodDetailName = v.findViewById(R.id.tv_info_item_name);
        tvFoodDetailDesc = v.findViewById(R.id.tv_info_item_desc);
        tvFoodDetailPrice = v.findViewById(R.id.tv_info_item_price);
        btnPriceDetail = v.findViewById(R.id.btn_price_detail);
        tvQuantity = v.findViewById(R.id.tv_quantity);
        btnPlusFood = v.findViewById(R.id.btn_plus);
        btnSubtractFood = v.findViewById(R.id.btn_subtract);
        edtGhiChu = v.findViewById(R.id.edt_note);
        tvQuantityOrder = v.findViewById(R.id.tv_quantity_order);
        btnFavorite = v.findViewById(R.id.btn_heart_symbol_order);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int idSanPham = bundle.getInt("IDSanPham", 0);
            maSanPham = idSanPham;

            // Gọi hàm lấy chi tiết đơn hàng
            getFoodOrderDetail();
            lichSuOrder = new LichSuOrder();


            // Khởi tạo listener cho item click
            itemClickListener = new ItemClickListener() {
                @Override
                public void onClick(JSONObject jsonObject) throws JSONException {

                    // Thông báo adapter cập nhật dữ liệu
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            foodOrderSizeAdapter.notifyDataSetChanged();
                        }
                    });

                    // Cập nhật size và giá tiền lịch sử order
                    String tenKichThuoc = jsonObject.getString("tenKichThuoc");
                    int giaTien = jsonObject.getInt("giaTien");


                    int thanhTien = (lichSuOrder.getThanhTien() - lichSuOrder.getSoLuong() * lichSuOrder.getGiaTienKichThuoc()) + (lichSuOrder.getSoLuong() * giaTien);
                    lichSuOrder.setKichThuoc(tenKichThuoc);
                    lichSuOrder.setGiaTienKichThuoc(giaTien);
                    lichSuOrder.setThanhTien(thanhTien);
                    btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");


                }
            };

            // Khởi tạo RecyclerView và adapter cho kích thước món ăn
            recyclerView = v.findViewById(R.id.rcv_select_size);
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            foodOrderSizeAdapter = new FoodOrderSizeAdapter(listFoodOrderSize, itemClickListener);
            recyclerView.setAdapter(foodOrderSizeAdapter);

            // Khởi tạo RecyclerView và adapter cho topping món ăn
            recyclerView2 = v.findViewById(R.id.rcv_select_topping);
            gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
            recyclerView2.setLayoutManager(gridLayoutManager2);
            foodOrderToppingAdapter = new FoodOrderToppingAdapter(listFoodOrderTopping, lichSuOrder, this);
            recyclerView2.setAdapter(foodOrderToppingAdapter);

            // Xử lý sự kiện tăng số lượng món ăn
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

            // Xử lý sự kiện giảm số lượng món ăn
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

            // Xử lý sự kiện thêm món ăn yêu thích
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check login
                    if (mainActivity.getTaiKhoan() == null) {
                        SigninFragment signinFragment = new SigninFragment();
                        mainActivity.loadFragment(signinFragment);
                        return;
                    }
                    handleClickFavoriteFood(foodOrder, btnFavorite);
                }
            });

            // Xử lý sự kiện nhấn nút đặt hàng
            btnPriceDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check login
                    if (mainActivity.getTaiKhoan() == null) {
                        SigninFragment signinFragment = new SigninFragment();
                        mainActivity.loadFragment(signinFragment);
                        return;
                    }

                    // Update lich su order
                    String ghiChu = edtGhiChu.getText().toString();
                    lichSuOrder.setGhiChu(ghiChu);


                    // Thêm lịch sử order vào cơ sở dữ liệu
                    themLichSuOrderDatabase();
                }
            });


        }

        return v;
    }


    // Xử lý sự kiện yêu thích món ăn
    public void handleClickFavoriteFood(FoodOrder foodOrder, Button btnFavorite) {
        try {
            String url = mainActivity.getString(R.string.endpoint_server) + "/sanphamyeuthich/update";
            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", mainActivity.getTaiKhoan().getSdtTaiKhoan());
            jsonBody.put("MaSanPham", foodOrder.getID());

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int result = jsonObject.getInt("data");
                                if (result == 1) {
                                    // tao yeu thich
                                    foodOrder.setFavorite(true);
                                    mainActivity.getDanhSachSanPhamYeuThich().add(foodOrder);
                                } else if (result == 0) {
                                    // xoa yeu thich
                                    foodOrder.setFavorite(false);
                                    mainActivity.getDanhSachSanPhamYeuThich().removeIf(item -> item.getID() == foodOrder.getID());
                                }
                                capNhatHinhAnhFavorite(foodOrder, btnFavorite);


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
                                    Toast.makeText(mainActivity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
    private void capNhatHinhAnhFavorite(FoodOrder foodOrder, Button btnFavorite) {
        int drawableId = foodOrder.getFavorite() ? R.drawable.heart_symbol_checked : R.drawable.heart_symbol_uncheck;
        btnFavorite.setBackgroundResource(drawableId);

    }

    // Thêm lịch sử đơn hàng vào cơ sở dữ liệu
    private void    themLichSuOrderDatabase() {
        try {
            String url =  getString(R.string.endpoint_server) + "/donhang/tao-lich-su-order";
            DonHang getDonHang = mainActivity.getDonHang();
            getDonHang.setThanhTien(getDonHang.getThanhTien() + lichSuOrder.getThanhTien());

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
            jsonBody.put("maDonHang", getDonHang.getMaDonHang());
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
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getActivity(), "Order thành công", Toast.LENGTH_SHORT).show();
                                // Thêm lịch sử order vào đơn hàng
                                lichSuOrder.setMaLichSuOrder(jsonObject.getInt("data"));
                                List<LichSuOrder> lichSuOrderList = getDonHang.getLichSuOrderList();
                                lichSuOrderList.add(lichSuOrder);
                                getDonHang.setLichSuOrderList(lichSuOrderList);


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

    // Cập nhật giá tiền khi chọn topping
    public void onUpdateLichSuOrder() {
        int thanhTien = lichSuOrder.getSoLuong() * (lichSuOrder.getGiaTienKichThuoc() + lichSuOrder.getGiaTienTopping());
        lichSuOrder.setThanhTien(thanhTien);
        btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgBack = (ImageView) view.findViewById(R.id.img_back);

        // Sự kiện khi bấm nuút back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    // Lấy thông tin chi tiết của sản phẩm từ API
    private void getFoodOrderDetail() {
        try {
            String url = getString(R.string.endpoint_server) + "/sanpham/chitiet";

            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maSanPham", maSanPham);
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
                                lichSuOrder.setFoodOrderSizeList(listFoodOrderSize);
                                foodOrderSizeAdapter.notifyDataSetChanged();
                                List<FoodOrder> danhSachYeuThich = mainActivity.getDanhSachSanPhamYeuThich();

                                // Item
                                dataArray = obj.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    foodOrder.setHinhAnh(jsonObject.getString("HinhAnh"));
                                    foodOrder.setDesc(jsonObject.getString("MoTa"));
                                    foodOrder.setName(jsonObject.getString("TenSanPham"));
                                    foodOrder.setID(jsonObject.getInt("MaSanPham"));
                                    foodOrder.setType(jsonObject.getInt("MaDanhMuc"));
                                    foodOrder.setPrice(jsonObject.getInt("GiaTien"));
                                    foodOrder.setFavorite(checkFoodIsFavorite(danhSachYeuThich, foodOrder));
                                    Picasso.get()
                                            .load(foodOrder.getHinhAnh())
                                            .fit()
                                            .centerInside()
                                            .into(imgFoodDetail);

                                    tvFoodDetailName.setText(foodOrder.getName());
                                    tvFoodDetailDesc.setText(foodOrder.getDesc());
                                    tvFoodDetailPrice.setText(String.valueOf(foodOrder.getPrice()) + " đ");

                                    // Create instance Lich su order
                                    lichSuOrder.setTenSanPham(foodOrder.getName());

                                    lichSuOrder.setMaSanPham(foodOrder.getID());
                                    lichSuOrder.setKichThuoc(jsonObject.getString("TenKichThuoc"));
                                    lichSuOrder.setGiaTienKichThuoc(foodOrder.getPrice());
                                    lichSuOrder.setSoLuong(1);
                                    int thanhTien = lichSuOrder.getSoLuong() * lichSuOrder.getGiaTienKichThuoc();
                                    lichSuOrder.setThanhTien(thanhTien);
                                    tvQuantity.setText(String.valueOf(lichSuOrder.getSoLuong()));
                                    btnPriceDetail.setText(String.valueOf(lichSuOrder.getThanhTien()) + " đ");
                                    capNhatHinhAnhFavorite(foodOrder, btnFavorite);
                                }

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
                                foodOrderToppingAdapter.notifyDataSetChanged();

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
    private Boolean checkFoodIsFavorite(List<FoodOrder> list, FoodOrder foodOrder) {
        for (FoodOrder item : list
        ) {
            if (item.getID() == foodOrder.getID()) {
                return true;
            }

        }
        return false;
    }

}
