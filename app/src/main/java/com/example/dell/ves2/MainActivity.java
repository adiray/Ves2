package com.example.dell.ves2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    //initialize user services area views



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to your the user services area views
        ImageView HousesButton = (ImageView) findViewById(R.id.houses_services_user_area);

        HousesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the code for the houses button action

                Intent intent = new Intent(MainActivity.this,Houses.class);

                MainActivity.this.startActivity(intent);

            }




        });

    }


}