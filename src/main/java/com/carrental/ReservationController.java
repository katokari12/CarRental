package com.carrental;

import com.carrental.customnodes.MyButton;
import com.carrental.models.Reservation;
import com.carrental.models.User;
import com.carrental.models.Vehicle;
import com.carrental.utils.DataReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;


public class ReservationController implements Initializable {

    public Label adminName;
    @FXML
    private TableView<DataReservation> TableViewReservation;

    @FXML
    private MyButton addReservation;

    @FXML
    private TableColumn<DataReservation, String> col_brandName;

    @FXML
    private TableColumn<DataReservation, String> col_edit;

    @FXML
    private TableColumn<DataReservation, Date> col_endDate;

    @FXML
    private TableColumn<DataReservation, String> col_fullname;

    @FXML
    private TableColumn<DataReservation, String> col_modelName;

    @FXML
    private TableColumn<DataReservation, String> col_phoneNumber;

    @FXML
    private TableColumn<DataReservation, Float> col_price;

    @FXML
    private TableColumn<DataReservation, Date> col_startDate;

    @FXML
    private TableColumn<DataReservation, String> col_status;

    @FXML
    private TextField searchId;

    @FXML
    private CheckBox statusId0;

    @FXML
    private CheckBox statusId1;

    @FXML
    private CheckBox statusId2;

    @FXML
    private CheckBox statusId3;
    @FXML
    private CheckBox statusId4;

    ObservableList<DataReservation> dataResList = FXCollections.observableArrayList();
    ArrayList<Reservation> resList ;
    public String searchKeyword;
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        adminName.setText(App.getUser().getFullName());
        searchId.setOnAction(event -> {
            searchKeyword = searchId.getText();
            applySearchFilter(searchKeyword);
        });

        addReservation.setOnAction(event -> {
            showAddDialog();
        });

