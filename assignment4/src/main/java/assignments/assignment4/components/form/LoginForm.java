package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp;
    private TextField nameInput;
    private TextField phoneInput;
    private Scene scene;

    public LoginForm(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.scene = createLoginForm();
    }

    private Scene createLoginForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Mengubah warna latar belakang GridPane
        grid.setStyle("-fx-background-color: linear-gradient(to bottom right, #A5D6A7, #388E3C);");

        // Menambahkan label "Welcome to DepeFood"
        Label welcomeLabel = new Label("Welcome to DepeFood!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        grid.add(welcomeLabel, 0, 0, 2, 1);

        Label nameLabel = new Label("Nama:");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);
        grid.add(nameLabel, 0, 1);

        nameInput = new TextField();
        grid.add(nameInput, 1, 1);

        Label phoneLabel = new Label("Nomor Telepon:");
        phoneLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        phoneLabel.setTextFill(Color.WHITE);
        grid.add(phoneLabel, 0, 2);

        // Mengubah tampilan input nomor telepon menjadi karakter dot
        phoneInput = new TextField();
        phoneInput.setPromptText("Masukkan nomor telepon");
        grid.add(phoneInput, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> handleLogin());
        grid.add(loginButton, 1, 3);
        GridPane.setHalignment(loginButton, HPos.RIGHT);

        return new Scene(grid, 400, 600);
    }

    // Method untuk menangani login
    private void handleLogin() {
        String name = nameInput.getText();
        String phone = phoneInput.getText();
        User user = DepeFood.getUser(name, phone);
        if (user != null) {
            if (user.getRole().equals("Admin")) {
                mainApp.setUser(user, "Admin");
            } else {
                mainApp.setUser(user, "Customer");
            }
        } else {
            showAlert("Login Gagal", "Kesalahan", "Nama atau nomor telepon tidak valid.", Alert.AlertType.ERROR);
        }
        nameInput.clear();
        phoneInput.clear();
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
        return this.scene;
    }
}