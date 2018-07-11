package com.chs.secure_transport.drop;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chs.secure_transport.R;
import com.chs.secure_transport.helpers.OnItemClickListener;
import com.chs.secure_transport.student.Student;

import java.util.List;

/**
 * Created by satya on 31-Mar-18.
 */


public class DropAttendanceAdapter extends RecyclerView.Adapter<DropAttendanceAdapter.ViewHolder> {
    private List<Student> mStudents;
    private OnItemClickListener mListner;
    private Context mContext;

    public DropAttendanceAdapter(List<Student> notifications, OnItemClickListener onItemClickListener) {
        mStudents = notifications;
        mListner = onItemClickListener;
    }

    @Override
    public DropAttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drop_attendance_item, parent, false);
        return new DropAttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DropAttendanceAdapter.ViewHolder holder, final int position) {
        Student payment = mStudents.get(position);

        holder.mSnoTV.setText(String.valueOf(position + 1));
        holder.mNameTV.setText(String.format("%s\n%s", mStudents.get(position).getName(), mStudents.get(position).getId()));
        holder.mClassTV.setText(mStudents.get(position).getClassName());
        holder.mStatusBtn.setText(mStudents.get(position).getStatus());
        holder.mStatusBtn.setTextColor(Color.WHITE);
        holder.mStatusBtn.setBackgroundResource(getColor(mStudents.get(position).getStatus()));
        holder.mStatusBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListner.onItemClick(position, holder.mStatusBtn);
            }
        });


    }

    private int getColor(String status) {


        if (status == null)
            return R.drawable.rouned_corner_rect_green;

        switch (status) {
            case "PRESENT":
                return R.drawable.rouned_corner_rect_green;
            case "ABSENT":
                return R.drawable.rouned_corner_rect_red;
            case "OWN_TRANSPORT":
                return R.drawable.rouned_corner_rect_blue;
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return /*mStudents.size();*/ 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mSnoTV;
        TextView mNameTV;
        TextView mClassTV;
        TextView mStatusBtn;

        ViewHolder(View itemView) {
            super(itemView);
            mSnoTV = itemView.findViewById(R.id.sno_tv);
            mNameTV = itemView.findViewById(R.id.name_tv);
            mClassTV = itemView.findViewById(R.id.class_tv);
            mStatusBtn = itemView.findViewById(R.id.status);
        }
    }


}

