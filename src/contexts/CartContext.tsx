
import React, { createContext, useContext, useEffect, useState } from "react";
import { CartItem, MenuItem } from "@/lib/types";
import { getCart, saveCart } from "@/lib/mock-data";
import { toast } from "sonner";

interface CartContextType {
  cartItems: CartItem[];
  addToCart: (item: MenuItem, quantity?: number) => void;
  removeFromCart: (itemId: string) => void;
  updateQuantity: (itemId: string, quantity: number) => void;
  clearCart: () => void;
  cartTotal: number;
  itemCount: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export function CartProvider({ children }: { children: React.ReactNode }) {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  
  useEffect(() => {
    const savedCart = getCart();
    setCartItems(savedCart);
  }, []);
  
  useEffect(() => {
    saveCart(cartItems);
  }, [cartItems]);
  
  const addToCart = (item: MenuItem, quantity: number = 1) => {
    setCartItems((prevItems) => {
      const existingItem = prevItems.find(
        (cartItem) => cartItem.menuItem.id === item.id
      );
      
      if (existingItem) {
        toast.success(`Updated quantity for ${item.name}`);
        return prevItems.map((cartItem) =>
          cartItem.menuItem.id === item.id
            ? { ...cartItem, quantity: cartItem.quantity + quantity }
            : cartItem
        );
      } else {
        toast.success(`Added ${item.name} to cart`);
        return [...prevItems, { menuItem: item, quantity }];
      }
    });
  };
  
  const removeFromCart = (itemId: string) => {
    setCartItems((prevItems) => {
      const itemToRemove = prevItems.find(item => item.menuItem.id === itemId);
      if (itemToRemove) {
        toast.info(`Removed ${itemToRemove.menuItem.name} from cart`);
      }
      return prevItems.filter((item) => item.menuItem.id !== itemId);
    });
  };
  
  const updateQuantity = (itemId: string, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(itemId);
      return;
    }
    
    setCartItems((prevItems) =>
      prevItems.map((item) =>
        item.menuItem.id === itemId ? { ...item, quantity } : item
      )
    );
  };
  
  const clearCart = () => {
    setCartItems([]);
  };
  
  const cartTotal = cartItems.reduce(
    (total, item) => total + item.menuItem.price * item.quantity,
    0
  );
  
  const itemCount = cartItems.reduce((count, item) => count + item.quantity, 0);
  
  return (
    <CartContext.Provider
      value={{
        cartItems,
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart,
        cartTotal,
        itemCount,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
}
