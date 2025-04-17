
package app.lovable.quickbites;

import java.util.Date;
import java.util.List;

public class Order {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PREPARING = "PREPARING";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    private String id;
    private String userId;
    private List<OrderItem> items;
    private String status;
    private double total;
    private Date orderedAt;
    private String couponCode;
    
    public Order(String id, String userId, List<OrderItem> items, String status, 
                double total, Date orderedAt, String couponCode) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.total = total;
        this.orderedAt = orderedAt;
        this.couponCode = couponCode;
    }
    
    public String getId() {
        return id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getTotal() {
        return total;
    }
    
    public Date getOrderedAt() {
        return orderedAt;
    }
    
    public String getCouponCode() {
        return couponCode;
    }
    
    public static class OrderItem {
        private String menuItemId;
        private String name;
        private double price;
        private int quantity;
        
        public OrderItem(String menuItemId, String name, double price, int quantity) {
            this.menuItemId = menuItemId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        
        public String getMenuItemId() {
            return menuItemId;
        }
        
        public String getName() {
            return name;
        }
        
        public double getPrice() {
            return price;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
}
