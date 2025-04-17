
package app.lovable.quickbites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.lovable.quickbites.adapters.OrderAdapter;
import app.lovable.quickbites.utils.DataManager;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView ordersRecyclerView;
    private TextView emptyView;
    private OrderAdapter orderAdapter;
    private List<Order> orders;
    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize components
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        emptyView = findViewById(R.id.empty_orders_text);
        
        // Get current user and data manager
        dataManager = new DataManager(this);
        currentUser = dataManager.getCurrentUser();
        
        if (currentUser == null) {
            finish(); // Go back if not logged in
            return;
        }

        // Load orders
        loadOrders();
    }

    private void loadOrders() {
        orders = dataManager.getUserOrders(currentUser.getId());
        
        if (orders.isEmpty()) {
            ordersRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            ordersRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            
            orderAdapter = new OrderAdapter(this, orders);
            ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ordersRecyclerView.setAdapter(orderAdapter);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh orders in case any status has changed
        loadOrders();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
