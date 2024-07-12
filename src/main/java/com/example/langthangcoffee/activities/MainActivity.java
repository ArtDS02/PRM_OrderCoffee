package com.example.langthangcoffee.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.langthangcoffee.adapters.DrawerAdapter;
import com.example.langthangcoffee.fragments.DangXuatFragment;
import com.example.langthangcoffee.fragments.DashBoardFragment;
import com.example.langthangcoffee.fragments.DatHangFragment;
import com.example.langthangcoffee.fragments.SigninFragment;
import com.example.langthangcoffee.fragments.TrangCaNhanFragment;
import com.example.langthangcoffee.fragments.TrangQuanLyFragment;
import com.example.langthangcoffee.models.DonHang;
import com.example.langthangcoffee.models.DrawerItem;
import com.example.langthangcoffee.models.FoodOrder;
import com.example.langthangcoffee.models.FoodOrderTopping;
import com.example.langthangcoffee.models.LichSuOrder;
import com.example.langthangcoffee.models.NavigationItem;
import com.example.langthangcoffee.models.SanPham;
import com.example.langthangcoffee.models.TaiKhoan;
import com.example.langthangcoffee.viewmodels.SimpleItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private List<NavigationItem> navigationItemList = new ArrayList<NavigationItem>();
    private SlidingRootNav slidingRootNav;
    private TaiKhoan taiKhoan = null;
    List<FoodOrder> danhSachSanPhamYeuThich = new ArrayList<>();
    private DonHang donHang = null;
    private RecyclerView mDrawerList;
    private DrawerAdapter drawerAdapter;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "ACCOUNT_LOGIN";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {

                }
            });

    // Đảm bảo ứng dụng có thể gửi thông báo đến người dùng
    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {

            } else {

                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo SharedPreferences để lưu trữ thông tin đăng nhập
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        // Thiết lập giao diện toàn màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Thiết lập toolbar
        Toolbar toolbar = findViewById(R.id.tb_activity);
        setSupportActionBar(toolbar);

        // Tạo MENU button (hamburger button)
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        // Thiết lập menu điều hướng
        drawerNavigation();

        // Kiểm tra và duy trì phiên đăng nhập
        keepLoginAccount();

    }

    // Thiết lập menu điều hướng
    public void drawerNavigation() {
        // Thiết lập các mục trong menu
        setNavigationItemList();
        List<DrawerItem> drawerItemList = new ArrayList<DrawerItem>();
        for (NavigationItem item : navigationItemList
        ) {
            DrawerItem drawerItem = createItemFor(item);
            drawerItemList.add(drawerItem);
        }
        drawerAdapter = new DrawerAdapter(drawerItemList);
        drawerAdapter.setListener(this);
        mDrawerList = findViewById(R.id.drawer_list);
        mDrawerList.setNestedScrollingEnabled(false);
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        mDrawerList.setAdapter(drawerAdapter);

        // Mặc định chọn mục đầu tiên
        drawerAdapter.setSelected(1);
    }

    // Phương thức tạo mục cho menu điều hướng
    private DrawerItem createItemFor(NavigationItem navigationItem) {
        return new SimpleItem(navigationItem.getDrawable(), navigationItem.getTitle())
                .withIconTint(color(R.color.deep_orange))
                .withTextTint(color(R.color.dark_sienna))
                .withSelectedIconTint(color(R.color.deep_orange))
                .withSelectedTextTint(color(R.color.deep_orange));
    }

    // Xử lý sự kiện khi chọn mục trong menu
    @Override
    public void onItemSelected(int position) {
        @SuppressLint("CommitTransaction") FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NavigationItem navigationItem = navigationItemList.get(position);
        String key = navigationItem.getKey();
        switch (key) {
            case "close":
                onBackPressed();
                break;
            case "home":
                // Xong
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                loadFragment(dashBoardFragment);
                break;
            case "profile":
                // Xong
                TrangCaNhanFragment trangCaNhanFragment = new TrangCaNhanFragment();
                loadFragment(trangCaNhanFragment);
                break;
            case "cart":
                // Xong
                DatHangFragment datHangFragment = new DatHangFragment();
                loadFragment(datHangFragment);
                break;
            case "signin":
                // Xong
                SigninFragment signinFragment = new SigninFragment();
                loadFragment(signinFragment);
                break;
            case "logout":
                // Xong
                DangXuatFragment dangXuatFragment = new DangXuatFragment();
                loadFragment(dangXuatFragment);
                break;
            case "admin":
                // Xong
                TrangQuanLyFragment trangQuanLyFragment = new TrangQuanLyFragment();
                loadFragment(trangQuanLyFragment);
                break;
            default:
                break;
        }
    }

    // Xử lý sự kiện khi nhấn nút back
    @Override
    public void onBackPressed() {
        finish();
    }

    // Thiết lập danh sách các mục điều hướng
    private void setNavigationItemList() {
        navigationItemList.clear();
        String[] screenTitles = loadScreenTitles();
        String[] screenKeys = loadScreenKeys();
        Drawable[] screenIcons = loadScreenIcons();
        for (int i = 0; i < screenTitles.length; i++) {
            NavigationItem navigationItem = new NavigationItem();
            navigationItem.setDrawable(screenIcons[i]);
            navigationItem.setKey(screenKeys[i]);
            navigationItem.setTitle(screenTitles[i]);
            navigationItemList.add(navigationItem);
        }
    }

    // Các phương thức hỗ trợ để tải tiêu đề, khóa và biểu tượng cho menu
    private String[] loadScreenTitles() {
        if (taiKhoan == null) {
            return getResources().getStringArray(R.array.ld_activityScreenTitles);
        } else {
            if (taiKhoan.getMaQuyenHan() == 1) {
                return getResources().getStringArray(R.array.ld_activityScreenTitlesAuthenciated);
            } else {
                return getResources().getStringArray(R.array.ld_activityScreenTitlesAuthenciatedAdmin);
            }
        }
    }

    private String[] loadScreenKeys() {
        if (taiKhoan == null) {
            return getResources().getStringArray(R.array.ld_activityScreenKeys);
        } else {
            if (taiKhoan.getMaQuyenHan() == 1) {
                return getResources().getStringArray(R.array.ld_activityScreenKeysAuthenciated);
            } else {
                return getResources().getStringArray(R.array.ld_activityScreenKeysAuthenciatedAdmin);
            }
        }
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta;

        // Kiểm tra đăng nhập để hiển thị các mục trong menu tương ứng với role (arrays.xml trong values)
        if (taiKhoan == null) {
            ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);

        } else {
            if (taiKhoan.getMaQuyenHan() == 1) {
                ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsAuthenciated);
            } else {
                ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsAuthenciatedAdmin);

            }
        }

        // Hiển thị icon dựa trên id get được trong arrays
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    // Phương thức lấy màu từ resources
    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    // Phương thức để chuyển đổi fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.add(fragment, null);
        transaction.replace(R.id.container_activity, fragment);
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Getter cho taiKhoan
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public List<FoodOrder> getDanhSachSanPhamYeuThich() {
        return danhSachSanPhamYeuThich;
    }

    public void setDanhSachSanPhamYeuThich(List<FoodOrder> danhSachSanPhamYeuThich) {
        this.danhSachSanPhamYeuThich = danhSachSanPhamYeuThich;
    }

    // Setter cho taiKhoan
    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        if (taiKhoan != null) {
            askNotificationPermission();
            getDonHangMoiNhat();
            getTokenNotification();
            loadDanhSachSanPhamYeuThich();
        } else {
            setDanhSachSanPhamYeuThich(new ArrayList<>());
            clearStorageInformationLogin();
        }
    }

    // Phương thức lưu trữ thông tin đăng nhập
    public void storageInformationLogin(String taiKhoan, String matKhau) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("SDTTaiKhoan", taiKhoan);
        editor.putString("MatKhau", matKhau);
        editor.commit();
    }

    // Phương thức xóa thông tin đăng nhập
    public void clearStorageInformationLogin() {
        SharedPreferences settings = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    // Phương thức duy trì phiên đăng nhập
    public void keepLoginAccount() {
        String sdtTaiKhoanStorage = sharedpreferences.getString("SDTTaiKhoan", "");
        String matKhauStorage = sharedpreferences.getString("MatKhau", "");
        if (sdtTaiKhoanStorage.length() != 0 && matKhauStorage.length() != 0) {
            Log.i("Account", sdtTaiKhoanStorage + " " + matKhauStorage);
            loginTaiKhoan(sdtTaiKhoanStorage, matKhauStorage);
        }

    }

    // Phương thức đăng nhập
    public void loginTaiKhoan(String sdt, String matKhau) {
        {
            String url = getString(R.string.endpoint_server) + "/taikhoan/sign-in";

            try {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("SDTTaiKhoan", sdt);
                jsonBody.put("MatKhau", matKhau);
                final String requestBody = jsonBody.toString();
                //creating a string request to send request to the url
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            // Instance TaiKhoan
                            TaiKhoan taiKhoan = new TaiKhoan();
                            JSONObject jsonObjectData = obj.getJSONObject("data");
                            taiKhoan.setSdtTaiKhoan(jsonObjectData.getString("SDTTaiKhoan"));
                            taiKhoan.setHo(jsonObjectData.getString("Ho"));
                            taiKhoan.setTen(jsonObjectData.getString("Ten"));
                            taiKhoan.setDiaChiGiaoHang(jsonObjectData.getString("DiaChiGiaoHang"));
                            taiKhoan.setMaQuyenHan(jsonObjectData.getInt("MaQuyenHan"));
                            taiKhoan.setTenQuyenHan(jsonObjectData.getString("TenQuyenHan"));
                            taiKhoan.setDiaChiGiaoHang(jsonObjectData.getString("DiaChiGiaoHang"));
                            setTaiKhoan(taiKhoan);
                            drawerNavigation();
                            // Load Dashboard fragment
                            DashBoardFragment dashBoardFragment = new DashBoardFragment();
                            loadFragment(dashBoardFragment);
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
                                JSONObject obj = new JSONObject(res);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                DangXuatFragment dangXuatFragment = new DangXuatFragment();
                                loadFragment(dangXuatFragment);
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
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                //adding the string request to request queue
                requestQueue.add(stringRequest);

            } catch (JSONException err) {
                err.printStackTrace();
            }
        }
    }

    // Phương thức lấy đơn hàng mới nhất
    public void getDonHangMoiNhat() {
        try {
            String url = getString(R.string.endpoint_server) + "/donhang/moinhat";
            donHang = null;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                // get don hang info
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                donHang = new DonHang();
                                donHang.setMaDonHang(jsonObjectData.getInt("MaDonHang"));
                                donHang.setPhiGiaoHang(jsonObjectData.getInt("PhiGiaoHang"));
                                donHang.setSoTienThanhToan(jsonObjectData.getInt("SoTienThanhToan"));
                                donHang.setThanhTien(jsonObjectData.getInt("ThanhTien"));
                                donHang.setTinhTrang(jsonObjectData.getInt("TinhTrang"));
                                donHang.setDiaChiGiaoHang(jsonObjectData.getString("DiaChiGiaoHang"));
                                donHang.setSdtKhachHang(jsonObjectData.getString("SDTTaiKhoan"));
                                donHang.setMaVoucher(jsonObjectData.getString("MaVoucher"));
                                // get lich su order cua don hang
                                List<LichSuOrder> lichSuOrderList = new ArrayList<LichSuOrder>();
                                JSONArray jsonArray = jsonObject.getJSONArray("lichSuOrder");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsobj = jsonArray.getJSONObject(i);
                                    LichSuOrder lichSuOrder = new LichSuOrder();
                                    lichSuOrder.setMaLichSuOrder(jsobj.getInt("MaLichSuOrder"));
                                    lichSuOrder.setSoLuong(jsobj.getInt("SoLuong"));
                                    lichSuOrder.setThanhTien(jsobj.getInt("ThanhTien"));
                                    lichSuOrder.setGhiChu(jsobj.getString("GhiChu"));
                                    lichSuOrder.setMaSanPham(jsobj.getInt("MaSanPham"));
                                    lichSuOrder.setMaDonHang(jsobj.getInt("MaDonHang"));
                                    lichSuOrder.setTenSanPham(jsobj.getString("TenSanPham"));
                                    lichSuOrder.setKichThuoc(jsobj.getString("KichThuoc"));
                                    lichSuOrder.setGiaTienKichThuoc(jsobj.getInt("GiaTienKichThuoc"));
                                    List<FoodOrderTopping> foodOrderToppingList = new ArrayList<FoodOrderTopping>();
                                    JSONArray toppingArray = jsobj.getJSONArray("Topping");
                                    for (int j = 0; j < toppingArray.length(); j++) {
                                        JSONObject toppingObject = toppingArray.getJSONObject(j);
                                        FoodOrderTopping foodOrderTopping = new FoodOrderTopping();
                                        foodOrderTopping.setTenTopping(toppingObject.getString("TenTopping"));
                                        foodOrderTopping.setGiaTien(toppingObject.getInt("GiaTien"));
                                        foodOrderTopping.setMaSanPham(toppingObject.getInt("MaSanPham"));
                                        foodOrderToppingList.add(foodOrderTopping);
                                    }
                                    lichSuOrder.setFoodOrderToppingList(foodOrderToppingList);
                                    lichSuOrderList.add(lichSuOrder);
                                }
                                donHang.setLichSuOrderList(lichSuOrderList);
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
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    // Phương thức tải danh sách sản phẩm yêu thích
    public void loadDanhSachSanPhamYeuThich() {
        try {
            String url = getString(R.string.endpoint_server) + "/sanphamyeuthich/get-danh-sach";

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray datasArray = jsonObject.getJSONArray("data");
                                List<FoodOrder> list = new ArrayList<>();
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
                                    list.add(foodOrder);

                                }
                                setDanhSachSanPhamYeuThich(list);
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
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    // Lấy registration token FCM (nhận thông báo)
    void getTokenNotification() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("error", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.i("token", token);
                        updateTokenNotificationDatabase(token);
                    }
                });
    }

    // Cập nhật registration token lên server
    void updateTokenNotificationDatabase(String token) {
        try {
            String url = getString(R.string.endpoint_server) + "/taikhoan/update-notification-token";
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SDTTaiKhoan", taiKhoan.getSdtTaiKhoan());
            jsonBody.put("Token", token);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                taiKhoan.setNotificationToken(token);
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
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (JSONException err) {
            err.printStackTrace();
        }

    }

}
