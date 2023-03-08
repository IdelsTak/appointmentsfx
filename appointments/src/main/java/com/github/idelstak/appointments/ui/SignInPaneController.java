package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.signin.Credentials;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SignInPaneController {

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

    private final List<Credentials> allCredentials;

    public SignInPaneController(Collection<Credentials> credentials) {
        allCredentials = new ArrayList<>(credentials);
    }

    @FXML
    protected void initialize() {

    }

    @FXML
    protected void signIn(ActionEvent event) {

    }
}
