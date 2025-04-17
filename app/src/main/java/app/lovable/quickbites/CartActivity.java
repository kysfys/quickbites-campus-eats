
package app.lovable.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import app.lovable.quickbites.utils.CartManager;
import app.lovable.quickbites.utils.DataManager;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private View emptyCartLayout, cartItemsLayout;
    private CartManager cartManager;
    private DataManager dataManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // Initialize UI components
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
        emptyCartLayout = findViewById(R.id.empty_cart_layout);
        cartItemsLayout = findViewById(R.id.cart_items_layout);

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
                CartItem item = cartItems.get(position);
                cartManager.removeItem(item.getId());
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
        cartItems = cartManager.getCartItems();
    }

    private void updateTotalPrice() {
        double total = cartManager.getCartTotal();
        totalPriceTextView.setText("₹" + total);
    }

    private void showEmptyCart() {
        emptyCartLayout.setVisibility(View.VISIBLE);
        cartItemsLayout.setVisibility(View.GONE);
    }

    private void processCheckout() {
        String couponCode = DataManager.generateCouponCode();
        
        // Create order items
        List<Order.OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            orderItems.add(new Order.OrderItem(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getQuantity()
            ));
        }
        
        // Create order
        final Order newOrder = new Order(
            "order-" + UUID.randomUUID().toString(),
            currentUser.getId(),
            orderItems,
            Order.STATUS_PENDING,
            cartManager.getCartTotal(),
            new Date(),
            couponCode
        );
        
        // Save order and show success dialog
        dataManager.saveOrder(newOrder);
        
        // Show success dialog
        showOrderSuccessDialog(couponCode);
    }
    
    private void showOrderSuccessDialog(String couponCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order_success, null);
        
        TextView couponText = dialogView.findViewById(R.id.coupon_code);
        Button viewOrderButton = dialogView.findViewById(R.id.view_orders_button);
        
        couponText.setText(couponCode);
        
        builder.setView(dialogView)
               .setCancelable(false);
        
        final AlertDialog dialog = builder.create();
        dialog.show();
        
        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear cart
                cartManager.clearCart();
                
                // Go to orders activity
                Intent intent = new Intent(CartActivity.this, OrdersActivity.class);
                startActivity(intent);
                
                // Close this activity and dialog
                dialog.dismiss();
                finish();
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
