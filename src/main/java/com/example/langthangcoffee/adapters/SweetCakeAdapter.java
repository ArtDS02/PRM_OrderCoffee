package com.example.langthangcoffee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.SweetCake;

import java.util.List;

public class SweetCakeAdapter extends RecyclerView.Adapter<SweetCakeAdapter.SweetCakeHolder>{
    private final List<SweetCake> ListSweetCake;

    public SweetCakeAdapter(List<SweetCake> ListSweetCake) {
        this.ListSweetCake = ListSweetCake;
    }

    @NonNull
    @Override
    public SweetCakeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new SweetCakeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SweetCakeHolder holder, int position) {
        SweetCake  sweetCake = ListSweetCake.get(position);
        if (sweetCake == null)
        {
            return;
        }
        holder.imageView.setImageResource(sweetCake.getImage());
        holder.tvName.setText(sweetCake.getName());
        holder.tvCost.setText(sweetCake.getCost());
    }

    @Override
    public int getItemCount() {
        if(ListSweetCake != null){
            return ListSweetCake.size();
        }
        return 0;
    }

    public static class SweetCakeHolder extends RecyclerView.ViewHolder
    {
        private final ImageView imageView;
        private final TextView tvName;
        private final TextView tvCost;

        public SweetCakeHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_food);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvCost = itemView.findViewById(R.id.tv_food_price);
        }
    }
}
