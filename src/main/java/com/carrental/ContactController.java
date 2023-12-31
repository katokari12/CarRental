package com.carrental;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {

    @FXML
    private ImageView facebook;

    @FXML
    private ImageView insta;

    @FXML
    private ImageView linkedin;

    @FXML
    private ImageView youtube;


    private static final String INSTAGRAM_URL = "https://www.instagram.com/rent__ez/";
    private static final String FACEBOOK_URL = "https://www.facebook.com/profile.php?id=100093280942401";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=6yn0y37oi60";
    private static final String LINKEDIN_URL = "https://www.linkedin.com/in/rent-ez-22906227b";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insta.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(INSTAGRAM_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        facebook.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(FACEBOOK_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        youtube.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(YOUTUBE_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        linkedin.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(LINKEDIN_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
