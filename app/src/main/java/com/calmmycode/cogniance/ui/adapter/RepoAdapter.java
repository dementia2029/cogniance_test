package com.calmmycode.cogniance.ui.adapter;

import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calmmycode.cogniance.databinding.FragmentResultsListItemRepoBinding;
import com.calmmycode.cogniance.model.data.Repo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private ArrayList<Repo> repos = new ArrayList<>();

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentResultsListItemRepoBinding binding = FragmentResultsListItemRepoBinding.inflate(inflater, parent, false);
        return new RepoViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.fragmentResultsListItemRepoBinding.setModel(repos.get(position));
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {
        FragmentResultsListItemRepoBinding fragmentResultsListItemRepoBinding;
        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            fragmentResultsListItemRepoBinding = DataBindingUtil.bind(itemView);
        }
    }

    public void updateData(ArrayList<Repo> repos){
        this.repos = repos;
    }
}
