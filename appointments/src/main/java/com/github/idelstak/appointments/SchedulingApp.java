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
package com.github.idelstak.appointments;

import com.github.idelstak.appointments.database.DatabaseConnectionPreferences;
import com.github.idelstak.appointments.database.DatabaseConnectionService;
import com.github.idelstak.appointments.database.DisplayedView;
import com.github.idelstak.appointments.database.DisplayedView.DisplayedPane;
import com.github.idelstak.appointments.signin.Credentials;
import com.github.idelstak.appointments.signin.SignInService;
import com.github.idelstak.appointments.ui.FxmlWithControllerLoader;
import com.github.idelstak.appointments.ui.MainStackPaneController;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SchedulingApp extends Application {

    private static final Logger LOG = Logger.getLogger(SchedulingApp.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var mainStackPane = (Parent) FxmlWithControllerLoader.load("/fxml/main-stack-pane.fxml",
                new MainStackPaneController(
                        new DatabaseConnectionService(new DatabaseConnectionPreferences()),
                        new SignInService(List.of(new Credentials("admin", "admin".toCharArray()))), 
                        new DisplayedView(DisplayedPane.DATABASE_CONNECTION_CHECK_PANE))
        );
        var scene = new Scene(mainStackPane);

        stage.setScene(scene);
        stage.setTitle("AppointmentsFX");
        stage.show();
    }

}
