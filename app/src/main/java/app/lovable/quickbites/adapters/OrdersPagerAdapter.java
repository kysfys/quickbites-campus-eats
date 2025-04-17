
package app.lovable.quickbites.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.lovable.quickbites.Order;
import app.lovable.quickbites.OrderActionListener;
import app.lovable.quickbites.R;

public class OrdersPagerAdapter extends RecyclerView.Adapter<OrdersPagerAdapter.OrderPageViewHolder> {
    private Context context;
    private List<Order> allOrders;
    private List<List<Order>> filteredOrders;
    private OrderActionListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault());
    
    // Tab indices
    private static final int TAB_PENDING = 0;
    private static final int TAB_PREPARING = 1;
    private static final int TAB_READY = 2;
    private static final int TAB_COMPLETED = 3;
    
    public OrdersPagerAdapter(Context context, List<Order> allOrders, OrderActionListener listener) {
        this.context = context;
        this.allOrders = allOrders;
        this.listener = listener;
        
        // Filter orders by status
        filterOrders();
    }
    
    private void filterOrders() {
        filteredOrders = new ArrayList<>();
        
        // Tab 0: Pending orders
        List<Order> pendingOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (Order.STATUS_PENDING.equals(order.getStatus())) {
                pendingOrders.add(order);
            }
        }
        filteredOrders.add(pendingOrders);
        
        // Tab 1: Preparing orders
        List<Order> preparingOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (Order.STATUS_PREPARING.equals(order.getStatus())) {
                preparingOrders.add(order);
            }
        }
        filteredOrders.add(preparingOrders);
        
        // Tab 2: Ready orders
        List<Order> readyOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (Order.STATUS_READY.equals(order.getStatus())) {
                readyOrders.add(order);
            }
        }
        filteredOrders.add(readyOrders);
        
        // Tab 3: Completed/Cancelled orders
        List<Order> completedOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (Order.STATUS_COMPLETED.equals(order.getStatus()) || 
                Order.STATUS_CANCELLED.equals(order.getStatus())) {
                completedOrders.add(order);
            }
        }
        filteredOrders.add(completedOrders);
    }

    @NonNull
    @Override
    public OrderPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_orders_page, parent, false);
        return new OrderPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderPageViewHolder holder, int position) {
        List<Order> orders = filteredOrders.get(position);
        
        if (orders.isEmpty()) {
            holder.emptyView.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.GONE);
        } else {
            holder.emptyView.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            
            OrderListAdapter adapter = new OrderListAdapter(context, orders, position, listener);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return filteredOrders.size();
    }

    public class OrderPageViewHolder extends RecyclerView.ViewHolder {
        TextView emptyView;
        RecyclerView recyclerView;

        public OrderPageViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyView = itemView.findViewById(R.id.empty_orders_text);
            recyclerView = itemView.findViewById(R.id.orders_recycler_view);
        }
    }
    
    // Adapter for the orders in each tab
    private class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
        private Context context;
        private List<Order> orders;
        private int tabPosition;
        private OrderActionListener listener;

        public OrderListAdapter(Context context, List<Order> orders, int tabPosition, OrderActionListener listener) {
            this.context = context;
            this.orders = orders;
            this.tabPosition = tabPosition;
            this.listener = listener;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            
            // Set order details
            holder.orderIdText.setText("Order #" + order.getId().substring(6) + " • " + order.getCouponCode());
            holder.orderDateText.setText(dateFormat.format(order.getOrderedAt()));
            holder.orderTotalText.setText("₹" + order.getTotal());
            
            // Setup items list
            StringBuilder itemsText = new StringBuilder();
            for (Order.OrderItem item : order.getItems()) {
                itemsText.append(item.getQuantity())
                        .append("x ")
                        .append(item.getName())
                        .append(" - ₹")
                        .append(item.getPrice() * item.getQuantity())
                        .append("\n");
            }
            holder.itemsText.setText(itemsText.toString().trim());
            
            // Setup action buttons based on tab
            setupActionButtons(holder, order);
        }
        
        private void setupActionButtons(OrderViewHolder holder, Order order) {
            // Clear existing buttons
            holder.actionButtonsContainer.removeAllViews();
            
            // Add appropriate buttons based on tab
            switch (tabPosition) {
                case TAB_PENDING:
                    addButton(holder, "Start Preparing", R.color.status_preparing, v -> {
                        listener.onOrderStatusChange(order.getId(), Order.STATUS_PREPARING);
                    });
                    addButton(holder, "Cancel", R.color.status_cancelled, v -> {
                        listener.onOrderStatusChange(order.getId(), Order.STATUS_CANCELLED);
                    });
                    break;
                    
                case TAB_PREPARING:
                    addButton(holder, "Mark as Ready", R.color.status_ready, v -> {
                        listener.onOrderStatusChange(order.getId(), Order.STATUS_READY);
                    });
                    break;
                    
                case TAB_READY:
                    addButton(holder, "Mark as Completed", R.color.status_completed, v -> {
                        listener.onOrderStatusChange(order.getId(), Order.STATUS_COMPLETED);
                    });
                    break;
                    
                case TAB_COMPLETED:
                    // No action buttons for completed/cancelled orders
                    break;
            }
        }
        
        private void addButton(OrderViewHolder holder, String text, int colorResId, View.OnClickListener clickListener) {
            Button button = new Button(context);
            button.setText(text);
            button.setBackgroundResource(colorResId);
            button.setTextColor(context.getResources().getColor(android.R.color.white));
            
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int) (8 * context.getResources().getDisplayMetrics().density);
            button.setLayoutParams(params);
            
            button.setOnClickListener(clickListener);
            holder.actionButtonsContainer.addView(button);
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        public class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView orderIdText;
            TextView orderDateText;
            TextView orderTotalText;
            TextView itemsText;
            ViewGroup actionButtonsContainer;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                orderIdText = itemView.findViewById(R.id.order_id_text);
                orderDateText = itemView.findViewById(R.id.order_date_text);
                orderTotalText = itemView.findViewById(R.id.order_total_text);
                itemsText = itemView.findViewById(R.id.items_text);
                actionButtonsContainer = itemView.findViewById(R.id.action_buttons_container);
            }
        }
    }
}
