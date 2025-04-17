
import React from "react";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { Navigate } from "react-router-dom";
import { UserRole, OrderStatus } from "@/lib/types";
import { getCatererOrders, updateOrderStatus } from "@/lib/mock-data";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { format } from "date-fns";
import { toast } from "sonner";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

export default function Dashboard() {
  const { user } = useAuth();
  const [orders, setOrders] = React.useState(getCatererOrders());
  
  // Redirect if not logged in or not a caterer
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  if (user.role !== UserRole.CATERER) {
    return <Navigate to="/menu" />;
  }
  
  // Helper function to change order status
  const changeOrderStatus = (orderId: string, status: OrderStatus) => {
    const success = updateOrderStatus(orderId, status);
    
    if (success) {
      toast.success(`Order status updated to ${status}`);
      // Refresh orders from storage
      setOrders(getCatererOrders());
    } else {
      toast.error("Failed to update order status");
    }
  };
  
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
  
  // Filter orders by status
  const pendingOrders = orders.filter(order => order.status === OrderStatus.PENDING);
  const preparingOrders = orders.filter(order => order.status === OrderStatus.PREPARING);
  const readyOrders = orders.filter(order => order.status === OrderStatus.READY);
  const completedOrders = orders.filter(order => order.status === OrderStatus.COMPLETED || order.status === OrderStatus.CANCELLED);
  
  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      <main className="flex-1 bg-gray-50">
        <div className="page-container py-8">
          <h1 className="text-3xl font-bold mb-2">Caterer Dashboard</h1>
          <p className="text-gray-600 mb-8">Manage and update order status</p>
          
          <div className="grid grid-cols-4 gap-4 mb-8">
            <Card className="bg-yellow-50 border-yellow-200">
              <CardHeader className="pb-2">
                <CardTitle className="text-yellow-800">Pending</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-4xl font-bold text-yellow-600">{pendingOrders.length}</p>
              </CardContent>
            </Card>
            <Card className="bg-blue-50 border-blue-200">
              <CardHeader className="pb-2">
                <CardTitle className="text-blue-800">Preparing</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-4xl font-bold text-blue-600">{preparingOrders.length}</p>
              </CardContent>
            </Card>
            <Card className="bg-green-50 border-green-200">
              <CardHeader className="pb-2">
                <CardTitle className="text-green-800">Ready</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-4xl font-bold text-green-600">{readyOrders.length}</p>
              </CardContent>
            </Card>
            <Card className="bg-gray-50 border-gray-200">
              <CardHeader className="pb-2">
                <CardTitle className="text-gray-800">Completed</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-4xl font-bold text-gray-600">{completedOrders.length}</p>
              </CardContent>
            </Card>
          </div>
          
          <Tabs defaultValue="pending">
            <TabsList className="mb-8">
              <TabsTrigger value="pending">
                Pending ({pendingOrders.length})
              </TabsTrigger>
              <TabsTrigger value="preparing">
                Preparing ({preparingOrders.length})
              </TabsTrigger>
              <TabsTrigger value="ready">
                Ready ({readyOrders.length})
              </TabsTrigger>
              <TabsTrigger value="completed">
                Completed ({completedOrders.length})
              </TabsTrigger>
            </TabsList>
            
            <TabsContent value="pending" className="mt-0">
              {renderOrderList(pendingOrders, [
                { label: "Start Preparing", status: OrderStatus.PREPARING, color: "bg-blue-500 hover:bg-blue-600" },
                { label: "Cancel", status: OrderStatus.CANCELLED, color: "bg-red-500 hover:bg-red-600" }
              ])}
            </TabsContent>
            
            <TabsContent value="preparing" className="mt-0">
              {renderOrderList(preparingOrders, [
                { label: "Mark as Ready", status: OrderStatus.READY, color: "bg-green-500 hover:bg-green-600" }
              ])}
            </TabsContent>
            
            <TabsContent value="ready" className="mt-0">
              {renderOrderList(readyOrders, [
                { label: "Mark as Completed", status: OrderStatus.COMPLETED, color: "bg-gray-500 hover:bg-gray-600" }
              ])}
            </TabsContent>
            
            <TabsContent value="completed" className="mt-0">
              {renderOrderList(completedOrders, [])}
            </TabsContent>
          </Tabs>
        </div>
      </main>
    </div>
  );
  
  function renderOrderList(orders: any[], actions: { label: string, status: OrderStatus, color: string }[]) {
    if (orders.length === 0) {
      return (
        <div className="bg-white rounded-lg shadow p-6 text-center">
          <p className="text-gray-500">No orders in this category</p>
        </div>
      );
    }
    
    return (
      <div className="space-y-4">
        {orders.map((order) => (
          <Card key={order.id}>
            <CardHeader className="pb-2">
              <div className="flex justify-between">
                <div>
                  <div className="text-sm text-gray-500">
                    Order #{order.id.slice(6)} • {order.couponCode}
                  </div>
                  <CardTitle className="text-lg">
                    {format(new Date(order.orderedAt), "dd MMM yyyy, h:mm a")}
                  </CardTitle>
                </div>
                <div className="flex flex-col items-end">
                  {getStatusBadge(order.status)}
                  <div className="text-sm font-medium mt-1">₹{order.total.toFixed(2)}</div>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div>
                <h3 className="font-semibold mb-2">Order Items:</h3>
                <ul className="divide-y">
                  {order.items.map((item: any, index: number) => (
                    <li key={index} className="py-1.5 flex justify-between">
                      <div>
                        <span className="font-medium">{item.quantity}x</span> {item.name}
                      </div>
                      <div className="font-medium">₹{(item.price * item.quantity).toFixed(2)}</div>
                    </li>
                  ))}
                </ul>
              </div>
              
              {actions.length > 0 && (
                <div className="mt-4 flex gap-2 justify-end">
                  {actions.map((action) => (
                    <Button
                      key={action.status}
                      className={action.color}
                      onClick={() => changeOrderStatus(order.id, action.status)}
                    >
                      {action.label}
                    </Button>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        ))}
      </div>
    );
  }
}
