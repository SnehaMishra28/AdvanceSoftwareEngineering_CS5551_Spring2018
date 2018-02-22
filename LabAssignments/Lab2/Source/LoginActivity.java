package com.example.snehamishra.auth2login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    private LinearLayout prof_Section;
    private Button signOut,navigateHome;
    private SignInButton signIn;
    private TextView Name, Email;
    private ImageView prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prof_Section = (LinearLayout)findViewById(R.id.prof_section);
        signOut = (Button)findViewById(R.id.bn_back);
        navigateHome = (Button)findViewById(R.id.bn_home);
        signIn = (SignInButton)findViewById(R.id.bn_login);
        Name = (TextView)findViewById(R.id.name);
        Email = (TextView)findViewById(R.id.email);
        prof_pic = (ImageView)findViewById(R.id.prof_pic);
        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        navigateHome.setOnClickListener(this);
        prof_Section.setVisibility(View.GONE);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bn_login:
                signIn();
                break;

            case R.id.bn_back:
                signOut();
                break;
            case R.id.bn_home:
                loadHomePage();
                break;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void signIn()
    {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);


    }

    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String image_url = account.getPhotoUrl().toString();
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(image_url).into(prof_pic);
            updateUI(true);
        }
        else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin)
    {

        if(isLogin){
            prof_Section.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.GONE);

        }
        else{
            prof_Section.setVisibility(View.GONE);
            signIn.setVisibility(View.VISIBLE);

        }
    }

    private void loadHomePage()
    {
        Intent intent = new Intent(this, TabbedHomeScreen.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
