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
import javafx.stage.Stage;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;
    private Scene scene;

    public LoginForm(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.scene = createLoginForm(); // Membuat scene saat instance dibuat
    }

    // Method untuk membuat form login
    private Scene createLoginForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Nama:");
        grid.add(nameLabel, 0, 0);

        nameInput = new TextField();
        grid.add(nameInput, 1, 0);

        Label phoneLabel = new Label("Nomor Telepon:");
        grid.add(phoneLabel, 0, 1);

        phoneInput = new TextField();
        grid.add(phoneInput, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());
        grid.add(loginButton, 1, 2);

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