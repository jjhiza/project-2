package com.elysium.nodlebar;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FoodDBHelper mFoodDBHelper;
    private Cursor cursor;
    private ProgressBar mProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);

        insertFoodDataAsync();

        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, "");
        onNewIntent(intent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().equals("")) {

                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, "");
                    onNewIntent(intent);
                }

                return false;
            }
        });

        ComponentName componentName = new ComponentName(this, this.getClass());

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            mFoodDBHelper = FoodDBHelper.getInstance(this);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);


            cursor = mFoodDBHelper.getFoodBySearch(query);

            if (!cursor.moveToFirst()){
                Toast.makeText(this, "0 Results Found", Toast.LENGTH_SHORT).show();
            }

            FoodListCursorAdapter foodListCursorAdapter = new FoodListCursorAdapter(this, cursor);
            mRecyclerView.setAdapter(foodListCursorAdapter);
        }
    }

    private void insertFoodDataAsync() { //TODO edit "Ramen" category

        Food food1 = new Food (
                1,
                500,
                R.drawable.ramen_1,
                "Chicken soyu Ramen",
                "Subtle, fresh flavors that goes down easy.",
                "Ramen",
                " Noodle Bowl ",
                13);

        Food food2 = new Food (
                2,
                500,
                R.drawable.ramen_2,
                "Chili Ramen w/ ground Chicken",
                "An intense blast of spice that is sure to fire up your day.",
                "Ramen",
                " Noodle Bowl ",
                18);

        Food food3 = new Food (
                3,
                500,
                R.drawable.ramen_3,
                "Spicy Paitan Ramen w/ Chicken",
                "Not as hot as the Chili bowl, but still has enough kick to make you notice.",
                "Ramen",
                " Noodle Bowl ",
                14);

        Food food4 = new Food (
                4,
                500,
                R.drawable.ramen_4,
                "Mega Paitan",
                "With chicken and Rayu, this king-sized bowl is packed with flavor, and enough food to keep you full all day.",
                "Mega Ramen",
                " Noodle Bowl ",
                24);

        Food food5 = new Food (
                5,
                500,
                R.drawable.ramen_5,
                "Mobile Ramen",
                "A small bowl of delicious ramen, designed to be eaten wherever you are.",
                "Ramen",
                " Noodle Bowl ",
                7);

        Food food6 = new Food (
                6,
                500,
                R.drawable.ramen_6,
                "Spicy Ramen w/ Diced Egg",
                "Simple yet flavorful, this bowl of noodles strikes the perfect balance.",
                "Ramen",
                " Noodle Bowl ",
                5);

        Food food7 = new Food (
                7,
                500,
                R.drawable.ramen_7,
                "Spring-time Ramen",
                "Packed with fresh eggs, bean sprouts, veggies, chicken, and a hint of citrus, this ramen has all the brightness of a crisp Spring day. ",
                "Ramen",
                " Noodle Bowl ",
                12);

        Food food8 = new Food (
                8,
                500,
                R.drawable.ramen_8,
                "Semi-spicy Chicken Soyu Ramen",
                "A hint of heat, and fresh ingredients make this a perfect ramen for any occasion.",
                "Ramen",
                " Noodle Bowl ",
                15);

        Food food9 = new Food (
                9,
                500,
                R.drawable.ramen_9,
                "Pepper flake Ramen",
                "Hot, crunchy, AND vegetarian? Yup, this ramen has it all.",
                "Ramen",
                " Noodle Bowl ",
                10);

        Food food10 = new Food (
                10,
                500,
                R.drawable.ramen_10,
                "Mastodon Soyu Ramen",
                "A mammoth mountain of mouth-watering chicken, veggies, and eggs - get a taste of this ramen before it goes extinct. ",
                "Mega Ramen",
                " Noodle Bowl ",
                24);

        final List<Food> foods = new ArrayList<>(10);

        foods.add(food1);
        foods.add(food2);
        foods.add(food3);
        foods.add(food4);
        foods.add(food5);
        foods.add(food6);
        foods.add(food7);
        foods.add(food8);
        foods.add(food9);
        foods.add(food10);

        final FoodDBHelper foodDBHelper = FoodDBHelper.getInstance(this);

        AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressbar.setMax(10);
                mProgressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                for(int i = 0; i < 10; i++) {
                    foodDBHelper.newFood(foods.get(i));
                    publishProgress(i + 1);
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mProgressbar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mProgressbar.setVisibility(View.INVISIBLE);
            }
        };

        User user = new User(1, "jay", "1234");

        Constants.user = user;

        foodDBHelper.newUser(Constants.user);

        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.btnGoToBasket) {
            Intent intent = new Intent (this, BasketActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertFoodData() { //TODO - Why is this not being used??????

        Food food1 = new Food (
                1,
                500,
                R.drawable.ramen_1,
                "Chicken soyu Ramen",
                "Subtle, fresh flavors that goes down easy.",
                "Ramen",
                " Noodle Bowl ",
                13);

        Food food2 = new Food (
                2,
                500,
                R.drawable.ramen_2,
                "Chili Ramen w/ ground Chicken",
                "An intense blast of spice that is sure to fire up your day.",
                "Ramen",
                " Noodle Bowl ",
                18);

        Food food3 = new Food (
                3,
                500,
                R.drawable.ramen_3,
                "Spicy Paitan Ramen w/ Chicken",
                "Not as hot as the Chili bowl, but still has enough kick to make you notice.",
                "Ramen",
                " Noodle Bowl ",
                14);

        Food food4 = new Food (
                4,
                500,
                R.drawable.ramen_4,
                "Mega Paitan",
                "With chicken and Rayu, this king-sized bowl is packed with flavor, and enough food to keep you full all day.",
                "Mega Ramen",
                " Noodle Bowl ",
                24);

        Food food5 = new Food (
                5,
                500,
                R.drawable.ramen_5,
                "Mobile Ramen",
                "A small bowl of delicious ramen, designed to be eaten wherever you are.",
                "Ramen",
                " Noodle Bowl ",
                7);

        Food food6 = new Food (
                6,
                500,
                R.drawable.ramen_6,
                "Spicy Ramen w/ Diced Egg",
                "Simple yet flavorful, this bowl of noodles strikes the perfect balance.",
                "Ramen",
                " Noodle Bowl ",
                5);

        Food food7 = new Food (
                7,
                500,
                R.drawable.ramen_7,
                "Spring-time Ramen",
                "Packed with fresh eggs, bean sprouts, veggies, chicken, and a hint of citrus, this ramen has all the brightness of a crisp Spring day. ",
                "Ramen",
                " Noodle Bowl ",
                12);

        Food food8 = new Food (
                8,
                500,
                R.drawable.ramen_8,
                "Semi-spicy Chicken Soyu Ramen",
                "A hint of heat, and fresh ingredients make this a perfect ramen for any occasion.",
                "Ramen",
                " Noodle Bowl ",
                15);

        Food food9 = new Food (
                10,
                500,
                R.drawable.ramen_9,
                "Mastodon Soyu Ramen",
                "A mammoth mountain of mouth-watering chicken, veggies, and eggs - get a taste of this ramen before it goes extinct. ",
                "Mega Ramen",
                " Noodle Bowl ",
                24);

        Food food10 = new Food(
                9,
                500,
                R.drawable.ramen_10,
                "Pepper flake Ramen",
                "Hot, crunchy, AND vegetarian? Yup, this ramen has it all.",
                "Ramen",
                " Noodle Bowl ",
                10);


        FoodDBHelper foodDBHelper = FoodDBHelper.getInstance(this);

        foodDBHelper.newFood(food1);
        foodDBHelper.newFood(food2);
        foodDBHelper.newFood(food3);
        foodDBHelper.newFood(food4);
        foodDBHelper.newFood(food5);
        foodDBHelper.newFood(food6);
        foodDBHelper.newFood(food7);
        foodDBHelper.newFood(food8);
        foodDBHelper.newFood(food9);
        foodDBHelper.newFood(food10);

        User user = new User(1,"jay", "1234");

        Constants.user = user;

        foodDBHelper.newUser(Constants.user);
    }
}
