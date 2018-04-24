package com.example.snehamishra.smartshopping;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

      // import android.support.design.widget.FloatingActionButton;
        //import android.support.design.widget.Snackbar;
        import javax.crypto.Mac;
        import javax.crypto.SecretKeyFactory;
        import javax.crypto.spec.PBEKeySpec;
        import javax.crypto.spec.SecretKeySpec;
//import javax.security.sasl.SaslClient;
//import javax.security.sasl.SaslException;

        import com.github.mikephil.charting.charts.PieChart;
        import com.github.mikephil.charting.data.PieData;
        import com.github.mikephil.charting.data.PieDataSet;
        import com.github.mikephil.charting.data.PieEntry;
        import com.github.mikephil.charting.utils.ColorTemplate;

        import java.security.Security;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;

        import com.mongodb.DBObject;
        import com.mongodb.MongoClient;
        import com.mongodb.MongoClientURI;
        import com.mongodb.client.MongoCollection;
        import com.mongodb.client.MongoCursor;
        import com.mongodb.client.MongoDatabase;
        import org.bson.Document;

public class ARActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;

    MongoClientURI uri = null;
    MongoClient client = null;
    MongoDatabase db = null;

    //int count[] = {10,15,5,30,10,30};
    List<Object> qty = new ArrayList<Object>();
    List<Object> cat = new ArrayList<Object> ();

    //String category[] = {"Sofa","Beds","Decor","Tables","Garden","Chairs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        back = findViewById(R.id.back_ToImage);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Security.addProvider(new gnu.javax.crypto.jce.GnuSasl());
        uri  = new MongoClientURI("mongodb://ruthvic:ruthvic@ds115569.mlab.com:15569/db1ase");
        client = new MongoClient(uri);
        db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> shoppingTrends = db.getCollection("ruth_Shopping");
        MongoCursor<Document> cursor = shoppingTrends.find().iterator();
        try {
            while (cursor.hasNext())
            {
                Document doc = cursor.next();
                cat.addAll(doc.keySet());
                qty.addAll(doc.values());
                cat.remove(0); qty.remove(0);
            }

        } finally {
            cursor.close();
        }

        setupPieChart();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = getPackageManager().getLaunchIntentForPackage("com.umkc.trump");
                if (launch != null) {
                    startActivity(launch);
                }
            }
        }); */


        // Set on click listener to buttons
        back.setOnClickListener(this);
    }

    private void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0;i< qty.size(); i++)
        {
            pieEntries.add(new PieEntry(Float.parseFloat(qty.get(i).toString()), String.valueOf(cat.get(i))));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Trending Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        //get the chart
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.setCenterText("Furniture Stats");
        chart.invalidate();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    /*
    public void startUnity(View view) {
        Intent launch = getPackageManager().getLaunchIntentForPackage("com.umkc.trump");
        if (launch != null)
        {
            startActivity(launch);
        }
        else
        {

        }
    }  */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(ARActivity.this, ImageActivity.class));
        finish();
    }

}
