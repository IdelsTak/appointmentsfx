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
import com.github.idelstak.appointments.database.DatabaseConnectionService.DatabaseConnectionStatus;
import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignInService;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class MainStackPaneControllerTest extends ApplicationWithSetStageTest {

    private static final DatabaseConnectionService DATABASE_CONNECTION_SERVICE;
    private static final SignInService SIGN_IN_SERVICE;
    private static final DisplayedView DISPLAYED_VIEW;

    static {
        DATABASE_CONNECTION_SERVICE = new DatabaseConnectionService(new DatabaseConnectionPreferences());
        SIGN_IN_SERVICE = new SignInService(List.of(new Credentials("admin", "admin".toCharArray())));
        DISPLAYED_VIEW = new DisplayedView(DisplayedPane.DATABASE_CONNECTION_CHECK_PANE);
    }

    public MainStackPaneControllerTest() {
        super(createRoot());
    }

    @Test
    public void shows_database_connection_attempt_progress_dialog_when_application_starts() throws Exception {
        var mainStackPane = (Pane) lookup("#mainStackPane").query();
        var databaseConnectionPaneIsVisible = mainStackPane
                .getChildrenUnmodifiable()
                .stream()
                .filter(Node::isVisible)
                .anyMatch(node -> Objects.equals(node.getId(), DisplayedPane.DATABASE_CONNECTION_CHECK_PANE.getPaneId()));

        assertThat(databaseConnectionPaneIsVisible, is(true));
    }

    @Test
    public void shows_signInPane_when_database_connection_is_successful() throws Exception {
        var databaseConnectionStatusProperty = DATABASE_CONNECTION_SERVICE.getDatabaseConnectionStatusProperty();

        databaseConnectionStatusProperty.set(DatabaseConnectionStatus.SUCCESSFUL);

        assertThat(DISPLAYED_VIEW.getPane(), equalTo(DisplayedPane.SIGN_IN_PANE));
    }

    private static Parent createRoot() {
        var fxmlPath = "/fxml/main-stack-pane.fxml";
        var controller = new MainStackPaneController(
                DATABASE_CONNECTION_SERVICE,
                SIGN_IN_SERVICE,
                DISPLAYED_VIEW
        );

        return (Parent) FxmlWithControllerLoader.load(fxmlPath, controller);
    }

}
