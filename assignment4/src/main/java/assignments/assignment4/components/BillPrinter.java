package assignments.assignment4.components;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    // Method untuk membuat form cetak bill
    private Scene createBillPrinterForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label label = new Label("Order ID:");
        TextField orderIdInput = new TextField();
        Button printButton = new Button("Print Bill");
        Button backButton = new Button("Kembali");

        printButton.setOnAction(e -> {
            String orderId = orderIdInput.getText();
            printBill(orderId);
            orderIdInput.clear();
        });

        backButton.setOnAction(e -> mainApp.setScene(user.getRole().equals("Admin") ?
                mainApp.getScene("AdminMenu") : mainApp.getScene("CustomerMenu")));

        layout.getChildren().addAll(label, orderIdInput, printButton, backButton);

        return new Scene(layout, 400, 200);
    }

    // Method untuk mencetak bill
    private void printBill(String orderId) {
        Order order = DepeFood.getOrderOrNull(orderId);
        if (order != null && user.isOrderBelongsToUser(orderId)) {
            showBillDialog(order);
        } else {
            showAlert("Order ID Tidak Valid", "Kesalahan", "Order ID tidak ditemukan atau tidak valid.", Alert.AlertType.ERROR);
        }
    }
    
    // Method untuk menampilkan dialog bill
    private void showBillDialog(Order order) {
        Stage dialog = new Stage();
        dialog.setTitle("DepeFood Ordering System");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        int row = 0;
    
        gridPane.add(new Label("Bill"), 0, row++, 2, 1);
    
        gridPane.add(new Label("Order ID:"), 0, row);
        gridPane.add(new Label(order.getOrderId()), 1, row++);
    
        gridPane.add(new Label("Tanggal Pemesanan:"), 0, row);
        gridPane.add(new Label(order.getTanggal()), 1, row++);
    
        gridPane.add(new Label("Restaurant:"), 0, row);
        gridPane.add(new Label(order.getRestaurant().getNama()), 1, row++);
    
        gridPane.add(new Label("Lokasi Pengiriman:"), 0, row);
        gridPane.add(new Label(DepeFood.getUserLoggedIn().getLokasi()), 1, row++);
    
        gridPane.add(new Label("Status Pengiriman:"), 0, row);
        gridPane.add(new Label(order.getOrderFinished() ? "Finished" : "Not Finished"), 1, row++);
    
        gridPane.add(new Label("Pesanan:"), 0, row++);
    
        VBox pesananBox = new VBox(5);
        for (Menu menu : order.getSortedMenu()) {
            Label menuItem = new Label("- " + menu.getNamaMakanan() + " Rp " + formatCurrency(menu.getHarga()));
            pesananBox.getChildren().add(menuItem);
        }
        gridPane.add(pesananBox, 0, row++, 2, 1);
    
        gridPane.add(new Label("Biaya Ongkos Kirim: Rp " + formatCurrency(order.getOngkir())), 0, row++, 2, 1);
        gridPane.add(new Label("Total Biaya: Rp " + formatCurrency(order.getTotalHarga())), 0, row++, 2, 1);
    
        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> dialog.close());
        gridPane.add(backButton, 0, row, 2, 1);
    
        Scene dialogScene = new Scene(gridPane, 400, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    
    // Method untuk memformat angka menjadi format mata uang
    private String formatCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat.format(amount);
    }
    
    // Method untuk menampilkan alert
    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public Scene getScene() {
        return this.createBillPrinterForm();
    }
    
    // Opsional
    public static class MenuItem {
        private final StringProperty itemName;
        private final StringProperty price;
    
        public MenuItem(String itemName, String price) {
            this.itemName = new SimpleStringProperty(itemName);
            this.price = new SimpleStringProperty(price);
        }
    
        public StringProperty itemNameProperty() {
            return itemName;
        }
    
        public StringProperty priceProperty() {
            return price;
        }
    
        public String getItemName() {
            return itemName.get();
        }
    
        public String getPrice() {
            return price.get();
        }
    }
}