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
package com.github.idelstak.appointments.database;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DisplayedView {

    private final ObjectProperty<DisplayedPane> displayedPaneProperty;

    public DisplayedView(DisplayedPane databaseConnectionPane) {
        this.displayedPaneProperty = new SimpleObjectProperty<>(databaseConnectionPane);
    }

    public ObjectProperty<DisplayedPane> getDisplayedPaneProperty() {
        return displayedPaneProperty;
    }

    public DisplayedPane getPane() {
        return displayedPaneProperty.getValue();
    }

    public void setPane(DisplayedPane displayedPane) {
        displayedPaneProperty.setValue(displayedPane);
    }

    public enum DisplayedPane {
        DATABASE_CONNECTION_CHECK_PANE {
            @Override
            public String getFxmlPath() {
                return "/fxml/database-check-progress-pane.fxml";
            }

            @Override
            public String getPaneId() {
                return "databaseConnectionCheckPane";
            }
        },
        DATABASE_SETTINGS_PANE {
            @Override
            public String getFxmlPath() {
                return "/fxml/database-settings-pane.fxml";
            }

            @Override
            public String getPaneId() {
                return "databaseSettingsPane";
            }
        },
        SIGN_IN_PANE {
            @Override
            public String getFxmlPath() {
                return "/fxml/sign-in-pane.fxml";
            }

            @Override
            public String getPaneId() {
                return "signInPane";
            }
        },
        APP_VIEW_PANE {
            @Override
            public String getFxmlPath() {
                return "/fxml/app-view-pane.fxml";
            }

            @Override
            public String getPaneId() {
                return "appViewPane";
            }
        };

        public abstract String getFxmlPath();

        public abstract String getPaneId();
    }
}
