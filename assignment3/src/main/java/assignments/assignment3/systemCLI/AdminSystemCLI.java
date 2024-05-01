package assignments.assignment3.systemCLI;

import assignments.assignment3.modifiedClass.Restaurant;
import assignments.assignment3.modifiedClass.User;
import assignments.assignment3.modifiedClass.Menu;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminSystemCLI extends UserSystemCLI {
    // Inisialisasi
    private ArrayList<Restaurant> restaurants;

    // Constructor
    public AdminSystemCLI(ArrayList<User> userList, ArrayList<Restaurant> restoList) {
    this.restaurants = restoList;
    this.input = new Scanner(System.in);
    }
    
    // Method yang akan berjalan ketika program dimulai
    public void run(String nama, String noTelp) {
        System.out.println("Selamat Datang " + nama + "!");
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }

    // Override method handleMenu untuk Admin
    @Override
    public boolean handleMenu(int command) {
        switch (command) {
            case 1 -> handleTambahRestoran();
            case 2 -> handleHapusRestoran();
            case 3 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    // Override method untuk men-display menu untuk Admin
    @Override
    public void displayMenu() {
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Method untuk menambah restoran beserta menunya
    protected void handleTambahRestoran() {
        System.out.println("----------------Tambah Restoran----------------");
        while (true) {
            System.out.print("Nama: ");
            String namaRestoran = input.nextLine();

            // Handling apabila nama restoran kurang dari 4 karakter
            if (namaRestoran.length() < 4) { 
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }
            
            // Handling apabila nama restoran sudah terdaftar
            if (findRestaurant(namaRestoran) != null) {
                System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda\n");
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
                if (namaMakanan.equals("") || menuRestoran.stream().map(Menu::getNamaMakanan).anyMatch(namaMakanan::equals)) {
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
            restaurants.add(new Restaurant(namaRestoran, menuRestoran));
            System.out.println("Restaurant " + namaRestoran + " berhasil ditambahkan.");
            break;
        }
    }

    // Method untuk menghapus restoran
    protected void handleHapusRestoran() {
        System.out.println("----------------Hapus Restoran----------------");
        // Apabila tidak ada restoran yang terdaftar -> terjadi infinite loop
        while (true) {
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            // Restoran ditemukan ? hapus : "restoran tidak ditemukan"
            Restaurant restoran = findRestaurant(namaRestoran);
            if (restoran != null) {
                restaurants.remove(restoran);
                System.out.println("Restoran berhasil dihapus.");
                break;
            } else {
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
            }
        }
    }

    // Method untuk mencari nama restaurant (check eksistensi restaurant)
    private Restaurant findRestaurant(String namaRestoran) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                return restaurant;
            }
        }
        return null;
    }
}