package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DisplayedView;
import java.util.Collections;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;

public class SignInPaneControllerTest extends ApplicationWithSetStageTest {

    public SignInPaneControllerTest() {
        super(createRoot());
    }

    @Test
    public void signInErrorLabel_is_hidden_when_user_starts_to_signIn() throws Exception {
        var signInErrorLabel = lookup("#signInErrorLabel").query();

        Assertions.assertThat(signInErrorLabel).isInvisible();
    }

    @Test
    public void signInProgressIndicator_is_hidden_when_user_starts_to_signIn() throws Exception {
        var signInProgressIndicator = lookup("#signInProgressIndicator").query();

        Assertions.assertThat(signInProgressIndicator).isInvisible();
    }

    @Test
    public void clicking_signInButton_displays_signInProgressIndicator() throws Exception {
        clickOn("#signInButton", MouseButton.PRIMARY);
        
        var signInProgressIndicator = lookup("#signInProgressIndicator").query();

        Assertions.assertThat(signInProgressIndicator).isVisible();
    }

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.SIGN_IN_PANE.getFxmlPath();
        var controller = new SignInPaneController(Collections.emptyList());

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }

}
