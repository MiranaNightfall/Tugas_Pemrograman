package assignments.assignment2;

import java.util.*;
import java.time.format.DateTimeFormatter;
import assignments.assignment1.OrderGenerator;

public class MainMenu {
    // Inisialisasi variabel
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;
    private static User loggedInUser;

    // Main method
    public static void main(String[] args) {
        restoList = new ArrayList<>();
        userList = new ArrayList<>();
        initUser(); // Add default user dari template
        
        printHeader();
        while (true) {
            startMenu();
            int command = input.nextInt();
            input.nextLine();
            
            // Input perintah pada menu (dijamin valid input berupa integer)
            if (command == 1) {
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();
    
                // Handling untuk user yang tidak terdaftar pada userList
                if (getUser(nama, noTelp) != null) { 
                    loggedInUser = getUser(nama, noTelp);;
                    boolean loggedIn = true;
                    
                    // Apabila user adalah customer
                    if (loggedInUser.getRole().equals("Customer")) {
                        System.out.println("Selamat Datang " + nama + "!");
                        while (loggedIn) {
                            menuCustomer();
                            int commandCust = input.nextInt();
                            input.nextLine();
    
                            switch (commandCust) {
                                case 1 -> handleBuatPesanan();
                                case 2 -> handleCetakBill();
                                case 3 -> handleLihatMenu();
                                case 4 -> handleUpdateStatusPesanan();
                                case 5 -> loggedIn = false;
                                default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                            }
                        }
                    } else { // Apabila user adalah admin
                        System.out.println("Selamat Datang " + nama + "!");
                        while (loggedIn) {
                            
                            menuAdmin();
                            int commandAdmin = input.nextInt();
                            input.nextLine();

                            switch (commandAdmin) {
                                case 1 -> handleTambahRestoran();
                                case 2 -> handleHapusRestoran();
                                case 3 -> loggedIn = false;
                                default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                            }
                        }
                    }
                } else { // Handling apabila input pada login tidak ditemukan
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan");
                }
            } else if (command == 2) { // Keluar dari program
                System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
                break;
            } else { // Handling validasi input perintah untuk menu
                System.out.println("Input perintah tidak valid!");
            }
        }
    }

    // Method untuk menangkap user sesuai dengan input nama dan nomor telepon
    public static User getUser(String nama, String nomorTelepon) {
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    // Method untuk mencari nama restaurant (digunakan untuk mengecek eksistensi dari restoran yang diinput)
    private static Restaurant findRestaurant(String namaRestoran) {
        for (Restaurant restaurant : restoList) {
            if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                return restaurant;
            }
        }
        return null;
    }

    // Method untuk mencari menu (digunakan untuk mengecek eksistensi dari menu yang diinput)
    private static Menu findMenu(Restaurant restaurant, String namaMenu) {
        for (Menu menu : restaurant.getMenu()) {
            if (menu.getNamaMakanan().equals(namaMenu)) {
                return menu;
            }
        }
        return null;
    }

    // Method untuk mencari order ID (digunakan untuk mengecek eksistensi dari order ID yang diinput)
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

    // Method untuk generate biaya ongkos kirim berdasarkan lokasi User
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

