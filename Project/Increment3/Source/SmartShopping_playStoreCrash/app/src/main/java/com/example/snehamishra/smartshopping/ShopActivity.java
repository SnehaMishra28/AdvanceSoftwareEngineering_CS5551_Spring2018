package com.example.snehamishra.smartshopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShopActivity extends AppCompatActivity {

    private TextView shopAnalysisText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        String analysisText = getIntent().getStringExtra("Analysis");

        shopAnalysisText.setText(analysisText);
    }
}
