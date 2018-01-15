package com.kethu.filetracker.target_group;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.ViewHelper;
import com.kethu.filetracker.user.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TargetGroupFragment extends Fragment implements OnItemClick {
    @BindView(R.id.recyler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    List<TargetGroup> mTargetGroups;
    private TargetGroupAdapter mTargetGroupAdapter;
    private int mIntSelectedPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_target_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] names = getContext().getResources().getStringArray(R.array.traget_group);

        mTargetGroups = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {

            TargetGroup targetGroup = new TargetGroup();
            targetGroup.setName(names[i]);
            mTargetGroups.add(targetGroup);

        }

        mTargetGroupAdapter = new TargetGroupAdapter(mTargetGroups, this);
        mRecyclerView.setAdapter(mTargetGroupAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showNameCodeDescDialog(true);

            }
        });

    }

    private void showNameCodeDescDialog(final boolean isAddingTG) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.name_code_desc, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);
        Button save = view.findViewById(R.id.save);

        if (!isAddingTG) {
            code.getEditText().setText(mTargetGroups.get(mIntSelectedPosition).getCode());
            name.getEditText().setText(mTargetGroups.get(mIntSelectedPosition).getName());
            desc.getEditText().setText(mTargetGroups.get(mIntSelectedPosition).getDescription());
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

                TargetGroup targetGroup = new TargetGroup();
                targetGroup.setName(ViewHelper.getString(name));
                targetGroup.setCode(ViewHelper.getString(code));
                targetGroup.setDescription(ViewHelper.getString(desc));

                if (!isAddingTG) {
                    mTargetGroups.set(mIntSelectedPosition, targetGroup);
                } else {
                    mTargetGroups.add(targetGroup);
                }
                mRecyclerView.invalidate();
                mTargetGroupAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });


    }

    @Override
    public void onItemClick(int pos, View view) {
        mIntSelectedPosition = pos;
        switch (view.getId()) {
            case R.id.delete:
                if (mTargetGroups != null && mTargetGroups.size() > 0)
                    mTargetGroups.remove(pos);
                mRecyclerView.invalidate();
                mTargetGroupAdapter.notifyDataSetChanged();
                break;
            case R.id.cardView:
                if (mIntSelectedPosition != mTargetGroups.size() - 1)
                    showNameCodeDescDialog(false);
                else
                    showNameCodeDescDialog(true);
                break;
        }
    }
}
