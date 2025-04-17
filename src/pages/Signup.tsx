
import React from "react";
import { AuthForm } from "@/components/auth/AuthForm";
import { MainNav } from "@/components/layout/MainNav";
import { useAuth } from "@/contexts/AuthContext";
import { Navigate } from "react-router-dom";

export default function Signup() {
  const { user } = useAuth();
  
  // Redirect if already logged in
  if (user) {
    return <Navigate to={user.role === "customer" ? "/menu" : "/dashboard"} />;
  }

  return (
    <div className="min-h-screen flex flex-col">
      <MainNav />
      <div className="flex-1 flex items-center justify-center p-4">
        <div className="w-full max-w-md">
          <AuthForm type="signup" />
        </div>
      </div>
    </div>
  );
}