        resList = Reservation.getAllReservations();
        for(Reservation i:resList) {
            DataReservation res = new DataReservation(i.getUser().getId(),i.getVehicle().getId(),i.getUser().getFullName(), i.getUser().getPhoneNumber(), i.getVehicle().getBrandName(), i.getVehicle().getModelName(), i.getVehicle().getPrice(), i.getStartDate(), i.getEndDate(), String.valueOf(i.getStatus()));
            dataResList.add(res);
        }
        col_fullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        col_modelName.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_status.setCellFactory(column -> new TableCell<DataReservation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    if (item.equals("Pending")) {
                        setTextFill(Color.ORANGE);
                    } else if (item.equals("Denied")) {
                        setTextFill(Color.RED);
                    } else if (item.equals("Approved")) {
                        setTextFill(Color.FORESTGREEN);
                    } else if (item.equals("Canceled")) {
                        setTextFill(Color.RED);
                    }else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });

        col_edit.setCellFactory(param -> new TableCell<DataReservation, String>() {
            private final MyButton modifyButton = new MyButton("Modify");
            private final MyButton deleteButton = new MyButton("Delete");
            private final MyButton approveButton = new MyButton("Approve");
            private final MyButton refuseButton = new MyButton("Deny");
            private final MyButton endButton = new MyButton("End");

            {
                modifyButton.setPrefWidth(70);
                deleteButton.setPrefWidth(70);
                approveButton.setPrefWidth(70);
                refuseButton.setPrefWidth(70);
                endButton.setPrefWidth(70);
                modifyButton.setOnAction(event -> {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());
                    showEditDialog(dataReservation);
                });
                modifyButton.setStyle("-fx-cursor: hand; -fx-background-radius: 30; -fx-background-color: #6279FF; -fx-border-radius: 30; -fx-pref-width: 70px");
                modifyButton.setTextFill(javafx.scene.paint.Color.WHITE);

                deleteButton.setOnAction(event -> {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());

                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirm.setTitle("Confirmation");
                    alertConfirm.setHeaderText("Are you sure?");
                    alertConfirm.setContentText("This action cannot be undone.");
                    DialogPane dialogPaneConfirm = alertConfirm.getDialogPane();
                    dialogPaneConfirm.getStylesheets().add(
                            getClass().getResource("style/stylesDelete.css").toExternalForm()
                    );
                    dialogPaneConfirm.getStyleClass().add("custom-alert");

                    Alert alertSure = new Alert(Alert.AlertType.INFORMATION);
                    alertSure.setTitle("Account Deleted");
                    alertSure.setHeaderText(null);
                    alertSure.setContentText("Your account has been deleted.");
                    DialogPane dialogPane = alertSure.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("style/stylesDelete.css").toExternalForm()
                    );
                    dialogPane.getStyleClass().add("custom-alert");

                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/delete.png")));
                    dialogPane.setGraphic(imageView);

                    alertConfirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            int i = dataResList.indexOf(dataReservation);
                            Reservation res = resList.get(i);
                            res.delete();
                            TableViewReservation.getItems().remove(dataReservation);
                            alertSure.showAndWait();
                        } else {
                            System.out.println("Action canceled!");
                        }
                    });
                });

                deleteButton.setStyle("-fx-cursor: hand; -fx-background-radius: 30; -fx-background-color: #6279FF; -fx-border-radius: 30; -fx-pref-width: 70px");
                deleteButton.setTextFill(javafx.scene.paint.Color.WHITE);

                approveButton.setOnAction(event -> {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());

                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirm.setTitle("Confirmation");
                    alertConfirm.setHeaderText("Are you sure?");
                    alertConfirm.setContentText("This action cannot be undone.");
                    DialogPane dialogPane = alertConfirm.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("style/stylesDelete.css").toExternalForm()
                    );
                    dialogPane.getStyleClass().add("custom-alert");
                    alertConfirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            String newStatus = "Approved";
                            int newStatusNbr = 1;
                            int i = dataResList.indexOf(dataReservation);
                            dataReservation.setStatus(newStatus);
                            Reservation res = resList.get(i);
                            res.setStatus(newStatusNbr);
                            res.getUser().sendNotification("Reservation", "Your reservation for "+ dataReservation.getBrandName() + " "
                                    + dataReservation.getModelName()+" has been approved.");
                            TableViewReservation.refresh();
                        } else {
                            System.out.println("Action canceled!");
                        }
                    });
                });
                approveButton.setStyle("-fx-cursor: hand; -fx-background-radius: 30; -fx-background-color: #228B22; -fx-border-radius: 30; -fx-pref-width: 70px");
                approveButton.setTextFill(javafx.scene.paint.Color.WHITE);

                refuseButton.setOnAction(event -> {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());

                    TextArea reason = new TextArea();
                    reason.setPrefWidth(200);
                    reason.setPrefHeight(70);

                    Dialog<DataReservation> dialogReason = new Dialog<>();
                    dialogReason.setTitle("Reason");
                    dialogReason.setHeaderText(null);

                    ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    dialogReason.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
                    GridPane grid = new GridPane();
                    dialogReason.getDialogPane().setContent(grid);
                    grid.add(new Label("Reasons "), 0, 1);
                    grid.add(reason, 1, 1);

                    dialogReason.setResultConverter(dialogButton -> {
                        if (dialogButton == buttonTypeOk) {
                            String newStatus = "Denied";
                            int newStatusNbr = -1;
                            int i = dataResList.indexOf(dataReservation);
                            dataReservation.setStatus(newStatus);
                            Reservation res = resList.get(i);
                            res.setStatus(newStatusNbr);
                            res.getUser().sendNotification("Reservation", "Your reservation for " + dataReservation.getBrandName() + " " + dataReservation.getModelName()
                                    + " has been Denied for the following reason : \n" + reason.getText());
                            return dataReservation;
                        } else {
                            System.out.println("Action canceled!");
                        }
                        return null;
                    });
                    Optional<DataReservation> result = dialogReason.showAndWait();
                    result.ifPresent(updatedRes -> {
                        int index = TableViewReservation.getItems().indexOf(updatedRes);
                        if (index != -1) {
                            dataResList.set(index, updatedRes);
                        }
                    });
                });
                endButton.setOnAction(event -> {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());

                    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirm.setTitle("Confirmation");
                    alertConfirm.setHeaderText("Are you sure?");
                    alertConfirm.setContentText("This action cannot be undone.");
                    DialogPane dialogPaneConfirm = alertConfirm.getDialogPane();
                    dialogPaneConfirm.getStylesheets().add(
                            getClass().getResource("style/stylesDelete.css").toExternalForm()
                    );
                    dialogPaneConfirm.getStyleClass().add("custom-alert");

                    Alert alertSure = new Alert(Alert.AlertType.INFORMATION);
                    alertSure.setTitle("Reservation Ended");
                    alertSure.setHeaderText(null);
                    alertSure.setContentText("Your account has been deleted.");
                    DialogPane dialogPane = alertSure.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("style/stylesDelete.css").toExternalForm()
                    );
                    dialogPane.getStyleClass().add("custom-alert");

                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/delete.png")));
                    dialogPane.setGraphic(imageView);

                    alertConfirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            String newStatus = "Ended";
                            int newStatusNbr = 2;
                            int i = dataResList.indexOf(dataReservation);
                            dataReservation.setStatus(newStatus);
                            Reservation res = resList.get(i);
                            res.setStatus(newStatusNbr);
                            res.getUser().sendNotification("Reservation", "Your reservation for " + dataReservation.getBrandName() + " " + dataReservation.getModelName()
                                    + " has finished for the following reason : \n");
                            TableViewReservation.refresh();
                        } else {
                            System.out.println("Action canceled!");
                        }
                    });
                });
                refuseButton.setStyle("-fx-cursor: hand; -fx-background-radius: 30; -fx-background-color: #FF0000; -fx-border-radius: 30; -fx-pref-width: 70px");
                refuseButton.setTextFill(javafx.scene.paint.Color.WHITE);
                endButton.setStyle("-fx-cursor: hand; -fx-background-radius: 30; -fx-background-color: #FF0000; -fx-border-radius: 30; -fx-pref-width: 70px");
                endButton.setTextFill(javafx.scene.paint.Color.WHITE);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    DataReservation dataReservation = getTableView().getItems().get(getIndex());
                    String status = dataReservation.getStatus();
                    if(status.equals("Pending")) {
                        HBox buttonBox = new HBox(approveButton, refuseButton, deleteButton);
                        buttonBox.setSpacing(10);
                        buttonBox.setAlignment(Pos.CENTER);
                        setGraphic(buttonBox);
                    } else if (status.equals("Approved")) {
                        HBox buttonBox = new HBox(endButton, modifyButton,deleteButton);
                        buttonBox.setSpacing(10);
                        buttonBox.setAlignment(Pos.CENTER);
                        setGraphic(buttonBox);
                }else{
                        HBox buttonBox = new HBox(modifyButton, deleteButton);
                        buttonBox.setSpacing(15);
                        buttonBox.setAlignment(Pos.CENTER);
                        setGraphic(buttonBox);
                    }
                }
            }
        });
        TableViewReservation.setItems(dataResList);

        statusId0.setOnAction(event -> {
            updateTableView();
        });
        statusId1.setOnAction(event -> {
            updateTableView();
        });
        statusId2.setOnAction(event -> {
            updateTableView();
        });
        statusId3.setOnAction(event -> {
            updateTableView();
        });
        statusId4.setOnAction(event -> {
            updateTableView();
        });
    }
    private void showEditDialog(DataReservation dataRes) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Dialog<DataReservation> dialog = new Dialog<>();
        dialog.setTitle("Modify the reservation");
        dialog.setHeaderText(null);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        ComboBox<Integer> idUserField = new ComboBox<>();
        idUserField.setItems(FXCollections.observableList(User.getAllUsersId()));
        idUserField.getSelectionModel().select(User.getAllUsersId().indexOf(dataRes.getIdU()));
        ComboBox<Integer> idVehicleField = new ComboBox<>();
        idVehicleField.setItems(FXCollections.observableList(Vehicle.getAllVehicleId()));
        idVehicleField.getSelectionModel().select(Vehicle.getAllVehicleId().indexOf(dataRes.getIdV()));
        TextField startDateField = new TextField(format.format(dataRes.getStartDate()));
        TextField endDateField = new TextField(format.format(dataRes.getEndDate()));
        ComboBox<String> statusField = new ComboBox<>();
        ObservableList<String> items = FXCollections.observableArrayList("Pending", "Approved", "Ended", "Denied", "Canceled");
        statusField.setItems(items);
        statusField.getSelectionModel().select(dataRes.getStatus());
        grid.add(new Label("Id User"), 0, 1);
        grid.add(idUserField, 1, 1);
        grid.add(new Label("Id Vehicle"), 0, 2);
        grid.add(idVehicleField, 1, 2);
        grid.add(new Label("Start Date"), 0, 3);
        grid.add(startDateField, 1, 3);
        grid.add(new Label("End Date"),0,4);
        grid.add(endDateField, 1, 4);
        grid.add(new Label("Status"),0,5);
        grid.add(statusField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                Integer newIdUser = idUserField.getValue();
                Integer newIdVehicle = idVehicleField.getValue();
                String newFullName = User.getUserById(newIdUser).getFullName();
                String newPhoneNumber = User.getUserById(newIdUser).getPhoneNumber();
                String newBrandName = Vehicle.getVehiclesById(newIdVehicle).getBrandName();
                String newModelName = Vehicle.getVehiclesById(newIdVehicle).getModelName();
                Float newPrice = Vehicle.getVehiclesById(newIdVehicle).getPrice();
                String newStartDate = startDateField.getText();
                if(newStartDate == null){
                    showAlert("Error", "Start Date is empty");
                    dialog.showAndWait();
                    return null;
                }
                String newEndDate = endDateField.getText();
                if(newEndDate == null){
                    showAlert("Error", "End Date is empty");
                    dialog.showAndWait();
                    return null;
                }
                String newStatus = statusField.getValue();
                dataRes.setFullName(newFullName);
                dataRes.setPhoneNumber(newPhoneNumber);
                dataRes.setBrandName(newBrandName);
                dataRes.setModelName(newModelName);
                dataRes.setPrice(newPrice);
                int i = dataResList.indexOf(dataRes);
                Reservation res = resList.get(i);
                res.setUser(User.getUserById(newIdUser));
                res.setVehicle(Vehicle.getVehiclesById(newIdVehicle));
                try {
                res.setStartDate(format.parse(newStartDate));
                res.setEndDate(format.parse(newEndDate));
                if(newStatus.equals("Pending")){
                    res.setStatus(0);
                }else if(newStatus.equals("Approved")){
                    res.setStatus(1);
                }else if(newStatus.equals("Ended")){
                    res.setStatus(2);
                }else if(newStatus.equals("Canceled")){
                    res.setStatus(-2);
                }else{
                    res.setStatus(-1);
                }
                dataRes.setStartDate(format.parse(newStartDate));
                dataRes.setEndDate(format.parse(newEndDate));
                } catch (ParseException e) {
                    showAlert("Error", "Date and Time Field is empty");
                    dialog.showAndWait();
                    //throw new RuntimeException(e);
                }
                dataRes.setStatus(newStatus);
                return dataRes;
            }
            return null;
        });

        Optional<DataReservation> result = dialog.showAndWait();
        result.ifPresent(updatedRes -> {
            int index = TableViewReservation.getItems().indexOf(updatedRes);
            if (index != -1) {
                dataResList.set(index, updatedRes);
            }
        });
    }

    private void showAddDialog() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Dialog<DataReservation> dialog = new Dialog<>();
        dialog.setTitle("Add a reservation");
        dialog.setHeaderText(null);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        ComboBox<Integer> idUserField = new ComboBox<>();
        ObservableList<Integer> idUList = FXCollections.observableArrayList();
        for(int idUser: User.getAllUsersId()){
            idUList.add(idUser);
        }
        idUserField.setItems(idUList);
        ComboBox<Integer> idVehicleField = new ComboBox<>();
        ObservableList<Integer> idVList = FXCollections.observableArrayList();
        for(int idVehicle: Vehicle.getAllVehicleId()){
                idVList.add(idVehicle);
        }
        idVehicleField.setItems(idVList);

        TextField startDateField = new TextField();
        startDateField.setPromptText("yyyy-MM-dd HH:mm:ss");
        TextField endDateField = new TextField();
        endDateField.setPromptText("yyyy-MM-dd HH:mm:ss");

        ComboBox<String> statusField = new ComboBox<>();
        ObservableList<String> items = FXCollections.observableArrayList("Pending", "Approved", "Ended", "Denied", "Canceled");
        statusField.setItems(items);
        statusField.getSelectionModel().selectFirst();
        grid.add(new Label("Id User"), 0, 1);
        grid.add(idUserField, 1, 1);
        grid.add(new Label("Id Vehicle"), 0, 2);
        grid.add(idVehicleField, 1, 2);
        grid.add(new Label("Start Date"), 0, 3);
        grid.add(startDateField, 1, 3);
        grid.add(new Label("End Date"),0,4);
        grid.add(endDateField, 1, 4);
        grid.add(new Label("Status"),0,5);
        grid.add(statusField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                Integer newIdUser = idUserField.getValue();
                if(newIdUser == null){
                    showAlert("Error", "IdUser is empty");
                    dialog.showAndWait();
                    return null;
                }
                Integer newIdVehicle = idVehicleField.getValue();
                if(newIdVehicle == null){
                    showAlert("Error", "IdVehicle is empty");
                    dialog.showAndWait();
                    return null;
                }
                String newFullName = User.getUserById(newIdUser).getFullName();
                String newPhoneNumber = User.getUserById(newIdUser).getPhoneNumber();
                String newBrandName = Vehicle.getVehiclesById(newIdVehicle).getBrandName();
                String newModelName = Vehicle.getVehiclesById(newIdVehicle).getModelName();
                float newPrice = Vehicle.getVehiclesById(newIdVehicle).getPrice();
                String newStartDate = String.valueOf(startDateField.getText());
                if(newStartDate == null){
                    showAlert("Error", "Start Date is empty");
                    dialog.showAndWait();
                    return null;
                }
                String newEndDate = String.valueOf(endDateField.getText());
                if(newEndDate == null){
                    showAlert("Error", "End Date is empty");
                    dialog.showAndWait();
                    return null;
                }
                String newStatus = statusField.getValue();
                Integer status = null;
                if(newStatus.equals("Pending")){
                    status = 0;
                }else if(newStatus.equals("Approved")){
                    status = 1;
                }else if(newStatus.equals("Ended")){
                    status = 2;
                }else if(newStatus.equals("Canceled")){
                    status = -2;
                }else{
                    status = -1;
                }
                try {
                    Date starDate = format.parse(newStartDate);
                    Date endDate = format.parse(newEndDate);
                    Reservation.create(User.getUserById(newIdUser), Vehicle.getVehiclesById(newIdVehicle), starDate, endDate, status);
                    DataReservation data = new DataReservation(newFullName, newPhoneNumber, newBrandName, newModelName, newPrice, format.parse(newStartDate), format.parse(newEndDate), newStatus);
                    dataResList.add(data);
                } catch (ParseException e) {
                    showAlert("Error", "Date and Time Field is empty");
                    dialog.showAndWait();
                    return null;
                }

                TableViewReservation.setItems(dataResList);
            }
            return null;
        });

        Optional<DataReservation> result = dialog.showAndWait();
        result.ifPresent(updatedRes -> {
            int index = TableViewReservation.getItems().indexOf(updatedRes);
            if (index != -1) {
                dataResList.set(index, updatedRes);
            }
        });
    }

    private void applySearchFilter(String searchKeyword) {
        TableViewReservation.setItems(dataResList.filtered(resData -> {
            if (searchKeyword.isEmpty()) {
                return true;
            } else {
                String lowerCaseSearchTerm = searchKeyword.toLowerCase();
                String fullName = resData.getFullName().toLowerCase();
                String brandName = resData.getBrandName().toLowerCase();
                String modelName = resData.getModelName().toLowerCase();
                fullName = fullName != null ? fullName : "";
                brandName = brandName != null ? brandName : "";
                modelName = modelName != null ? modelName : "";
                return fullName.contains(lowerCaseSearchTerm) ||
                        brandName.contains(lowerCaseSearchTerm) ||
                        modelName.contains(lowerCaseSearchTerm);
            }
        }));
        TableViewReservation.refresh();
    }

    private void updateTableView() {
        TableViewReservation.setItems(dataResList.filtered(reservation -> {
            boolean statusFilter = statusId0.isSelected() && reservation.getStatus().equals("Pending") ||
                    (statusId1.isSelected() && reservation.getStatus().equals("Approved")) ||
                    (statusId2.isSelected() && reservation.getStatus().equals("Ended")) ||
                    (statusId3.isSelected() && reservation.getStatus().equals("Denied"))||
                    (statusId4.isSelected() && reservation.getStatus().equals("Canceled"));;
            return statusFilter;
        }));
        if(!statusId0.isSelected() && !statusId1.isSelected() &&!statusId2.isSelected() &&!statusId3.isSelected() &&!statusId4.isSelected()){
            clearFilter();
        }
    }

    private void clearFilter(){
        dataResList.clear();
        resList = Reservation.getAllReservations();
        for(Reservation i:resList) {
            DataReservation res = new DataReservation(i.getUser().getId(),i.getVehicle().getId(),i.getUser().getFullName(), i.getUser().getPhoneNumber(), i.getVehicle().getBrandName(), i.getVehicle().getModelName(), i.getVehicle().getPrice(), i.getStartDate(), i.getEndDate(), String.valueOf(i.getStatus()));
            dataResList.add(res);
        }
        TableViewReservation.setItems(dataResList);
    }
}