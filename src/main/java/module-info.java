module com.example.tabellonecodesagra {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires jakarta.inject;
    requires com.github.kwhat.jnativehook;


    opens com.example.tabellonecodesagra to javafx.fxml;
    exports com.example.tabellonecodesagra;
}