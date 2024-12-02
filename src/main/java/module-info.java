module com.finalsproject.finalsproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.finalsproject.finalsproject.controllers to javafx.fxml, javafx.base;
    exports com.finalsproject.finalsproject.controllers;

    opens com.finalsproject.finalsproject to javafx.fxml;
    exports com.finalsproject.finalsproject;
}