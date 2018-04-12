package com.example.android.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentContacts extends Fragment {
    View v;
    private RecyclerView myRecyclerView;
    static RecylcerViewAdapter recylcerViewAdapter;
    static boolean hasDB;
    List <Contact> myContactList;
    List <Contact> listFromDB;
    EditText et_search;
    Button bt_getContact;
    ContactDB contactDB;

    public FragmentContacts() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_fragment, container, false);
        myContactList = new ArrayList<>();
        et_search = v.findViewById(R.id.editText_search);
        bt_getContact = v.findViewById(R.id.button_getContact);
        contactDB = new ContactDB(getContext());
        hasDB = contactDB.doesTableExist();
        refreshContact();
        applySearchOption();
        Log.d("hasDb", Boolean.toString(hasDB));
        if(!hasDB){
            check();
        }

        listFromDB = contactDB.getAllData();
        handleRecyclerView(listFromDB);


        return v;
    }

    private void handleRecyclerView(List<Contact> listFromDB) {
        myRecyclerView = v.findViewById(R.id.contact_recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylcerViewAdapter = new RecylcerViewAdapter(getContext(), listFromDB);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(recylcerViewAdapter);
    }

    private void refreshContact() {
        bt_getContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myContactList = new ArrayList<>();
                listFromDB = new ArrayList<>();
                contactDB.deleteAllfromTable();
                check();
                handleRecyclerView(listFromDB);
//                v.refreshDrawableState();
            }
        });
    }

    private void applySearchOption() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

            private void filter(String s) {
                s = s.toLowerCase();
                ArrayList<Contact> newList = new ArrayList<>();

                for(Contact contact : listFromDB){
                    String name = contact.name.toLowerCase();
                    if(name.contains(s)){
                        newList.add(contact);
                    }
                    FragmentContacts.recylcerViewAdapter.setFilter(newList);

                }
            }
        });
    }


    private void getContactList() {
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        getActivity().startManagingCursor(cursor);

        if (cursor != null && cursor.moveToFirst() ) {
            try {
                while (!cursor.isClosed() && cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.));
                    String imageId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//                    Contact contact = new Contact(name, phoneNo, bitmap);
                    Contact myContact = new Contact(name, phoneNo, imageId);
//                    if(!contactList.contains(contact)){
//                        contactList.add(contact);
//                    }
                    if(!myContactList.contains(myContact)){
                        myContactList.add(myContact);
                    }
                }
            }catch (IllegalArgumentException e) {
                Log.e("error ==", e.getMessage());
            } finally {
                try {
                    if (!cursor.isClosed()) {
                        Log.d("cursor: ", "closed");
                        cursor.close();
                    }
                    cursor = null;
                } catch (Exception e) {
                    Log.e("While closing cursor", e.getMessage());
                }
            }
        }
        contactDB.insert( myContactList);
        Log.d("ContactListLength: ", Integer.toString(myContactList.size()));
    }



    private void check(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getContactList();

                }
            });
//            Thread thread = new Thread(r);
//            thread.start();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getActivity(), "Read contacts permission is required to function app correctly", Toast.LENGTH_LONG).show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1);                }

            }
        }
    }


}