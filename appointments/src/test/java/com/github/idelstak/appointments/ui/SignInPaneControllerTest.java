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
import com.github.idelstak.appointments.signin.SignedIn;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import static org.hamcrest.CoreMatchers.equalTo;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;

public class SignInPaneControllerTest extends ApplicationWithSetStageTest {

    private static final SignInService SIGN_IN_SERVICE = new SignInService(List.of(new Credentials("admin", "admin".toCharArray())));
    private static final DisplayedView DISPLAYED_VIEW = new DisplayedView(DisplayedPane.DATABASE_CONNECTION_CHECK_PANE);

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
        var signInProgressIndicator = sleep(3_000L).lookup("#signInProgressIndicator").query();

        Assertions.assertThat(signInProgressIndicator).isInvisible();
    }

    @Test
    public void clicking_signInButton_displays_signInProgressIndicator() throws Exception {
        clickOn("#signInButton", MouseButton.PRIMARY);

        var signInProgressIndicator = lookup("#signInProgressIndicator").query();

        Assertions.assertThat(signInProgressIndicator).isVisible();
    }

    @Test
    public void shows_signInErrorLabel_when_signInButton_is_clicked_and_supplied_credentials_dont_match_existing_ones() throws Exception {
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("johndoe");
        doubleClickOn("#passwordField", MouseButton.PRIMARY).write("bad-password");

        clickOn("#signInButton", MouseButton.PRIMARY);

        sleep(3_000L);
        
        MatcherAssert.assertThat(SIGN_IN_SERVICE.getSignInStatusProperty().get(), equalTo(SignInStatus.SIGNED_OUT));
    }
    
    @Test
    public void hides_signInErrorLabel_when_signInButton_is_clicked_and_supplied_credentials_match_existing_ones() throws Exception {
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordField", MouseButton.PRIMARY).write("admin");

        clickOn("#signInButton", MouseButton.PRIMARY);
        
        sleep(3_000L);

        MatcherAssert.assertThat(SIGN_IN_SERVICE.getSignInStatusProperty().get(), equalTo(SignInStatus.SIGNED_IN));
    }
    
    @Test
    public void shows_main_appViewPane_when_sign_in_is_successful() throws Exception {
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordField", MouseButton.PRIMARY).write("admin");

        clickOn("#signInButton", MouseButton.PRIMARY);
        
        sleep(3_000L);
        
        assertThat(DISPLAYED_VIEW.getPane(), equalTo(DisplayedPane.APP_VIEW_PANE));
    }

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.SIGN_IN_PANE.getFxmlPath();
        var controller = new SignInPaneController(SIGN_IN_SERVICE, DISPLAYED_VIEW, new SignedIn());

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }
    

}
