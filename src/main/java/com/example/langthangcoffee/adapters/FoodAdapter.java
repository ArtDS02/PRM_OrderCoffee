package com.example.langthangcoffee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langthangcoffee.R;
import com.example.langthangcoffee.models.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private final List<Food> mListFood;

    public FoodAdapter(List<Food> mListFood) {
        this.mListFood = mListFood;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = mListFood.get(position);
        if (food == null)
        {
            return;
        }
        holder.imageView.setImageResource(food.getImage());

        holder.tvName.setText(food.getName());
        holder.tvCost.setText(food.getPrice());
    }

    @Override
    public int getItemCount() {
        if(mListFood != null){
            return mListFood.size();
        }
            return 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView imageView;
        private final TextView tvName;
        private final TextView tvCost;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_food);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvCost = itemView.findViewById(R.id.tv_food_price);
        }
    }
}
