
import React, { useState } from "react";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { Navigate, useNavigate } from "react-router-dom";
import { UserRole } from "@/lib/types";
import { useCart } from "@/contexts/CartContext";
import { Button } from "@/components/ui/button";
import { Minus, Plus, Trash2 } from "lucide-react";
import { generateCouponCode, saveOrder } from "@/lib/mock-data";
import { OrderStatus } from "@/lib/types";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";

export default function Cart() {
  const { user } = useAuth();
  const { cartItems, updateQuantity, removeFromCart, cartTotal, clearCart } = useCart();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [couponCode, setCouponCode] = useState("");
  const navigate = useNavigate();

  // Redirect if not logged in or not a customer
  if (!user) {
    return <Navigate to="/login" />;
  }

  if (user.role !== UserRole.CUSTOMER) {
    return <Navigate to="/dashboard" />;
  }

  const handlePlaceOrder = () => {
    if (cartItems.length === 0) {
      toast.error("Your cart is empty!");
      return;
    }

    setIsSubmitting(true);

    // Simulate API call
    setTimeout(() => {
      const code = generateCouponCode();
      setCouponCode(code);

      // Create order object
      const newOrder = {
        id: `order-${Date.now()}`,
        userId: user.id,
        items: cartItems.map(item => ({
          menuItemId: item.menuItem.id,
          name: item.menuItem.name,
          price: item.menuItem.price,
          quantity: item.quantity
        })),
        status: OrderStatus.PENDING,
        total: cartTotal,
        orderedAt: new Date(),
        couponCode: code
      };

      // Save order
      saveOrder(newOrder);
      setIsSubmitting(false);
      setShowSuccessModal(true);
    }, 1000);
  };

  const handleCloseSuccessModal = () => {
    setShowSuccessModal(false);
    clearCart();
    navigate("/orders");
  };

  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      <main className="flex-1 bg-gray-50">
        <div className="page-container py-8">
          <h1 className="text-3xl font-bold mb-6">Your Cart</h1>

          {cartItems.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <div className="text-gray-500 mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="mx-auto mb-4">
                  <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z"/>
                  <path d="M3 6h18"/>
                  <path d="M16 10a4 4 0 0 1-8 0"/>
                </svg>
                <h2 className="text-xl font-semibold mb-2">Your cart is empty</h2>
                <p className="mb-6">Looks like you haven't added anything to your cart yet.</p>
                <Button className="bg-qb-orange hover:bg-qb-orange/90" onClick={() => navigate("/menu")}>
                  Browse Menu
                </Button>
              </div>
            </div>
          ) : (
            <div className="grid md:grid-cols-3 gap-8">
              <div className="md:col-span-2">
                <div className="bg-white rounded-lg shadow overflow-hidden">
                  <ul className="divide-y">
                    {cartItems.map((item) => (
                      <li key={item.menuItem.id} className="p-4 flex items-center">
                        <div className="w-16 h-16 rounded overflow-hidden flex-shrink-0">
                          <img 
                            src={item.menuItem.imageUrl} 
                            alt={item.menuItem.name} 
                            className="w-full h-full object-cover"
                          />
                        </div>
                        <div className="ml-4 flex-grow">
                          <h3 className="font-medium">{item.menuItem.name}</h3>
                          <p className="text-sm text-gray-500">₹{item.menuItem.price} each</p>
                        </div>
                        <div className="flex items-center space-x-2">
                          <Button 
                            variant="outline" 
                            size="icon" 
                            onClick={() => updateQuantity(item.menuItem.id, item.quantity - 1)}
                          >
                            <Minus size={16} />
                          </Button>
                          <span className="w-8 text-center font-medium">{item.quantity}</span>
                          <Button 
                            variant="outline" 
                            size="icon" 
                            onClick={() => updateQuantity(item.menuItem.id, item.quantity + 1)}
                          >
                            <Plus size={16} />
                          </Button>
                          <Button 
                            variant="ghost"
                            size="icon" 
                            className="text-red-500 hover:text-red-700"
                            onClick={() => removeFromCart(item.menuItem.id)}
                          >
                            <Trash2 size={18} />
                          </Button>
                        </div>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
              <div>
                <div className="bg-white rounded-lg shadow p-6 sticky top-24">
                  <h2 className="text-xl font-semibold mb-4">Order Summary</h2>
                  <div className="space-y-3 mb-6">
                    <div className="flex justify-between">
                      <span>Subtotal</span>
                      <span>₹{cartTotal.toFixed(2)}</span>
                    </div>
                    <div className="border-t pt-3 font-medium text-lg flex justify-between">
                      <span>Total</span>
                      <span>₹{cartTotal.toFixed(2)}</span>
                    </div>
                  </div>
                  <Button 
                    className="w-full bg-qb-orange hover:bg-qb-orange/90" 
                    onClick={handlePlaceOrder}
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? "Processing..." : "Place Order"}
                  </Button>
                </div>
              </div>
            </div>
          )}
        </div>
      </main>

      <Dialog open={showSuccessModal} onOpenChange={setShowSuccessModal}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Order Placed Successfully!</DialogTitle>
            <DialogDescription>
              <div className="py-4">
                <div className="bg-green-50 rounded-lg p-4 mb-4 flex items-center gap-3">
                  <div className="bg-green-100 rounded-full p-2 text-green-600">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><path d="m9 11 3 3L22 4"/></svg>
                  </div>
                  <div>
                    <p className="font-medium">Your order has been received!</p>
                    <p className="text-sm text-gray-600">The kitchen is preparing your food.</p>
                  </div>
                </div>
                
                <div className="mb-4">
                  <p className="font-bold text-lg mb-1">Your Coupon Code:</p>
                  <div className="bg-gray-100 p-3 rounded-md text-center font-mono text-xl">
                    {couponCode}
                  </div>
                  <p className="text-sm mt-2 text-gray-500">
                    Show this code when collecting your order.
                  </p>
                </div>
                
                <Button className="w-full" onClick={handleCloseSuccessModal}>
                  View Order Status
                </Button>
              </div>
            </DialogDescription>
          </DialogHeader>
        </DialogContent>
      </Dialog>
    </div>
  );
}
