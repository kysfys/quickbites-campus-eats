
package app.lovable.quickbites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged();
        void onItemRemoved(int position);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText("₹" + item.getPrice());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        holder.subtotalTextView.setText("₹" + item.getSubtotal());
        
        // Load image using Glide
        Glide.with(context)
            .load(item.getImageUrl())
            .placeholder(R.drawable.placeholder_food)
            .error(R.drawable.placeholder_food)
            .into(holder.imageView);
            
        holder.decreaseButton.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                item.setQuantity(currentQuantity - 1);
                holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
                holder.subtotalTextView.setText("₹" + item.getSubtotal());
                listener.onQuantityChanged();
            }
        });
        
        holder.increaseButton.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity < 10) {
                item.setQuantity(currentQuantity + 1);
                holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
                holder.subtotalTextView.setText("₹" + item.getSubtotal());
                listener.onQuantityChanged();
            }
        });
        
        holder.removeButton.setOnClickListener(v -> {
            listener.onItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        TextView subtotalTextView;
        ImageButton decreaseButton;
        ImageButton increaseButton;
        ImageButton removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.item_price);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
            subtotalTextView = itemView.findViewById(R.id.item_subtotal);
            decreaseButton = itemView.findViewById(R.id.decrease_button);
            increaseButton = itemView.findViewById(R.id.increase_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}
