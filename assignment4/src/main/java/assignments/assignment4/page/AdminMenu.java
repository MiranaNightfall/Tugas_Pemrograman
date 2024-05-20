package assignments.assignment4.page;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//import javafx.collections.ObservableList;
import assignments.assignment3.Menu;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private List<Restaurant> restoList = new ArrayList<>();
    private MainApp mainApp;
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    //private ListView<String> menuItemsListView = new ListView<>();

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.restoList = DepeFood.getRestoList();
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(20));

        // Mengubah warna latar belakang VBox
        menuLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #A5D6A7, #388E3C);");

        Label welcomeLabel = new Label("Selamat Datang, " + user.getNama());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.WHITE);
        menuLayout.getChildren().add(welcomeLabel);

        Button addRestaurantButton = new Button("Tambah Restoran");
        addRestaurantButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addRestaurantButton.setOnAction(e -> stage.setScene(addRestaurantScene));

        Button addMenuButton = new Button("Tambah Menu");
        addMenuButton.setOnAction(e -> stage.setScene(addMenuScene));

        Button viewRestaurantsButton = new Button("Lihat Daftar Restoran");
        viewRestaurantsButton.setOnAction(e -> stage.setScene(viewRestaurantsScene));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainApp.logout());
        menuLayout.getChildren().addAll(addRestaurantButton, addMenuButton, viewRestaurantsButton, logoutButton);

        return new Scene(menuLayout, 400, 600);
    }

    // Method untuk membuat form tambah restoran
    private Scene createAddRestaurantForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Mengubah warna latar belakang VBox
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #A5D6A7, #388E3C);");

        Label label = new Label("Nama Restoran:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        TextField nameInput = new TextField();

        Button addButton = new Button("Tambah Restoran");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        addButton.setOnAction(e -> {
            String nama = nameInput.getText();
            handleTambahRestoran(nama);
            nameInput.clear();
        });
        backButton.setOnAction(e -> stage.setScene(scene));
        layout.getChildren().addAll(label, nameInput, addButton, backButton);
        return new Scene(layout, 400, 600);
    }

    // Method untuk membuat form tambah menu
    private Scene createAddMenuForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Mengubah warna latar belakang VBox
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #A5D6A7, #388E3C);");

        Label restaurantLabel = new Label("Restoran:");
        restaurantLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        restaurantLabel.setTextFill(Color.WHITE);
        restaurantComboBox = new ComboBox<>();
        updateRestaurantComboBox();
        restaurantComboBox.setItems(FXCollections.observableArrayList(
                restoList.stream()
                        .map(Restaurant::getNama)
                        .collect(Collectors.toList())
        ));

        Label itemLabel = new Label("Nama Menu:");
        itemLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        itemLabel.setTextFill(Color.WHITE);
        TextField itemInput = new TextField();
        Label priceLabel = new Label("Harga:");
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.WHITE);
        TextField priceInput = new TextField();

        // Tambahkan TextFormatter untuk hanya menerima input numerik
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> priceFormatter = new TextFormatter<>(filter);
        priceInput.setTextFormatter(priceFormatter);

        Button addButton = new Button("Tambah Menu");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        addButton.setOnAction(e -> {
            String restaurantName = restaurantComboBox.getValue();
            String itemName = itemInput.getText();
            double price;
            if (priceInput.getText().isEmpty()) {
                showAlert("Menu Gagal Ditambahkan", "Input Tidak Valid", "Harga menu tidak boleh kosong.", Alert.AlertType.ERROR);
                return;
            } else {
                price = Double.parseDouble(priceInput.getText());
            }
            Restaurant restaurant = DepeFood.getRestaurantByName(restaurantName);
            handleTambahMenuRestoran(restaurant, itemName, price);
            itemInput.clear();
            priceInput.clear();
        });

        backButton.setOnAction(e -> stage.setScene(scene));

        layout.getChildren().addAll(restaurantLabel, restaurantComboBox, itemLabel, itemInput, priceLabel, priceInput, addButton, backButton);
        return new Scene(layout, 400, 600);
    }

    // Method untuk membuat form lihat daftar restoran
    private Scene createViewRestaurantsForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Mengubah warna latar belakang VBox
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #A5D6A7, #388E3C);");

        Label restaurantLabel = new Label("Restaurant Name:");
        restaurantLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        restaurantLabel.setTextFill(Color.WHITE);
        TextField restaurantInput = new TextField();

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        ListView<String> menuListView = new ListView<>();

        searchButton.setOnAction(e -> {
            String restaurantName = restaurantInput.getText();
            Restaurant selectedRestaurant = restoList.stream()
                    .filter(r -> r.getNama().equalsIgnoreCase(restaurantName))
                    .findFirst()
                    .orElse(null);

            if (selectedRestaurant != null) {
                List<String> menuItems = selectedRestaurant.getMenu().stream()
                        .sorted((m1, m2) -> {
                            if (Double.compare(m1.getHarga(), m2.getHarga()) != 0) {
                                return Double.compare(m1.getHarga(), m2.getHarga()); // Sort by price ascending
                            } else {
                                return m1.getNamaMakanan().compareTo(m2.getNamaMakanan()); // If prices are equal, sort by name alphabetically
                            }
                        })
                        .map(menu -> menu.getNamaMakanan() + " - " + formatRupiah(menu.getHarga()))
                        .collect(Collectors.toList());
                menuListView.setItems(FXCollections.observableArrayList(menuItems));
            } else {
                menuListView.setItems(FXCollections.emptyObservableList());
                showAlert("Restoran Tidak Ditemukan", "Kesalahan", "Restoran dengan nama '" + restaurantName + "' tidak ditemukan.", AlertType.ERROR);
            }
        });

        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> stage.setScene(scene));

        layout.getChildren().addAll(restaurantLabel, restaurantInput, searchButton, menuListView, backButton);

        return new Scene(layout, 400, 600);
    }

    // Method untuk memformat angka menjadi format mata uang
    private String formatRupiah(double harga) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(harga);
    }

    // Method untuk menangani penambahan restoran
    private void handleTambahRestoran(String nama) {
        String validName = DepeFood.getValidRestaurantName(nama);
        if (validName != null) {
            if (!validName.equals(nama)) {
                showAlert("Restoran Gagal Ditambahkan", "Kesalahan Input", validName, Alert.AlertType.ERROR);
            } else {
                List<String> existingRestaurantNames = DepeFood.getRestoList().stream()
                        .map(Restaurant::getNama)
                        .collect(Collectors.toList());
                if (existingRestaurantNames.contains(nama)) {
                    showAlert("Restoran Gagal Ditambahkan", "Nama Restoran Sudah Ada", "Nama restoran '" + nama + "' sudah terdaftar. Mohon gunakan nama lain.", Alert.AlertType.ERROR);
                } else {
                    Restaurant newRestaurant = new Restaurant(nama);
                    DepeFood.getRestoList().add(newRestaurant);
                    updateRestaurantComboBox(); // Memperbarui combobox di semua scene
                    showAlert("Restoran Berhasil Ditambahkan", "Sukses", "Restoran " + nama + " berhasil ditambahkan.", Alert.AlertType.INFORMATION);
                }
            }
        } else {
            showAlert("Restoran Gagal Ditambahkan", "Kesalahan", "Restoran gagal ditambahkan.", Alert.AlertType.ERROR);
        }
    }

    // Method untuk menangani penambahan menu restoran
    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, double price) {
        if (restaurant == null) {
            showAlert("Menu Gagal Ditambahkan", "Restoran Tidak Ditemukan", "Mohon pilih restoran yang valid.", Alert.AlertType.ERROR);
        } else if (itemName.isEmpty()) {
            showAlert("Menu Gagal Ditambahkan", "Input Tidak Valid", "Nama menu tidak boleh kosong.", Alert.AlertType.ERROR);
        } else if (price == 0) {
            showAlert("Menu Gagal Ditambahkan", "Input Tidak Valid", "Harga menu tidak boleh kosong.", Alert.AlertType.ERROR);
        } else {
            Optional<Menu> existingMenu = restaurant.getMenu().stream()
                    .filter(menu -> menu.getNamaMakanan().equalsIgnoreCase(itemName))
                    .findFirst();
    
            if (existingMenu.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Menu Sudah Terdaftar");
                alert.setHeaderText("Menu '" + itemName + "' sudah terdaftar di " + restaurant.getNama());
                alert.setContentText("Apakah Anda ingin mengganti menu ini dengan harga baru " + price + "?");
    
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    // Menghapus menu lama dari daftar menu restoran
                    List<Menu> menuList = new ArrayList<>(restaurant.getMenu());
                    menuList.removeIf(menu -> menu.getNamaMakanan().equalsIgnoreCase(itemName));
                    restaurant.getMenu().clear();
                    restaurant.getMenu().addAll(menuList);
    
                    // Menambahkan menu baru ke daftar menu restoran
                    restaurant.addMenu(new Menu(itemName, price));
                    showAlert("Menu Berhasil Diperbarui", "Sukses", "Menu " + itemName + " dengan harga " + price + " berhasil diperbarui di " + restaurant.getNama(), Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Menu Tidak Diperbarui", "Dibatalkan", "Menu " + itemName + " tidak diperbarui.", Alert.AlertType.INFORMATION);
                }
            } else {
                restaurant.addMenu(new Menu(itemName, price));
                showAlert("Menu Berhasil Ditambahkan", "Sukses", "Menu " + itemName + " dengan harga " + price + " berhasil ditambahkan ke " + restaurant.getNama(), Alert.AlertType.INFORMATION);
            }
        }
    }

    // Method untuk memperbarui combobox restoran
    private void updateRestaurantComboBox() {
        restaurantComboBox.getItems().clear();
        restaurantComboBox.setItems(FXCollections.observableArrayList(
                DepeFood.getRestoList().stream()
                        .map(Restaurant::getNama)
                        .collect(Collectors.toList())
        ));
    }
}        