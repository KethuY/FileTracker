package com.kethu.filetracker.file;

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
import com.kethu.filetracker.target_group.TargetGroup;
import com.kethu.filetracker.user.OnItemClick;
import com.kethu.filetracker.user.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileFragment extends Fragment implements OnItemClick {
    @BindView(R.id.recyler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    List<File> mFiles;
    private int mIntSelectedPosition;
    private FileAdapter mFileAdapter;

    public FileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecylerView = view.findViewById(R.id.recyler_view);
        mFiles = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            File file = new File();
            file.setName("File " + (i + 1));
            mFiles.add(file);
        }

        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecylerView.setHasFixedSize(true);
        mFileAdapter = new FileAdapter(mFiles, this);
        mRecylerView.setAdapter(mFileAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFileType(true);

            }
        });

    }

    private void addFileType(final boolean isAddingFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_file, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);
        Button save = view.findViewById(R.id.save);

        if (!isAddingFile) {
            code.getEditText().setText(mFiles.get(mIntSelectedPosition).getCode());
            name.getEditText().setText(mFiles.get(mIntSelectedPosition).getName());
            desc.getEditText().setText(mFiles.get(mIntSelectedPosition).getDesc());
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

                File targetGroup = new File();
                targetGroup.setName(ViewHelper.getString(name));
                targetGroup.setCode(ViewHelper.getString(code));
                targetGroup.setDesc(ViewHelper.getString(desc));

                if (!isAddingFile) {
                    mFiles.set(mIntSelectedPosition, targetGroup);
                } else {
                    mFiles.add(targetGroup);
                }
                mRecylerView.invalidate();
                mFileAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });
    }

    @Override
    public void onItemClick(int pos, View view) {
        mIntSelectedPosition = pos;
        switch (view.getId()) {
            case R.id.delete:
                if (mFiles != null && mFiles.size() > 0)
                    mFiles.remove(pos);
                mRecylerView.invalidate();
                mFileAdapter.notifyDataSetChanged();
                break;
            case R.id.cardView:
                addFileType(false);

                break;
        }
    }
}
