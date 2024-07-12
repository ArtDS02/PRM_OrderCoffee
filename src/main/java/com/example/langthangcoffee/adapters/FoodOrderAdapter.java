package com.example.langthangcoffee.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.langthangcoffee.fragments.DatHangChiTietFragment;
import com.example.langthangcoffee.fragments.SigninFragment;
import com.example.langthangcoffee.models.FoodOrder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

@SuppressWarnings("deprecation")
public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.FoodOrderViewHolder> {

    // Danh sách sản phẩm (FoodOrder) để hiển thị trong RecyclerView
    private List<FoodOrder> mListFoodOrder;

    // Biến lưu số lượng sản phẩm
    private int mQuantity = 0;
    private MainActivity mainActivity;
    public FoodOrderAdapter(List<FoodOrder> mListFoodOrder) {
        this.mListFoodOrder = mListFoodOrder; // Khởi tạo danh sách sản phẩm từ tham số truyền vào
    }

    @NonNull
    @Override
    public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Tạo một view mới từ layout item_food_order.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_order, parent, false);
        mainActivity = (MainActivity) parent.getContext(); // Lấy context của MainActivity
        return new FoodOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final FoodOrder foodOrder = mListFoodOrder.get(position); // Lấy đối tượng FoodOrder tại vị trí position
        if (foodOrder == null) {
            return;
        }

        // Sử dụng Picasso để tải hình ảnh sản phẩm
        Picasso.get()
                .load(foodOrder.getHinhAnh())
                .fit()
                .centerInside()
                .into(holder.imageView);

        // Set tên và giá sản phẩm
        holder.tvName.setText(foodOrder.getName());
        holder.tvPrice.setText(String.valueOf(foodOrder.getPrice()) + " đ");

        // Sự kiện click vào tên sản phẩm để chuyển đến trang chi tiết
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity myActivity = (MainActivity) v.getContext();
                DatHangChiTietFragment datHangChiTietFragment = new DatHangChiTietFragment();

                // Truyền ID sản phẩm đến trang chi tiết
                Bundle bundle = new Bundle();
                bundle.putInt("IDSanPham", foodOrder.getID());
                datHangChiTietFragment.setArguments(bundle);

                // Mở trang chi tiết
                myActivity.loadFragment(datHangChiTietFragment);
            }
        });

        // Cập nhật hình ảnh biểu tượng yêu thích dựa trên trạng thái yêu thích của sản phẩm
        capNhatHinhAnhFavorite(foodOrder, holder.btnFavorite);

        // Sự kiện click vào nút yêu thích
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra đăng nhập
                if (mainActivity.getTaiKhoan() == null) {
                    SigninFragment signinFragment = new SigninFragment();

                    // Mở fragment đăng nhập nếu chưa đăng nhập
                    mainActivity.loadFragment(signinFragment);
                    return;
                }
                handleClickFavoriteFood(foodOrder, holder.btnFavorite, position); // Xử lý sự kiện yêu thích sản phẩm
            }

        });


    }

    // Phương thức cập nhật hình ảnh biểu tượng yêu thích dựa trên trạng thái yêu thích của sản phẩm
    private void capNhatHinhAnhFavorite(FoodOrder foodOrder, Button btnFavorite) {
        int drawableId = foodOrder.getFavorite() ? R.drawable.heart_symbol_checked : R.drawable.heart_symbol_uncheck;
        btnFavorite.setBackgroundResource(drawableId);

    }

    // Phương thức xử lý sự kiện click vào nút yêu thích sản phẩm
    public void handleClickFavoriteFood(FoodOrder foodOrder, Button btnFavorite, int position) {
        try {
            String url = mainActivity.getString(R.string.endpoint_server) + "/sanphamyeuthich/update";
            final ProgressDialog progressDialog = new ProgressDialog(mainActivity); // Tạo ProgressDialog
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", mainActivity.getTaiKhoan().getSdtTaiKhoan()); // Thêm số điện thoại tài khoản vào JSON body
            jsonBody.put("MaSanPham", foodOrder.getID()); // Thêm ID sản phẩm vào JSON body

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
                                notifyItemChanged(position);

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


    // Lấy số lượng sản phẩm
    public int getQuantity() {
        return mQuantity;
    }

    @Override
    public int getItemCount() {
        if (mListFoodOrder != null) {
            return mListFoodOrder.size(); // Trả về số lượng sản phẩm trong danh sách
        }
        return 0;
    }

    public static class FoodOrderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView; // ImageView hiển thị hình ảnh sản phẩm
        private final TextView tvName; // TextView hiển thị tên sản phẩm
        private final TextView tvPrice; // TextView hiển thị giá sản phẩm
        private final Button btnFavorite; // Button yêu thích sản phẩm


        public FoodOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_food_order);
            tvName = itemView.findViewById(R.id.tv_food_name_order);
            tvPrice = itemView.findViewById(R.id.tv_food_price_order);
            btnFavorite = itemView.findViewById(R.id.btn_heart_symbol_order);


        }
    }
}

