package assignments.assignment3;

import assignments.assignment3.systemCLI.AdminSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.systemCLI.UserSystemCLI;

public class LoginManager {
    // Atribut final untuk menyimpan objek AdminSystemCLI dan CustomerSystemCLI
    private final AdminSystemCLI adminSystem;
    private final CustomerSystemCLI customerSystem;

    // Constructor untuk initialize object AdminSystemCLI dan CustomerSystemCLI
    public LoginManager(AdminSystemCLI adminSystem, CustomerSystemCLI customerSystem) {
        this.adminSystem = adminSystem;
        this.customerSystem = customerSystem;
    }

    // Method untuk menangkap role user yang logged in
    public UserSystemCLI getSystem(String role) {
        if (role.equals("Admin")) {
            return adminSystem;
        } else {
            return customerSystem;
        }
    }
}
