
import React from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/contexts/AuthContext";
import { useCart } from "@/contexts/CartContext";
import { ShoppingBag, User, LogOut } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { UserRole } from "@/lib/types";

export function MainNav() {
  const { user, logout } = useAuth();
  const { itemCount } = useCart();

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background">
      <div className="page-container">
        <div className="flex h-16 items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <div className="bg-gradient-to-r from-qb-orange to-qb-yellow rounded-md p-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-white">
                <path d="M21.96 11.22a10 10 0 0 0-21.91 0c.26 3.8 3.45 7.35 6.07 10.55a2.55 2.55 0 0 0 4.01 0c2.62-3.2 5.8-6.75 6.07-10.55Z"/>
                <circle cx="12" cy="8" r="1"/>
                <path d="M12 13v3"/>
              </svg>
            </div>
            <span className="font-display text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-qb-orange to-qb-yellow">
              QuickBites
            </span>
          </Link>

          <nav className="hidden md:flex items-center space-x-6">
            <Link to="/" className="text-foreground/70 hover:text-foreground transition-colors">
              Home
            </Link>
            {user && user.role === UserRole.CUSTOMER && (
              <>
                <Link to="/menu" className="text-foreground/70 hover:text-foreground transition-colors">
                  Menu
                </Link>
                <Link to="/orders" className="text-foreground/70 hover:text-foreground transition-colors">
                  My Orders
                </Link>
              </>
            )}
            {user && user.role === UserRole.CATERER && (
              <Link to="/dashboard" className="text-foreground/70 hover:text-foreground transition-colors">
                Dashboard
              </Link>
            )}
          </nav>

          <div className="flex items-center space-x-4">
            {user ? (
              <>
                {user.role === UserRole.CUSTOMER && (
                  <Link to="/cart" className="relative">
                    <Button variant="ghost" size="icon" aria-label="Cart">
                      <ShoppingBag size={20} />
                      {itemCount > 0 && (
                        <span className="absolute -top-1 -right-1 bg-qb-orange text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                          {itemCount}
                        </span>
                      )}
                    </Button>
                  </Link>
                )}
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon" aria-label="User menu">
                      <User size={20} />
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end" className="w-56">
                    <div className="p-2">
                      <p className="font-medium">{user.name}</p>
                      <p className="text-sm text-muted-foreground">{user.email}</p>
                    </div>
                    <DropdownMenuSeparator />
                    {user.role === UserRole.CUSTOMER ? (
                      <DropdownMenuItem asChild>
                        <Link to="/orders" className="cursor-pointer w-full">My Orders</Link>
                      </DropdownMenuItem>
                    ) : (
                      <DropdownMenuItem asChild>
                        <Link to="/dashboard" className="cursor-pointer w-full">Dashboard</Link>
                      </DropdownMenuItem>
                    )}
                    <DropdownMenuItem onClick={logout} className="text-red-500 cursor-pointer">
                      <LogOut size={16} className="mr-2" />
                      Logout
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </>
            ) : (
              <div className="flex space-x-2">
                <Button variant="outline" asChild>
                  <Link to="/login">Login</Link>
                </Button>
                <Button className="bg-qb-orange hover:bg-qb-orange/90" asChild>
                  <Link to="/signup">Sign Up</Link>
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}
