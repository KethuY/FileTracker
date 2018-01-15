package com.kethu.filetracker.node;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kethu.filetracker.R;
import com.kethu.filetracker.target_group.TargetGroup;
import com.kethu.filetracker.user.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by satya on 14-Jan-18.
 */

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

    List<Node> mNodes;
    OnItemClick mOnItemClick;

    public NodeAdapter(List<Node> targetGroups, OnItemClick onItemClick) {
        this.mNodes = targetGroups;
        this.mOnItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(mNodes.get(position).getName());
        holder.type.setText(mNodes.get(position).getType());
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
        return mNodes == null ? 0 : mNodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.node_type)
        TextView type;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
