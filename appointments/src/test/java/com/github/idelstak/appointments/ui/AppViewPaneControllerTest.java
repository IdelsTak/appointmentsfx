package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignedIn;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;

public class AppViewPaneControllerTest extends ApplicationWithSetStageTest {

    private static final SignedIn SIGNED_IN;
    private static final DisplayedView DISPLAYED_VIEW;

    static {
        DISPLAYED_VIEW = new DisplayedView(DisplayedPane.APP_VIEW_PANE);
        SIGNED_IN = new SignedIn();
        SIGNED_IN
                .getCredentialsProperty()
                .setValue(Optional.of(new Credentials("admin", "admin".toCharArray())));
    }

    public AppViewPaneControllerTest() {
        super(createRoot());
    }

    @Test
    public void shows_signedIn_username_on_start() throws Exception {
        var loggedInUsenameLabel = (Labeled) lookup("#loggedInUsenameLabel").query();

        Assertions.assertThat(loggedInUsenameLabel).hasText(SIGNED_IN.getCredentialsProperty().get().map(Credentials::getUsername).orElseThrow());
    }

    @Test
    public void clicking_logoutButton_displays_signInPane() throws Exception {
        clickOn("#logoutButton", MouseButton.PRIMARY);
        
        MatcherAssert.assertThat(DISPLAYED_VIEW.getPane(), CoreMatchers.equalTo(DisplayedPane.SIGN_IN_PANE));
    }

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.APP_VIEW_PANE.getFxmlPath();
        var controller = new AppViewPaneController(SIGNED_IN, DISPLAYED_VIEW);

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }
    

}
