
import React, { useState } from "react";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { Navigate } from "react-router-dom";
import { UserRole } from "@/lib/types";
import { categories, menuItems } from "@/lib/mock-data";
import { useCart } from "@/contexts/CartContext";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus, Minus } from "lucide-react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

export default function Menu() {
  const { user } = useAuth();
  const { addToCart, cartItems } = useCart();
  const [selectedCategory, setSelectedCategory] = useState<string>("all");
  
  // Redirect if not logged in or not a customer
  if (!user) {
    return <Navigate to="/login" />;
  }
  
  if (user.role !== UserRole.CUSTOMER) {
    return <Navigate to="/dashboard" />;
  }
  
  const filteredItems = selectedCategory === "all" 
    ? menuItems 
    : menuItems.filter(item => item.category === selectedCategory);
  
  // Function to check if item is in cart
  const getItemQuantityInCart = (itemId: string): number => {
    const cartItem = cartItems.find(item => item.menuItem.id === itemId);
    return cartItem ? cartItem.quantity : 0;
  };
  
  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      <main className="flex-1 bg-gray-50">
        <div className="page-container py-8">
          <h1 className="text-3xl font-bold mb-6">Menu</h1>
          
          <Tabs defaultValue="all" className="mb-8">
            <TabsList className="mb-8 flex flex-wrap">
              <TabsTrigger 
                value="all" 
                onClick={() => setSelectedCategory("all")}
              >
                All Items
              </TabsTrigger>
              {categories.map((category) => (
                <TabsTrigger 
                  key={category} 
                  value={category}
                  onClick={() => setSelectedCategory(category)}
                >
                  {category}
                </TabsTrigger>
              ))}
            </TabsList>
            
            <TabsContent value={selectedCategory} className="mt-0">
              <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                {filteredItems.map((item) => {
                  const itemQuantity = getItemQuantityInCart(item.id);
                  
                  return (
                    <Card key={item.id} className="overflow-hidden">
                      <div className="aspect-video relative overflow-hidden">
                        <img 
                          src={item.imageUrl} 
                          alt={item.name} 
                          className="w-full h-full object-cover transition-transform hover:scale-105 duration-300"
                        />
                        <Badge className="absolute top-2 right-2 bg-qb-orange">
                          ₹{item.price}
                        </Badge>
                      </div>
                      <CardHeader className="pb-3">
                        <h3 className="font-bold text-lg">{item.name}</h3>
                      </CardHeader>
                      <CardContent className="pb-4">
                        <p className="text-sm text-gray-600">{item.description}</p>
                      </CardContent>
                      <CardFooter>
                        {itemQuantity === 0 ? (
                          <Button 
                            onClick={() => addToCart(item)} 
                            className="w-full bg-qb-orange hover:bg-qb-orange/90"
                          >
                            Add to Cart
                          </Button>
                        ) : (
                          <div className="flex items-center justify-between w-full">
                            <Button 
                              variant="outline" 
                              size="icon" 
                              onClick={() => addToCart(item, -1)}
                            >
                              <Minus size={16} />
                            </Button>
                            <span className="font-medium">{itemQuantity}</span>
                            <Button 
                              variant="outline" 
                              size="icon" 
                              onClick={() => addToCart(item)}
                            >
                              <Plus size={16} />
                            </Button>
                          </div>
                        )}
                      </CardFooter>
                    </Card>
                  );
                })}
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </main>
    </div>
  );
}
