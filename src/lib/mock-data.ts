
import { MenuItem, Order, OrderStatus, User, UserRole } from "./types";
import { v4 as uuidv4 } from 'uuid';

// Sample menu items with realistic images
export const menuItems: MenuItem[] = [
  {
    id: "1",
    name: "Classic Burger",
    description: "Juicy beef patty with lettuce, tomato, cheese, and our special sauce",
    price: 120,
    category: "Burgers",
    imageUrl: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "2",
    name: "Veggie Sandwich",
    description: "Fresh vegetables, cheese, and pesto on whole grain bread",
    price: 90,
    category: "Sandwiches",
    imageUrl: "https://images.unsplash.com/photo-1540914124281-342587941389?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "3",
    name: "Chicken Caesar Wrap",
    description: "Grilled chicken with Caesar dressing, romaine lettuce, and parmesan",
    price: 110,
    category: "Wraps",
    imageUrl: "https://images.unsplash.com/photo-1626700051175-6818013e1d4f?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "4",
    name: "French Fries",
    description: "Crispy golden fries seasoned with our special spice blend",
    price: 60,
    category: "Sides",
    imageUrl: "https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "5",
    name: "Chocolate Milkshake",
    description: "Thick, creamy chocolate shake topped with whipped cream",
    price: 80,
    category: "Beverages",
    imageUrl: "https://images.unsplash.com/photo-1572490122747-3968b75cc699?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "6",
    name: "Margherita Pizza",
    description: "Classic pizza with tomato sauce, mozzarella, and basil",
    price: 180,
    category: "Pizza",
    imageUrl: "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "7",
    name: "Caesar Salad",
    description: "Fresh romaine lettuce, parmesan cheese, croutons, and Caesar dressing",
    price: 100,
    category: "Salads",
    imageUrl: "https://images.unsplash.com/photo-1551248429-40975aa4de74?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
  {
    id: "8",
    name: "Masala Chai",
    description: "Spiced tea brewed with milk and sweetened to perfection",
    price: 40,
    category: "Beverages",
    imageUrl: "https://images.unsplash.com/photo-1561336313-0bd5e0b27ec8?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
  },
];

// Menu categories
export const categories = Array.from(
  new Set(menuItems.map((item) => item.category))
);

// Mock users
export const sampleUsers: User[] = [
  {
    id: "user1",
    email: "student1@college.edu",
    name: "Alex Student",
    role: UserRole.CUSTOMER,
  },
  {
    id: "user2",
    email: "caterer1@canteen.edu",
    name: "Sam Caterer",
    role: UserRole.CATERER,
  },
];

// Mock orders
export const sampleOrders: Order[] = [
  {
    id: "order1",
    userId: "user1",
    items: [
      {
        menuItemId: "1",
        name: "Classic Burger",
        price: 120,
        quantity: 1,
      },
      {
        menuItemId: "4",
        name: "French Fries",
        price: 60,
        quantity: 1,
      },
    ],
    status: OrderStatus.COMPLETED,
    total: 180,
    orderedAt: new Date(Date.now() - 24 * 60 * 60 * 1000), // 1 day ago
    couponCode: "QB-1234",
  },
  {
    id: "order2",
    userId: "user1",
    items: [
      {
        menuItemId: "6",
        name: "Margherita Pizza",
        price: 180,
        quantity: 1,
      },
      {
        menuItemId: "5",
        name: "Chocolate Milkshake",
        price: 80,
        quantity: 2,
      },
    ],
    status: OrderStatus.READY,
    total: 340,
    orderedAt: new Date(Date.now() - 30 * 60 * 1000), // 30 minutes ago
    couponCode: "QB-5678",
  },
];

// Local storage keys
export const STORAGE_KEYS = {
  USERS: 'quickbites-users',
  CURRENT_USER: 'quickbites-current-user',
  ORDERS: 'quickbites-orders',
  CART: 'quickbites-cart',
};

// Helper functions for mock data persistence
export const initializeLocalStorage = () => {
  if (!localStorage.getItem(STORAGE_KEYS.USERS)) {
    localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(sampleUsers));
  }
  
  if (!localStorage.getItem(STORAGE_KEYS.ORDERS)) {
    localStorage.setItem(STORAGE_KEYS.ORDERS, JSON.stringify(sampleOrders));
  }
};

export const getUsers = (): User[] => {
  const usersJson = localStorage.getItem(STORAGE_KEYS.USERS);
  return usersJson ? JSON.parse(usersJson) : [];
};

export const getCurrentUser = (): User | null => {
  const userJson = localStorage.getItem(STORAGE_KEYS.CURRENT_USER);
  return userJson ? JSON.parse(userJson) : null;
};

export const setCurrentUser = (user: User | null) => {
  if (user) {
    localStorage.setItem(STORAGE_KEYS.CURRENT_USER, JSON.stringify(user));
  } else {
    localStorage.removeItem(STORAGE_KEYS.CURRENT_USER);
  }
};

export const getOrders = (): Order[] => {
  const ordersJson = localStorage.getItem(STORAGE_KEYS.ORDERS);
  return ordersJson ? JSON.parse(ordersJson) : [];
};

export const saveOrder = (order: Order) => {
  const orders = getOrders();
  orders.push(order);
  localStorage.setItem(STORAGE_KEYS.ORDERS, JSON.stringify(orders));
};

export const updateOrderStatus = (orderId: string, status: OrderStatus) => {
  const orders = getOrders();
  const orderIndex = orders.findIndex(order => order.id === orderId);
  
  if (orderIndex >= 0) {
    orders[orderIndex].status = status;
    localStorage.setItem(STORAGE_KEYS.ORDERS, JSON.stringify(orders));
    return true;
  }
  
  return false;
};

export const addUser = (user: Omit<User, "id">): User => {
  const users = getUsers();
  const newUser = { ...user, id: uuidv4() };
  users.push(newUser);
  localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
  return newUser;
};

export const generateCouponCode = (): string => {
  return `QB-${Math.floor(1000 + Math.random() * 9000)}`;
};

// Functions for cart manipulation
export const getCart = () => {
  const cartJson = localStorage.getItem(STORAGE_KEYS.CART);
  return cartJson ? JSON.parse(cartJson) : [];
};

export const saveCart = (cart: any[]) => {
  localStorage.setItem(STORAGE_KEYS.CART, JSON.stringify(cart));
};

export const clearCart = () => {
  localStorage.removeItem(STORAGE_KEYS.CART);
};

export const getUserOrders = (userId: string): Order[] => {
  const orders = getOrders();
  return orders.filter(order => order.userId === userId);
};

export const getCatererOrders = (): Order[] => {
  const orders = getOrders();
  // Sort by date, newest first but put pending and preparing at the top
  return orders.sort((a, b) => {
    // First by status priority
    const statusPriority = {
      [OrderStatus.PENDING]: 0,
      [OrderStatus.PREPARING]: 1,
      [OrderStatus.READY]: 2,
      [OrderStatus.COMPLETED]: 3,
      [OrderStatus.CANCELLED]: 4,
    };
    
    const aPriority = statusPriority[a.status];
    const bPriority = statusPriority[b.status];
    
    if (aPriority !== bPriority) {
      return aPriority - bPriority;
    }
    
    // Then by date
    return new Date(b.orderedAt).getTime() - new Date(a.orderedAt).getTime();
  });
};
