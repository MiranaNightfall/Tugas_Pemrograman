package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import assignments.assignment1.OrderGenerator;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter;
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private MainApp mainApp;
    private List<Restaurant> restoList = new ArrayList<>();
    private User user;

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        DepeFood.setPenggunaLoggedIn(user); // Simpan user yang sedang login
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user);
        this.restoList = DepeFood.getRestoList();
        updateRestaurantComboBox();
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();

        // Tambahkan scene-scene baru ke dalam allScenes di MainApp
        mainApp.addScene("CustomerMenu", this.scene);
        mainApp.addScene("TambahPesanan", this.addOrderScene);
        mainApp.addScene("PrintBill", this.printBillScene);
        mainApp.addScene("BayarBill", this.payBillScene);
        mainApp.addScene("CekSaldo", this.cekSaldoScene);
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(20));

        // Menambahkan label untuk menampilkan pesan selamat datang
        Label welcomeLabel = new Label("Selamat Datang, " + user.getNama());
        menuLayout.getChildren().add(welcomeLabel);

        Button addOrderButton = new Button("Buat Pesanan");
        addOrderButton.setOnAction(e -> mainApp.setScene(mainApp.getScene("TambahPesanan")));
        Button printBillButton = new Button("Cetak Bill");
        printBillButton.setOnAction(e -> stage.setScene(printBillScene));
        Button payBillButton = new Button("Bayar Bill");
        payBillButton.setOnAction(e -> stage.setScene(payBillScene));
        Button checkBalanceButton = new Button("Cek Saldo");
        checkBalanceButton.setOnAction(e -> stage.setScene(cekSaldoScene));
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainApp.logout());

        menuLayout.getChildren().addAll(addOrderButton, printBillButton, payBillButton, checkBalanceButton, logoutButton);
        return new Scene(menuLayout, 400, 600);
    }

    // Method untuk menambahkan form pesanan
    private Scene createTambahPesananForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label restaurantLabel = new Label("Restoran:");
        layout.getChildren().add(restaurantLabel);
        layout.getChildren().add(restaurantComboBox);

        Label dateLabel = new Label("Tanggal Pemesanan (DD/MM/YYYY):");
        TextField dateInput = new TextField();

        Label menuItemsLabel = new Label("Menu Items:");
        // Menggunakan ListView dengan SelectionMode.MULTIPLE
        ListView<String> menuItemsListView = new ListView<>();
        menuItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        restaurantComboBox.setOnAction(e -> populateMenuList(restaurantComboBox.getValue(), menuItemsListView));

        Button addButton = new Button("Tambah Pesanan");
        Button backButton = new Button("Kembali");

        addButton.setOnAction(e -> {
            String restaurantName = restaurantComboBox.getValue();
            String tanggalPemesanan = dateInput.getText();
            List<String> menuItems = new ArrayList<>(menuItemsListView.getSelectionModel().getSelectedItems());

            if (!OrderGenerator.validateDate(tanggalPemesanan)) {
                showAlert("Tanggal Tidak Valid", "Kesalahan", "Tanggal tidak sesuai format (DD/MM/YYYY)!", AlertType.ERROR);
                return;
            }

            if (menuItems.isEmpty()) {
                showAlert("Pilih Menu", "Kesalahan", "Anda belum memilih menu!", AlertType.ERROR);
                return;
            }

            handleBuatPesanan(restaurantName, tanggalPemesanan, menuItems);
            dateInput.clear();
            menuItemsListView.getSelectionModel().clearSelection();
        });

        backButton.setOnAction(e -> stage.setScene(scene));

        layout.getChildren().addAll(dateLabel, dateInput, menuItemsLabel, menuItemsListView, addButton, backButton);

        return new Scene(layout, 400, 600);
    }

    // Initialize bill printer
    private Scene createBillPrinter() {
        return billPrinter.getScene();
    }

    // Method untuk membayar Bill pesanan
    private Scene createBayarBillForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label orderIdLabel = new Label("Order ID:");
        TextField orderIdInput = new TextField();

        Label paymentOptionLabel = new Label("Pilihan Pembayaran:");
        ComboBox<String> paymentOptionComboBox = new ComboBox<>(FXCollections.observableArrayList("Credit Card", "Debit"));

        Button payButton = new Button("Bayar");
        Button backButton = new Button("Kembali");

        payButton.setOnAction(e -> {
            String orderId = orderIdInput.getText();
            String paymentOption = paymentOptionComboBox.getValue();
            handleBayarBill(orderId, paymentOption);
            orderIdInput.clear();
        });

        backButton.setOnAction(e -> stage.setScene(scene));

        layout.getChildren().addAll(orderIdLabel, orderIdInput, paymentOptionLabel, paymentOptionComboBox, payButton, backButton);

        return new Scene(layout, 400, 600);
    }

    // Method untuk membuat scene Cek Saldo
    private Scene createCekSaldoScene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        refresh(); // Memuat ulang data dari DepeFood
        User currentUser = DepeFood.getUserLoggedIn();
        String userName = currentUser.getNama();
        long userBalance = currentUser.getSaldo();

        Label nameLabel = new Label(userName);
        Label balanceLabel = new Label("Saldo: Rp" + userBalance);
        Button backButton = new Button("Kembali");

        backButton.setOnAction(e -> stage.setScene(scene));

        layout.getChildren().addAll(nameLabel, balanceLabel, backButton);

        return new Scene(layout, 400, 200);
    }

    // Method untuk handling error ketika memesan
    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
        try {
            // Validasi tanggal
            if (!OrderGenerator.validateDate(tanggalPemesanan)) {
                showAlert("Tanggal Tidak Valid", "Kesalahan", "Tanggal tidak sesuai format (DD/MM/YYYY)!", AlertType.ERROR);
                return;
            }
    
            // Validasi menu
            if (menuItems.isEmpty()) {
                showAlert("Pilih Menu", "Kesalahan", "Anda belum memilih menu!", AlertType.ERROR);
                return;
            }

            String tempCheck = OrderGenerator.generateOrderID(namaRestoran, tanggalPemesanan, user.getNomorTelepon());
            Order existingOrder = DepeFood.getOrderOrNull(tempCheck);
            User currentUser = DepeFood.getUserLoggedIn();

            List<Order> userOrderHistory = currentUser.getOrderHistory();
            if (existingOrder != null && userOrderHistory.contains(existingOrder)) {

                // Order ID dan user yang sama, maka tampilkan konfirmasi overwrite
                String orderId = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, menuItems.size(), menuItems);
                stage.setScene(createOverwriteConfirmationScene(orderId));
            } else if (existingOrder == null) {

                // Order ID belum ada, maka tambahkan pesanan baru
                String orderId = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, menuItems.size(), menuItems);
                showAlert("Pesanan Berhasil Dibuat", "Sukses", "Pesanan dengan ID " + orderId + " berhasil dibuat.", AlertType.INFORMATION);
            } else {

                // Order ID sudah ada tetapi bukan milik user ini, maka tampilkan pesan error
                showAlert("Order ID Sudah Ada", "Kesalahan", "Order ID telah terdaftar, tetapi bukan milik Anda.", AlertType.ERROR);   

            }
        } catch (Exception e) {
            showAlert("Pesanan Gagal Dibuat", "Kesalahan", e.getMessage(), AlertType.ERROR);
        }
    }

    // Method untuk handling order ID untuk di-overwrite
    private void handleOverwriteOrder(String orderId) {
        Order existingOrder = DepeFood.getOrderOrNull(orderId);
        if (existingOrder != null) {
            User currentUser = DepeFood.getUserLoggedIn();
            List<Order> userOrderHistory = currentUser.getOrderHistory();
    
            if (userOrderHistory.contains(existingOrder)) {
                currentUser.getOrderHistory().remove(existingOrder);
    
                // Membuat order baru dengan order ID yang sama
                String tanggalPemesanan = existingOrder.getTanggal();
                Restaurant restaurant = existingOrder.getRestaurant();
                Menu[] menuItems = existingOrder.getItems();
                int ongkir = existingOrder.getOngkir();
    
                Order newOrder = new Order(orderId, tanggalPemesanan, ongkir, restaurant, menuItems);
                currentUser.addOrderHistory(newOrder);
    
                showAlert("Pesanan Berhasil Dioverwrite", "Sukses", "Pesanan dengan ID " + orderId + " berhasil dioverwrite.", AlertType.INFORMATION);
            } else {
                // User tidak memiliki akses untuk melakukan overwrite pada order ini
                showAlert("Akses Ditolak", "Kesalahan", "Anda tidak memiliki akses untuk melakukan overwrite pada pesanan tersebut.", AlertType.ERROR);
            }
        }
    }

    // Method untuk membuat scene fitur overwrite
    private Scene createOverwriteConfirmationScene(String orderId) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
    
        Label label = new Label("Order ID " + orderId + " sudah ada. Apa yang ingin Anda lakukan?");
        Button overwriteButton = new Button("Overwrite");
        Button cancelButton = new Button("Batal");
    
        overwriteButton.setOnAction(e -> {
            // Lakukan overwrite order
            handleOverwriteOrder(orderId);
            stage.setScene(scene); // Kembali ke scene utama
        });
    
        cancelButton.setOnAction(e -> {
            // Batal order baru
            stage.setScene(scene); // Kembali ke scene utama
        });
    
        layout.getChildren().addAll(label, overwriteButton, cancelButton);
        return new Scene(layout, 400, 200);
    }
    
    // Method untuk handling error pada saat bayar Bill
    private void handleBayarBill(String orderID, String paymentOption) {
        Order order = DepeFood.getOrderOrNull(orderID);

        if (order == null) {
            showAlert("Order ID Tidak Ditemukan", "Kesalahan", "Order ID tidak dapat ditemukan.", AlertType.ERROR);
            return;
        }

        if (order.getOrderFinished()) {
            showAlert("Pesanan Sudah Dibayar", "Informasi", "Pesanan dengan ID " + orderID + " sudah dibayar sebelumnya.", AlertType.INFORMATION);
            return;
        }

        User user = DepeFood.getUserLoggedIn();
        DepeFoodPaymentSystem paymentSystem = user.getPaymentSystem();
        boolean isCreditCard = paymentSystem instanceof CreditCardPayment;

        if ((isCreditCard && paymentOption.equals("Debit")) || (!isCreditCard && paymentOption.equals("Credit Card"))) {
            showAlert("Metode Pembayaran Tidak Tersedia", "Kesalahan", user.getNama() + " belum memiliki metode pembayaran ini", AlertType.ERROR);
            return;
        }

        long amountToPay = 0;

        try {
            amountToPay = paymentSystem.processPayment(user.getSaldo(), (long) order.getTotalHarga());
        } catch (Exception e) {
            if (e.getMessage().contains("Saldo tidak mencukupi")) {
                showAlert("Saldo Tidak Cukup", "Kesalahan", "Saldo yang dimiliki " + user.getNama() + " tidak cukup!", AlertType.ERROR);
            } else {
                showAlert("Pembayaran Gagal", "Kesalahan", e.getMessage(), AlertType.ERROR);
            }
            return;
        }

        long saldoLeft = user.getSaldo() - amountToPay;
        user.setSaldo(saldoLeft);
        DepeFood.setPenggunaLoggedIn(user);

        System.out.println(DepeFood.getUserLoggedIn().getSaldo());
        DepeFood.handleUpdateStatusPesanan(order);

        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        showAlert("Pembayaran Berhasil", "Sukses", "Berhasil Membayar Bill sebesar Rp " + decimalFormat.format(order.getTotalHarga()) + " dengan biaya transaksi sebesar Rp " + decimalFormat.format(amountToPay - order.getTotalHarga()), AlertType.INFORMATION);
    }
    
    private void populateMenuList(String restaurantName, ListView<String> menuListView) {
        Restaurant selectedRestaurant = restoList.stream()
                .filter(r -> r.getNama().equals(restaurantName))
                .findFirst()
                .orElse(null);

        if (selectedRestaurant != null) {
            List<String> menuItems = selectedRestaurant.getMenu().stream()
                    .map(Menu::getNamaMakanan)
                    .collect(Collectors.toList());
            menuListView.setItems(FXCollections.observableArrayList(menuItems));
        } else {
            menuListView.setItems(FXCollections.emptyObservableList());
        }
    }
    
    // Method untuk update combobox
    private void updateRestaurantComboBox() {
        restaurantComboBox.getItems().clear();
        restaurantComboBox.setItems(FXCollections.observableArrayList(
                restoList.stream()
                        .map(Restaurant::getNama)
                        .collect(Collectors.toList())
        ));
    }

    // refresh
    @Override
    public void refresh() {
        this.restoList = DepeFood.getRestoList();
        updateRestaurantComboBox(); // Panggil method untuk memperbarui ComboBox
        DepeFood.setPenggunaLoggedIn(user);
    }
}