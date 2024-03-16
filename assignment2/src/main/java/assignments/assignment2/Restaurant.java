package assignments.assignment2;

import java.util.ArrayList;

public class Restaurant {
    // Inisialisasi attribut
    private String nama;
    private ArrayList<Menu> menu;

    // Constructor method
    public Restaurant(String nama, ArrayList<Menu> menu) {
        this.nama = nama;
        this.menu = menu;
    }

    // Getter dan setter method
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }
}
