package assignments.assignment3.modifiedClass;

import java.util.ArrayList;

public class Order {
    // Inisialisasi attribut
    private String orderID;
    private String tanggalPemesanan;
    private int biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished;

    // Constructor method
    public Order(String orderID, String tanggalPemesanan, int biayaOngkosKirim, Restaurant restaurant, ArrayList<Menu> items, boolean orderFinished) {
        this.orderID = orderID;
        this.tanggalPemesanan = tanggalPemesanan;
        this.biayaOngkosKirim = biayaOngkosKirim;
        this.restaurant = restaurant;
        this.items = items;
        this.orderFinished = orderFinished;
    }

    // Getter dan setter method
    public String getOrderID() {
        return orderID;
    }

    public String getTanggalPemesanan() {
        return tanggalPemesanan;
    }

    public void setTanggalPemesanan(String tanggalPemesanan) {
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public int getBiayaOngkosKirim() {
        return biayaOngkosKirim;
    }

    public void setBiayaOngkosKirim(int biayaOngkosKirim) {
        this.biayaOngkosKirim = biayaOngkosKirim;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<Menu> getItems() {
        return items;
    }

    public void setItems(ArrayList<Menu> items) {
        this.items = items;
    }

    public boolean isOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(boolean orderFinished) {
        this.orderFinished = orderFinished;
    }

}
