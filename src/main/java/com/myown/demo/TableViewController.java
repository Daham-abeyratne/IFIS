package com.myown.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TableViewController implements Initializable {

    @FXML private TableView<RecordsWrapper> recordsTable;
    @FXML private TableColumn<RecordsWrapper, String> incomeCodeColumn;
    @FXML private TableColumn<RecordsWrapper, String> descriptionColumn;
    @FXML private TableColumn<RecordsWrapper, String> dateColumn;
    @FXML private TableColumn<RecordsWrapper, Double> incomeAmountColumn;
    @FXML private TableColumn<RecordsWrapper, Double> withholdingTaxColumn;
    @FXML private TableColumn<RecordsWrapper, Integer> checksumColumn;
    @FXML private TableColumn<RecordsWrapper, Boolean> validColumn;
    @FXML private Label importedRecordCount;
    @FXML private Label validRecordCount;
    @FXML private Label invalidRecordCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recordsTable.setEditable(true);

        incomeCodeColumn.setCellValueFactory(new PropertyValueFactory<>("incomeCode"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        incomeAmountColumn.setCellValueFactory(new PropertyValueFactory<>("incomeAmount"));
        withholdingTaxColumn.setCellValueFactory(new PropertyValueFactory<>("withholdingTax"));
        checksumColumn.setCellValueFactory(new PropertyValueFactory<>("checksum"));
        validColumn.setCellValueFactory(new PropertyValueFactory<>("valid"));

        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        incomeAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new TwoDecimalDoubleConverter()));
        withholdingTaxColumn.setCellFactory(TextFieldTableCell.forTableColumn(new TwoDecimalDoubleConverter()));


        descriptionColumn.setOnEditCommit(e -> {
            String tempdescripiton = e.getOldValue();
            Records record = e.getRowValue().getRecord();
            record.setDescription(e.getNewValue());
            if(record.getDescription().length()>20){  //if the description exceed the limit throws an alert and restore the old value in the same place
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Exceeding 20 characters");
                alert.setContentText("The description can not exceed 20 characters");
                alert.showAndWait();
                record.setDescription(tempdescripiton);
                updateSingleRowChecksum(record);
            }else {
                updateSingleRowChecksum(record);
            }
        });

        dateColumn.setOnEditCommit(e -> {
            String tempdate = e.getOldValue();
            Records record = e.getRowValue().getRecord();
            record.setDate(e.getNewValue());
            if (record.getDate().matches("^\\d{2}/\\d{2}/\\d{4}$")){
                updateSingleRowChecksum(record);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Date format error");
                alert.setContentText("The date should be in dd/mm/yyyy format");
                alert.showAndWait();
                record.setDate(tempdate);
                updateSingleRowChecksum(record);
            }
        });

        incomeAmountColumn.setOnEditCommit(e -> {
            Double tempIncomeAmount = e.getOldValue();
            Records record = e.getRowValue().getRecord();
            record.setIncomeAmount(e.getNewValue());
            if(record.getIncomeAmount()<=0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Income Amount error");
                alert.setContentText("The income amount can not be a negative or zero");
                alert.showAndWait();
                record.setIncomeAmount(tempIncomeAmount);
                updateSingleRowChecksum(record);
            }
            else {
                updateSingleRowChecksum(record);
            }
        });

        withholdingTaxColumn.setOnEditCommit(e -> {
            Records record = e.getRowValue().getRecord();
            record.setWithHoldingTax(e.getNewValue());
            updateSingleRowChecksum(record);
        });

        validColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✓" : "✗");
                    setStyle(item ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }
        });

        recordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public class TwoDecimalDoubleConverter extends StringConverter<Double> {
        @Override
        public String toString(Double value) {
            return (value == null) ? "" : String.format("%.2f", value);
        }

        @Override
        public Double fromString(String text) {
            try {
                return (text == null || text.trim().isEmpty()) ? 0.0 : Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return 0.0; // Or throw an exception if needed
            }
        }
    }

    public void setRecordsData(List<Records> recordsData) {
        ObservableList<RecordsWrapper> data = FXCollections.observableArrayList();
        int total = 0, valid = 0, invalid = 0;

        for (Records record : recordsData) {
            data.add(new RecordsWrapper(record));
            total++;
            if (record.isValid()) valid++;
            else invalid++;
        }

        recordsTable.setItems(data);
        importedRecordCount.setText(String.valueOf(total));
        validRecordCount.setText(String.valueOf(valid));
        invalidRecordCount.setText(String.valueOf(invalid));
    }

    @FXML
    private void OnValidateChecksumButtonClick() {
        for (RecordsWrapper wrapper : recordsTable.getItems()) {
            Records r = wrapper.getRecord();
            int newChecksum = RecordsWrapper.calculateItemChecksum(r.getIncomeCode(), r.getDescription(), r.getDate(), r.getIncomeAmount(), r.getWithHoldingTax());
            r.setChecksum(newChecksum);
            r.setValid(true);
            r.isIncomeCodeValid();
            r.isDescriptionValid();
        }
        setRecordsData(recordsTable.getItems().stream().map(RecordsWrapper::getRecord).toList());
    }

    @FXML
    private void OnUpdateButtonClick(){
        return;
    }

//    public void tableEditUpdate(){
//
//    }
    private void updateSingleRowChecksum(Records record) {
        int newChecksum = RecordsWrapper.calculateItemChecksum(
                record.getIncomeCode(),
                record.getDescription(),
                record.getDate(),
                record.getIncomeAmount(),
                record.getWithHoldingTax()
        );
        record.setChecksum(newChecksum);
        recordsTable.refresh();
    }

    @FXML
    private void OnDeleteButtonClick() {
        ObservableList<RecordsWrapper> validItems = FXCollections.observableArrayList();
        for (RecordsWrapper w : recordsTable.getItems()) {
            if (w.getRecord().isValid()) {
                validItems.add(w);
            }
        }
        setRecordsData(validItems.stream().map(RecordsWrapper::getRecord).toList());
    }
}
