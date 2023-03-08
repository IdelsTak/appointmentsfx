/*
 * MIT License
 *
 * Copyright (c) 2023 Hiram K
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DatabaseConnectionService;
import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class DatabaseCheckProgressPaneController {

    private static final Logger LOG = Logger.getLogger(DatabaseCheckProgressPaneController.class.getName());
    private final DatabaseConnectionService databaseConnectionService;
    private final DisplayedView displayedView;
    @FXML
    private Label databaseConnectionStatusLabel;
    @FXML
    private Label databaseConnectionErrorLabel;
    @FXML
    private ProgressIndicator databaseConnectionCheckProgressIndicator;
    @FXML
    private Button databaseSettingsButton;

    public DatabaseCheckProgressPaneController(DatabaseConnectionService databaseConnectionService, DisplayedView displayedView) {
        this.databaseConnectionService = databaseConnectionService;
        this.displayedView = displayedView;
    }

    @FXML
    public void initialize() {
        databaseConnectionStatusLabel.textProperty().bind(databaseConnectionService.titleProperty());
        databaseConnectionCheckProgressIndicator.progressProperty().bind(databaseConnectionService.progressProperty());
        databaseConnectionCheckProgressIndicator.visibleProperty().bind(databaseConnectionService.runningProperty());
        databaseConnectionErrorLabel.visibleProperty().bind(databaseConnectionService.runningProperty().not());
        databaseConnectionErrorLabel.textProperty().bind(databaseConnectionService.messageProperty());
        databaseSettingsButton.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> databaseConnectionErrorLabel.getText() != null && !databaseConnectionErrorLabel.getText().isBlank(),
                        databaseConnectionErrorLabel.textProperty()
                )
        );

        databaseConnectionService.start();
    }

    @FXML
    public void showDatabaseSettingsPane(ActionEvent event) {
        displayedView
                .getDisplayedPaneProperty()
                .setValue(DisplayedPane.DATABASE_SETTINGS_PANE);
        event.consume();
    }

}
