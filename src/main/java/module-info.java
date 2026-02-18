module org.example.tapisjeufamilial {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires annotations;

    opens org.example.tapisjeufamilial to javafx.fxml;
    exports org.example.tapisjeufamilial;
}