package com.example.dell.ves2;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.ves2.Adapters.HousesActivityMainRecViewAdapterClass;
import com.example.dell.ves2.Utility.HousesResponse;
import com.example.dell.ves2.Utility.RetrofitClient;
import com.example.dell.ves2.Utility.housesDiffUtilCallBack;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Houses extends AppCompatActivity {


    EditText housePriceEditText;
    CheckBox forRentCheck, forSaleCheck;
    Button filterHousesButton;
    Boolean isForRent = false;
    Boolean isForSale = false;
    ArrayList<String> queryStringsHolder = new ArrayList();
    String queryString = null;
    StringBuilder mb = new StringBuilder();
    ArrayList<HousesResponse> filteredHousesResponseArray, allHousesResponseArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        final RecyclerView housesMainRecView = findViewById(R.id.houses_activity_rec_view);
        housesMainRecView.setHasFixedSize(true);
        housesMainRecView.setLayoutManager(new GridLayoutManager(Houses.this, 1, 1, false));


        final Spinner houseLocationSpinner = (Spinner) findViewById(R.id.location_spinner);

        Retrofit.Builder builder;
        Retrofit myretrofit;

        retrofit2.Call<ArrayList<HousesResponse>> allHousescall;


        //initialize the retrofit client builder using the backendless api
        builder = new Retrofit.Builder();
        builder.baseUrl("https://api.backendless.com/125AF8BD-1879-764A-FF22-13FB1C162400/6F40C4D4-6CFB-E66A-FFC7-D71E4A8BF100/data/")
                .addConverterFactory(GsonConverterFactory.create());

        //use your builder to build a retrofit object
        myretrofit = builder.build();

        //create a retrofit client using the retrofit object
        final RetrofitClient mywebclient = myretrofit.create(RetrofitClient.class);

        //create your calls using the retrofit client
        allHousescall = mywebclient.getAllHouses();


        //initialize the checkbox and the edit text in the filter area
        housePriceEditText = findViewById(R.id.house_max_price_edit_text);
        forRentCheck = findViewById(R.id.for_rent_check_box);
        forSaleCheck = findViewById(R.id.for_sale_check_box);
        filterHousesButton = findViewById(R.id.submit_houses_filter_button);





        //add the onclick listener for the filter button
        filterHousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View mview = view;

                //the if statement checks if the house location spinner's selected item is 'all' and skips adding the query if it is
                if (!houseLocationSpinner.getSelectedItem().equals("ALL")) {

                    //this adds "AND" to the query if it is appended to another query and adds nothing if it is alone
                    if (queryStringsHolder.isEmpty()) {
                        queryStringsHolder.add("Location%3D" + "'" + houseLocationSpinner.getSelectedItem().toString() + "'");
                    } else {
                        queryStringsHolder.add("%20AND%20Location%3D" + "'" + houseLocationSpinner.getSelectedItem().toString() + "'");
                    }
                }

                if (!housePriceEditText.getText().toString().isEmpty()) {

                    if (queryStringsHolder.isEmpty()) {
                        queryStringsHolder.add("price%3C%3D" + housePriceEditText.getText().toString());
                    } else {
                        queryStringsHolder.add("%20AND%20price%3C%3D" + housePriceEditText.getText().toString());
                    }

                }

                if (forRentCheck.isChecked()) {

                    if (queryStringsHolder.isEmpty()) {
                        isForRent = true;
                        queryStringsHolder.add("Rent%3D" + isForRent.toString());
                    } else {
                        isForRent = true;
                        queryStringsHolder.add("%20AND%20Rent%3D" + isForRent.toString());
                    }


                }

                if (forSaleCheck.isChecked()) {
                    if (queryStringsHolder.isEmpty()) {
                        isForSale = true;
                        queryStringsHolder.add("for_sale%3D" + isForSale.toString());
                    } else if (isForRent) {
                        isForSale = true;
                        queryStringsHolder.add("%20OR%20for_sale%3D" + isForSale.toString());
                    } else if (!isForRent) {
                        isForSale = true;
                        queryStringsHolder.add("%20AND%20for_sale3D" + isForSale.toString());
                    }


                }


                for (int i = 0; i < queryStringsHolder.size(); i++) {

                    mb.append(queryStringsHolder.get(i));

                }


                queryString = mb.toString();
                Log.d(queryString, "mquery");
                retrofit2.Call<ArrayList<HousesResponse>> filteredHousesCall = mywebclient.getFilteredHouses(queryString);


                //Toast queryToast = Toast.makeText(Houses.this, queryString, Toast.LENGTH_LONG);
                //queryToast.show();

                filteredHousesCall.clone().enqueue(new Callback<ArrayList<HousesResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<HousesResponse>> call, Response<ArrayList<HousesResponse>> response) {


                        Toast queryToast = Toast.makeText(Houses.this, response.raw().request().url().toString(), Toast.LENGTH_LONG);
                        queryToast.show();
                        filteredHousesResponseArray = response.body();
                        if (filteredHousesResponseArray != null) {
                            Snackbar.make(mview, filteredHousesResponseArray.get(0).getTitle(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(mview, "message" + response.message(), Snackbar.LENGTH_LONG).show();
                        }

                        RecyclerView.Adapter mhousesAdapter = housesMainRecView.getAdapter();

                        final sortHouses msorthouses = new sortHouses(mhousesAdapter);

                        msorthouses.execute(filteredHousesResponseArray, allHousesResponseArray);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<HousesResponse>> call, Throwable t) {

                        Snackbar.make(mview, "WELL, THAT'S EMBARRASSING!" + t.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });


               // msorthouses.execute(filteredHousesResponseArray, allHousesResponseArray);

                mb.delete(0, mb.length());
                queryStringsHolder.clear();


            }


        });


        //make the retrofit call for all houses
        allHousescall.enqueue(new Callback<ArrayList<HousesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<HousesResponse>> call, Response<ArrayList<HousesResponse>> response) {

                //fill the all houses array
                allHousesResponseArray = response.body();

                //create an array to hold the locations
                ArrayList<String> locationOptions = new ArrayList<String>();

                //add an All locations option for the user to select all available locations
                locationOptions.add(getString(R.string.ALL));

                String stringHolder;
                int rSize = response.body().size();

                //a for statement to cycle through the response and add every unique location to the locationOptions array
                for (int counter = 0; counter < rSize; counter++) {
                    stringHolder = response.body().get(counter).getLocation();
                    if (!locationOptions.contains(stringHolder)) {
                        locationOptions.add(stringHolder);
                    }
                }


                //initialize the spinner and assign it an adapter
                ArrayAdapter<String> locationSpinnerAdapter = new ArrayAdapter<>(Houses.this, android.R.layout.simple_spinner_item, locationOptions);

                //set the layout resource for the spinner adapter
                locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                houseLocationSpinner.setAdapter(locationSpinnerAdapter);

                //build out the recycler view


                //build out and assign the recycler view adapter
                HousesActivityMainRecViewAdapterClass housesAdapter = new HousesActivityMainRecViewAdapterClass(allHousesResponseArray, Houses.this);
                housesMainRecView.setAdapter(housesAdapter);


            }

            @Override
            public void onFailure(Call<ArrayList<HousesResponse>> call, Throwable t) {

                Log.d("Comms failure", "onFailure: call for all the houses failed");

            }
        });






    }

    public class sortHouses extends AsyncTask<ArrayList<HousesResponse>, Integer, DiffUtil.DiffResult> {

        RecyclerView.Adapter housesAdapter;
        ArrayList<HousesResponse> newList = new ArrayList<HousesResponse>(), oldList = new ArrayList<HousesResponse>();

        public sortHouses(RecyclerView.Adapter housesAdapter) {
            this.housesAdapter = housesAdapter;
        }


        @Override
        protected DiffUtil.DiffResult doInBackground(ArrayList<HousesResponse>... arrayLists) {

            if (oldList.isEmpty()) {
                oldList = arrayLists[1];
            }

            newList = arrayLists[0];

            housesDiffUtilCallBack mhousecallback = new housesDiffUtilCallBack(newList, oldList);

            return DiffUtil.calculateDiff(mhousecallback);
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            super.onPostExecute(diffResult);
            diffResult.dispatchUpdatesTo(housesAdapter);

            this.oldList.clear();
            this.oldList.addAll(newList);


        }
    }
}


