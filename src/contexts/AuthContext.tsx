
import React, { createContext, useContext, useEffect, useState } from "react";
import { User, UserRole } from "@/lib/types";
import { addUser, getCurrentUser, getUsers, setCurrentUser } from "@/lib/mock-data";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  signup: (email: string, name: string, password: string, role: UserRole) => Promise<boolean>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    // Check if user is already logged in
    const storedUser = getCurrentUser();
    if (storedUser) {
      setUser(storedUser);
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    setIsLoading(true);
    
    try {
      // In a real app, you'd validate the password here
      // For now, we'll just check if the user exists
      const users = getUsers();
      const foundUser = users.find((u) => u.email === email);
      
      if (foundUser) {
        setUser(foundUser);
        setCurrentUser(foundUser);
        toast.success("Login successful");
        setIsLoading(false);
        navigate(foundUser.role === UserRole.CUSTOMER ? "/menu" : "/dashboard");
        return true;
      } else {
        toast.error("Invalid email or password");
        setIsLoading(false);
        return false;
      }
    } catch (error) {
      console.error("Login error:", error);
      toast.error("Login failed. Please try again.");
      setIsLoading(false);
      return false;
    }
  };

  const signup = async (email: string, name: string, password: string, role: UserRole) => {
    setIsLoading(true);
    
    try {
      // Check if user already exists
      const users = getUsers();
      const userExists = users.some((u) => u.email === email);
      
      if (userExists) {
        toast.error("Email already in use");
        setIsLoading(false);
        return false;
      }
      
      // Create new user
      const newUser = addUser({ email, name, role });
      setUser(newUser);
      setCurrentUser(newUser);
      toast.success("Account created successfully");
      setIsLoading(false);
      navigate(role === UserRole.CUSTOMER ? "/menu" : "/dashboard");
      return true;
    } catch (error) {
      console.error("Signup error:", error);
      toast.error("Signup failed. Please try again.");
      setIsLoading(false);
      return false;
    }
  };

  const logout = () => {
    setUser(null);
    setCurrentUser(null);
    toast.success("Logged out successfully");
    navigate("/");
  };

  return (
    <AuthContext.Provider value={{ user, isLoading, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
