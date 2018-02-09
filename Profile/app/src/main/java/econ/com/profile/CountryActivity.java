package econ.com.profile;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.LinkedList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.widget.Toast;

public class CountryActivity extends AppCompatActivity{

    private static final String TAG = Profile.class.getSimpleName();
    public static final String COUNTRY_NAME = "countryName";


    private RecyclerView mRecyclerView;
    private CountryListAdapter mAdapter;

    private final LinkedList<String> mCountryList = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        try {
            ParseXml();
            // Create recycler view.
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            // Create an adapter and supply the data to be displayed.
            mAdapter = new CountryListAdapter(this, mCountryList,new CountryListAdapter.RecyclerViewClickListener(){
                @Override
                public void recyclerViewListClicked(String text,int position) {
                    // Do anything with the item position
                    Log.d(TAG,"Selected TEXT: "+ text + " - " + position);
                    openCountryDetails(text);
                }

            });
            // Connect the adapter with the recycler view.
            mRecyclerView.setAdapter(mAdapter);
            // Give the recycler view a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    private void openCountryDetails(String country)
    {
        boolean isNetworkConnected = checkInternetConenction();
        if(!isNetworkConnected) {
            Toast.makeText(this, "Internet Not Connected ", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, CountryDetails.class);
        intent.putExtra(COUNTRY_NAME,country);
        startActivity(intent);
    }

    private void ParseXml() throws IOException, XmlPullParserException {
        mCountryList.clear();
        Resources res = this.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.countries);
        xpp.next();
        while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType()== XmlPullParser.TEXT) {
                Log.d(TAG,"\nTEXT: "+xpp.getText());
                mCountryList.addLast(xpp.getText().replace("\\","")); // handling the escape sequence character
            }
            xpp.next();
        }
    }

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
