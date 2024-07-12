package com.example.langthangcoffee.fragments;

import static com.example.langthangcoffee.R.color;
import static com.example.langthangcoffee.R.drawable;
import static com.example.langthangcoffee.R.id;
import static com.example.langthangcoffee.R.layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.langthangcoffee.activities.MainActivity;
import com.example.langthangcoffee.adapters.FoodOrderAdapter;
import com.example.langthangcoffee.models.FoodOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class DanhSachYeuThichFragment extends Fragment {
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
        mainActivity = (MainActivity) getActivity();

        list.clear();
        ViewGroup v = (ViewGroup) inflater.inflate(layout.fragment_trang_order_yeu_thich, container, false);

        recyclerView = v.findViewById(id.rcv_order);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        foodOrderAdapter = new FoodOrderAdapter(list);
        recyclerView.setAdapter(foodOrderAdapter);
        getListFoodOrder();
        imgBack = v.findViewById(id.img_back);

        searchButton = v.findViewById(id.btn_search_food_order);

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

        defText = ColorStateList.valueOf(getResources().getColor(color.black));
        // Search Food
        searchFoodOrder = v.findViewById(id.edt_search_item);
        searchFoodOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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
                    Filter(s.toString());
                }
            }
        });
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xoá nội dung của EditText
                searchFoodOrder.setText("");
            }
        });


        return v;
    }

    private void Filter(String text) {
        for (FoodOrder item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        recyclerView.setAdapter(new FoodOrderAdapter(filterList));
        foodOrderAdapter.notifyDataSetChanged();
    }

    public void getListFoodOrder() {
        try {
            String url = getString(R.string.endpoint_server) + "/sanphamyeuthich/get-danh-sach";

            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", mainActivity.getTaiKhoan().getSdtTaiKhoan());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray datasArray = jsonObject.getJSONArray("data");
                                List<FoodOrder> listOrder = new ArrayList<>();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < datasArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject sanPhamObj = datasArray.getJSONObject(i);
                                    FoodOrder foodOrder = new FoodOrder();
                                    foodOrder.setHinhAnh(sanPhamObj.getString("HinhAnh"));
                                    foodOrder.setDesc(sanPhamObj.getString("MoTa"));
                                    foodOrder.setName(sanPhamObj.getString("TenSanPham"));
                                    foodOrder.setID(sanPhamObj.getInt("MaSanPham"));
                                    foodOrder.setType(sanPhamObj.getInt("MaDanhMuc"));
                                    foodOrder.setPrice(sanPhamObj.getInt("GiaTien"));
                                    foodOrder.setFavorite(true);
                                    listOrder.add(foodOrder);
                                }
                                list.addAll(listOrder);
                                foodOrderAdapter.notifyDataSetChanged();
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
