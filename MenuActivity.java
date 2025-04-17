
package app.lovable.quickbites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView menuRecyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");

        // Initialize RecyclerView
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        // Load menu items
        loadMenuItems();
        
        // Setup adapter
        menuAdapter = new MenuAdapter(this, menuItems, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                Toast.makeText(MenuActivity.this, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                // Here you would add the item to the cart
            }
        });
        
        menuRecyclerView.setAdapter(menuAdapter);

        // Setup cart button
        findViewById(R.id.cart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to cart activity
                // Intent intent = new Intent(MenuActivity.this, CartActivity.class);
                // startActivity(intent);
                Toast.makeText(MenuActivity.this, "Cart functionality not implemented in this example", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMenuItems() {
        menuItems = new ArrayList<>();
        
        // Add sample menu items
        menuItems.add(new MenuItem("1", "Burger", "Delicious beef burger with cheese", 150.0, "Fast Food", "https://example.com/burger.jpg"));
        menuItems.add(new MenuItem("2", "Pizza", "Margherita pizza with extra cheese", 250.0, "Italian", "https://example.com/pizza.jpg"));
        menuItems.add(new MenuItem("3", "Salad", "Fresh garden salad", 120.0, "Healthy", "https://example.com/salad.jpg"));
        menuItems.add(new MenuItem("4", "Ice Cream", "Vanilla ice cream", 80.0, "Dessert", "https://example.com/icecream.jpg"));
        menuItems.add(new MenuItem("5", "Sandwich", "Club sandwich with fries", 180.0, "Fast Food", "https://example.com/sandwich.jpg"));
        menuItems.add(new MenuItem("6", "Pasta", "Spaghetti with tomato sauce", 200.0, "Italian", "https://example.com/pasta.jpg"));
    }
}
