package com.kethu.filetracker.node;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.ToastHelper;
import com.kethu.filetracker.helpers.ViewHelper;
import com.kethu.filetracker.user.OnItemClick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class NodeFragment extends Fragment implements OnItemClick, AdapterView.OnItemSelectedListener {
    @BindView(R.id.recyler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    List<Node> mNodes;
    private NodeAdapter mNodeAdapter;
    private int mIntSelectedPosition;
    private List<String> mNodesTypes;
    private ArrayAdapter<String> mSpinerAdapter;
    private ArrayList<String> mOffices;
    private ArrayList<String> mParentNodes;
    private ArrayAdapter<String> mOfficeAdapter;
    private ArrayAdapter<String> mParentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_node, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] names = getContext().getResources().getStringArray(R.array.node);

        mNodes = new ArrayList<>();

        for (String name : names) {
            Log.e("TAG", "name " + name);
            Node node = new Node();
            node.setName(name);
            node.setType("Bench");
            mNodes.add(node);

        }

        mNodeAdapter = new NodeAdapter(mNodes, this);
        mRecyclerView.setAdapter(mNodeAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNode(true);

            }
        });
    }

    private void addNode(final boolean isAddingTG) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_node, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);
        final Spinner typeSpiner = view.findViewById(R.id.node_type_spin);
        final Spinner officeSpiner = view.findViewById(R.id.office_spin);
        final Spinner parentNodeSpiner = view.findViewById(R.id.parent_spin);
        mNodesTypes = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.node_type)));
        mOffices = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.office)));
        mParentNodes = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.parent_node)));

        mSpinerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, mNodesTypes);
        mOfficeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, mNodesTypes);
        mParentAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, mNodesTypes);
        typeSpiner.setAdapter(mSpinerAdapter);
        officeSpiner.setAdapter(mOfficeAdapter);
        parentNodeSpiner.setAdapter(mParentAdapter);
        typeSpiner.setOnItemSelectedListener(this);
        officeSpiner.setOnItemSelectedListener(this);
        parentNodeSpiner.setOnItemSelectedListener(this);
        Button save = view.findViewById(R.id.save);


        if (!isAddingTG) {
            code.getEditText().setText(mNodes.get(mIntSelectedPosition).getCode());
            name.getEditText().setText(mNodes.get(mIntSelectedPosition).getName());
            desc.getEditText().setText(mNodes.get(mIntSelectedPosition).getDesc());
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

                Node node = new Node();
                node.setName(ViewHelper.getString(name));
                node.setCode(ViewHelper.getString(code));
                node.setType(ViewHelper.getString(desc));

                if (!isAddingTG) {
                    mNodes.set(mIntSelectedPosition, node);
                } else {
                    mNodes.add(node);
                }
                mRecyclerView.invalidate();
                mNodeAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });


    }

    private void addNodeType(boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.name_code_desc, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);
        final TextInputLayout add = view.findViewById(R.id.address);

        if (b)
            add.setVisibility(View.VISIBLE);
        else
            add.setVisibility(GONE);
        Button save = view.findViewById(R.id.save);


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ViewHelper.getString(name).isEmpty()) {
                    name.setError("Name required");
                    return;
                }

                mNodesTypes.add(mNodesTypes.size() - 1, ViewHelper.getString(name));
                mSpinerAdapter.notifyDataSetChanged();

                alertDialog.cancel();
            }
        });


    }

    @Override
    public void onItemClick(int pos, View view) {
        mIntSelectedPosition = pos;
        switch (view.getId()) {
            case R.id.delete:
                if (mNodes != null && mNodes.size() > 0)
                    mNodes.remove(pos);
                mRecyclerView.invalidate();
                mNodeAdapter.notifyDataSetChanged();
                break;
            case R.id.cardView:
                addNode(false);

                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.node_type_spin:
                if (position > 0) {

                    if (mNodesTypes.size() - 1 == position)
                        addNodeType(false);
                } else {
                    ToastHelper.showToastLenLong(getContext(), "Select type");
                }
                break;

            case R.id.office_spin:
                addNodeType(true);
                break;
            case R.id.parent_spin:
                addNodeType(false);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
