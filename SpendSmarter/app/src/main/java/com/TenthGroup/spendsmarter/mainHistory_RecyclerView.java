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

public class mainHistory_RecyclerView extends RecyclerView.Adapter<mainHistory_RecyclerView.ViewHolder> {
    private List<Transaction> trans_list;
    private category_RecyclerView.OnItemClickListener mListener;

    public void setOnItemClickListener(category_RecyclerView.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView categoryImg;
        private final TextView categoryTv;
        private final TextView amountTv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            categoryImg = itemView.findViewById(R.id.icon);
            categoryTv = itemView.findViewById(R.id.category);
            amountTv = itemView.findViewById(R.id.amount);
        };
    }

    public mainHistory_RecyclerView(List<Transaction> trans_list) {
        this.trans_list = trans_list;
    }

    public void updateData(List<Transaction> trans_list) {
        Log.e("TAG", trans_list.toString() );
        this.trans_list = trans_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public mainHistory_RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_transaction_layout, parent, false);
        return new mainHistory_RecyclerView.ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull mainHistory_RecyclerView.ViewHolder holder, int position) {
        Transaction transaction = trans_list.get(position);
        Category category = AppSystem.getAppSystem().getCategoryById(transaction.getCategory_id());

        String imagePath = "android.resource://com.TenthGroup.spendsmarter/drawable/" + category.getIcon_url();

        holder.categoryImg.setImageURI(Uri.parse(imagePath));
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(category.getColor()));
        holder.categoryImg.setImageTintList(colorStateList);
        holder.categoryTv.setText(category.getName());
        holder.amountTv.setText(String.valueOf(transaction.getAmount()));
    }

    @Override
    public int getItemCount() {
        return trans_list.size();
    }
}
