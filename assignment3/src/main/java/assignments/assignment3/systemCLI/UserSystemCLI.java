package assignments.assignment3.systemCLI;

import java.util.Scanner;

public abstract class UserSystemCLI {
    protected Scanner input;

    // Method yang akan berjalan ketika program dimulai
    public void run() {
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }

    // Abstract method yang akan diimplementasikan pada subclass
    abstract void displayMenu();

    // Abstract method yang akan diimplementasikan pada subclass
    abstract boolean handleMenu(int command);
}
