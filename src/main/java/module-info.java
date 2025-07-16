module com.myown.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.myown.demo to javafx.fxml;
    exports com.myown.demo;
}