package econ.com.profile;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.id.input;

public class CountryDetails extends AppCompatActivity {

    private String TAG = CountryDetails.class.getSimpleName();

    private ProgressDialog pDialog;

    private static String url = "https://restcountries.eu/rest/v1/name/";


    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_CAPITAL = "capital";
    private static final String JSON_KEY_POPULATION = "population";
    private static final String JSON_KEY_AREA = "area";
    private static final String JSON_KEY_REGION = "region";
    private static final String JSON_KEY_SUB_REGION = "subregion";
    private static final String JSON_KEY_ALT_SPELLINGS = "altSpellings";

    private static final String NOT_AVAILABLE = "NA";

    private String countryName,capital,population,area,region,subregion ="";
    private TextView countryDes;
    private TextView capitalDes;
    private TextView populationDesc;
    private TextView areaDesc;
    private TextView regionDesc;
    private TextView subRegionDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countrydetails);
        countryDes = (TextView) findViewById(R.id.country);
        capitalDes = (TextView) findViewById(R.id.capitaldesc);
        populationDesc = (TextView) findViewById(R.id.populationdesc);
        areaDesc = (TextView) findViewById(R.id.areadesc);
        regionDesc = (TextView) findViewById(R.id.regiondesc);
        subRegionDesc = (TextView) findViewById(R.id.subregiondesc);
        countryName = getIntent().getStringExtra(CountryActivity.COUNTRY_NAME);
        countryDes.setText(countryName);
        new GetCountryDetails().execute(url + Uri.encode(countryName));

    }



    protected String removeEscapeChars(String regex, String remainingValue) {
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(remainingValue);
        while (matcher.find()) {
            String before = remainingValue.substring(0, matcher.start());
            String after = remainingValue.substring(matcher.start() + 1);
            remainingValue = (before + after);
        }
        return remainingValue;
    }


    private class GetCountryDetails extends AsyncTask<String, String, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CountryDetails.this);
            pDialog.setMessage("Processing");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... requrl) {
            HttpHandler handler = new HttpHandler();
            String url = requrl[0];
            // Making a request to url and getting response
            Log.d(TAG, "Request  url: " + url);
            String jsonStr = handler.httpRequest(url);

            boolean isFound = false;
            Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray countryArr = new JSONArray(jsonStr);
                    int objLength = countryArr.length();
                    if(objLength > 0) {
                        //Enumerating all the objects and match the name with the country name and get the Country Details from the object
                        for(int i=0; i < objLength; i++){
                            String value = countryArr.getJSONObject(i).getString(JSON_KEY_NAME);
                            if(value.equals(countryName))
                            {
                                JSONObject jsonObj = countryArr.getJSONObject(i);
                                Log.d(TAG, "countryArr.length(): " + countryArr.length());

                                capital = jsonObj.optString(JSON_KEY_CAPITAL).toString();
                                population = jsonObj.optString(JSON_KEY_POPULATION).toString();
                                area = jsonObj.optString(JSON_KEY_AREA).toString();
                                region = jsonObj.optString(JSON_KEY_REGION).toString();
                                subregion = jsonObj.optString(JSON_KEY_SUB_REGION).toString();
                                isFound = true;
                                break;
                            }
                        }
                        //Verify the country name  in the altSpellings to get the Country Details
                        if(!isFound) {
                            for(int i=0; i < objLength; i++){
                                String value = countryArr.getJSONObject(i).getString(JSON_KEY_ALT_SPELLINGS);
                                if(value != null) {
                                    JSONArray countryallArr = new JSONArray(value);
                                    int arrLength = countryallArr.length();
                                    if(arrLength > 0) {
                                        for(int index=0; index < arrLength; index++) {
                                            Log.d(TAG, "countryallArr.getString: " + countryallArr.getString(index));
                                            String convertedString = Normalizer.normalize(countryallArr.getString(index), Normalizer.Form.NFD)
                                                            .replaceAll("[^\\p{ASCII}]", "");
                                            if(convertedString.contains(countryName))
                                            {
                                                JSONObject jsonObj = countryArr.getJSONObject(i);
                                                Log.d(TAG, "countryArr.length(): " + countryArr.length());

                                                capital = jsonObj.optString(JSON_KEY_CAPITAL).toString();
                                                population = jsonObj.optString(JSON_KEY_POPULATION).toString();
                                                area = jsonObj.optString(JSON_KEY_AREA).toString();
                                                region = jsonObj.optString(JSON_KEY_REGION).toString();
                                                subregion = jsonObj.optString(JSON_KEY_SUB_REGION).toString();
                                                isFound = true;
                                                break;
                                            }

                                        }
                                    }
                                }

                            }

                        }


                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

            }

            return null;
        }

        private String validateJSONValue(String jsonValuesString) {
            if(jsonValuesString == null){
                return NOT_AVAILABLE;
            }else if(jsonValuesString.equals("")){
                return NOT_AVAILABLE;
            }else {
                return jsonValuesString;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            capitalDes.setText(validateJSONValue(capital));
            populationDesc.setText(validateJSONValue(population));
            areaDesc.setText(validateJSONValue(area));
            regionDesc.setText(validateJSONValue(region));
            subRegionDesc.setText(validateJSONValue(subregion));

        }

    }
}
