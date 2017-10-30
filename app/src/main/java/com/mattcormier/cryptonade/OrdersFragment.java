package com.mattcormier.cryptonade;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mattcormier.cryptonade.clients.APIClient;

import java.util.Timer;

public class OrdersFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "OrdersFragment";
    ListView lvOpenOrders;
    ListView lvOrderTransactions;
    Spinner spnPairs;
    Spinner spnClients;
    TextView tvRightHeader;
    TextView tvOrderTransactionsRightHeader;

    MainActivity mainActivity;
    View ordersView;
    Context context;

    APIClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        mainActivity = (MainActivity) getActivity();
        ordersView = inflater.inflate(R.layout.orders_layout, container, false);

        tvRightHeader = ordersView.findViewById(R.id.tvOrdersHeaderRight);
        tvOrderTransactionsRightHeader = ordersView.findViewById(R.id.tvOrdertransactionsHeaderRight);
        spnPairs = (Spinner) mainActivity.findViewById(R.id.spnPairs);
        spnPairs.setOnItemSelectedListener(this);
        spnClients = (Spinner) mainActivity.findViewById(R.id.spnClients);
        spnClients.setOnItemSelectedListener(this);

        context = getActivity();
        lvOpenOrders = (ListView) ordersView.findViewById(R.id.lvOpenOrders);
        lvOrderTransactions = (ListView) ordersView.findViewById(R.id.lvOrdertransactions);

        setHasOptionsMenu(true);

        updateOrdersFrag();
        return ordersView;
    }

    public void updateOrdersFrag() {
        Log.d(TAG, "updateOrdersFrag: ");
        tvRightHeader.setText(((Spinner)spnPairs).getSelectedItem().toString());
        tvOrderTransactionsRightHeader.setText(((Spinner)spnPairs).getSelectedItem().toString());
        final APIClient client = (APIClient) spnClients.getSelectedItem();
        client.UpdateOpenOrders(context);
        client.UpdateOrderTransactions(context);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        lvOpenOrders.invalidateViews();
        lvOrderTransactions.invalidateViews();

        if (parent.getId() == spnClients.getId()) {
            mainActivity.UpdatePairsSpinner();
            ((APIClient)spnClients.getSelectedItem()).UpdateBalances(context);
        }
        updateOrdersFrag();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh) {
            updateOrdersFrag();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
