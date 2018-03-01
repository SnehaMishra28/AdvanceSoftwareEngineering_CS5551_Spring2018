package com.example.snehamishra.firebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    // views and widgets
    private Button logoutButton;
    private TextView welcomeUserText;

    //Firebase Authentication Fileds
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Assign IDs
        logoutButton = (Button)findViewById(R.id.logout_btn);
        welcomeUserText = (TextView)findViewById(R.id.welcomeTextView);

        //Assign Instances
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            //user already logged in
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();
        welcomeUserText.setText("Welcome " +user.getEmail() +" !!");
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==logoutButton){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
