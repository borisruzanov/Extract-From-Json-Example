package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private Sandwich sandwich;
    private TextView tv_also, tv_origin, tv_description, tv_ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        tv_also = (TextView)findViewById(R.id.also_known_tv);
        tv_origin = (TextView)findViewById(R.id.origin_tv);
        tv_description = (TextView) findViewById(R.id.description_tv);
        tv_ingredients = (TextView) findViewById(R.id.ingredients_tv);


        Intent intent = getIntent();
        if (intent == null) {
            Log.v("secondary", "Inside checking on null");
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            Log.v("secondary", "Inside checking on position");
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            Log.v("secondary", "Inside checking on SANDWICH");
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        Log.v("primary", "returned sandwich is NOT null "+ sandwich.toString());

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        //Criteria 3
        Log.v("primary", "Data used for UI " + "\n" +
                "Name: " + sandwich.getMainName().toString() + "\n" +
                "Knows as: " + sandwich.getAlsoKnownAs().toString() + "\n" +
                "Origin: " + sandwich.getPlaceOfOrigin().toString() + "\n" +
                "Description: " + sandwich.getDescription().toString() + "\n" +
                "Image link: " + sandwich.getImage().toString() + "\n" +
                "Ingredients: " + sandwich.getIngredients().toString());
        if (sandwich.getPlaceOfOrigin().isEmpty()){
            tv_origin.setText("no data");
        }else {
            tv_origin.setText(sandwich.getPlaceOfOrigin().toString());
        }
        if (sandwich.getAlsoKnownAs().isEmpty()){
            tv_also.setText("no data");
        }else {
            tv_also.setText(listModel(sandwich.getAlsoKnownAs()));
        }
        tv_description.setText(sandwich.getDescription());
        tv_ingredients.setText(listModel(sandwich.getIngredients()));
    }

    public StringBuilder listModel(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i)).append("\n");
        }
        return stringBuilder;
    }
}
