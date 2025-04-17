
package app.lovable.quickbites;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import app.lovable.quickbites.adapters.OrdersPagerAdapter;
import app.lovable.quickbites.utils.DataManager;

public class DashboardActivity extends AppCompatActivity implements OrderActionListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DataManager dataManager;
    private User currentUser;
    private OrdersPagerAdapter ordersPagerAdapter;
    private List<Order> allOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Caterer Dashboard");

        // Initialize components
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        
        // Get current user and data manager
        dataManager = new DataManager(this);
        currentUser = dataManager.getCurrentUser();
        
        if (currentUser == null || !currentUser.isCaterer()) {
            Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load orders
        loadOrders();
    }

    private void loadOrders() {
        allOrders = dataManager.getOrders();
        
        // Setup ViewPager
        ordersPagerAdapter = new OrdersPagerAdapter(this, allOrders, this);
        viewPager.setAdapter(ordersPagerAdapter);
        
        // Setup TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Pending");
                    break;
                case 1:
                    tab.setText("Preparing");
                    break;
                case 2:
                    tab.setText("Ready");
                    break;
                case 3:
                    tab.setText("Completed/Cancelled");
                    break;
            }
        }).attach();
        
        // Update tab badges
        updateTabBadges();
    }
    
    private void updateTabBadges() {
        int pendingCount = countOrdersByStatus(Order.STATUS_PENDING);
        int preparingCount = countOrdersByStatus(Order.STATUS_PREPARING);
        int readyCount = countOrdersByStatus(Order.STATUS_READY);
        
        TabLayout.Tab pendingTab = tabLayout.getTabAt(0);
        TabLayout.Tab preparingTab = tabLayout.getTabAt(1);
        TabLayout.Tab readyTab = tabLayout.getTabAt(2);
        
        if (pendingCount > 0) {
            pendingTab.setText("Pending (" + pendingCount + ")");
        } else {
            pendingTab.setText("Pending");
        }
        
        if (preparingCount > 0) {
            preparingTab.setText("Preparing (" + preparingCount + ")");
        } else {
            preparingTab.setText("Preparing");
        }
        
        if (readyCount > 0) {
            readyTab.setText("Ready (" + readyCount + ")");
        } else {
            readyTab.setText("Ready");
        }
    }
    
    private int countOrdersByStatus(String status) {
        int count = 0;
        for (Order order : allOrders) {
            if (status.equals(order.getStatus())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void onOrderStatusChange(String orderId, String newStatus) {
        if (dataManager.updateOrderStatus(orderId, newStatus)) {
            Toast.makeText(this, "Order status updated", Toast.LENGTH_SHORT).show();
            
            // Refresh data
            loadOrders();
        } else {
            Toast.makeText(this, "Failed to update order status", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh orders in case any new orders were placed
        loadOrders();
    }
}

// Interface to communicate between adapter and activity
interface OrderActionListener {
    void onOrderStatusChange(String orderId, String newStatus);
}
