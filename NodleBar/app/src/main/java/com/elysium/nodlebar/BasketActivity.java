package com.elysium.nodlebar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class BasketActivity extends AppCompatActivity implements View.OnClickListener, BasketListRecyclerViewAdapter.OnChangeQuantityListener{
    RecyclerView mRecyclerView;
    Button btnBack, btnCheckOut, btnRemoveAll;
    BasketListRecyclerViewAdapter adapter;
    UserBasket basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        setTitle("Basket");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar); Threw an error...

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        basket = UserBasket.getInstance();

        btnBack = (Button) findViewById(R.id.btnGoBack);
        btnCheckOut = (Button) findViewById(R.id.btnCheckout);
        btnRemoveAll = (Button) findViewById(R.id.btnClearAll);

        btnBack.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);
        btnRemoveAll.setOnClickListener(this);

        btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                UserBasket.getInstance().getTotalBill()));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewBasket);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnItemTouchListener(getSwipeListener());

        adapter = new BasketListRecyclerViewAdapter(basket.mFoodItemList, this, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                UserBasket.getInstance().getTotalBill()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
            case R.id.btnGoBack:
                finish();
                break;
            case R.id.btnCheckout:

                if (basket.mFoodItemList.size() > 0) {
                    getCheckoutCheckDialog().create().show();
                }

                break;

            case R.id.btnClearAll:
                if (basket.mFoodItemList.size() == 0) {

                    Toast.makeText(this, "There's nothing in your cart! Aren't you hungry?", Toast.LENGTH_SHORT).show();
                    break;
                }

                final List<FoodInBasket> temp = new LinkedList<>(basket.mFoodItemList);
                Log.i("iiiiiiiiiiii", temp.size() + "  okay");
                basket.mFoodItemList.clear();
                adapter.notifyItemRangeRemoved(0, temp.size());
                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                        UserBasket.getInstance().getTotalBill()));

                Snackbar.make(mRecyclerView,"All items removed... :(", Snackbar.LENGTH_LONG).
                        setAction("Undo", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                basket.mFoodItemList = temp;
                                adapter.setFoodItemList(basket.mFoodItemList);
                                Log.i("iiiiiiiiiiii", temp.size() + " not okay");
                                adapter.notifyItemRangeInserted(0, basket.mFoodItemList.size());
                                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                        UserBasket.getInstance().getTotalBill()));
                            }
                        })

                        .show();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public SwipeableRecyclerViewTouchListener getSwipeListener() {

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            FoodInBasket food;

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    food = basket.mFoodItemList.get(position);
                                    basket.mFoodItemList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }

                                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                        UserBasket.getInstance().getTotalBill()));
                                adapter.notifyDataSetChanged();
                                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                        UserBasket.getInstance().getTotalBill()));

                                Snackbar.make(recyclerView,"Removed item from basket.", Snackbar.LENGTH_LONG).
                                        setAction("Undo", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                basket.mFoodItemList.add(food);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }) .show();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                for (int position : reverseSortedPositions) {
                                    food = basket.mFoodItemList.get(position);
                                    basket.mFoodItemList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                        UserBasket.getInstance().getTotalBill()));

                                Snackbar.make(recyclerView,"Removed item from cart", Snackbar.LENGTH_LONG).
                                        setAction("Undo", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                basket.mFoodItemList.add(food);
                                                adapter.notifyDataSetChanged();
                                                btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                                        UserBasket.getInstance().getTotalBill()));
                                            }

                                        }).show();
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }
                        });

        return swipeTouchListener;
    }

    @Override
    public void onChangeQuantity(int total) {
        btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                total));
    }

    public static class CheckoutCheckDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_message_checkout + UserBasket.getInstance().getTotalBill() + ".00")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.not_yet, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            return builder.create();
        }
    }

    private AlertDialog.Builder getCheckoutCheckDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_message_checkout) +
                UserBasket.getInstance().getTotalBill() + ".00")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        FoodDBHelper dbHelper = FoodDBHelper.getInstance(BasketActivity.this);

                        for (FoodInBasket foodInBasket : basket.mFoodItemList) {
                            dbHelper.newFoodItem(foodInBasket.getAsFoodItem());

                            dbHelper.changeFoodCount(foodInBasket,
                                    foodInBasket.getCount() - foodInBasket.getQuantity());
                        }

                        Toast.makeText(BasketActivity.this,
                                "Let your ramen adventure begin!",
                                Toast.LENGTH_SHORT).show();

                        basket.mFoodItemList.clear();
                        btnCheckOut.setText(String.format(Locale.ENGLISH, "CHECKOUT($%d)",
                                UserBasket.getInstance().getTotalBill()));
                        adapter.notifyDataSetChanged();
                    }
                })

                .setNegativeButton(R.string.not_yet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder;

    }

}

