package com.carrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import com.carrental.models.Vehicle;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private HBox cardLayout;

    @FXML
    private Label totalVeh;
    public ArrayList<Vehicle> vehicles =  new ArrayList<Vehicle>();

    @FXML
    private Button nextPage;

    @FXML
    private Label pageNumber;

    @FXML
    private Button previousPage;
    int maxPages;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        previousPage.setVisible(false);
        nextPage.setVisible(false);
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        vehicles.add(new Vehicle("Volkswagen","Touareg","Family","Petrol","Manual",5,1000,140,120,220,"vehicles/volkswagen-touareg.png","brands/volkswagen.png"));
        totalVeh.setText(String.valueOf(vehicles.size())+" Vehicle found");
        maxPages = (int) vehicles.size()/4 +1;
        if (maxPages > 1){
            nextPage.setVisible(true);
        }
        try {
            for (int i = 0; i < vehicles.size(); i++) {
                Vehicle vehicle = vehicles.get(i);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("vehicle-card-view.fxml"));
                VBox vehicleCard = fxmlLoader.load();
                VehicleCardController vehicleCardController = fxmlLoader.getController();
                vehicleCardController.setData(vehicle);
                cardLayout.getChildren().add(vehicleCard);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void nextPageDisplay(){
        int nbrPage = Integer.parseInt(pageNumber.getText());
        nbrPage++;
        pageNumber.setText(String.valueOf(nbrPage));
        if(!previousPage.isVisible()) {
            previousPage.setVisible(true);
        }
        if(nbrPage == maxPages){
            nextPage.setVisible(false);
        }
    }
    @FXML
    public void previousPageDisplay(){
        int nbrPage = Integer.parseInt(pageNumber.getText());
        nbrPage--;
        pageNumber.setText(String.valueOf(nbrPage));
        if (nbrPage == 1) {
            previousPage.setVisible(false);
        }
        if(nbrPage < maxPages){
            nextPage.setVisible(true);
        }
    }
}
