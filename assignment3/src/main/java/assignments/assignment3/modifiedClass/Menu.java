package assignments.assignment3.modifiedClass;

public class Menu {
    // Inisialisasi attribut
    private String namaMakanan;
    private double harga;

    // Constructor method
    public Menu(String namaMakanan, double harga) {
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    // Getter dan setter method 
    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
