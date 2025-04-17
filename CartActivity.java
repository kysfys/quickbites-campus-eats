
package app.lovable.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalPriceTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI components
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);

        // Setup RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCartItems();
        
        // Setup adapter
        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
            }
            
            @Override
            public void onItemRemoved(int position) {
                cartItems.remove(position);
                cartAdapter.notifyItemRemoved(position);
                updateTotalPrice();
                if (cartItems.isEmpty()) {
                    showEmptyCart();
                }
            }
        });
        
        cartRecyclerView.setAdapter(cartAdapter);
        
        // Update total price
        updateTotalPrice();
        
        // Set checkout button click listener
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Process checkout
                    processCheckout();
                }
            }
        });
        
        // Show empty cart message if cart is empty
        if (cartItems.isEmpty()) {
            showEmptyCart();
        }
    }

    private void loadCartItems() {
        // In a real app, this would load cart items from a database or shared preferences
        // For this example, we'll create some sample items
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("1", "Burger", 150.0, 2, "https://example.com/burger.jpg"));
        cartItems.add(new CartItem("2", "Pizza", 250.0, 1, "https://example.com/pizza.jpg"));
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPriceTextView.setText("₹" + total);
    }

    private void showEmptyCart() {
        findViewById(R.id.empty_cart_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.cart_items_layout).setVisibility(View.GONE);
    }

    private void processCheckout() {
        // In a real app, this would process the payment and create an order
        // For this example, just show a toast and navigate to order confirmation
        Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
        // Clear cart
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
        showEmptyCart();
    }
}
