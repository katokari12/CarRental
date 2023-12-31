package com.carrental;

import com.carrental.customnodes.MyDatePicker;
import com.carrental.customnodes.MyTextField;
import com.carrental.customnodes.MyTimePicker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import com.carrental.models.Vehicle;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class HomeController implements Initializable {
    @FXML
    public ComboBox<String> typeDropList;
    @FXML
    public HBox paginationBox;
    public Button searchBtn;
    public HBox filtersBox;
    public ImageView locIcon;
    public MyDatePicker pickupDateTF;
    public MyTimePicker pickupTimeTF;
    public MyDatePicker returnDateTF;
    public MyTimePicker returnTimeTF;
    public MyTextField locationTF;
    @FXML
    private ComboBox<String> brandsDropList;

    @FXML
    private HBox cardLayout;

    @FXML
    private Button clearFilterButton;

    @FXML
    private ComboBox<String> colorsDropList;

    @FXML
    private ComboBox<String> fuelDropList;

    @FXML
    private ComboBox<String> gearDropList;

    @FXML
    private Button nextPageButton;

    @FXML
    private Label pageNumberLabel;

    @FXML
    private Button previousPageButton;

    @FXML
    private HBox searchBox;

    @FXML
    private Label totalVeh;

    @FXML
    private VBox vehicleCardsBox;

    @FXML
    private VBox mainBox;

    public ArrayList<Vehicle> vehicles =  new ArrayList<Vehicle>();
    public List<List<Vehicle>> vehiclesHolder = new ArrayList<>();
    int maxPages;
    public ArrayList<String> filterSettings = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
    public Date pickDate = null;
    public Date returnDate = null;
    public static <T> List<List<T>> split(Collection<T> data, int size)
    {
        List<List<T>> lists=new ArrayList<>((data.size()+size-1)/size);
        List<T> list=new ArrayList<>(size);
        for(T t:data)
        {
            list.add(t);
            if(list.size()==size)
            {
                lists.add(list);
                list=new ArrayList<>(size);
            }
        }
        if(list.size()>0) lists.add(list);
        return lists;
    }

    public void loadCardsByPage(int pageNumber){
        paginationBox.getChildren().clear();
        paginationBox.getChildren().add(previousPageButton);
        int startPage = Math.max(1, pageNumber - 2);
        int endPage = Math.min(startPage + 4 - 1, maxPages);
        if (startPage > 1) {

            Label numberLbl = new Label("1");
            numberLbl.setFont(pageNumberLabel.getFont());
            numberLbl.setOnMouseEntered(event -> numberLbl.setUnderline(true));
            numberLbl.setOnMouseExited(event -> numberLbl.setUnderline(false));
            numberLbl.setOnMouseClicked(event ->{
                new Thread(() -> {
                    Platform.runLater(()-> loadCardsByPage(Integer.parseInt(numberLbl.getText())));
                }).start();
            });
            Label separatorLabel = new Label("...");
            paginationBox.getChildren().addAll(numberLbl,separatorLabel);
        }

        for (int i = startPage; i <= endPage; i++) {
            Label numberLbl = new Label(String.valueOf(i));
            numberLbl.setFont(pageNumberLabel.getFont());
            if(i==pageNumber){
                numberLbl.setStyle("-fx-text-fill: #6279ff");
            }
            numberLbl.setOnMouseEntered(event -> numberLbl.setUnderline(true));
            numberLbl.setOnMouseExited(event -> numberLbl.setUnderline(false));
            numberLbl.setOnMouseClicked(event ->{
                new Thread(() -> {
                    Platform.runLater(()-> loadCardsByPage(Integer.parseInt(numberLbl.getText())));
                }).start();
            });
            paginationBox.getChildren().add(numberLbl);
        }

        if (endPage < maxPages) {
            Label numberLbl = new Label(String.valueOf(maxPages));
            numberLbl.setFont(pageNumberLabel.getFont());
            numberLbl.setOnMouseEntered(event -> numberLbl.setUnderline(true));
            numberLbl.setOnMouseExited(event -> numberLbl.setUnderline(false));
            numberLbl.setOnMouseClicked(event ->{
                new Thread(() -> {
                    Platform.runLater(()-> loadCardsByPage(Integer.parseInt(numberLbl.getText())));
                }).start();
            });
            Label separatorLabel = new Label("...");
            paginationBox.getChildren().addAll(separatorLabel,numberLbl);
        }
        paginationBox.getChildren().add(nextPageButton);
        cardLayout.getChildren().clear();
        nextPageButton.setVisible(false);
        previousPageButton.setVisible(false);
        pageNumberLabel.setText(String.valueOf(pageNumber));
        if (maxPages > pageNumber){
            nextPageButton.setVisible(true);
        }
        if (pageNumber > 1){
            previousPageButton.setVisible(true);
        }
        if(vehicles.size()> 0) {
            totalVeh.setText(vehicles.size() + " Vehicles found");
            pageNumber--;
            try {
                for (int i = 0; i < vehiclesHolder.get(pageNumber).size(); i++) { // vehiclesHolder.get(0) = first 4 items in vehicles // first page
                    Vehicle vehicle = vehiclesHolder.get(pageNumber).get(i);
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("vehicle-card-view.fxml"));
                    VBox vehicleCard = fxmlLoader.load();
                    VehicleCardController vehicleCardController = fxmlLoader.getController();
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(pickupDateTF.getDate());
                    cal2.setTime(pickupTimeTF.getDate());
                    int year = cal1.get(Calendar.YEAR);
                    int month = cal1.get(Calendar.MONTH);
                    int day = cal1.get(Calendar.DAY_OF_MONTH);
                    cal1.set(year, month, day, cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), cal2.get(Calendar.SECOND));
                    pickDate = cal1.getTime();
                    cal1.setTime(returnDateTF.getDate());
                    cal2.setTime(returnTimeTF.getDate());
                    year = cal1.get(Calendar.YEAR);
                    month = cal1.get(Calendar.MONTH);
                    day = cal1.get(Calendar.DAY_OF_MONTH);
                    cal1.set(year, month, day, cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), cal2.get(Calendar.SECOND));
                    returnDate = cal1.getTime();
                    vehicleCardController.setData(vehicle,pickDate,returnDate);
                    cardLayout.getChildren().add(vehicleCard);
                    vehicleCardController.loadIn();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            totalVeh.setText("0 Vehicle found");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> cardLayout.requestFocus());
        locIcon.setImage(new Image(getClass().getResourceAsStream("icons/loc.png"),24,24,true,true));

    }


    public void refreshComboBoxes(){
        filtersBox.getChildren().clear();
        brandsDropList = new ComboBox<>();
        brandsDropList.getStyleClass().add("menu");
        brandsDropList.getStylesheets().add(getClass().getResource("style/menu.css").toExternalForm());
        brandsDropList.setPromptText("Brands");
        brandsDropList.setPrefWidth(99);
        brandsDropList.setOnAction(event -> {
                    filterVehicles(event);
                }
                );

        gearDropList = new ComboBox<>();
        gearDropList.getStyleClass().add("menu");
        gearDropList.getStylesheets().add(getClass().getResource("style/menu.css").toExternalForm());
        gearDropList.setPromptText("Gear Type");
        gearDropList.setPrefWidth(116);
        gearDropList.setOnAction(event -> {
                    filterVehicles(event);
                }
        );

        fuelDropList = new ComboBox<>();
        fuelDropList.getStyleClass().add("menu");
        fuelDropList.getStylesheets().add(getClass().getResource("style/menu.css").toExternalForm());
        fuelDropList.setPromptText("Fuel Type");
        fuelDropList.setPrefWidth(113);
        fuelDropList.setOnAction(event -> {
                    filterVehicles(event);
                }
        );

        colorsDropList = new ComboBox<>();
        colorsDropList.getStyleClass().add("menu");
        colorsDropList.getStylesheets().add(getClass().getResource("style/menu.css").toExternalForm());
        colorsDropList.setPromptText("Colors");
        colorsDropList.setPrefWidth(97);
        colorsDropList.setOnAction(event -> {
                    filterVehicles(event);
                }
        );

        typeDropList = new ComboBox<>();
        typeDropList.getStyleClass().add("menu");
        typeDropList.getStylesheets().add(getClass().getResource("style/menu.css").toExternalForm());
        typeDropList.setPromptText("Class");
        typeDropList.setPrefWidth(89);
        typeDropList.setOnAction(event -> {
                    filterVehicles(event);
                }
        );

       for(String brand:Vehicle.getAllBrandsAvailable(vehicles)) {
            brandsDropList.getItems().add(brand);
        }
        for(String color:Vehicle.getAllColorsAvailable(vehicles)) {
            colorsDropList.getItems().add(color);
        }

        gearDropList.getItems().addAll("Manual","Automated");
        fuelDropList.getItems().addAll("Diesel","Petrol","Electric","Hybrid");
        typeDropList.getItems().addAll("Sedan","Wagon","SUV","Hatchback","Coupe","Sport","Pickup","Micro");
        filtersBox.getChildren().addAll(gearDropList,brandsDropList,fuelDropList,colorsDropList,typeDropList,clearFilterButton);
    }
    @FXML
    public void filterVehicles(javafx.event.ActionEvent event) {
        pickDate = pickupDateTF.getDate();
        returnDate = returnDateTF.getDate();
        App.getMainController().setStartDate(pickupDateTF.getDate());
        App.getMainController().setEndDate(returnDateTF.getDate());
        App.getMainController().setStartTime(pickupTimeTF.getDate());
        App.getMainController().setEndTime(returnTimeTF.getDate());
        App.getMainController().setLocation(locationTF.getText());
        try {
            ComboBox<String> dropList = (ComboBox<String>) event.getSource();
            Text theText = new Text(dropList.getValue());
            double width = (int) theText.getBoundsInLocal().getWidth() + 63;
            dropList.setPrefWidth(width);
            if (dropList == gearDropList) filterSettings.set(0, dropList.getValue());
            else if (dropList == fuelDropList) filterSettings.set(1, dropList.getValue());
            else if (dropList == brandsDropList) filterSettings.set(2, dropList.getValue());
            else if (dropList == colorsDropList) filterSettings.set(3, dropList.getValue());
            else if (dropList == typeDropList) filterSettings.set(4, dropList.getValue());
        } catch (Exception e) {
            ;
        }
        vehicles = Vehicle.filterVehicles(filterSettings,pickDate,returnDate);
        vehiclesHolder = HomeController.split(vehicles,4);
        maxPages = vehiclesHolder.size();
        new Thread(() -> {
            Platform.runLater(()-> loadCardsByPage(1));
        }).start();
    }

    @FXML
    public void clearAllFilters(javafx.event.ActionEvent event) {
        filterSettings = new ArrayList<String>(Arrays.asList(null,null,null,null,null,null,null));
        vehicles = Vehicle.getAllVehicles();
        vehiclesHolder = HomeController.split(vehicles,4);
        maxPages = vehiclesHolder.size();
        new Thread(() -> {Platform.runLater(()->loadCardsByPage(1));}).start();
        gearDropList.getSelectionModel().clearSelection();
        gearDropList.setValue(null);
        //gearDropList.setPromptText("Gear Type");
        refreshComboBoxes();
    }
    @FXML
    public void nextPageDisplay(){
        int pageNumber = Integer.parseInt(pageNumberLabel.getText());
        pageNumber++;
        int finalPageNumber = pageNumber;
        new Thread(() -> {
            Platform.runLater(()-> loadCardsByPage(finalPageNumber));
        }).start();
    }
    @FXML
    public void previousPageDisplay(){
        int pageNumber = Integer.parseInt(pageNumberLabel.getText());
        pageNumber--;
        int finalPageNumber = pageNumber;
        new Thread(() -> {
            Platform.runLater(()-> loadCardsByPage(finalPageNumber));
        }).start();
    }

    public void setData(Date pickupDate,Date returnDate,Date pickupTime,Date returnTime,String location){

        Platform.runLater(()-> {
            vehicles = Vehicle.filterVehicles(filterSettings,pickDate,returnDate);
            vehiclesHolder = HomeController.split(vehicles,4);
            maxPages = vehiclesHolder.size();

            refreshComboBoxes();
            pickupTimeTF.setDate(pickupTime);
            pickupDateTF.setDate(pickupDate);
            returnDateTF.setDate(returnDate);
            returnTimeTF.setDate(returnTime);
            locationTF.setText(location);
            new Thread(() -> {
                Platform.runLater(()-> {
                    this.pickDate = pickupDateTF.getDate();
                    this.returnDate = returnDateTF.getDate();
                    vehicles = Vehicle.filterVehicles(filterSettings,pickupDate,returnDate);
                    vehiclesHolder = HomeController.split(vehicles,4);
                    maxPages = vehiclesHolder.size();
                    loadCardsByPage(1);
                });
            }).start();
        });
    }

}
