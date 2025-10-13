module org.example.hotelgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;


    opens org.example.hotelgui to javafx.fxml;
    exports org.example.hotelgui;
    exports org.example.hotelgui.model;
    opens org.example.hotelgui.model to javafx.fxml;
    exports org.example.hotelgui.controller;
    opens org.example.hotelgui.controller to javafx.fxml;
}