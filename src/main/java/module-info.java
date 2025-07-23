module com.myown.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.myown.demo to javafx.fxml;
    exports com.myown.demo;
}