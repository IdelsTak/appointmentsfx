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
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignInService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainStackPaneController {

    private static final Logger LOG = Logger.getLogger(MainStackPaneController.class.getName());
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private StackPane mainStackPane;
    private final DatabaseConnectionService databaseConnectionService;
    private final DisplayedView displayedView;

    public MainStackPaneController(DatabaseConnectionService databaseConnectionService, DisplayedView displayedView) {
        this.databaseConnectionService = databaseConnectionService;
        this.displayedView = displayedView;
    }

    @FXML
    protected void initialize() throws IOException {
        var databaseCheckProgressPaneController = new DatabaseCheckProgressPaneController(databaseConnectionService, displayedView);
        var checkDatabaseProgressPane = (Node) FxmlWithControllerLoader.load(DisplayedPane.DATABASE_CONNECTION_CHECK_PANE.getFxmlPath(), databaseCheckProgressPaneController);
        var databaseSettingsPaneController = new DatabaseSettingsPaneController(databaseConnectionService);
        var databaseSettingsPane = (Node) FxmlWithControllerLoader.load(DisplayedPane.DATABASE_SETTINGS_PANE.getFxmlPath(), databaseSettingsPaneController);
        var signInService = new SignInService(List.of(new Credentials("admin", "admin".toCharArray())));
        var signInPaneController = new SignInPaneController(signInService);
        var signInPane = (Node) FxmlWithControllerLoader.load(DisplayedPane.SIGN_IN_PANE.getFxmlPath(), signInPaneController);

        mainStackPane
                .getChildren()
                .addAll(checkDatabaseProgressPane, databaseSettingsPane, signInPane);

        databaseConnectionService
                .getDatabaseConnectionStatusProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue == DatabaseConnectionService.DatabaseConnectionStatus.SUCCESSFUL) {
                        displayedView.getDisplayedPaneProperty().set(DisplayedPane.SIGN_IN_PANE);
                    }
                });

        displayedView
                .getDisplayedPaneProperty()
                .addListener((observable, oldValue, newValue) -> {
                    updateDisplayedPane(newValue);
                });

        updateDisplayedPane(displayedView.getDisplayedPaneProperty().get());
    }

    private void updateDisplayedPane(DisplayedPane displayedPane) {
        mainStackPane
                .getChildrenUnmodifiable()
                .stream()
                .forEach(node -> {
                    var idsMatch = Objects.equals(node.getId(), displayedPane.getPaneId());
                    node.setVisible(idsMatch);
                });
    }
}
