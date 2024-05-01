package assignments.assignment3.modifiedClass;
import java.util.ArrayList;

public class Restaurant {
    // Inisialisasi attribut
    private String nama;
    private ArrayList<Menu> menu;
    private long saldo; // Penambahan atribut saldo

    // Constructor method
    public Restaurant(String nama, ArrayList<Menu> menu) {
        this.nama = nama;
        this.menu = menu;
        this.saldo = 0; // Inisialisasi saldo dengan 0
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

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}