package assignments.assignment3.modifiedClass;

import java.util.ArrayList;

import assignments.assignment3.payment.DepeFoodPaymentSystem;

public class User {
    // Inisialisasi variabel
    private String nama;
    private String nomorTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory; 
    private DepeFoodPaymentSystem payment; // A
    private long saldo;

    // Constructor method 
    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem payment, long saldo) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        this.orderHistory = new ArrayList<>();
        this.payment = payment;
        this.saldo = saldo;
    }

    // Getter dan setter method
    public ArrayList<Order> getOrders() {
        return orderHistory;
    }

    public void setOrders(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DepeFoodPaymentSystem getPayment() {
        return payment;
    }

    public void setPayment(DepeFoodPaymentSystem payment) {
        this.payment = payment;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}