package com.example.marcus.mappy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendFrag extends PreferenceFragment {

    private View view;
    private Button button, buttonAdd, buttonDelete;
    private Spinner listPals;
    private User user;
    private EditText editText;
    private InternetManager netManager;
    private ArrayAdapter adapter;

    public static FriendFrag newInstance(User someObject) {
        FriendFrag fragment = new FriendFrag();
        fragment.setUser(someObject);
        fragment.netManager = new InternetManager(someObject);
        return fragment;
    }

    private void setUser(User someObject){
        user = someObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.friendsettings, container, false);
        button = (Button) view.findViewById(R.id.buttonback);
        listPals = (Spinner) view.findViewById(R.id.spinner);
        buttonAdd = (Button) view.findViewById(R.id.buttonadd);
        buttonDelete = (Button) view.findViewById(R.id.buttondelete);
        editText = (EditText) view.findViewById(R.id.editText);

        ArrayList<UserSpinnerItem> showFriends = new ArrayList<UserSpinnerItem>();
        ArrayList<User> getFriendsTmp = user.getFriendsInfo();

        for(int i = 0 ; i < getFriendsTmp.size(); i++){
            showFriends.add(new UserSpinnerItem(getFriendsTmp.get(i).getName()));
        }

        adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, showFriends);
        listPals.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newFriendName = editText.getText().toString() + "#";
                netManager.execute(newFriendName);
                Toast.makeText(getActivity().getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserSpinnerItem removedFriend = (UserSpinnerItem) listPals.getSelectedItem();
                netManager.execute(removedFriend.toString() + "¤");
                Toast.makeText(getActivity().getApplicationContext(), "Deletion complete", Toast.LENGTH_SHORT).show();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setBackgroundColor(Color.WHITE);

    }

    private class UserSpinnerItem{
        private String name;
        public UserSpinnerItem(String name){
            this.name = name;
        }
        @Override
        public String toString(){
            return name;
        }
    }

}