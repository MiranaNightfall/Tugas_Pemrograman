package assignments.assignment4.page;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.util.List;

public abstract class MemberMenu {
    private Scene scene;
    protected ObservableList<String> restaurantList;
    protected ListView<String> restaurantListView;

    abstract protected Scene createBaseMenu();

    protected void showAlert(String title, String header, String content, Alert.AlertType c) {
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Scene getScene() {
        if (this.scene == null) {
            this.scene = createBaseMenu();
        }
        return this.scene;
    }

    protected void refresh() {
        // Mengambil daftar restoran terbaru dari DepeFood
        List<Restaurant> restoList = DepeFood.getRestoList();

        // Update ObservableList with the latest restaurant list
        restaurantList = FXCollections.observableArrayList(
                restoList.stream()
                        .map(Restaurant::getNama)
                        .toList()
        );

        // Update the ListView with the updated ObservableList
        restaurantListView.setItems(restaurantList);
    }
}