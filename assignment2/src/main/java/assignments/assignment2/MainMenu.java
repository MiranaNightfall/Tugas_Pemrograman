package assignments.assignment2;

import java.util.*;
import java.time.format.DateTimeFormatter;
import assignments.assignment1.OrderGenerator;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;
    private static User loggedInUser;

    public static void main(String[] args) {
        restoList = new ArrayList<>();
        userList = new ArrayList<>();
        initUser();
        
        printHeader();
        while (true) {
            startMenu();
            int command = input.nextInt();
            input.nextLine();
    
            if (command == 1) {
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();
    
                User userLoggedIn = getUser(nama, noTelp);
                if (userLoggedIn != null) {
                    loggedInUser = userLoggedIn;
                    boolean isLoggedIn = true;
    
                    if (loggedInUser.getRole().equals("Customer")) {
                        System.out.println("Selamat Datang " + nama + "!");
                        while (isLoggedIn) {
                            menuCustomer();
                            int commandCust = input.nextInt();
                            input.nextLine();
    
                            switch (commandCust) {
                                case 1 -> handleBuatPesanan();
                                case 2 -> handleCetakBill();
                                case 3 -> handleLihatMenu();
                                case 4 -> handleUpdateStatusPesanan();
                                case 5 -> isLoggedIn = false;
                                default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                            }
                        }
                    } else {
                        System.out.println("Selamat Datang " + nama + "!");
                        while (isLoggedIn) {
                            
                            menuAdmin();
                            int commandAdmin = input.nextInt();
                            input.nextLine();

                            switch (commandAdmin) {
                                case 1 -> handleTambahRestoran();
                                case 2 -> handleHapusRestoran();
                                case 3 -> isLoggedIn = false;
                                default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                            }
                        }
                    }
                } else {
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan");
                }
            } else if (command == 2) {
                break;
            } else {
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon) {
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    public static void handleLihatMenu() {
        System.out.println("----------------Lihat Menu----------------");
        System.out.print("Nama Restoran: ");
        String namaRestoran = input.nextLine();
        Restaurant restoran = findRestaurant(namaRestoran);
        if (restoran != null) {
            ArrayList<Menu> menuRestoran = restoran.getMenu();
            menuRestoran.sort(Comparator.comparing(Menu::getHarga).thenComparing(Menu::getNamaMakanan));
            System.out.println("Menu:");
            for (int i = 0; i < menuRestoran.size(); i++) {
                Menu menu = menuRestoran.get(i);
                System.out.println((i + 1) + ". " + menu.getNamaMakanan() + " " + menu.getHarga());
            }
        } else {
            System.out.println("Restoran " + namaRestoran + " tidak terdaftar pada sistem.");
        }
    }

    public static void handleTambahRestoran() {
        System.out.println("----------------Tambah Restoran----------------");
        System.out.print("Nama: ");
        String namaRestoran = input.nextLine();
        if (namaRestoran.length() < 4) {
            System.out.println("Nama Restoran tidak valid!");
            return;
        }
    
        if (findRestaurant(namaRestoran) != null) {
            System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda");
            return;
        }
    
        System.out.print("Jumlah Makanan: ");
        int jumlahMakanan = input.nextInt();
        input.nextLine();
    
        ArrayList<Menu> menuRestoran = new ArrayList<>();
        for (int i = 0; i < jumlahMakanan; i++) {
            String menuMakanan = input.nextLine();
            String[] formattedMenu = menuMakanan.split(" ");
    
            String namaMakanan = "";
            for (int j = 0; j < formattedMenu.length - 1; j++) {
                namaMakanan += formattedMenu[j] + " ";
            }
            namaMakanan = namaMakanan.trim();
    
            if (namaMakanan.equals("")) {
                System.out.println("Input menu tidak valid");
                return;
            }
    
            double hargaMakanan;
            try {
                hargaMakanan = Double.parseDouble(formattedMenu[formattedMenu.length - 1]);
            } catch (NumberFormatException e) {
                System.out.println("Harga menu harus bilangan bulat!");
                return;
            }
    
            menuRestoran.add(new Menu(namaMakanan, hargaMakanan));
        }
        restoList.add(new Restaurant(namaRestoran, menuRestoran));
        System.out.println("Restaurant " + namaRestoran + " berhasil ditambahkan.");
        System.out.println(restoList);
    }
    
    public static void handleHapusRestoran() {
        System.out.println("----------------Hapus Restoran----------------");
        System.out.print("Nama Restoran: ");
        String namaRestoran = input.nextLine();
        Restaurant restoran = findRestaurant(namaRestoran);
        if (restoran != null) {
            restoList.remove(restoran);
            System.out.println("Restoran berhasil dihapus.");
        } else {
            System.out.println("Restoran tidak terdaftar pada sistem.");
        }
    }

    public static void handleCetakBill() {
        System.out.print("Masukkan Order ID: ");
        String orderID = input.nextLine();
        int totalBiaya = 0;
        
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
                System.out.println("- " + item.getNamaMakanan() + " " + item.getHarga());
                totalBiaya += item.getHarga();
            }
            System.out.println("Biaya Ongkos Kirim: Rp " + generateBill(loggedInUser));
            totalBiaya = totalBiaya + generateBill(loggedInUser);
            System.out.println("Total Biaya: Rp " + totalBiaya);
        } else {
            System.out.println("Order dengan ID " + orderID + " tidak ditemukan.");
        }
    }

    public static Order findOrder(String orderID) {
        for (User user : userList) {
            if (user.getOrders() != null) {
                for (Order order : user.getOrders()) {
                    if (order.getOrderID().equals(orderID)) {
                        return order;
                    }
                }
            }
        }
        return null;
    }

    public static void handleBuatPesanan() {
        String orderID;
        String namaRestoran;
        String tanggalPemesanan;
        ArrayList<Menu> items = new ArrayList<>();
        Restaurant restoran;
        System.out.println("----------------Buat Pesanan----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine();
            restoran = findRestaurant(namaRestoran);
            if (restoran == null) {
                System.out.println("Restoran tidak terdaftar pada sistem\n");
                continue;
            }
    
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalPemesanan = input.nextLine();
            if (!OrderGenerator.checkDate(tanggalPemesanan, DateTimeFormatter.ofPattern("dd/MM/yyyy"))) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }
    
            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = input.nextInt();
            input.nextLine();
            System.out.println("Order:");
    
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
        String nomorTelepon = loggedInUser.getNomorTelepon();
        orderID = OrderGenerator.generateOrderID(namaRestoran, tanggalPemesanan, nomorTelepon);
        Order newOrder = new Order(orderID, tanggalPemesanan, generateBill(loggedInUser), restoran, items, false);
        loggedInUser.getOrders().add(newOrder);
    
        System.out.println("Pesanan dengan ID " + orderID + " berhasil ditambahkan.");
    }
    
    public static void menuAdmin() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void printHeader() {
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu() {
        System.out.println("\nSelamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static int generateBill(User loggedInUser) {
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

    public static void initUser() {
        userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
        userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
        userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
        userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
        userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));
        userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
        userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    private static Restaurant findRestaurant(String namaRestoran) {
        for (Restaurant restaurant : restoList) {
            if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                return restaurant;
            }
        }
        return null;
    }

    private static Menu findMenu(Restaurant restaurant, String namaMenu) {
        for (Menu menu : restaurant.getMenu()) {
            if (menu.getNamaMakanan().equalsIgnoreCase(namaMenu)) {
                return menu;
            }
        }
        return null;
    }

    public static void handleUpdateStatusPesanan() {
        System.out.println("----------------Update Status Pesanan----------------");
        System.out.print("Order ID: ");
        String orderID = input.nextLine();
        if (findOrder(orderID) == null) {
            System.out.println("Order ID tidak dapat ditemukan.");
            return;
        }

        for (User user : userList) {
            if (user.getOrders() != null) {
                for (Order order : user.getOrders()) {
                    if (order.getOrderID().equals(orderID)) {
                        System.out.print("Status: ");
                        String newStatus = input.nextLine();
                        if (order.isOrderFinished()) {
                            if (newStatus.equalsIgnoreCase("Selesai")){
                                System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
                            } else if (newStatus.equalsIgnoreCase("not finished")) {
                                order.setOrderFinished(false);
                                System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                            } else {
                                System.out.println("Input status tidak valid!");
                            }
                        } else {
                            if (newStatus.equalsIgnoreCase("Selesai")){
                                order.setOrderFinished(true);
                                System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                            } else if (newStatus.equalsIgnoreCase("not finished")) {
                                System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
                            } else {
                                System.out.println("Input status tidak valid!");

                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}