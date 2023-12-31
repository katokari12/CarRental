package com.carrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public HBox logoutBtn;
    public HBox homeBtn;
    @FXML
    private HBox dashboardBtn;

    @FXML
    private HBox fleetManageBtn;

    @FXML
    private HBox reservationsBtn;

    @FXML
    private HBox userManageBtn;

    @FXML
    private HBox mainBox;

    @FXML
    private VBox navBar;

    HBox pageSelected = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageSelected = dashboardBtn;
        loadDashboard();
        dashboardBtn.setOnMouseClicked(event ->{
            loadDashboard();
        });
        fleetManageBtn.setOnMouseClicked(event ->{
            loadFleetManagement();
        });
        userManageBtn.setOnMouseClicked(event ->{
            loadUserManagement();
        });
        reservationsBtn.setOnMouseClicked(event ->{
            loadReservationsManagement();
        });
        homeBtn.setOnMouseClicked(event ->{
          App.openMain();
        });
        logoutBtn.setOnMouseClicked(event ->{
            App.setUser(null);
            App.openMain();
        });
        dashboardBtn.setOnMouseEntered(event ->{
            glowButton(dashboardBtn);
        });
        userManageBtn.setOnMouseEntered(event ->{
            glowButton(userManageBtn);
        });
        fleetManageBtn.setOnMouseEntered(event ->{
            glowButton(fleetManageBtn);
        });
        reservationsBtn.setOnMouseEntered(event ->{
            glowButton(reservationsBtn);
        });
        homeBtn.setOnMouseEntered(event ->{
            glowButton(homeBtn);
        });
        logoutBtn.setOnMouseEntered(event ->{
            glowButton(logoutBtn);
        });
        dashboardBtn.setOnMouseExited(event ->{
            unGlowButton(dashboardBtn);
            glowButton(pageSelected);
        });
        userManageBtn.setOnMouseExited(event ->{
            unGlowButton(userManageBtn);
            glowButton(pageSelected);
        });
        fleetManageBtn.setOnMouseExited(event ->{
            unGlowButton(fleetManageBtn);
            glowButton(pageSelected);
        });
        reservationsBtn.setOnMouseExited(event ->{
            unGlowButton(reservationsBtn);
            glowButton(pageSelected);
        });
        homeBtn.setOnMouseExited(event ->{
            unGlowButton(homeBtn);
            glowButton(pageSelected);
        });
        logoutBtn.setOnMouseExited(event ->{
            unGlowButton(logoutBtn);
            glowButton(pageSelected);
        });

    }
    public void glowButton(HBox btn){
        btn.getChildren().get(1).setStyle("-fx-text-fill: white; -fx-cursor: hand;");
        ((ImageView) btn.getChildren().get(0)).setImage(new Image(getClass().getResourceAsStream("icons/admin-view/"+btn.getChildren().get(0).getId()+"-W.png"),24,24,true,true));
    }
    public void unGlowButton(HBox btn){
        btn.getChildren().get(1).setStyle("-fx-text-fill: #d9deff; -fx-cursor: hand;");
        ((ImageView) btn.getChildren().get(0)).setImage(new Image(getClass().getResourceAsStream("icons/admin-view/"+btn.getChildren().get(0).getId()+".png"),24,24,true,true));
    }
    public void animation(HBox btn){
        unGlowButton(pageSelected);
        glowButton(btn);
        pageSelected = btn;
    }

    public void loadDashboard(){
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("dashboard-view.fxml"));
            VBox dashboardPage = loader.load();
            //Stage stage =(Stage)mainBox.getScene().getWindow();
            mainBox.getChildren().clear();
            mainBox.getChildren().addAll(navBar,dashboardPage);
            animation(dashboardBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadUserManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("users-view.fxml"));
            AnchorPane usersPage = loader.load();
            //Stage stage =(Stage)mainBox.getScene().getWindow();
            mainBox.getChildren().clear();
            mainBox.getChildren().addAll(navBar,usersPage);
            animation(userManageBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFleetManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("fleet-view.fxml"));
            AnchorPane fleetPage = loader.load();
            //Stage stage =(Stage)mainBox.getScene().getWindow();
            mainBox.getChildren().clear();
            mainBox.getChildren().addAll(navBar,fleetPage);
            animation(fleetManageBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadReservationsManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("reservation-view.fxml"));
            AnchorPane reservationsPage = loader.load();
            //Stage stage =(Stage)mainBox.getScene().getWindow();
            mainBox.getChildren().clear();
            mainBox.getChildren().addAll(navBar,reservationsPage);
            animation(reservationsBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
