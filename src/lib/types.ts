
export enum UserRole {
  CUSTOMER = 'customer',
  CATERER = 'caterer',
}

export type User = {
  id: string;
  email: string;
  name: string;
  role: UserRole;
};

export enum OrderStatus {
  PENDING = 'pending',
  PREPARING = 'preparing',
  READY = 'ready',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled',
}

export type MenuItem = {
  id: string;
  name: string;
  description: string;
  price: number;
  category: string;
  imageUrl: string;
};

export type CartItem = {
  menuItem: MenuItem;
  quantity: number;
};

export type Order = {
  id: string;
  userId: string;
  items: OrderItem[];
  status: OrderStatus;
  total: number;
  orderedAt: Date;
  couponCode: string;
};

export type OrderItem = {
  menuItemId: string;
  name: string;
  price: number;
  quantity: number;
};
