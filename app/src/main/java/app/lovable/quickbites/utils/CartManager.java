
package app.lovable.quickbites.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.lovable.quickbites.CartItem;
import app.lovable.quickbites.MenuItem;

public class CartManager {
    private static final String PREF_NAME = "CartPrefs";
    private static final String CART_ITEMS_KEY = "cartItems";
    
    private Context context;
    private SharedPreferences preferences;
    private List<CartItem> cartItems;
    
    public CartManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.cartItems = loadCartItems();
    }
    
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public void addToCart(MenuItem menuItem) {
        // Check if item already exists in cart
        for (CartItem item : cartItems) {
            if (item.getId().equals(menuItem.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                saveCartItems();
                Toast.makeText(context, menuItem.getName() + " quantity updated", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        // If not, add as new item
        CartItem newItem = new CartItem(
                menuItem.getId(), 
                menuItem.getName(), 
                menuItem.getPrice(), 
                1, 
                menuItem.getImageUrl()
        );
        cartItems.add(newItem);
        saveCartItems();
        Toast.makeText(context, menuItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }
    
    public void updateQuantity(String itemId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getId().equals(itemId)) {
                if (quantity <= 0) {
                    removeItem(itemId);
                } else {
                    item.setQuantity(quantity);
                    saveCartItems();
                }
                return;
            }
        }
    }
    
    public void removeItem(String itemId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId().equals(itemId)) {
                cartItems.remove(i);
                saveCartItems();
                return;
            }
        }
    }
    
    public void clearCart() {
        cartItems.clear();
        saveCartItems();
    }
    
    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    private List<CartItem> loadCartItems() {
        String json = preferences.getString(CART_ITEMS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
    
    private void saveCartItems() {
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(cartItems);
        editor.putString(CART_ITEMS_KEY, json);
        editor.apply();
    }
}
