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
import com.github.idelstak.appointments.database.DatabaseConnectionService.DatabaseConnectionStatus;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DatabaseSettingsPaneController {

    private static final Logger LOG = Logger.getLogger(DatabaseSettingsPaneController.class.getName());
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ProgressIndicator connectionAttemptProgress;
    @FXML
    private Label connectionErrorStatusLabel;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private TextField databaseNameTextField;
    @FXML
    private VBox databaseSettingsPane;
    @FXML
    private TextField databaseURLTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button saveConnectionSettingsButton;
    @FXML
    private Button testConnectionButton;
    @FXML
    private TextField userNameTextField;
    private final DatabaseConnectionService databaseConnectionService;

    public DatabaseSettingsPaneController(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @FXML
    protected void initialize() {
        var url = databaseConnectionService.getDatabaseURL();

        if (notNullAndMoreThanFiveLetters(url)) {
            var uri = URI.create(url.substring(5));

            logUri(uri);

            hostTextField.setText(uri.getHost());
            databaseNameTextField.setText(uri.getPath().substring(1));
            databaseURLTextField.setText(url);
        }

        userNameTextField.setText(databaseConnectionService.getUsername());
        passwordTextField.setText(new String(databaseConnectionService.getPassword()));

        hostTextField.textProperty().addListener((o, ov, nv) -> updateURL(nv, databaseNameTextField.getText()));
        databaseNameTextField.textProperty().addListener((o, ov, nv) -> updateURL(hostTextField.getText(), nv));

        saveConnectionSettingsButton
                .disableProperty()
                .bind(Bindings.createBooleanBinding(
                        () -> databaseConnectionService.getDatabaseConnectionStatusProperty().get() == DatabaseConnectionStatus.FAILED,
                        databaseConnectionService.getDatabaseConnectionStatusProperty())
                );

        connectionAttemptProgress.visibleProperty().bind(databaseConnectionService.runningProperty());
        connectionErrorStatusLabel.textProperty().bind(databaseConnectionService.messageProperty());
        connectionErrorStatusLabel.visibleProperty().bind(saveConnectionSettingsButton.disableProperty());
    }

    @FXML
    protected void testDatabaseConnection(ActionEvent event) {
        databaseConnectionService.setDatabaseURL(databaseURLTextField.getText());
        databaseConnectionService.setUsername(userNameTextField.getText());
        var password = passwordTextField.getText();
        databaseConnectionService.setPassword(password == null ? new char[]{} : password.toCharArray());
        databaseConnectionService.restart();
    }

    private void updateURL(String hostName, String databaseName) {
        databaseURLTextField.setText(String.format("jdbc:mysql://%s/%s", hostName, databaseName));
    }

    private static boolean notNullAndMoreThanFiveLetters(String text) {
        return text != null && text.length() > 5;
    }

    private static void logUri(URI uri) {
        LOG.log(
                Level.INFO,
                """
                
                authority: {0}; 
                fragment: {1}; 
                host: {2}; 
                path: {3}; 
                port: {4}; 
                query: {5}; 
                scheme: {6}; 
                schemeSpecificPart: {7}; 
                userInfo: {8}
                """,
                new Object[]{
                    uri.getAuthority(),
                    uri.getFragment(),
                    uri.getHost(),
                    uri.getPath(),
                    Integer.toString(uri.getPort()),
                    uri.getQuery(),
                    uri.getScheme(),
                    uri.getSchemeSpecificPart(),
                    uri.getUserInfo()
                }
        );
    }

}
