
package app.lovable.quickbites.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.lovable.quickbites.MenuItem;
import app.lovable.quickbites.Order;
import app.lovable.quickbites.User;

public class DataManager {
    private static final String PREF_NAME = "DataPrefs";
    private static final String MENU_ITEMS_KEY = "menuItems";
    private static final String ORDERS_KEY = "orders";
    private static final String CURRENT_USER_KEY = "currentUser";
    
    private Context context;
    private SharedPreferences preferences;
    
    public DataManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initializeData();
    }
    
    private void initializeData() {
        // Only initialize if data doesn't exist
        if (getMenuItems().isEmpty()) {
            List<MenuItem> menuItems = createSampleMenuItems();
            saveMenuItems(menuItems);
        }
    }
    
    private List<MenuItem> createSampleMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        
        items.add(new MenuItem("1", "Veggie Burger", "Delicious vegetable patty with fresh vegetables", 130.0, "Burgers", "https://source.unsplash.com/random/300x200?burger"));
        items.add(new MenuItem("2", "Cheese Burger", "Classic beef burger with cheddar cheese", 150.0, "Burgers", "https://source.unsplash.com/random/300x200?cheeseburger"));
        items.add(new MenuItem("3", "Double Cheese Pizza", "Pizza with extra cheese and oregano", 250.0, "Pizza", "https://source.unsplash.com/random/300x200?pizza"));
        items.add(new MenuItem("4", "Pepperoni Pizza", "Pizza topped with pepperoni and cheese", 280.0, "Pizza", "https://source.unsplash.com/random/300x200?pepperoni"));
        items.add(new MenuItem("5", "Caesar Salad", "Fresh garden salad with caesar dressing", 120.0, "Salads", "https://source.unsplash.com/random/300x200?salad"));
        items.add(new MenuItem("6", "Fruit Salad", "Mix of seasonal fresh fruits", 100.0, "Salads", "https://source.unsplash.com/random/300x200?fruitsalad"));
        items.add(new MenuItem("7", "Chocolate Ice Cream", "Rich chocolate ice cream", 80.0, "Desserts", "https://source.unsplash.com/random/300x200?icecream"));
        items.add(new MenuItem("8", "Vanilla Ice Cream", "Classic vanilla ice cream", 70.0, "Desserts", "https://source.unsplash.com/random/300x200?vanillaicecream"));
        items.add(new MenuItem("9", "French Fries", "Crispy fried potato fingers", 90.0, "Sides", "https://source.unsplash.com/random/300x200?fries"));
        items.add(new MenuItem("10", "Onion Rings", "Crispy fried onion rings", 80.0, "Sides", "https://source.unsplash.com/random/300x200?onionrings"));
        items.add(new MenuItem("11", "Coca Cola", "Refreshing cola drink", 40.0, "Beverages", "https://source.unsplash.com/random/300x200?cola"));
        items.add(new MenuItem("12", "Iced Tea", "Chilled tea with lemon", 45.0, "Beverages", "https://source.unsplash.com/random/300x200?icedtea"));
        
        return items;
    }
    
    public List<MenuItem> getMenuItems() {
        String json = preferences.getString(MENU_ITEMS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<MenuItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
    
    private void saveMenuItems(List<MenuItem> menuItems) {
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(menuItems);
        editor.putString(MENU_ITEMS_KEY, json);
        editor.apply();
    }
    
    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> allItems = getMenuItems();
        if (category == null || category.isEmpty() || category.equalsIgnoreCase("All")) {
            return allItems;
        }
        
        List<MenuItem> filteredItems = new ArrayList<>();
        for (MenuItem item : allItems) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }
    
    public List<String> getCategories() {
        List<MenuItem> allItems = getMenuItems();
        List<String> categories = new ArrayList<>();
        categories.add("All");
        
        for (MenuItem item : allItems) {
            if (!categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }
        return categories;
    }
    
    public void saveOrder(Order order) {
        List<Order> orders = getOrders();
        orders.add(order);
        
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(orders);
        editor.putString(ORDERS_KEY, json);
        editor.apply();
    }
    
    public List<Order> getOrders() {
        String json = preferences.getString(ORDERS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
    
    public List<Order> getUserOrders(String userId) {
        List<Order> allOrders = getOrders();
        List<Order> userOrders = new ArrayList<>();
        
        for (Order order : allOrders) {
            if (order.getUserId().equals(userId)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }
    
    public boolean updateOrderStatus(String orderId, String status) {
        List<Order> orders = getOrders();
        
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.setStatus(status);
                
                SharedPreferences.Editor editor = preferences.edit();
                String json = new Gson().toJson(orders);
                editor.putString(ORDERS_KEY, json);
                editor.apply();
                
                return true;
            }
        }
        return false;
    }
    
    public void saveCurrentUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(user);
        editor.putString(CURRENT_USER_KEY, json);
        editor.apply();
    }
    
    public User getCurrentUser() {
        String json = preferences.getString(CURRENT_USER_KEY, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, User.class);
    }
    
    public void clearCurrentUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CURRENT_USER_KEY);
        editor.apply();
    }
    
    public static String generateCouponCode() {
        return "QB" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
