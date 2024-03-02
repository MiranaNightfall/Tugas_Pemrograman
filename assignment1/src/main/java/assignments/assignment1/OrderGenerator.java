package assignments.assignment1;

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class OrderGenerator {
    // Inisialisasi variabel
    private static final Scanner input = new Scanner(System.in);
    private static String dataOrderID = "";  
    private static String dataTanggal = "";
    private static int dataIndex = 0;

    // Method untuk menampilkan daftar menu
    public static void showMenu(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.err.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
        System.out.println("Pilih menu:");
        System.err.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
    }

    // Method untuk membuat ID pesanan
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        String formattedTotalNumber;
        int totalNum = 0;
        namaRestoran = namaRestoran.substring(0, 4).toUpperCase();

        // Menjumlahkan masing-masing digit nomor telepon
        for (int i = 0; i < noTelepon.length(); i++) {
            totalNum += Character.getNumericValue(noTelepon.charAt(i));
        }
        formattedTotalNumber = Integer.toString(totalNum);
        if (totalNum < 10) {
            formattedTotalNumber = "0" + totalNum;
        }
        // Result dari nomor telepon yang telah diformat untuk dimasukkan pada order ID
        formattedTotalNumber = formattedTotalNumber.substring(formattedTotalNumber.length() - 2);

        return checkSum(namaRestoran + tanggalOrder.replace("/", "") + formattedTotalNumber);
    }

    // Method untuk format ODD dan EVEN sum pada 2 digit di belakang ID pesanan
    private static String checkSum(String orderID){
        int oddSum = 0;
        int evenSum = 0;
        String formattedOddSum;
        String formattedEvenSum;
        for (int i = 0; i < orderID.length(); i++) {
            // Convert ke bilangan ASCII (decimal)
            if (i % 2 == 0) {
                if (Character.isDigit(orderID.charAt(i))) {
                    evenSum += Character.getNumericValue(orderID.charAt(i));
                } else {
                    evenSum += ((int) orderID.charAt(i)) - 55;
                }
            } else {
                if (Character.isDigit(orderID.charAt(i))) {
                    oddSum += Character.getNumericValue(orderID.charAt(i));
                } else {
                    oddSum += ((int) orderID.charAt(i)) - 55;
                }
            }
        }

        // Reverse dari bilangan ASCII ke character sesuai dengan code 39 character
        oddSum = oddSum % 36; // index ganjil
        if (oddSum > 9) {
            oddSum += 55;
            formattedOddSum = String.valueOf((char) oddSum);
        } else {
            formattedOddSum = Integer.toString(oddSum);
        }
        evenSum = evenSum % 36; // index genap
        if (evenSum > 9) {
            evenSum += 55;
            formattedEvenSum = String.valueOf((char) evenSum);
        } else {
            formattedEvenSum = Integer.toString(evenSum);
        }
        return orderID + formattedEvenSum + formattedOddSum;
    }

    // Error handling pada input tanggal
    private static boolean checkDate(String tanggalOrder, DateTimeFormatter formatTanggal){
        try {
            LocalDate.parse(tanggalOrder, formatTanggal);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method untuk menmberikan bill sesuai dengan lokasi, serta output data pesanan
    public static String generateBill(String inputOrderID, String lokasi){
        String hargaOngkir;

        // Mengambil harga ongkos kirim sesuai lokasi
        if (lokasi.toUpperCase().equals("P")) {
            hargaOngkir = "10.000";
        } else if (lokasi.toUpperCase().equals("U")) {   
            hargaOngkir = "20.000";
        } else if (lokasi.toUpperCase().equals("T")) {   
            hargaOngkir ="35.000";
        } else if (lokasi.toUpperCase().equals("S")) {   
            hargaOngkir = "40.000";
        } else {
            hargaOngkir = "60.000";
        }
        
        // Output dari data pesanan
        return "Bill:\n" + //
                "Order ID: " + inputOrderID + "\n" + 
                "Tanggal Pemesanan: " + dataTanggal.substring(dataIndex*10, dataIndex*10+10) + "\n" + 
                "Lokasi Pengiriman: " + lokasi.toUpperCase() + "\n" + 
                "Biaya Ongkos Kirim: Rp " + hargaOngkir;

    }

    // Main program
    public static void main(String[] args) {
        while (true) {
            // Inisialisasi variabel
            String lokasi;
            String inputOrderID;
            String customerOrderID = "";
            String tempTanggal = "";

            // Memilih menu
            showMenu();
            System.out.println("-".repeat(44));
            System.out.print("Pilihan menu: ");
            int menuInput = input.nextInt();

            input.nextLine();

            if (menuInput == 1) { // Menu ke-1 (generate order ID)
                while (true) {
                    String namaRestoran;
                    Long inputNoTelepon;
                    String tanggalOrder;
                    
                    // Input nama restoran
                    System.out.print("Nama Restoran: ");
                    String inputNamaRestoran = input.nextLine();
                    namaRestoran = inputNamaRestoran.replace(" ", "");

                    // Error handling untuk nama restoran
                    if (namaRestoran.length() < 4) {
                        System.out.println("Nama restoran tidak valid!\n");
                    } else {
                        // Input tanggal pemesanan
                        System.out.print("Tanggal Pemesanan: ");
                        tanggalOrder = input.nextLine();
                        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        // Error handling untuk tanggal pemesanan
                        if (checkDate(tanggalOrder, formatTanggal)) {
                            tempTanggal += tanggalOrder;
                            System.out.print("No. Telepon: ");

                            // Input nomor telepon + error handling untuk nomor telepon
                            if (input.hasNextLong()) {
                                inputNoTelepon = input.nextLong();
                                input.nextLine();

                                String noTelepon = Long.toString(inputNoTelepon);

                                customerOrderID = generateOrderID(inputNamaRestoran, tanggalOrder, noTelepon);

                                // Menyimpan data order ID dan tanggal pada suatu variabel
                                dataOrderID += customerOrderID;
                                dataTanggal += tempTanggal;

                                System.out.println("Order ID " + customerOrderID + " diterima!\n");
                                break;
                            } else {
                                System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.\n");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Tanggal pemesanan dalam format DD/MM/YYYY!\n");
                        }
                    }
                }
            } else if (menuInput == 2) { // Menu ke-2 (generate bill)
                while (true) {
                    // Input order ID
                    System.out.print("Order ID: ");
                    inputOrderID = input.nextLine();

                    // Error handling pada order ID
                    if (inputOrderID.length() != 16) {
                        System.out.println("Order ID harus memiliki 16 karakter.\n");
                    } else {

                        boolean found = false;
                        int dataLength = dataOrderID.length();

                        // Check eksistensi input order ID
                        for (int i = 0; i < dataLength; i += 16) {
                            if (inputOrderID.equals(dataOrderID.substring(i, i + 16))) {
                                // Menyimpan index ke-i yang nantinya akan digunakan untuk mengambil index ke-i pada tanggal
                                dataIndex = i/16;
                                found = true;
                                break;
                            }
                        }
                        if (found) { // Keluar dari loop while apabila True
                            break;
                        } else {
                            System.out.println("Order ID tidak ditemukan.\n");
                        }
                    }
                }

                while (true) {
                    // Input lokasi pengiriman
                    System.out.print("Lokasi Pengiriman: ");
                    lokasi = input.nextLine().toUpperCase();

                    // Error handling pada lokasi pengiriman
                    if ("PUTSB".contains(lokasi) && lokasi.length() == 1) {
                        System.out.println(generateBill(inputOrderID, lokasi) + "\n");
                        break;
                    } else {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!\n");
                    }
                }
            } else if (menuInput == 3) {  // Keluar dari menu/program
                System.out.print("Terima kasih telah menggunakan DepeFood!");
                break;
            } else { // Kondisi apabila perintah menu tidak valid
                System.out.println("Input tidak valid!\n");
            }
        }
        input.close();
    }
}