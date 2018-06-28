package com.example.slots.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.slots.R;

import java.util.List;

public final class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<Integer> items;


    public static Adapter newInstance(Context context, List<Integer> items) {
        return new Adapter(items);//возвращаем пререданный список
    }


    private Adapter(List<Integer> items) {
        this.items = items;//присваиваем переданный список список для отображения
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot, parent, false);
        return ViewHolder.newInstance(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int resId = items.get(position);
        holder.setImageResource(resId);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public static ViewHolder newInstance(View itemView) {
            ImageView imageView = itemView.findViewById(R.id.slotImage);
            return new ViewHolder(itemView, imageView);
        }

        private ViewHolder(View itemView, ImageView imageView) {
            super(itemView);
            this.imageView = imageView;
        }

        public void setImageResource(int resId) {
            imageView.setImageResource(resId);
        }
    }
}
