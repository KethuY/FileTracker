package com.kethu.filetracker.status_designation;

import android.app.AlertDialog;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.ViewHelper;
import com.kethu.filetracker.target_group.TargetGroup;
import com.kethu.filetracker.target_group.TargetGroupAdapter;
import com.kethu.filetracker.user.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusDesignationActivity extends AppCompatActivity implements OnItemClick {

    @BindView(R.id.recyler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    List<StaDesgination> mStatusDesigns;
    private DesignationStatusAdapter mDesgnStatusAdapter;
    private int mIntSelectedPosition;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_designation);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean isStatus = getIntent().getBooleanExtra("isStatus", false);

        if (isStatus) {
            mToolbar.setTitle("User Status");
            names = getResources().getStringArray(R.array.user_status);
        } else {
            mToolbar.setTitle("User Designation");
            names = getResources().getStringArray(R.array.user_designation);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mStatusDesigns = new ArrayList<>();

        for (String name : names) {
            StaDesgination targetGroup = new StaDesgination();
            targetGroup.setName(name);
            mStatusDesigns.add(targetGroup);
        }

        mDesgnStatusAdapter = new DesignationStatusAdapter(mStatusDesigns, this);
        mRecyclerView.setAdapter(mDesgnStatusAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showNameCodeDescDialog(true);

            }
        });

    }


    private void showNameCodeDescDialog(final boolean isAddingTG) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatusDesignationActivity.this);
        View view = getLayoutInflater().inflate(R.layout.name_code_desc, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);
        Button save = view.findViewById(R.id.save);

        if (!isAddingTG) {
            code.getEditText().setText(mStatusDesigns.get(mIntSelectedPosition).getCode());
            name.getEditText().setText(mStatusDesigns.get(mIntSelectedPosition).getName());
            desc.getEditText().setText(mStatusDesigns.get(mIntSelectedPosition).getDesc());
            save.setText(R.string.modify);
        }

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ViewHelper.getString(name).isEmpty()) {
                    name.setError("Name required");
                    return;
                }

                StaDesgination targetGroup = new StaDesgination();
                targetGroup.setName(ViewHelper.getString(name));
                targetGroup.setCode(ViewHelper.getString(code));
                targetGroup.setDesc(ViewHelper.getString(desc));

                if (!isAddingTG) {
                    mStatusDesigns.set(mIntSelectedPosition, targetGroup);
                } else {
                    mStatusDesigns.add(targetGroup);
                }
                mRecyclerView.invalidate();
                mDesgnStatusAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });


    }

    @Override
    public void onItemClick(int pos, View view) {
        mIntSelectedPosition = pos;
        switch (view.getId()) {
            case R.id.delete:
                if (mStatusDesigns != null && mStatusDesigns.size() > 0)
                    mStatusDesigns.remove(pos);
                mRecyclerView.invalidate();
                mDesgnStatusAdapter.notifyDataSetChanged();
                break;
            case R.id.cardView:
                if (mIntSelectedPosition != mStatusDesigns.size() - 1)
                    showNameCodeDescDialog(false);
                else
                    showNameCodeDescDialog(true);
                break;
        }
    }
}
