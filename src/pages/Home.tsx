
import React from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { UserRole } from "@/lib/types";

export default function Home() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      
      <main className="flex-1">
        {/* Hero Section */}
        <section className="hero-gradient text-white py-16 md:py-24">
          <div className="page-container">
            <div className="grid md:grid-cols-2 gap-8 items-center">
              <div className="space-y-6">
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold">
                  Order Campus Food Without the Wait
                </h1>
                <p className="text-lg md:text-xl opacity-90">
                  Skip the lines and get notified when your food is ready. Order ahead from your favorite campus eateries.
                </p>
                <div className="flex flex-wrap gap-4">
                  {!user ? (
                    <>
                      <Button size="lg" className="bg-white text-qb-orange hover:bg-white/90" asChild>
                        <Link to="/signup">Create Account</Link>
                      </Button>
                      <Button size="lg" variant="outline" className="border-white text-white hover:bg-white/10" asChild>
                        <Link to="/login">Login</Link>
                      </Button>
                    </>
                  ) : user.role === UserRole.CUSTOMER ? (
                    <Button size="lg" className="bg-white text-qb-orange hover:bg-white/90" asChild>
                      <Link to="/menu">Order Now</Link>
                    </Button>
                  ) : (
                    <Button size="lg" className="bg-white text-qb-orange hover:bg-white/90" asChild>
                      <Link to="/dashboard">Manage Orders</Link>
                    </Button>
                  )}
                </div>
              </div>
              <div className="hidden md:block">
                <img 
                  src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600&auto=format&fit=crop&q=80" 
                  alt="Delicious campus food" 
                  className="rounded-lg shadow-xl"
                />
              </div>
            </div>
          </div>
        </section>
        
        {/* How It Works Section */}
        <section className="py-16 bg-gray-50">
          <div className="page-container">
            <h2 className="text-3xl md:text-4xl font-bold text-center mb-12">
              How QuickBites Works
            </h2>
            <div className="grid md:grid-cols-3 gap-8">
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="w-16 h-16 bg-qb-light-orange rounded-full text-qb-orange flex items-center justify-center mx-auto mb-4">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M9 14h6"/><path d="M9 18h6"/><path d="M4 8h.01"/><path d="M7 8h.01"/><path d="M10 8h.01"/></svg>
                </div>
                <h3 className="text-xl font-bold mb-2">Browse & Order</h3>
                <p className="text-gray-600">
                  Browse the menu and place your order through the app
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="w-16 h-16 bg-qb-light-orange rounded-full text-qb-orange flex items-center justify-center mx-auto mb-4">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M7 10h10"/><path d="M7 14h10"/><path d="M9 18h6"/><path d="M10 6h4"/></svg>
                </div>
                <h3 className="text-xl font-bold mb-2">Track Status</h3>
                <p className="text-gray-600">
                  Get real-time updates as your order is prepared
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="w-16 h-16 bg-qb-light-orange rounded-full text-qb-orange flex items-center justify-center mx-auto mb-4">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 7v10m0 0 3-3m-3 3-3-3"/><rect width="20" height="14" x="2" y="3" rx="2"/><path d="M12 17v4"/><path d="M8 21h8"/></svg>
                </div>
                <h3 className="text-xl font-bold mb-2">Pickup Ready Food</h3>
                <p className="text-gray-600">
                  Receive a notification when your order is ready for pickup
                </p>
              </div>
            </div>
          </div>
        </section>
        
        {/* Features Section */}
        <section className="py-16">
          <div className="page-container">
            <div className="grid md:grid-cols-2 gap-12 items-center">
              <div>
                <img 
                  src="https://images.unsplash.com/photo-1583608205776-bfd35f0d9f83?w=500&auto=format&fit=crop&q=80" 
                  alt="Mobile app showing food order" 
                  className="rounded-lg shadow-lg"
                />
              </div>
              <div className="space-y-6">
                <h2 className="text-3xl md:text-4xl font-bold">
                  Streamlined Campus Dining
                </h2>
                <div className="space-y-4">
                  <div className="flex gap-4">
                    <div className="flex-shrink-0 h-10 w-10 rounded-full bg-qb-light-orange flex items-center justify-center text-qb-orange">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20Z"/><path d="m9 12 2 2 4-4"/></svg>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold mb-1">Digital Coupons</h3>
                      <p className="text-gray-600">No more paper coupons or waiting in line - everything is digital</p>
                    </div>
                  </div>
                  <div className="flex gap-4">
                    <div className="flex-shrink-0 h-10 w-10 rounded-full bg-qb-light-orange flex items-center justify-center text-qb-orange">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="m8 6 4-4 4 4"/><path d="M12 2v10.3"/><rect width="12" height="10" x="6" y="12"/></svg>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold mb-1">Real-time Updates</h3>
                      <p className="text-gray-600">Know exactly when your food will be ready for pickup</p>
                    </div>
                  </div>
                  <div className="flex gap-4">
                    <div className="flex-shrink-0 h-10 w-10 rounded-full bg-qb-light-orange flex items-center justify-center text-qb-orange">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/></svg>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold mb-1">Easy Payments</h3>
                      <p className="text-gray-600">Multiple payment options for your convenience</p>
                    </div>
                  </div>
                </div>
                <Button className="bg-qb-orange hover:bg-qb-orange/90" size="lg" asChild>
                  <Link to={user ? "/menu" : "/signup"}>
                    {user ? "Order Now" : "Get Started"}
                  </Link>
                </Button>
              </div>
            </div>
          </div>
        </section>
      </main>
      
      <footer className="bg-gray-900 text-white py-8">
        <div className="page-container">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="flex items-center gap-2 mb-4 md:mb-0">
              <div className="bg-gradient-to-r from-qb-orange to-qb-yellow rounded-md p-1">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-white">
                  <path d="M21.96 11.22a10 10 0 0 0-21.91 0c.26 3.8 3.45 7.35 6.07 10.55a2.55 2.55 0 0 0 4.01 0c2.62-3.2 5.8-6.75 6.07-10.55Z"/>
                  <circle cx="12" cy="8" r="1"/>
                  <path d="M12 13v3"/>
                </svg>
              </div>
              <span className="font-display text-xl font-bold">QuickBites</span>
            </div>
            <div className="text-center md:text-right text-sm">
              <p>© 2023 QuickBites. All rights reserved.</p>
              <p className="text-gray-400 mt-1">Developed for Campus Dining</p>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
