
import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { UserRole } from "@/lib/types";
import { useAuth } from "@/contexts/AuthContext";

interface AuthFormProps {
  type: "login" | "signup";
}

export function AuthForm({ type }: AuthFormProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [role, setRole] = useState<UserRole>(UserRole.CUSTOMER);
  const { login, signup, isLoading } = useAuth();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    if (type === "login") {
      await login(email, password);
    } else {
      await signup(email, name, password, role);
    }
  };

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader>
        <CardTitle className="text-2xl font-bold">
          {type === "login" ? "Login" : "Sign Up"}
        </CardTitle>
        <CardDescription>
          {type === "login"
            ? "Enter your credentials to access your account"
            : "Create a new account to get started"}
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          {type === "signup" && (
            <div className="space-y-2">
              <Label htmlFor="name">Name</Label>
              <Input
                id="name"
                type="text"
                placeholder="Enter your name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
          )}
          
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input
              id="email"
              type="email"
              placeholder={type === "login" ? "Enter your email" : "Use your college email"}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <Input
              id="password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          
          {type === "signup" && (
            <div className="space-y-2">
              <Label>I am a:</Label>
              <RadioGroup 
                defaultValue={role}
                onValueChange={(value) => setRole(value as UserRole)}
                className="flex space-x-4"
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value={UserRole.CUSTOMER} id="customer" />
                  <Label htmlFor="customer">Student</Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value={UserRole.CATERER} id="caterer" />
                  <Label htmlFor="caterer">Canteen Staff</Label>
                </div>
              </RadioGroup>
            </div>
          )}
          
          <Button 
            type="submit" 
            className="w-full bg-qb-orange hover:bg-qb-orange/90"
            disabled={isLoading}
          >
            {isLoading ? "Processing..." : type === "login" ? "Login" : "Sign Up"}
          </Button>
        </form>
        
        <div className="mt-4 text-center text-sm">
          {type === "login" ? (
            <>
              Don't have an account?{" "}
              <a href="/signup" className="text-qb-blue hover:underline">
                Sign up
              </a>
            </>
          ) : (
            <>
              Already have an account?{" "}
              <a href="/login" className="text-qb-blue hover:underline">
                Login
              </a>
            </>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
