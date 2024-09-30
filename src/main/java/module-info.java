module milktea.milktea {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires org.slf4j;


//    opens milktea.milktea.GUI to javafx.fxml;
    opens milktea.milktea.DTO to javafx.base;
//    opens milktea.milktea.BUS to javafx.base;
    exports milktea.milktea.DTO;
//    exports milktea.milktea.GUI;
}