package com.example.langthangcoffee.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.adapters.FoodAdapter;
import com.example.langthangcoffee.models.Food;

import java.util.ArrayList;
import java.util.List;

public class ThirdPageDashBoardFragment extends Fragment implements View.OnClickListener {
    private int POS_TYPE_CAFE = 0;
    private int POS_TYPPE_TRASUA = 0;
    private int POS_TYPPE_SODA = 0;

    private RecyclerView recyclerView;
    ColorStateList def;
    TextView item1,item2,item3,select;

    GridLayoutManager gridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_third_page_dash_board, container, false);
        Toolbar toolbar = v.findViewById(R.id.tb_select);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView = v.findViewById(R.id.rcv_page_3_dash_board);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        FoodAdapter foodAdapter = new FoodAdapter(getListFood());
        recyclerView.setAdapter(foodAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                int positionOfCafe = 0;
                int positionOfTrasua = POS_TYPPE_TRASUA;
                int positionOfSoda = POS_TYPPE_SODA;
                int firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();
                int lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();

                if (positionOfCafe >= firstVisiblePosition && positionOfCafe <= lastVisiblePosition)
                {
                    select.animate().x(0).setDuration(100);
                    item1.setTextColor(R.color.charcoal);
                    item2.setTextColor(def);
                    item3.setTextColor(def);
                }
                else if (positionOfTrasua >= firstVisiblePosition && positionOfTrasua <= lastVisiblePosition)
                {
                    item1.setTextColor(def);
                    item2.setTextColor(R.color.charcoal);
                    item3.setTextColor(def);
                    int size = item2.getWidth();
                    select.animate().x(size).setDuration(100);
                }
                else if (positionOfSoda >= firstVisiblePosition && positionOfSoda <= lastVisiblePosition)
                {
                    select.animate().x(0).setDuration(100);
                    item1.setTextColor(def);
                    item2.setTextColor(def);
                    item3.setTextColor(R.color.charcoal);
                    int size = item2.getWidth() * 2;
                    select.animate().x(size).setDuration(100);
                }
            }

        });

        item1 = v.findViewById(R.id.cf_selected);
        item2 = v.findViewById(R.id.ts_selected);
        item3 = v.findViewById(R.id.sd_selected);

        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);

        select = v.findViewById(R.id.select);
        def = item2.getTextColors();
        return v;
    }

    private List<Food> getListFood() {
        List<Food> list = new ArrayList<>();
        //cafe
        list.add(new Food(R.drawable.cf1,"Cà phê đen","25000 đ",Food.TYPE_CAFE));
        list.add(new Food(R.drawable.cf2,"Cà phê sữa","25000 đ",Food.TYPE_CAFE));
        list.add(new Food(R.drawable.cf3,"Cà phê dừa","25000 đ",Food.TYPE_CAFE));
        list.add(new Food(R.drawable.cf4,"Cà phê phô mai","25000 đ",Food.TYPE_CAFE));
        list.add(new Food(R.drawable.cf5,"Milo","25000 đ",Food.TYPE_CAFE));
        //tra sua
        list.add(new Food(R.drawable.ts1,"Trà sữa đường đen","25000 đ",Food.TYPE_TRASUA));
        list.add(new Food(R.drawable.ts2,"Trà sữa matcha","25000 đ",Food.TYPE_TRASUA));
        list.add(new Food(R.drawable.ts3,"Trà sữa khoai môn","25000 đ",Food.TYPE_TRASUA));
        list.add(new Food(R.drawable.ts4,"Trà sữa cheese","25000 đ",Food.TYPE_TRASUA));
        list.add(new Food(R.drawable.ts5,"Trà sữa cacao","25000 đ",Food.TYPE_TRASUA));
        //soda
        list.add(new Food(R.drawable.sd1,"Soda đào","30000 đ",Food.TYPE_SODA));
        list.add(new Food(R.drawable.sd2,"Soda nho","30000 đ",Food.TYPE_SODA));
        list.add(new Food(R.drawable.sd3,"Soda dâu","30000 đ",Food.TYPE_SODA));
        list.add(new Food(R.drawable.sd4,"Soda chanh","30000 đ",Food.TYPE_SODA));
        list.add(new Food(R.drawable.sd5,"Soda cam","30000 đ",Food.TYPE_SODA));

        updatePosition(list);

        return list;
    }

    private void updatePosition(List<Food> list) {
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getType() == 2)
            {
                POS_TYPPE_TRASUA = i;
                break;
            }
        }

        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getType() == 3)
            {
                POS_TYPPE_SODA = i;
                break;
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cf_selected)
        {
            select.animate().x(0).setDuration(100);
            item1.setTextColor(R.color.charcoal);
            item2.setTextColor(def);
            item3.setTextColor(def);
            scrollToItem(POS_TYPE_CAFE);
        }
        else if (v.getId() == R.id.ts_selected)
        {
            item1.setTextColor(def);
            item2.setTextColor(R.color.charcoal);
            item3.setTextColor(def);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            scrollToItem(POS_TYPPE_TRASUA);
        }
        else if (v.getId() == R.id.sd_selected)
        {
            select.animate().x(0).setDuration(100);
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(R.color.charcoal);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
            scrollToItem(POS_TYPPE_SODA);
        }

    }

    public void scrollToItem(int position)
    {
        if (gridLayoutManager == null)
            return;
        gridLayoutManager.scrollToPositionWithOffset(position,0);
    }

}
