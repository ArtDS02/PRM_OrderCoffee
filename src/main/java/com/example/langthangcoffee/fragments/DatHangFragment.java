package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.color;
import static com.example.langthangcoffee.R.drawable;
import static com.example.langthangcoffee.R.drawable.custom_btn_deep_orange_radius_10dp;
import static com.example.langthangcoffee.R.drawable.custom_btn_pale_cream_radius_10dp;
import static com.example.langthangcoffee.R.id;
import static com.example.langthangcoffee.R.layout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.langthangcoffee.R;
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.adapters.FoodOrderAdapter;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.FoodOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class DatHangFragment extends Fragment implements View.OnClickListener {
    private final int POS_TYPE_CAFE = 0;
    private int POS_TYPE_TRASUA = 0;
    private int POS_TYPE_SODA = 0;
    private int POS_TYPE_SWEETCAKE = 0;
    private RecyclerView recyclerView;
    private FoodOrderAdapter foodOrderAdapter;
    private EditText searchFoodOrder;
    private List<FoodOrder> filterList = new ArrayList<>();
    private List<FoodOrder> list = new ArrayList<>();
    private ColorStateList defText;
    private Button item1, item2, item3, item4, searchButton, priceButton;
    private TextView tvQuantity, tvQuantityOrder;
    GridLayoutManager gridLayoutManager;
    ImageView imgBack;

    public MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Khởi tạo activity chính
        mainActivity = (MainActivity) getActivity();

        list.clear();
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_order, container, false);

        // Khởi tạo các thành phần giao diện
        recyclerView = v.findViewById(id.rcv_order);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set các sản phẩm sẽ hiện trong recyclerView
        foodOrderAdapter = new FoodOrderAdapter(list);
        recyclerView.setAdapter(foodOrderAdapter);

        getListFoodOrder();

        imgBack = v.findViewById(id.img_back);
        item1 = v.findViewById(id.btn_caffe_selected);
        item2 = v.findViewById(id.btn_milktea_selected);
        item3 = v.findViewById(id.btn_soda_selected);
        item4 = v.findViewById(id.btn_sweetcake_selected);
        searchButton = v.findViewById(id.btn_search_food_order);
        priceButton = v.findViewById(id.btn_price);

        tvQuantityOrder = v.findViewById(id.tv_quantity_order);

        // Cập nhật giao diện dựa trên tài khoản và đơn hàng hiện tại
        if (mainActivity.getTaiKhoan() != null) {
            DonHang getDonHang = mainActivity.getDonHang();
            if (getDonHang != null) {
                priceButton.setText(String.valueOf(getDonHang.getThanhTien()) + " đ");
                tvQuantityOrder.setText(String.valueOf(getDonHang.getLichSuOrderList().size()));

            } else {
                priceButton.setText("0 đ");
                tvQuantityOrder.setText("0");
            }
            Log.i("DatHang", "Reload");
        }

        // Thiết lập sự kiện click cho các nút (4 nút cho 4 loại sản phẩm)
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        item4.setOnClickListener(this);

        // Sự kiện khi bấm nút back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() == null) {
                    DashBoardFragment dashBoardFragment = new DashBoardFragment();
                    mainActivity.loadFragment(dashBoardFragment);
                } else {
                    getFragmentManager().popBackStack();
                }
            }
        });

        // Sự kiện khi bấm nút thanh toán
        defText = ColorStateList.valueOf(getResources().getColor(R.color.black));
        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.getTaiKhoan() == null) {
                    SigninFragment signinFragment = new SigninFragment();
                    mainActivity.loadFragment(signinFragment);
                } else {
                    XacNhanDatHangFragment xacNhanDatHangFragment = new XacNhanDatHangFragment();
                    mainActivity.loadFragment(xacNhanDatHangFragment);
                }
            }
        });

        // Thiết lập sự kiện cho thanh tìm kiếm
        searchFoodOrder = v.findViewById(id.edt_search_item);
        searchFoodOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // Khi ô search được điền thì thay đổi biểu tượng (searh/xóa)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    searchButton.setEnabled(false);
                    searchButton.setBackgroundResource(drawable.ic_search);
                } else {
                    searchButton.setEnabled(true);
                    searchButton.setBackgroundResource(drawable.ic_close);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList.clear();
                if (s.toString().isEmpty()) {
                    recyclerView.setAdapter(foodOrderAdapter);
                    foodOrderAdapter.notifyDataSetChanged();
                } else {

                    // Gọi hàm search theo tên sản phẩm
                    Filter(s.toString());
                }
            }
        });

        // ẩn bàn phím sau khi search
        searchFoodOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Ẩn bàn phím sau khi người dùng nhập xong
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchFoodOrder.getWindowToken(), 0);
                    searchFoodOrder.clearFocus();
                    return true;
                }
                return false;
            }
        });

        // Click nút xóa thì xóa nội dung ô search
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xoá nội dung của EditText
                searchFoodOrder.setText("");
            }
        });


        return v;
    }

    // Lọc sản phẩm theo tên sản phẩm được nhập
    private void Filter(String text) {
        for (FoodOrder item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        recyclerView.setAdapter(new FoodOrderAdapter(filterList));
        foodOrderAdapter.notifyDataSetChanged();
    }

    // GET danh sách các sản phẩm qua API
    private void getListFoodOrder() {
        String url = getString(R.string.endpoint_server) + "/sanpham";
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
                            List<FoodOrder> danhSachSanPhamYeuThich = mainActivity.getDanhSachSanPhamYeuThich();

                            //now looping through all the elements of the json array
                            for (int i = 0; i < datasArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = datasArray.getJSONObject(i);
                                FoodOrder foodOrder = new FoodOrder();
                                foodOrder.setHinhAnh(jsonObject.getString("HinhAnh"));
                                foodOrder.setDesc(jsonObject.getString("MoTa"));
                                foodOrder.setName(jsonObject.getString("TenSanPham"));
                                foodOrder.setID(jsonObject.getInt("MaSanPham"));
                                foodOrder.setType(jsonObject.getInt("MaDanhMuc"));
                                foodOrder.setPrice(jsonObject.getInt("GiaTien"));
                                foodOrder.setFavorite(checkFoodIsFavorite(danhSachSanPhamYeuThich, foodOrder));
                                list.add(foodOrder);

                            }
                            foodOrderAdapter.notifyDataSetChanged();
                            updatePosition(list);
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

    // Kiểm tra sản phẩm có nằm trong danh sách favorite không
    private Boolean checkFoodIsFavorite(List<FoodOrder> list, FoodOrder foodOrder) {
        for (FoodOrder item : list
        ) {
            if (item.getID() == foodOrder.getID()) {
                return true;
            }

        }
        return false;
    }

    private void updatePosition(List<FoodOrder> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 2) {
                POS_TYPE_SODA = i;
                break;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 3) {
                POS_TYPE_TRASUA = i;
                break;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 4) {
                POS_TYPE_SWEETCAKE = i;
                break;
            }
        }
    }

    // Set màu cho mục sản phẩm được chọn
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (v.getId() == id.btn_caffe_selected) {
            item1.setTextColor(getResources().getColor(color.white));
            item2.setTextColor(defText);
            item3.setTextColor(defText);
            item4.setTextColor(defText);

            item1.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
            item2.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item3.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item4.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

            scrollToItem(POS_TYPE_CAFE);
        } else if (v.getId() == id.btn_milktea_selected) {
            item1.setTextColor(defText);
            item2.setTextColor(getResources().getColor(color.white));
            item3.setTextColor(defText);
            item4.setTextColor(defText);

            item1.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item2.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
            item3.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item4.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

            scrollToItem(POS_TYPE_TRASUA);
        } else if (v.getId() == id.btn_soda_selected) {
            item1.setTextColor(defText);
            item2.setTextColor(defText);
            item3.setTextColor(getResources().getColor(color.white));
            item4.setTextColor(defText);

            item1.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item2.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item3.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));
            item4.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));

            scrollToItem(POS_TYPE_SODA);
        } else if (v.getId() == id.btn_sweetcake_selected) {
            item1.setTextColor(defText);
            item2.setTextColor(defText);
            item3.setTextColor(defText);
            item4.setTextColor(getResources().getColor(color.white));

            item1.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item2.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item3.setBackgroundDrawable(getResources().getDrawable(custom_btn_pale_cream_radius_10dp));
            item4.setBackgroundDrawable(getResources().getDrawable(custom_btn_deep_orange_radius_10dp));

            scrollToItem(POS_TYPE_SWEETCAKE);
        }
    }

    public void scrollToItem(int position) {
        if (gridLayoutManager == null)
            return;
        gridLayoutManager.scrollToPositionWithOffset(position, 0);
    }
}
