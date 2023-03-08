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

import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignInService;
import com.github.idelstak.appointments.signin.SignInService.SignInStatus;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SignInPaneController {

    private static final Logger LOG = Logger.getLogger(SignInPaneController.class.getName());
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Label signInErrorLabel;
    @FXML
    private VBox signInPane;
    @FXML
    private ProgressIndicator signInProgressIndicator;
    @FXML
    private TextField userNameTextField;

    private final SignInService signInService;
    private final DisplayedView displayedView;

    public SignInPaneController(SignInService signInService, DisplayedView displayedView) {
        this.signInService = signInService;
        this.displayedView = displayedView;
    }

    @FXML
    protected void initialize() {
        signInErrorLabel.setVisible(false);
        signInService
                .getSignInStatusProperty()
                .addListener((observable, oldSignInStatus, newSignInStatus) -> {
                    signInErrorLabel.setVisible(newSignInStatus == SignInStatus.SIGNED_OUT);
                });

        Platform.runLater(() -> {
            signInProgressIndicator.visibleProperty().bind(signInService.runningProperty());
            signInProgressIndicator.progressProperty().bind(signInService.progressProperty());
        });
    }

    @FXML
    protected void signIn(ActionEvent event) {
        var passwordText = passwordField.getText();

        signInService.check(new Credentials(userNameTextField.getText(), passwordText == null ? new char[]{} : passwordText.toCharArray()));

        signInService.setOnSucceeded(stateEvent -> {
            var optionalCredentials = (Optional<Credentials>) stateEvent.getSource().getValue();

            optionalCredentials
                    .ifPresent(credentials -> {
                        displayedView
                                .getDisplayedPaneProperty()
                                .setValue(DisplayedPane.APP_VIEW_PANE);
                    });
        });
    }
}