    // Method untuk menambahkan restoran (role: Admin)
    public static void handleTambahRestoran() {
        System.out.println("----------------Tambah Restoran----------------");
        while (true) {
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine();

            // Handling apabila nama restoran kurang dari 4 karakter
            if (namaRestoran.length() < 4) { 
                System.out.println("Nama Restoran tidak valid!");
                continue;
            }
            
            // Handling apabila nama restoran sudah terdaftar
            if (findRestaurant(namaRestoran) != null) {
                System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda");
                continue;
            }
            
            // Input jumlah makanan pada restoran (input dijamin valid(integer))
            System.out.print("Jumlah Makanan: ");
            int jumlahMakanan = input.nextInt();
            input.nextLine();
            
            // Input menu pada restoran (temporary)
            ArrayList<String> tempMenuRestoran = new ArrayList<>();
            for (int i = 0; i < jumlahMakanan; i++) {
                String menuMakanan = input.nextLine();        
                tempMenuRestoran.add(menuMakanan);
            }

            // Error handling pada nama makanan dan nama harga
            ArrayList<Menu> menuRestoran = new ArrayList<>();
            boolean isMenuValid = true;
            for (String menu : tempMenuRestoran) {
                String[] formattedMenu = menu.split(" ");
                
                // Fetch untuk nama makanan dari input menu
                String namaMakanan = "";
                for (int j = 0; j < formattedMenu.length - 1; j++) {
                    namaMakanan += formattedMenu[j] + " ";
                }
                namaMakanan = namaMakanan.trim();

                // Handling apabila makanan adalah string kosong/input menu hanya ada 1 kata
                if (namaMakanan.equals("")) {
                    System.out.println("Input nama makanan tidak valid\n");
                    isMenuValid = false;
                    break;
                }

                // Fetch untuk harga makanan dari input menu + Handling error
                double hargaMakanan;
                try {
                    hargaMakanan = Double.parseDouble(formattedMenu[formattedMenu.length - 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Harga menu harus bilangan bulat!\n");
                    isMenuValid = false;
                    break;
                }

                menuRestoran.add(new Menu(namaMakanan, hargaMakanan));
            }
            if (!isMenuValid) {
                continue;
            }

            // Menambahkan nama restoran beserta menu pada restoList
            restoList.add(new Restaurant(namaRestoran, menuRestoran));
            System.out.println("Restaurant " + namaRestoran + " berhasil ditambahkan.");
            break;
        }
    }
    
    // Method untuk menghapus restoran (role: Admin)
    public static void handleHapusRestoran() {
        System.out.println("----------------Hapus Restoran----------------");
        System.out.print("Nama Restoran: ");
        String namaRestoran = input.nextLine();

        // Restoran ditemukan ? hapus : "restoran tidak ditemukan"
        Restaurant restoran = findRestaurant(namaRestoran);
        if (restoran != null) {
            restoList.remove(restoran);
            System.out.println("Restoran berhasil dihapus.");
        } else {
            System.out.println("Restoran tidak terdaftar pada sistem.");
        }
    }

    // Method untuk membuat pesanan (role: Customer)
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

    // Method untuk mencetak bill (role: Customer)
    public static void handleCetakBill() {
        System.out.println("----------------Cetak Bill----------------");
        System.out.print("Masukkan Order ID: ");
        String orderID = input.nextLine();
        int totalBiaya = 0;
        
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

    // Method untuk melihat menu restoran (role: Customer)
    public static void handleLihatMenu() {
        System.out.println("----------------Lihat Menu----------------");
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
                System.out.println((i + 1) + ". " + menu.getNamaMakanan() + " " + menu.getHarga());
            }
        } else {
            System.out.println("Restoran " + namaRestoran + " tidak terdaftar pada sistem.");
        }
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
                if ((menu1.getHarga() == menu2.getHarga() && menu1.getNamaMakanan().compareTo(menu2.getNamaMakanan()) > 0) || (menu1.getHarga() > menu2.getHarga())) {
                    menuRestoran.set(index1, menu2);
                    menuRestoran.set(index2, menu1);
                }
            }
        }
        sortMenu(menuRestoran, startIndex + 1);
    }

    // Method untuk mengubah status pesanan (role: Customer)
    public static void handleUpdateStatusPesanan() {
        System.out.println("----------------Update Status Pesanan----------------");
        System.out.print("Order ID: ");
        String orderID = input.nextLine();
        Order order = findOrder(orderID);
        
        // Handling apabila order ID tidak ditemukan
        if (order == null) {
            System.out.println("Order ID tidak dapat ditemukan.");
            return;
        }
        
        // Update status
        System.out.print("Status: ");
        String newStatus = input.nextLine();
        
        // Apabila status pesanan sudah selesai dan input = "selesai", maka tidak akan berhasil diupdate
        if (order.isOrderFinished() && newStatus.equalsIgnoreCase("Selesai")) {
            System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");

        // Apabila status pesanan belum selesai dan input = "not finished", maka tidak akan berhasil diupdate
        } else if (!order.isOrderFinished() && newStatus.equalsIgnoreCase("not finished")) {
            System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!");
        } else {
            order.setOrderFinished(newStatus.equalsIgnoreCase("Selesai"));
            System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
        }
    }
    
    // Method untuk menu dengan role Admin
    public static void menuAdmin() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Method untuk menu dengan role Customer
    public static void menuCustomer() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Method untuk menampilkan header menu
    public static void printHeader() {
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    // Method untuk menampilkan bagian awal menu
    public static void startMenu() {
        System.out.println("\nSelamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Method untuk inisiasi user yang terdaftar pada data userList
    public static void initUser() {
        userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
        userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
        userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
        userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
        userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));
        userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
        userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }
}