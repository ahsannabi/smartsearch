package com.example.halfm_1.smartsearch;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import java.net.URI;

public class MainActivity extends AppCompatActivity {
    Uri contactInfo;
    URI contactinfo;
    String []phone=new String[50];
    String []name=new String[50];
    int []type=new int[50];
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadPhoneContacts(getApplicationContext());
        Display();

    }


    public void Display(){

        for(int i=0;i<count;i++){
            Log.e(name[count],"contact name");
            Log.println(type[count],"phone type","phone type");
            Log.e(phone[count],"contact number");
        }
    }

    public void update(View v) {
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        // startActivityForResult(intent, 1);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, 1);
        ContentResolver cr = getContentResolver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // super.onActivityResult(requestCode,resultCode,data);
        // PhoneContacts phoneContacts = new PhoneContacts(service);
        // phoneContacts.ReadPhoneContacts(cntx);
        // if(requestCode==1){contactInfo=data.getData();}
    }

    public void ReadPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                  //  editor.putString("Name","Harneet");
                  //  editor.apply();
                  // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext()) {
                        int phoneType = pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        if(count<49) {
                            phone[count] = phoneNo;
                            type[count] = phoneType;
                            name[count] = contactName;
                            count++;
                        }

                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                Log.e(contactName + ": TYPE_MOBILE", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                Log.e(contactName + ": TYPE_HOME", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                Log.e(contactName + ": TYPE_WORK", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                Log.e(contactName + ": TYPE_WORK_MOBILE", " " + phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                Log.e(contactName + ": TYPE_OTHER", " " + phoneNo);
                                break;
                            default:
                                break;
                        }
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }
    }

}
