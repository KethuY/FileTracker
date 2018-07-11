package com.chs.secure_transport.student;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chs.secure_transport.R;

import java.util.List;

/**
 * Created by satya on 31-Mar-18.
 */

public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Student> mStudents;

    public StudentAdapter(List<Student> notifications) {
        mStudents = notifications;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studnet_child_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Student payment = mStudents.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mSnoTV.setText(String.format("%s", ""/*+(position+1)*/));
        viewHolder.mNameTV.setText(String.format("%s", payment.getName()));
        viewHolder.mClassTV.setText(String.format("%s", payment.getClassName()));
    }


    @Override
    public int getItemCount() {
        return /*mStudents.size();*/ 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mSnoTV;
        TextView mNameTV;
        TextView mClassTV;

        ViewHolder(View itemView) {
            super(itemView);
            mSnoTV = itemView.findViewById(R.id.sno_tv);
            mNameTV = itemView.findViewById(R.id.name_tv);
            mClassTV = itemView.findViewById(R.id.class_tv);
        }
    }


}
