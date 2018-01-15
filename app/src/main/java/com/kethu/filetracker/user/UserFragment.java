package com.kethu.filetracker.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.kethu.filetracker.R;
import com.kethu.filetracker.db.SqliteAdapter;
import com.kethu.filetracker.helpers.ToastHelper;
import com.kethu.filetracker.helpers.Utility;
import com.kethu.filetracker.helpers.ViewHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class UserFragment extends Fragment implements UserView, TextWatcher, AdapterView.OnItemSelectedListener, View.OnClickListener, OnItemClick, MyListener {

    private OnFragmentInteractionListener mListener;
    private ExpandableListView mExpandableListview;
    UserPresnter mUserPresnter;
    private MyExpandableListAdapter mAdapter;
    private SqliteAdapter mSqliteAdapter;
    private FloatingActionButton mFloatingButton;
    private List<String> mHeaders;
    private Map<String, List<User>> mChildrens;
    TextInputLayout mNameTIL;
    TextInputLayout mMobileTIL;
    TextInputLayout mAadhar;
    TextInputLayout mAddress;
    Spinner mTypeSpin;
    Spinner mOfficeSpin;
    Spinner mDesgSpin;
    Spinner mStatusSpin;
    private OnItemClick mOnItemClick;
    MyListener myListener;
    private String[] mArrayNames = {"Type", "Office", "Designation", "Status"};

    private List<String> mTypes;
    private List<String> mOffices;
    private List<String> mDesignation;
    private List<String> mStatus;
    private ArrayAdapter<String> mTypeAdapter;
    private ArrayAdapter<String> mOfficeAdapter;
    private ArrayAdapter<String> mDesignationAdapter;
    private ArrayAdapter<String> mStatusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserPresnter = new UserPresenterImpl(getContext(), this);
        mExpandableListview = view.findViewById(R.id.expandable_lv);
        mUserPresnter.getUserData();
        mSqliteAdapter = new SqliteAdapter(getContext());
        mOnItemClick = this;


        mTypes = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.user_type)));
        mOffices = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.office)));
        mDesignation = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.user_designation)));
        mStatus = new ArrayList<>(Arrays.asList(getContext().getResources().getStringArray(R.array.user_status)));
        mTypeAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_textview,mTypes);
        mOfficeAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_textview,mOffices);
        mDesignationAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_textview,mDesignation);
        mStatusAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_textview,mStatus);

        myListener = this;
        mFloatingButton = view.findViewById(R.id.floatingActionButton);

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBoxForAddingUser();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogBoxForAddingUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.adding_user_layout, null);
        builder.setView(view);

        mNameTIL = view.findViewById(R.id.name);
        mMobileTIL = view.findViewById(R.id.mobile);
        mAadhar = view.findViewById(R.id.aadhar);
        mAddress = view.findViewById(R.id.add);
        mTypeSpin = view.findViewById(R.id.type_spin);
        mOfficeSpin = view.findViewById(R.id.office_spin);
        mDesgSpin = view.findViewById(R.id.desgnation_spin);
        mStatusSpin = view.findViewById(R.id.status_spin);

        mTypeSpin.setOnItemSelectedListener(this);
        mOfficeSpin.setOnItemSelectedListener(this);
        mDesgSpin.setOnItemSelectedListener(this);
        mStatusSpin.setOnItemSelectedListener(this);

        mTypeSpin.setAdapter(mTypeAdapter);
        mOfficeSpin.setAdapter(mOfficeAdapter);
        mDesgSpin.setAdapter(mDesignationAdapter);
        mStatusSpin.setAdapter(mStatusAdapter);
        mNameTIL.getEditText().addTextChangedListener(this);
        mMobileTIL.getEditText().addTextChangedListener(this);
        mAadhar.getEditText().addTextChangedListener(this);
        mAddress.getEditText().addTextChangedListener(this);

        Button saveUser = view.findViewById(R.id.save);

        final AlertDialog alert = builder.create();
        alert.show();

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setId(Utility.randInt(0, 999999999));
                user.setName(ViewHelper.getString(mNameTIL));
                user.setOffice(ViewHelper.getString(mOfficeSpin));
                user.setDesignation(ViewHelper.getString(mDesgSpin));
                user.setUserStatus(ViewHelper.getString(mStatusSpin));
                user.setMobile(ViewHelper.getString(mMobileTIL));
                user.setAadhar(ViewHelper.getString(mAadhar));
                user.setAdd(ViewHelper.getString(mAddress));
                user.setUserType(ViewHelper.getString(mTypeSpin));


                int id = Utility.randInt(0, 2);
                @SuppressLint("ResourceType")
                String[] dd = {"Admin", "User", "Incharge"};

                user.setUserRole(dd[id]);

                List<User> users = new ArrayList<>();
                users.add(user);

                if (mSqliteAdapter.addUser(user) > 0) {
                    mHeaders.add(dd[id]);
                    mChildrens.put(dd[id], users);
                    mAdapter = new MyExpandableListAdapter(getContext(), mHeaders, mChildrens, myListener);
                    mExpandableListview.setAdapter(mAdapter);
                    ToastHelper.showToastLenLong(getActivity(), "User added successfullly");
                } else {
                    ToastHelper.showToastLenLong(getActivity(), "User not added");
                }

                alert.cancel();

            }
        });

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setDataToExpandableListView(List<String> headers, Map<String, List<User>> users) {
        mHeaders = headers;
        mChildrens = users;

        mAdapter = new MyExpandableListAdapter(getContext(), mHeaders, mChildrens, myListener);
        mExpandableListview.setAdapter(mAdapter);

        mExpandableListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        mExpandableListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (parent.getId()) {
            case R.id.type_spin:
                if (mTypes.size() - 1 == position)
                    showNameCodeDescDialog(parent.getId());
                break;
            case R.id.office_spin:
                if (mOffices.size() - 1 == position)
                    showNameCodeDescDialog(parent.getId());
                break;
            case R.id.desgnation_spin:
                if (mDesignation.size() - 1 == position)
                    showNameCodeDescDialog(parent.getId());
                break;
            case R.id.status_spin:
                if (mStatus.size() - 1 == position)
                    showNameCodeDescDialog(parent.getId());
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void showNameCodeDescDialog( final int spinId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.name_code_desc, null);
        builder.setView(view);

        final TextInputLayout code = view.findViewById(R.id.code);
        final TextInputLayout name = view.findViewById(R.id.name);
        final TextInputLayout desc = view.findViewById(R.id.desc);


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



                switch (spinId) {
                    case R.id.type_spin:
                       mTypes.add(mTypes.size()-1,ViewHelper.getString(name));
                       mTypeAdapter.notifyDataSetChanged();
                        break;
                    case R.id.office_spin:
                        mOffices.add(mOffices.size()-1,ViewHelper.getString(name));
                        mOfficeAdapter.notifyDataSetChanged();
                        break;
                    case R.id.desgnation_spin:
                        mDesignation.add(mDesignation.size()-1,ViewHelper.getString(name));
                        mDesignationAdapter.notifyDataSetChanged();
                        break;
                    case R.id.status_spin:
                        mStatus.add(mStatus.size()-1,ViewHelper.getString(name));
                        mStatusAdapter.notifyDataSetChanged();
                        break;
                }

                alertDialog.cancel();
            }
        });


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int pos, View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete confirmation");
        builder.setMessage("Do you want to this user?");
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

    }

    @Override
    public void onMyItemClickListener(int pos, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete confirmation");
        builder.setMessage("Do you want to this user?");
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
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
