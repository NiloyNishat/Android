package com.example.android.contacts;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GoogleAuth extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    private static final int RC_API_CHECK = 0;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions signInOptions;
    SignInButton signInButton;
    static final int RC_INTENT = 1;
    PeoplesAsync pep;
    static Context context;
    static String info;
    private RecyclerView myRecyclerView;
    static RecylcerViewAdapter recylcerViewAdapter;
    static List <Contact> contactList = new ArrayList<>();
    Bitmap finalBitmap;
    GoogleDB googleDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_auth);
        setTitle("Contact from Google");
        signInButton = findViewById(R.id.main_googlesigninbtn);
        context = this;
        googleDB = new GoogleDB(context);
        finalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);

        myRecyclerView = findViewById(R.id.googleContact_recyclerview);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        signingIn();
    }



    private void signingIn() {
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // The serverClientId is an OAuth 2.0 web client ID
                .requestServerAuthCode(getString(R.string.web_clientID))
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_ME),
                        new Scope(PeopleScopes.CONTACTS_READONLY),
                        new Scope(PeopleScopes.USER_PHONENUMBERS_READ),
                        new Scope(PeopleScopes.USER_EMAILS_READ),
                        new Scope(PeopleScopes.USERINFO_PROFILE))
                .build();

        // To connect with Google Play Services and Sign In
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this )
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdToken();

            }
            private void getIdToken() {
                // Shows an account picker to let the user choose a Google account from the device.
                // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
                // consent screen will be shown here.
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_INTENT);
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path + "/myContact");
                boolean deleted = deleteRecursive(dir);

            }
        });

    }

    public boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()){
            File[] fileList = fileOrDirectory.listFiles();
           if(fileList != null){
               for (File child : fileList)
                   deleteRecursive(child);
           }
        }
        return fileOrDirectory.delete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_INTENT:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    Log.d("success", "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
                    // This is what we need to exchange with the server.
                    pep = new PeoplesAsync();
                    pep.execute(acct.getServerAuthCode());

                } else {
                    Log.d("failed", result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
                }
                break;
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), RC_API_CHECK);
        dialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static People setUp(Context context, String serverAuthCode) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Redirect URL for web based applications.
        // Can be empty too.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

        // STEP 1
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                context.getString(R.string.web_clientID),
                context.getString(R.string.web_client_secret),
                serverAuthCode,
                redirectUrl).execute();

        // STEP 2
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(context.getString(R.string.web_clientID), context.getString(R.string.web_client_secret))
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build();

        credential.setFromTokenResponse(tokenResponse);

        // STEP 3
        return new People.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Contacts")
                .build();
    }


    public class PeoplesAsync extends AsyncTask<String, Void, List<String>> {
        List<String> idList = new ArrayList<>();
        List<Contact> myGoogleContact = new ArrayList<>();

        ProgressDialog dialogue;

        private PeoplesAsync() {
            dialogue = new ProgressDialog(context);
        }

        protected void onPreExecute() {
            this.dialogue.setMessage("Please wait");
            this.dialogue.setCancelable(false);
            this.dialogue.show();
        }


        @Override
        protected List<String> doInBackground(String... params) {
            try {
                People peopleService = GoogleAuth.setUp(GoogleAuth.context, params[0]);

                ListConnectionsResponse response = peopleService.people().connections()
                        .list("people/me")
                        // This line's really important! Here's why:
                        // http://stackoverflow.com/questions/35604406/retrieving-information-about-a-contact-with-google-people-api-java
                        .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers,person.photos")
                        .execute();
                Log.d("response: ", response.toPrettyString());
                List<Person> connections = response.getConnections();
                List<Name> names;
                List<PhoneNumber> phoneNumbers;
                List<EmailAddress> emails;
                List<Photo> photos;
                List <Contact> myList = new ArrayList<>();

                for (Person person : connections) {
                    if (!person.isEmpty()) {
                        String n, p, e = "", i = "";
                        names = person.getNames();
                        phoneNumbers = person.getPhoneNumbers();
                        emails = person.getEmailAddresses();
                        photos = person.getPhotos();

                        n = names.get(0).getDisplayName();
                        p = phoneNumbers.get(0).getValue();
                        if(emails != null) e = emails.get(0).getValue();
                        if(photos != null){
                            i = photos.get(0).getUrl();
                        }
                        Contact c = new Contact(n,p,i,e);
                        c.fromGoogle = true;
                        myList.add(c);
                    }
                }

                myGoogleContact = myList;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return idList;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            Toast.makeText(context, "Contact list is fetched!", Toast.LENGTH_LONG).show();

            super.onPostExecute(strings);
            if ( dialogue!=null && dialogue.isShowing() ){
                dialogue.dismiss();
            }
            signInButton.setVisibility(View.GONE);
            fillContact();
            setRecyclerView();
        }

        private void setRecyclerView() {
            recylcerViewAdapter = new RecylcerViewAdapter(context, contactList);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            myRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
            myRecyclerView.setHasFixedSize(true);
            myRecyclerView.setItemViewCacheSize(contactList.size());
            myRecyclerView.setDrawingCacheEnabled(true);
            myRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            myRecyclerView.setAdapter(recylcerViewAdapter);
        }

        private void fillContact() {
            contactList = myGoogleContact;
            googleDB.deleteAllfromTable();
            googleDB.insert(myGoogleContact);
        }

    }


}


