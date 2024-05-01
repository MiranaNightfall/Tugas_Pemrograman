package assignments.assignment3.systemCLI;

import assignments.assignment3.modifiedClass.User;
import assignments.assignment3.modifiedClass.Restaurant;
import assignments.assignment3.modifiedClass.Order;
import assignments.assignment1.OrderGenerator;
import assignments.assignment3.modifiedClass.Menu;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerSystemCLI extends UserSystemCLI {
    private ArrayList<User> users;
    private ArrayList<Restaurant> restaurants;
    private User loggedInUser;

    public void run(String nama, String noTelp) {
        loggedInUser = getUserByNameAndPhone(nama, noTelp);
        if (loggedInUser != null) {
            System.out.println("Selamat Datang " + nama + "!");
            boolean isLoggedIn = true;
            while (isLoggedIn) {
                displayMenu();
                int command = input.nextInt();
                input.nextLine();
                isLoggedIn = handleMenu(command);
            }
        } else {
            System.out.println("Pengguna tidak ditemukan.");
        }
    }
    
    private User getUserByNameAndPhone(String nama, String noTelp) {
        for (User user : users) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(noTelp)) {
                return user;
            }
        }
        return null;
    }

    public CustomerSystemCLI(ArrayList<User> users, ArrayList<Restaurant> restaurants) {
        this.users = users;
        this.restaurants = restaurants;
        this.input = new Scanner(System.in);
        this.loggedInUser = null; // Inisialisasi loggedInUser menjadi null
    }

    @Override
    public boolean handleMenu(int choice) {
        switch (choice) {
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    @Override
    public void displayMenu() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    void handleBuatPesanan() {
        String orderID;
        String namaRestoran;
        String tanggalPemesanan;
        ArrayList<Menu> items = new ArrayList<>();
        Restaurant restoran;
        System.out.println("----------------Buat Pesanan----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine();

            // Handling apabila restoran tidak terdaftar
            restoran = findRestaurant(namaRestoran);
            if (restoran == null) {
                System.out.println("Restoran tidak terdaftar pada sistem\n");
                continue;
            }
    
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalPemesanan = input.nextLine();

            // Handling validasi format tanggal
            if (!OrderGenerator.checkDate(tanggalPemesanan, DateTimeFormatter.ofPattern("dd/MM/yyyy"))) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }
            
            // Input jumlah pesanan
            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = input.nextInt(); // Input dijamin valid/(integer)
            input.nextLine();
            System.out.println("Order:");
            
            // Input jumlah menu yang ingin dipesan + handling error
            for (int i = 0; i < jumlahPesanan; i++) {
                while (true) {
                    String namaMenu = input.nextLine();
                    Menu menu = findMenu(restoran, namaMenu);
                    if (menu == null) {
                        System.out.println("Menu tidak ditemukan di restoran ini!");
                    } else {
                        items.add(menu);
                        break;
                    }
                }
            }
            break;
        }
        // Mendaftarkan order ID yang telah dipesan
        String nomorTelepon = loggedInUser.getNomorTelepon();
        orderID = OrderGenerator.generateOrderID(namaRestoran, tanggalPemesanan, nomorTelepon);
        Order newOrder = new Order(orderID, tanggalPemesanan, generateBill(loggedInUser), restoran, items, false);
        loggedInUser.getOrders().add(newOrder);
    
        System.out.println("Pesanan dengan ID " + orderID + " berhasil ditambahkan.");
    }

    void handleCetakBill() {
        System.out.println("----------------Cetak Bill----------------");
        // Apabila tidak ada order ID yang terdaftar -> terjadi infinite loop
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderID = input.nextLine();
            double totalBiaya = 0;
            
            // Apabila order ditemukan -> cetak bill sesuai dengan yang dipesan
            Order order = findOrder(orderID);
            if (order != null) {    
                System.out.println("\nBill:");
                System.out.println("Order ID: " + order.getOrderID());
                System.out.println("Tanggal Pemesanan: " + order.getTanggalPemesanan());
                System.out.println("Restaurant: " + order.getRestaurant().getNama());
                System.out.println("Lokasi Pengiriman: " + loggedInUser.getLokasi());
                System.out.print("Status Pengiriman: ");
                System.out.println(order.isOrderFinished() ? "Selesai" : "Not Finished");
                System.out.println("Pesanan:");
                for (Menu item : order.getItems()) {
                    // Formatting harga pada setiap menu yang dipesan
                    String formattedHarga = String.format("%.2f", item.getHarga());
                    if (formattedHarga.endsWith("0")) {
                        formattedHarga = String.valueOf((int) item.getHarga());
                    }
                    System.out.println("- " + item.getNamaMakanan() + " " + formattedHarga);
                    totalBiaya += item.getHarga();
                }
                System.out.println("Biaya Ongkos Kirim: Rp " + generateBill(loggedInUser));
                totalBiaya += generateBill(loggedInUser);

                // Formatting harga total biaya
                String formattedTotalBiaya = String.format("%.2f", totalBiaya);
                if (formattedTotalBiaya.endsWith("0")) {
                    formattedTotalBiaya = String.valueOf((int) totalBiaya);
                }
                System.out.println("Total Biaya: Rp " + formattedTotalBiaya);
                break;
            } else {
                System.out.println("Order ID tidak dapat ditemukan.\n");
            }
        }
    }

    void handleLihatMenu() {
        System.out.println("----------------Lihat Menu----------------");
        // Apabila tidak ada restoran yang terdaftar -> terjadi infinite loop
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Apabila restoran ditemukan -> tampilkan menu yang ada pada restoran
            Restaurant restoran = findRestaurant(namaRestoran);
            if (restoran != null) {
                ArrayList<Menu> menuRestoran = restoran.getMenu();

                // Sorting berdasarkan harga. apabila harga sama -> sorting berdasarkan alfabetis
                sortMenu(menuRestoran, 0);
                System.out.println("Menu:");
                for (int i = 0; i < menuRestoran.size(); i++) {
                    Menu menu = menuRestoran.get(i);
                    double harga = menu.getHarga();
                    String formattedHarga;
                    if (harga == (int) harga) {
                        formattedHarga = String.format("%.0f", harga);
                    } else {
                        formattedHarga = String.format("%.2f", harga);
                    }
                    System.out.println((i + 1) + ". " + menu.getNamaMakanan() + " " + formattedHarga);
                }
                break;
            } else {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
            }
        }
    }

    private static Menu findMenu(Restaurant restaurant, String namaMenu) {
        for (Menu menu : restaurant.getMenu()) {
            if (menu.getNamaMakanan().equals(namaMenu)) {
                return menu;
            }
        }
        return null;
    }

    // Method untuk sorting berdasarkan harga. apabila harga sama -> sorting berdasarkan alfabetis
    public static void sortMenu(ArrayList<Menu> menuRestoran, int startIndex) {
        if (startIndex >= menuRestoran.size() - 1) { 
            return;
        }
        for (int index1 = startIndex; index1 < menuRestoran.size(); index1++) {
            for (int index2 = index1+1; index2 < menuRestoran.size(); index2++) {
                Menu menu1 = menuRestoran.get(index1);
                Menu menu2 = menuRestoran.get(index2);
    
                /* Apabila ada perbedaan harga -> sort dari yang terkecil
                   Harga sama? urutkan secara alfabetis
                */
                if ((menu1.getHarga() == menu2.getHarga() && compareMenu(menu1.getNamaMakanan(), menu2.getNamaMakanan()) > 0) || (menu1.getHarga() > menu2.getHarga())) {
                    menuRestoran.set(index1, menu2);
                    menuRestoran.set(index2, menu1);
                }
            }
        }
        sortMenu(menuRestoran, startIndex + 1);
    }
    
    // Method untuk membandingkan menu makanan berdasarkan alfabetis
    public static int compareMenu(String string1, String string2) {
        int minLength = Math.min(string1.length(), string2.length());
        for (int i = 0; i < minLength; i++) {
            if (string1.charAt(i) != string2.charAt(i)) {
                System.out.println(string1.charAt(i) - string2.charAt(i));
                return string1.charAt(i) - string2.charAt(i);
            }
        }
        return string1.length() - string2.length();
    }

    void handleBayarBill() {
        // Implementasi method untuk handle ketika customer ingin membayar bill
        System.out.println("----------------Bayar Bill----------------");
        System.out.print("Masukkan Order ID: ");
        String orderId = input.nextLine();

        Order order = findOrder(orderId);
        if (order == null) {
            System.out.println("Order ID tidak dapat ditemukan.");
        } else if (order.isOrderFinished()) {
            System.out.println("Pesanan dengan ID ini sudah lunas!");
        } else {
            System.out.println();
            printBill(order);

            System.out.println("\nOpsi Pembayaran:");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit");
            System.out.print("Pilihan Metode Pembayaran: ");
            int paymentChoice = input.nextInt();
            input.nextLine();

            DepeFoodPaymentSystem paymentSystem;
            if (paymentChoice == 1) {
                paymentSystem = new CreditCardPayment();
            } else if (paymentChoice == 2) {
                paymentSystem = new DebitPayment(loggedInUser.getSaldo()); // Menambahkan parameter saldo
            } else {
                System.out.println("Metode pembayaran tidak valid!");
                return;
            }

            if (!loggedInUser.getPayment().getClass().equals(paymentSystem.getClass())) {
                System.out.println("User belum memiliki metode pembayaran ini!");
                return;
            }

            long totalBiaya = calculateTotalBiaya(order);
            try {
                long paymentAmount = paymentSystem.processPayment(totalBiaya);
                if (paymentAmount > 0) {
                    loggedInUser.setSaldo(loggedInUser.getSaldo() - paymentAmount);
                    order.getRestaurant().setSaldo(order.getRestaurant().getSaldo() + (totalBiaya - paymentAmount));
                    order.setOrderFinished(true);
                    System.out.println("Berhasil Membayar Bill sebesar Rp " + totalBiaya + (paymentSystem instanceof CreditCardPayment ? " dengan biaya transaksi sebesar Rp " + (paymentAmount - totalBiaya) : ""));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void handleUpdateStatusPesanan() {
        System.out.println("----------------Update Status Pesanan----------------");
        // Apabila tidak ada order ID yang terdaftar -> terjadi infinite loop
        while (true) {
            System.out.print("Order ID: ");
            String orderID = input.nextLine();
            Order order = findOrder(orderID);
            
            // Handling apabila order ID tidak ditemukan
            if (order == null) {
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
            
            // Update status
            System.out.print("Status: ");
            String newStatus = input.nextLine();
            
            // Apabila status berhasil diupdate
            if (!order.isOrderFinished() && newStatus.equalsIgnoreCase("Selesai")) {
                order.setOrderFinished(newStatus.equalsIgnoreCase("Selesai"));
                System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                break;

            // Apabila status tidak berhasil diupdate
            } else {
                System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!\n");
            }
        }
    }

    void handleCekSaldo() {
        // Implementasi method untuk handle ketika customer ingin mengecek saldo yang dimiliki
        System.out.println("Sisa saldo sebesar Rp " + loggedInUser.getSaldo());
    }

    private Order findOrder(String orderId) {
        for (Order order : loggedInUser.getOrders()) {
            if (order.getOrderID().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    private void printBill(Order order) {
        System.out.println("Bill:");
        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Tanggal Pemesanan: " + order.getTanggalPemesanan());
        System.out.println("Restaurant: " + order.getRestaurant().getNama());
        System.out.println("Lokasi Pengiriman: " + loggedInUser.getLokasi());
        System.out.println("Status Pengiriman: " + (order.isOrderFinished() ? "Selesai" : "Not Finished"));
        System.out.println("Pesanan:");
        for (Menu item : order.getItems()) {
            System.out.println("- " + item.getNamaMakanan() + " " + item.getHarga());
        }
        System.out.println("Biaya Ongkos Kirim: Rp " + order.getBiayaOngkosKirim());
    }

    private static int generateBill(User loggedInUser) {
        String lokasi = loggedInUser.getLokasi();
        if (lokasi.toUpperCase().equals("P")) {
            return 10000;
        } else if (lokasi.toUpperCase().equals("U")) {
            return 20000;
        } else if (lokasi.toUpperCase().equals("T")) {
            return 35000; 
        } else if (lokasi.toUpperCase().equals("S")) {
            return 40000;
        } else {
            return 60000;
        }
    }

    private Restaurant findRestaurant(String namaRestoran) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                return restaurant;
            }
        }
        return null;
    }

    private long calculateTotalBiaya(Order order) {
        long totalBiaya = 0;
        for (Menu item : order.getItems()) {
            totalBiaya += item.getHarga();
        }
        totalBiaya += order.getBiayaOngkosKirim();
        return totalBiaya;
    }
}