package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignedIn;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class AppViewPaneController {

    private static final Logger LOG = Logger.getLogger(AppViewPaneController.class.getName());
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private BorderPane appViewPane;
    @FXML
    private RadioButton appointmentsRadioButton;
    @FXML
    private RadioButton customersRadioButton;
    @FXML
    private Label headerLabel;
    @FXML
    private Label loggedInUsenameLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private StackPane mainContentStackPane;
    @FXML
    private ToggleGroup navigationButtonsGroup;
    @FXML
    private RadioButton reportsRadioButton;
    private final SignedIn signedIn;
    private final DisplayedView displayedView;

    public AppViewPaneController(SignedIn signedIn, DisplayedView displayedView) {
        this.signedIn = signedIn;
        this.displayedView = displayedView;
    }

    @FXML
    protected void initialize() {
        loggedInUsenameLabel
                .textProperty()
                .bind(Bindings.createStringBinding(
                        () -> {
                            var optionalCredentials = signedIn.getCredentialsProperty().get();
                            return optionalCredentials.map(Credentials::getUsername).orElse("");
                        },
                        signedIn.getCredentialsProperty()
                ));
    }

    @FXML
    protected void logOut(ActionEvent event) {
        displayedView.setPane(DisplayedPane.SIGN_IN_PANE);
        event.consume();
    }
}
