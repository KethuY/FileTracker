package com.chs.secure_transport.menu;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.secure_transport.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

private Context mContext;
private List<MenuIcon> mHomeItems;
private ViewHolder viewHolder = null;

GridViewAdapter(Context context, List<MenuIcon> homeIconsList) {

    mContext = context;
    mHomeItems = homeIconsList;
}

@Override
public int getCount() {
    return mHomeItems != null ? mHomeItems.size() : 0;
}

@Override
public MenuIcon getItem(int i) {
    return mHomeItems.get(i);
}

@Override
public long getItemId(final int i) {
    return i;
}

@Override
public View getView(final int i, View view, ViewGroup viewGroup) {

    if (view == null) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.menu_icons_item, viewGroup, false);
        viewHolder=new ViewHolder(view);
        view.setTag(viewHolder);
    }else{
        viewHolder= (ViewHolder) view.getTag();
    }

    viewHolder.title.setText(mHomeItems.get(i).getName());
    viewHolder.imageId.setBackgroundResource(mHomeItems.get(i).getImageId());

    if(mHomeItems.get(i).isClicked()){
        viewHolder.cardView.setCardElevation(8f);
        viewHolder.title.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
       // viewHolder.imageId.getBackground().setColorFilter(ContextCompat.getColor(mContext,R.color.colorPrimary), PorterDuff.Mode.SRC);
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   // viewHolder.cardView.setCardElevation(1f);

                    mHomeItems.get(i).setClicked(false);
                    try {
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }else{
        viewHolder.cardView.setCardElevation(1f);
        viewHolder.title.setTextColor(ContextCompat.getColor(mContext,android.R.color.darker_gray));
      //  viewHolder.imageId.getBackground().setColorFilter(ContextCompat.getColor(mContext,android.R.color.darker_gray), PorterDuff.Mode.SRC);

    }

    return view;
}

class ViewHolder {
    ImageView imageId;
    TextView title;
    CardView cardView;

    public ViewHolder(View view) {
        imageId=view.findViewById(R.id.icon_iv);
        title=view.findViewById(R.id.title_tv);
        cardView=view.findViewById(R.id.cardView);
    }
}
}
