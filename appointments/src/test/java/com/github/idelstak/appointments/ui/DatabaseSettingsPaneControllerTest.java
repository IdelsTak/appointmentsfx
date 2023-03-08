package com.github.idelstak.appointments.ui;

import com.github.idelstak.appointments.database.DatabaseConnectionPreferences;
import com.github.idelstak.appointments.database.DatabaseConnectionService;
import com.github.idelstak.appointments.database.DisplayedView;
import java.net.URI;
import java.util.prefs.Preferences;
import javafx.scene.Parent;
import javafx.scene.control.TextInputControl;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class DatabaseSettingsPaneControllerTest extends ApplicationWithSetStageTest {

    private static final DatabaseConnectionPreferences databaseConnectionPreferences;

    static {
        databaseConnectionPreferences = new DatabaseConnectionPreferences(Preferences.userNodeForPackage(DatabaseSettingsPaneControllerTest.class));
        databaseConnectionPreferences.setUsername("abcd");
        databaseConnectionPreferences.setPassword("password".toCharArray());
        databaseConnectionPreferences.setDatabaseURL("jdbc:mysql://localhost:3306/client_schedule");
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

    private static Parent createRoot() {
        var fxmlPath = DisplayedView.DisplayedPane.DATABASE_SETTINGS_PANE.getFxmlPath();
        var controller = new DatabaseSettingsPaneController(
                new DatabaseConnectionService(databaseConnectionPreferences)
        );

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }
}
