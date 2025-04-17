
package app.lovable.quickbites.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import app.lovable.quickbites.Order;
import app.lovable.quickbites.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault());

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        // Set order ID and date
        holder.orderIdText.setText("Order #" + order.getId().substring(6));
        holder.orderDateText.setText(dateFormat.format(order.getOrderedAt()));
        
        // Set order status with appropriate color
        holder.orderStatusText.setText(getStatusDisplayText(order.getStatus()));
        holder.orderStatusText.setTextColor(getStatusColor(order.getStatus()));
        
        // Set coupon code
        holder.couponCodeText.setText(order.getCouponCode());
        
        // Set total
        holder.totalText.setText("₹" + order.getTotal());
        
        // Setup items recycler view
        OrderItemAdapter itemAdapter = new OrderItemAdapter(context, order.getItems());
        holder.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.itemsRecyclerView.setAdapter(itemAdapter);
        
        // Set ready message visibility
        if (Order.STATUS_READY.equals(order.getStatus())) {
            holder.readyMessageLayout.setVisibility(View.VISIBLE);
        } else {
            holder.readyMessageLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String getStatusDisplayText(String status) {
        switch (status) {
            case Order.STATUS_PENDING:
                return "Pending";
            case Order.STATUS_PREPARING:
                return "Preparing";
            case Order.STATUS_READY:
                return "Ready for pickup";
            case Order.STATUS_COMPLETED:
                return "Completed";
            case Order.STATUS_CANCELLED:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    private int getStatusColor(String status) {
        switch (status) {
            case Order.STATUS_PENDING:
                return ContextCompat.getColor(context, R.color.status_pending);
            case Order.STATUS_PREPARING:
                return ContextCompat.getColor(context, R.color.status_preparing);
            case Order.STATUS_READY:
                return ContextCompat.getColor(context, R.color.status_ready);
            case Order.STATUS_COMPLETED:
                return ContextCompat.getColor(context, R.color.status_completed);
            case Order.STATUS_CANCELLED:
                return ContextCompat.getColor(context, R.color.status_cancelled);
            default:
                return ContextCompat.getColor(context, R.color.text_secondary);
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText;
        TextView orderDateText;
        TextView orderStatusText;
        TextView couponCodeText;
        TextView totalText;
        RecyclerView itemsRecyclerView;
        View readyMessageLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id_text);
            orderDateText = itemView.findViewById(R.id.order_date_text);
            orderStatusText = itemView.findViewById(R.id.order_status_text);
            couponCodeText = itemView.findViewById(R.id.coupon_code_text);
            totalText = itemView.findViewById(R.id.order_total_text);
            itemsRecyclerView = itemView.findViewById(R.id.order_items_recycler_view);
            readyMessageLayout = itemView.findViewById(R.id.ready_message_layout);
        }
    }
    
    // Inner adapter for order items
    private class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {
        private Context context;
        private List<Order.OrderItem> items;
        
        public OrderItemAdapter(Context context, List<Order.OrderItem> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Order.OrderItem item = items.get(position);
            holder.itemNameText.setText(item.getQuantity() + "x " + item.getName());
            holder.itemPriceText.setText("₹" + (item.getPrice() * item.getQuantity()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
        
        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView itemNameText;
            TextView itemPriceText;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemNameText = itemView.findViewById(R.id.item_name_text);
                itemPriceText = itemView.findViewById(R.id.item_price_text);
            }
        }
    }
}
