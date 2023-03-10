package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignedIn;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;

public class AppViewPaneControllerTest extends ApplicationWithSetStageTest {

    private static final SignedIn SIGNED_IN;
    
    static {
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

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.APP_VIEW_PANE.getFxmlPath();
        var controller = new AppViewPaneController(SIGNED_IN);

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }
    

}
