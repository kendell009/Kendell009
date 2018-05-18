package com.example.manco.googlemapp2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UserFragment extends Fragment {
    private Controller controller;
    private Button btnRegGrp;
    private EditText dialogEtUsername;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private String[] groups;
    private String selectedGroup;
    private EditText etCreateGroupName;
    private EditText etCreateUsername;
    private String myId = "";

    public UserFragment() {
        // Required empty public constructor
    }


    public void setController(Controller controller){
        this.controller = controller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_controll, container, false);
        initializeComponents(view);
        registerListeners();
        controller.getGroups();
        return view;
    }


    public void populateList(String[] groupsArray) {
        this.groups = groupsArray;
        for(int i=0;i<groupsArray.length;i++){
            Log.i("UserFragment","populateList() : " + groupsArray[i]);
        }
        Log.i("UserFragment","populateList() : " + groupsArray);
        if(groupsArray.length>0){
            arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, groupsArray);
            listView.setAdapter(arrayAdapter);
        }
    }


    public void showMembersDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View groupDialog = factory.inflate(R.layout.list_item_dialog,null);
        ListView membersList = (ListView)groupDialog.findViewById(R.id.dialogListView);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, controller.getMemberArray());
        dialogEtUsername = (EditText) groupDialog.findViewById(R.id.etDialogUsername);
        membersList.setAdapter(arrayAdapter);
        builder.setView(groupDialog);
        builder.setPositiveButton(R.string.btnConnect, new btnConnectClicked());
        builder.setNeutralButton(R.string.btnCancel, new btnCancelClicked());
        builder.setNegativeButton(R.string.btnDisconnect, new btnDisconnectClicked());
        builder.create().show();
    }


    private void initializeComponents(View view) {
        btnRegGrp = (Button) view.findViewById(R.id.buttonRegisterGroup);
        listView = (ListView) view.findViewById(R.id.listView2);
    }


    private void registerListeners() {
        btnRegGrp.setOnClickListener(new btnRegGrpClicked());
        listView.setOnItemClickListener(new listItemClicked());
    }


    public void setId(String groupId) {
        this.myId = groupId;
    }

    private class btnRegGrpClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            etCreateUsername = (EditText) getView().findViewById(R.id.etCreateUsername);
            etCreateGroupName = (EditText)getView().findViewById(R.id.etCreateGroupName);
            Log.i("UserFragment","btnRegGrpClicked(), Skapat grupp :" + etCreateGroupName.getText().toString() + ", " + etCreateUsername.getText().toString());
            if(etCreateUsername.getText().toString().equals(null) || etCreateUsername.getText().toString().equals("") ||
                    etCreateGroupName.getText().toString().equals(null) || etCreateGroupName.getText().toString().equals("")){
                Toast.makeText(getActivity(),R.string.toastError, Toast.LENGTH_SHORT).show();
            }else{
                controller.registerNewGroup(etCreateGroupName.getText().toString(), etCreateUsername.getText().toString());
                etCreateGroupName.setText(null);
                etCreateUsername.setText(null);
            }
            controller.getGroups();
        }
    }

    private class listItemClicked implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            controller.getMembers(groups[position]);
            Log.i("UserFragment","onItemClick : Group:" + groups[position]);
            selectedGroup = groups[position];
        }
    }

    private class btnConnectClicked implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialogEtUsername.getText().toString().equals(null)||dialogEtUsername.getText().toString().equals("")){
                Toast.makeText(getActivity(),R.string.toastError, Toast.LENGTH_SHORT).show();
            }else{
                controller.registerNewGroup(selectedGroup, dialogEtUsername.getText().toString());
            }
        }
    }

    private class btnCancelClicked implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class btnDisconnectClicked implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            controller.unregisterGroup(myId);
            dialog.dismiss();
        }
    }
}