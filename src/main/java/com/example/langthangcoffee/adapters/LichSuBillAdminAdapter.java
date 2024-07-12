package com.example.langthangcoffee.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.langthangcoffee.fragments.BillDangChoAdminFragment;
import com.example.langthangcoffee.fragments.BillDangGiaoAdminFragment;
import com.example.langthangcoffee.fragments.BillHoanThanhAdminFragment;
import com.example.langthangcoffee.fragments.BillHuyAdminFragment;

@SuppressWarnings("ALL")
public class LichSuBillAdminAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 4;

    public LichSuBillAdminAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BillDangChoAdminFragment();
            case 1:
                return new BillDangGiaoAdminFragment();
            case 2:
                return new BillHoanThanhAdminFragment();
            case 3:
                return new BillHuyAdminFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }
}


