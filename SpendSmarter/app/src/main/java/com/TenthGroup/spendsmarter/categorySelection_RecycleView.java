package com.TenthGroup.spendsmarter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class categorySelection_RecycleView extends RecyclerView.Adapter<categorySelection_RecycleView.ViewHolder> {
    private List<String> categorySelectionList;
    private OnItemClickListener event_listener;
    private int LayoutType = 0;

    public void setOnItemClickListener(OnItemClickListener listener) {
        event_listener = listener;
    }

    public interface OnItemClickListener {
        void set_onclick(String path);
        void set_Colorclick(String path);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon_img;
        private LinearLayout background;
        public ViewHolder(View itemView) {
            super(itemView);
            icon_img = itemView.findViewById(R.id.icon);
            background = itemView.findViewById(R.id.icon_background);
        };
    }

    public categorySelection_RecycleView(List<String> categorySelectionList, int LayoutType) {
        this.categorySelectionList = categorySelectionList;
        this.LayoutType = LayoutType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (LayoutType == 1)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_selection_item_layout, parent, false);
        if (LayoutType == 2)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_selection_color_layout, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder view, int position) {
        if (LayoutType == 1) {
            String filename = categorySelectionList.get(position);
            String imagePath = "android.resource://com.TenthGroup.spendsmarter/drawable/" + filename;

            view.icon_img.setImageURI(Uri.parse(imagePath));
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#11998e"));
            view.icon_img.setBackgroundTintList(colorStateList);

            view.itemView.setOnClickListener(v -> {
                event_listener.set_onclick(filename);});
        }
        else {
            String color_string = categorySelectionList.get(position);
            int color = Color.parseColor(color_string);

            ColorStateList colorStateList = ColorStateList.valueOf(color);
            view.background.setBackgroundTintList(colorStateList);
            view.itemView.setOnClickListener(v -> {
                event_listener.set_Colorclick(categorySelectionList.get(position));});
        }
    }

    @Override
    public int getItemCount() {
        return categorySelectionList.size();
    }
}
