
import React from "react";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { Navigate } from "react-router-dom";
import { UserRole, OrderStatus } from "@/lib/types";
import { getUserOrders } from "@/lib/mock-data";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { format } from "date-fns";

export default function Orders() {
  const { user } = useAuth();
  
  // Redirect if not logged in or not a customer
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  if (user.role !== UserRole.CUSTOMER) {
    return <Navigate to="/dashboard" />;
  }
  
  const orders = getUserOrders(user.id);
  
  // Helper function for order status badge
  const getStatusBadge = (status: OrderStatus) => {
    switch (status) {
      case OrderStatus.PENDING:
        return <Badge className="bg-yellow-500">Pending</Badge>;
      case OrderStatus.PREPARING:
        return <Badge className="bg-blue-500">Preparing</Badge>;
      case OrderStatus.READY:
        return <Badge className="bg-green-500">Ready for pickup</Badge>;
      case OrderStatus.COMPLETED:
        return <Badge className="bg-gray-500">Completed</Badge>;
      case OrderStatus.CANCELLED:
        return <Badge className="bg-red-500">Cancelled</Badge>;
      default:
        return <Badge>Unknown</Badge>;
    }
  };
  
  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      <main className="flex-1 bg-gray-50">
        <div className="page-container py-8">
          <h1 className="text-3xl font-bold mb-6">Your Orders</h1>
          
          {orders.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <div className="text-gray-500">
                <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="mx-auto mb-4">
                  <rect width="18" height="18" x="3" y="3" rx="2" ry="2" />
                  <line x1="3" x2="21" y1="9" y2="9" />
                  <path d="M8 12h.01" />
                  <path d="M12 12h.01" />
                  <path d="M16 12h.01" />
                  <path d="M8 16h.01" />
                  <path d="M12 16h.01" />
                  <path d="M16 16h.01" />
                </svg>
                <h2 className="text-xl font-semibold mb-2">No orders yet</h2>
                <p>You haven't placed any orders yet.</p>
              </div>
            </div>
          ) : (
            <div className="space-y-6">
              {orders.map((order) => (
                <Card key={order.id} className="overflow-hidden">
                  <CardHeader className="bg-gray-50">
                    <div className="flex justify-between items-center">
                      <div>
                        <div className="text-sm text-gray-500">
                          Order #
                          <span className="font-mono">{order.id.slice(6)}</span>
                        </div>
                        <CardTitle className="text-lg">
                          {format(new Date(order.orderedAt), "dd MMM yyyy, h:mm a")}
                        </CardTitle>
                      </div>
                      <div className="flex flex-col items-end gap-1">
                        {getStatusBadge(order.status)}
                        <div className="text-sm font-medium">₹{order.total.toFixed(2)}</div>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent className="pt-4">
                    <div className="mb-4">
                      <h3 className="font-semibold mb-2">Coupon Code</h3>
                      <div className="bg-gray-100 p-2 rounded font-mono text-center">
                        {order.couponCode}
                      </div>
                    </div>
                    
                    <h3 className="font-semibold mb-2">Order Items</h3>
                    <ul className="divide-y">
                      {order.items.map((item, index) => (
                        <li key={index} className="py-2 flex justify-between">
                          <div>
                            <span className="font-medium">{item.quantity}x</span> {item.name}
                          </div>
                          <div className="font-medium">₹{(item.price * item.quantity).toFixed(2)}</div>
                        </li>
                      ))}
                    </ul>
                    
                    {order.status === OrderStatus.READY && (
                      <div className="mt-4 bg-green-50 p-3 rounded-md border border-green-200 flex items-start gap-3">
                        <div className="text-green-600 mt-0.5">
                          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                            <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6" />
                            <polyline points="15 3 21 3 21 9" />
                            <line x1="10" x2="21" y1="14" y2="3" />
                          </svg>
                        </div>
                        <div>
                          <p className="font-medium text-green-800">Your order is ready for pickup!</p>
                          <p className="text-sm text-green-700">Please collect it from the counter using your coupon code.</p>
                        </div>
                      </div>
                    )}
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
