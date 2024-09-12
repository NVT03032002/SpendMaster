package com.TenthGroup.spendsmarter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class category_RecyclerView extends RecyclerView.Adapter<category_RecyclerView.ViewHolder> {
    private List<Category> categoryList;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView iconimg;
        private final TextView description;

        public ViewHolder(View itemView)
        {
            super(itemView);
            iconimg = itemView.findViewById(R.id.icon);
            description = itemView.findViewById(R.id.description);
        };
    }

    public category_RecyclerView(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void updateData(List<Category> newCategoryList) {
        Log.e("TAG", newCategoryList.toString());
        categoryList = newCategoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // initialize variable
        Category category;
        String imagePath;
        String color;

        category = categoryList.get(position);
        imagePath = "android.resource://com.TenthGroup.spendsmarter/drawable/" + category.getIcon_url();
        if (category.getColor() != null) color = category.getColor();
        else color = "#11998e";

        // Set style
        holder.iconimg.setImageURI(Uri.parse(imagePath));
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(color));
        holder.iconimg.setBackgroundTintList(colorStateList);
        holder.description.setText(category.getName());

        // Set Onclick event
        holder.itemView.setOnClickListener(view -> {
            mListener.onItemClick(category, position);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
