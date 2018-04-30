package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import static com.udacity.sandwichclub.utils.JsonUtils.parseSandwichJson;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    TextView mAlsoKnownAs;
    TextView mPlaceOfOrigin;
    TextView mDescription;
    TextView mIngredients;
    TextView mPlaceOfOriginLabel;
    TextView mAlsoKnownAsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void populateUI(Sandwich sandwich) {

        mAlsoKnownAsLabel = (TextView) findViewById(R.id.also_known_as_label);
        mAlsoKnownAs = (TextView) findViewById(R.id.also_known_tv);
            if (sandwich.getAlsoKnownAs() != null && !sandwich.getAlsoKnownAs().isEmpty()) {
                for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                    mAlsoKnownAs.append(sandwich.getAlsoKnownAs().get(i) + "\n");
                }
            } else {
                mAlsoKnownAs.setVisibility(View.GONE);
                mAlsoKnownAsLabel.setVisibility(View.GONE);
            }
        mPlaceOfOriginLabel = (TextView) findViewById(R.id.place_of_origin_label);
        mPlaceOfOrigin = (TextView) findViewById(R.id.origin_tv);
            if(!TextUtils.isEmpty(sandwich.getPlaceOfOrigin())){
                mPlaceOfOrigin.setText(sandwich.getPlaceOfOrigin());
            }else {
                mPlaceOfOrigin.setVisibility(View.GONE);
                mPlaceOfOriginLabel.setVisibility(View.GONE);
        }

        mDescription = (TextView) findViewById(R.id.description_tv);
        mDescription.setText(sandwich.getDescription());

        mIngredients = (TextView) findViewById(R.id.ingredients_tv);
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                mIngredients.append(sandwich.getIngredients().get(i) + "\n");
            }


        }



    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}




