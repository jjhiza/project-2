package com.elysium.nodlebar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class FoodDetailActivity extends AppCompatActivity {

    Toolbar mToolbar;
    long foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar); was throwing errors...

        foodId = getIntent().getLongExtra(FoodDBHelper.COL_ID_FOOD, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.home:
                finish();
                break;

            case R.id.btnGoToBasket:
                Intent intent = new Intent(this, BasketActivity.class);
                startActivity(intent);
                break;

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        super.onResume();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(FoodDBHelper.COL_ID_FOOD, foodId);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
