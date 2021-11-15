package com.example.filterpost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filterpost.databinding.ItemsPostsBinding;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    Context context;
    public List<Posts> arrayList;
    public List<Posts> arrayListFiltered;
    ItemsPostsBinding binding;

    public StoryAdapter(ArrayList<Posts> list, Context context) {
        this.arrayList = list;
        this.context = context;
        this.arrayListFiltered = arrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemsPostsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posts post = arrayList.get(position);
        binding.title.setText(post.getId()+". "+post.getTitle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemsPostsBinding itemsPostsBinding;

        public ViewHolder(@NonNull ItemsPostsBinding postsBinding) {
            super(binding.getRoot());
            itemsPostsBinding = postsBinding;
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Posts> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Posts item : arrayListFiltered) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                arrayList = filteredList;
            }

            FilterResults results = new FilterResults();
            results.values = arrayList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            arrayList = (List<Posts>) results.values;
            notifyDataSetChanged();
        }
    };

    public void updateList() {
        if (arrayListFiltered != null) {
            arrayList.clear();
            arrayList.addAll(arrayListFiltered);
            notifyDataSetChanged();
        }
    }
}

