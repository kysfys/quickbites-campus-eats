
package app.lovable.quickbites;

public class User {
    private String id;
    private String name;
    private String email;
    private String role;  // "customer" or "caterer"
    
    public User(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getRole() {
        return role;
    }
    
    public boolean isCustomer() {
        return "customer".equals(role);
    }
    
    public boolean isCaterer() {
        return "caterer".equals(role);
    }
}
