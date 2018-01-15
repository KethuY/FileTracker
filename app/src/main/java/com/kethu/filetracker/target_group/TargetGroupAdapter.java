package com.kethu.filetracker.target_group;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kethu.filetracker.R;
import com.kethu.filetracker.user.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by satya on 14-Jan-18.
 */

public class TargetGroupAdapter extends RecyclerView.Adapter<TargetGroupAdapter.ViewHolder> {

    List<TargetGroup> mTargetGroups;
    OnItemClick mOnItemClick;

    public TargetGroupAdapter(List<TargetGroup> targetGroups, OnItemClick onItemClick) {
        this.mTargetGroups = targetGroups;
        this.mOnItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.target_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mTargetGroups.get(position).getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(position, holder.cardView);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(position, holder.delete);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTargetGroups == null ? 0 : mTargetGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.delete)
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
