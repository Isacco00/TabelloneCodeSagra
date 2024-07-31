module com.example.tabellonecodesagra {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.github.kwhat.jnativehook;
    requires org.postgresql.jdbc;


    opens com.example.tabellonecodesagra to javafx.fxml;
    exports com.example.tabellonecodesagra;
}