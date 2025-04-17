
package app.lovable.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import app.lovable.quickbites.utils.CartManager;
import app.lovable.quickbites.utils.DataManager;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private TabLayout categoryTabs;
    private FloatingActionButton cartButton;
    private TextView cartBadgeText;
    private CartManager cartManager;
    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");
        
        // Initialize managers
        cartManager = new CartManager(this);
        dataManager = new DataManager(this);
        currentUser = dataManager.getCurrentUser();
        
        if (currentUser == null) {
            // Not logged in, go to login
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Initialize views
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        categoryTabs = findViewById(R.id.tab_layout);
        cartButton = findViewById(R.id.cart_button);
        cartBadgeText = findViewById(R.id.cart_badge);
        
        // Setup RecyclerView
        menuRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        // Load menu items
        loadMenuItems(null); // Load all items initially
        
        // Setup category tabs
        setupCategoryTabs();
        
        // Setup cart button
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, CartActivity.class));
            }
        });
        
        // Update cart badge
        updateCartBadge();
    }
    
    private void setupCategoryTabs() {
        // Clear existing tabs
        categoryTabs.removeAllTabs();
        
        // Get categories
        List<String> categories = dataManager.getCategories();
        
        // Add tabs
        for (String category : categories) {
            categoryTabs.addTab(categoryTabs.newTab().setText(category));
        }
        
        // Set tab selection listener
        categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category = tab.getText().toString();
                if ("All".equals(category)) {
                    loadMenuItems(null);
                } else {
                    loadMenuItems(category);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }

    private void loadMenuItems(String category) {
        menuItems = dataManager.getMenuItemsByCategory(category);
        
        // Setup adapter
        menuAdapter = new MenuAdapter(this, menuItems, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                cartManager.addToCart(item);
                updateCartBadge();
            }
        });
        
        menuRecyclerView.setAdapter(menuAdapter);
    }
    
    private void updateCartBadge() {
        int itemCount = cartManager.getCartItems().size();
        
        if (itemCount > 0) {
            cartBadgeText.setVisibility(View.VISIBLE);
            cartBadgeText.setText(String.valueOf(itemCount));
        } else {
            cartBadgeText.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Update cart badge in case it changed
        updateCartBadge();
    }
}
