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
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import java.util.prefs.Preferences;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

public class DatabaseCheckProgressPaneControllerTest extends ApplicationWithSetStageTest {

    private static final DatabaseConnectionPreferences databaseConnectionPreferences;
    private static final DisplayedView displayedView;

    static {
        databaseConnectionPreferences = new DatabaseConnectionPreferences(Preferences.userNodeForPackage(DatabaseCheckProgressPaneControllerTest.class));
        databaseConnectionPreferences.setUsername("abcd");
        databaseConnectionPreferences.setPassword("password".toCharArray());
        databaseConnectionPreferences.setDatabaseURL("jdbc:mysql://localhost:3306/client_schedule");

        displayedView = new DisplayedView(DisplayedPane.DATABASE_CONNECTION_CHECK_PANE);
    }

    public DatabaseCheckProgressPaneControllerTest() {
        super(createRoot());
    }

    @Test
    public void shows_error_message_when_database_connection_fails_on_start() throws Exception {
        var databaseConnectionErrorLabel = (Labeled) sleep(3_000L).lookup("#databaseConnectionErrorLabel").query();

        assertThat(databaseConnectionErrorLabel.getText(), notNullValue());
    }

    @Test
    public void shows_database_settings_button_when_connection_fails_on_start() throws Exception {
        var databaseSettingsButton = sleep(3_000L).lookup("#databaseSettingsButton").query();

        assertThat(databaseSettingsButton.isVisible(), is(true));
    }

    @Test
    public void clicking_database_settings_button_opens_the_database_settings_pane() throws Exception {
        sleep(3_000L).clickOn("#databaseSettingsButton", MouseButton.PRIMARY);

        assertThat(displayedView.getDisplayedPaneProperty().get(), equalTo(DisplayedPane.DATABASE_SETTINGS_PANE));
    }

    private static Parent createRoot() {
        var fxmlPath = DisplayedPane.DATABASE_CONNECTION_CHECK_PANE.getFxmlPath();
        var controller = new DatabaseCheckProgressPaneController(
                new DatabaseConnectionService(databaseConnectionPreferences),
                displayedView
        );

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }

}
