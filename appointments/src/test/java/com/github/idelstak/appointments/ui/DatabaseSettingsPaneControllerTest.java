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

import com.github.idelstak.appointments.database.DatabaseConnectionPreferences;
import com.github.idelstak.appointments.database.DatabaseConnectionService;
import com.github.idelstak.appointments.database.DisplayedView;
import java.net.URI;
import java.util.prefs.Preferences;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseButton;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

public class DatabaseSettingsPaneControllerTest extends ApplicationWithSetStageTest {

    private static final DatabaseConnectionService databaseConnectionService;
    private static final DatabaseConnectionPreferences databaseConnectionPreferences;

    static {
        databaseConnectionPreferences = new DatabaseConnectionPreferences(Preferences.userNodeForPackage(DatabaseSettingsPaneControllerTest.class));
        databaseConnectionPreferences.setUsername("abcd");
        databaseConnectionPreferences.setPassword("password".toCharArray());
        databaseConnectionPreferences.setDatabaseURL("jdbc:mysql://localhost:3306/client_schedule");

        databaseConnectionService = new DatabaseConnectionService(databaseConnectionPreferences);
    }

    public DatabaseSettingsPaneControllerTest() {
        super(createRoot());
    }

    @Test
    public void host_field_shows_host_database_connection_preference() throws Exception {
        var hostTextField = (TextInputControl) lookup("#hostTextField").query();
        var url = databaseConnectionPreferences.getDatabaseURL();
        var uri = URI.create(url.substring(5));

        assertThat(hostTextField.getText(), equalTo(uri.getHost()));
    }

    @Test
    public void database_name_field_shows_database_name_database_connection_preference() throws Exception {
        var databaseNameTextField = (TextInputControl) lookup("#databaseNameTextField").query();
        var url = databaseConnectionPreferences.getDatabaseURL();
        var uri = URI.create(url.substring(5));

        assertThat(databaseNameTextField.getText(), equalTo(uri.getPath().substring(1)));
    }

    @Test
    public void databaseUrl_field_shows_databaseUrl_database_connection_preference() throws Exception {
        var databaseURLTextField = (TextInputControl) lookup("#databaseURLTextField").query();
        var url = databaseConnectionPreferences.getDatabaseURL();

        assertThat(databaseURLTextField.getText(), equalTo(url));
    }

    @Test
    public void username_field_shows_username_database_connection_preference() throws Exception {
        var userNameTextField = (TextInputControl) lookup("#userNameTextField").query();
        var username = databaseConnectionPreferences.getUsername();

        assertThat(userNameTextField.getText(), equalTo(username));
    }

    @Test
    public void password_field_shows_password_database_connection_preference() throws Exception {
        var passwordTextField = (TextInputControl) lookup("#passwordTextField").query();
        var password = new String(databaseConnectionPreferences.getPassword());

        assertThat(passwordTextField.getText(), equalTo(password));
    }

    @Test
    public void typing_in_hostName_and_databaseName_fields_updates_databaseUrl_field() throws Exception {
        doubleClickOn("#hostTextField", MouseButton.PRIMARY).write("127.0.0.1");
        doubleClickOn("#databaseNameTextField", MouseButton.PRIMARY).write("sample");

        var hostTextField = (TextInputControl) lookup("#hostTextField").query();
        var databaseNameTextField = (TextInputControl) lookup("#databaseNameTextField").query();
        var databaseURLTextField = (TextInputControl) lookup("#databaseURLTextField").query();

        assertThat(databaseURLTextField.getText(), equalTo(String.format("jdbc:mysql://%s/%s", hostTextField.getText(), databaseNameTextField.getText())));
    }

    @Test
    public void clicking_testConnection_button_starts_databaseConnectionService() throws Exception {
        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var databaseConnectionServiceIsRunning = waitForAsyncFx(10L, databaseConnectionService::isRunning);

        assertThat(databaseConnectionServiceIsRunning, is(true));
    }

    @Test
    public void connection_test_progress_shows_when_connection_test_starts() throws Exception {
        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var connectionAttemptProgress = (Control) lookup("#connectionAttemptProgress").query();

        Assertions.assertThat(connectionAttemptProgress).isVisible();
    }
    
    @Test
    public void connection_test_progress_becomes_indeterminate_when_connection_test_starts() throws Exception {
        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var connectionAttemptProgress = (ProgressIndicator) lookup("#connectionAttemptProgress").query();

        assertThat(connectionAttemptProgress.isIndeterminate(), is(true));
    }

    @Test
    public void connection_error_label_shows_when_connection_test_fails() throws Exception {
        doubleClickOn("#hostTextField", MouseButton.PRIMARY).write("127.0.0.1");
        doubleClickOn("#databaseNameTextField", MouseButton.PRIMARY).write("client_schedule");
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordTextField", MouseButton.PRIMARY).write("bad-password");

        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var connectionErrorStatusLabel = (Labeled) sleep(3_000L).lookup("#connectionErrorStatusLabel").query();

        Assertions.assertThat(connectionErrorStatusLabel).isVisible();
    }

    @Test
    public void connection_error_label_is_hidden_when_connection_test_succeeds() throws Exception {
        doubleClickOn("#hostTextField", MouseButton.PRIMARY).write("127.0.0.1");
        doubleClickOn("#databaseNameTextField", MouseButton.PRIMARY).write("client_schedule");
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordTextField", MouseButton.PRIMARY).write("password");

        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var connectionErrorStatusLabel = (Labeled) sleep(3_000L).lookup("#connectionErrorStatusLabel").query();

        Assertions.assertThat(connectionErrorStatusLabel).matches(node -> !node.isVisible());
    }
    
    @Test
    public void save_settings_button_is_disabled_when_pane_is_first_opened() throws Exception {
        var saveConnectionSettingsButton = lookup("#saveConnectionSettingsButton").query();

        Assertions.assertThat(saveConnectionSettingsButton).isDisabled();
    }
    
    @Test
    public void save_settings_button_is_disabled_when_connection_test_fails() throws Exception {
        doubleClickOn("#hostTextField", MouseButton.PRIMARY).write("127.0.0.1");
        doubleClickOn("#databaseNameTextField", MouseButton.PRIMARY).write("client_schedule");
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordTextField", MouseButton.PRIMARY).write("bad-password");

        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var saveConnectionSettingsButton = sleep(3_000L).lookup("#saveConnectionSettingsButton").query();

        Assertions.assertThat(saveConnectionSettingsButton).isDisabled();
    }
    
    @Test
    public void save_settings_button_is_enabled_when_connection_test_succeeds() throws Exception {
        doubleClickOn("#hostTextField", MouseButton.PRIMARY).write("127.0.0.1");
        doubleClickOn("#databaseNameTextField", MouseButton.PRIMARY).write("client_schedule");
        doubleClickOn("#userNameTextField", MouseButton.PRIMARY).write("admin");
        doubleClickOn("#passwordTextField", MouseButton.PRIMARY).write("password");

        clickOn("#testConnectionButton", MouseButton.PRIMARY);

        var saveConnectionSettingsButton = sleep(3_000L).lookup("#saveConnectionSettingsButton").query();

        Assertions.assertThat(saveConnectionSettingsButton).isEnabled();
    }

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.DATABASE_SETTINGS_PANE.getFxmlPath();
        var controller = new DatabaseSettingsPaneController(
                databaseConnectionService);

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }

}
