module com.dtphat.slangdictionary {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.dtphat.slangdictionary to javafx.fxml;
    exports com.dtphat.slangdictionary;
}