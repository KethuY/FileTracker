package com.kethu.filetracker.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.CircleImageView;
import com.kethu.filetracker.profile.ProfileActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by satya on 06-Jan-18.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mCotext;
    private List<String> mHeaders;
    private Map<String, List<User>> mSubChilds;
    MyListener mOnItemClick;

    public MyExpandableListAdapter(Context context, List<String> listDataHeader, Map<String, List<User>> listChildData,MyListener onItemClick) {
        this.mCotext = context;
        this.mHeaders = listDataHeader;
        this.mSubChilds = listChildData;
        mOnItemClick=onItemClick;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mSubChilds.get(this.mHeaders.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, final View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);
        final User user = (User) getChild(groupPosition, childPosition);
        LayoutInflater infalInflater = (LayoutInflater) this.mCotext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=infalInflater.inflate(R.layout.child_item, null);


        TextView name = (TextView) view.findViewById(R.id.name);
        TextView mobile = (TextView) view.findViewById(R.id.mobile);
        TextView designation = (TextView) view.findViewById(R.id.designation);
        TextView status = (TextView) view.findViewById(R.id.status);
        CircleImageView circleImageView=view.findViewById(R.id.image);
      final   ImageView delete=view.findViewById(R.id.delete);
        name.setText(user.getName());
        mobile.setText(user.getMobile());
        designation.setText(user.getDesignation());
        status.setText(user.getUserStatus());
        CardView cardView=view.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCotext.startActivity(new Intent(mCotext, ProfileActivity.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mCotext);
                    builder.setTitle("Delete confirmation");
                    builder.setMessage("Do you want to delete this user?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });


                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });

                   // mOnItemClick.onMyItemClickListener(childPosition,delete);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mSubChilds.get(this.mHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mCotext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
