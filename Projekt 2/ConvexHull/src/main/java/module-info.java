module org.pwo.convexhull {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.slf4j;
    requires transitive javafx.graphics;
    requires java.sql;


    opens org.pwo.convexhull to javafx.fxml;
    exports org.pwo.convexhull;
}