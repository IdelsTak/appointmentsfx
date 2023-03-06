package com.github.idelstak.appointments.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainWindow extends StackPane {

    public MainWindow() {
        var checkingDatabaseLabel = new Label("Checking database connection...");
        checkingDatabaseLabel.setId("checking-database-label");
        
        getChildren().add(checkingDatabaseLabel);
    }

}
