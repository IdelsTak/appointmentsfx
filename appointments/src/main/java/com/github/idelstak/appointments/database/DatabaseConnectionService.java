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

import static com.github.idelstak.appointments.database.DatabaseConnectionService.DatabaseConnectionStatus.FAILED;
import static com.github.idelstak.appointments.database.DatabaseConnectionService.DatabaseConnectionStatus.SUCCESSFUL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DatabaseConnectionService extends Service<Void> {

    private static final Logger LOG = Logger.getLogger(DatabaseConnectionService.class.getName());

    private Task<Void> createDatabaseConnectionTask() {
        var databaseConnectionTask = new DatabaseConnectionTask();

        databaseConnectionTask
                .messageProperty()
                .addListener((observable, oldMessage, newMessage) -> {
                    if (newMessage == null || newMessage.isBlank()) {
                        databaseConnectionStatusProperty.setValue(SUCCESSFUL);
                    } else {
                        databaseConnectionStatusProperty.setValue(FAILED);
                    }
                });

        return databaseConnectionTask;
    }

    public enum DatabaseConnectionStatus {
        SUCCESSFUL, FAILED;
    }

    private final ObjectProperty<DatabaseConnectionStatus> databaseConnectionStatusProperty;
    private final DatabaseConnectionPreferences databaseConnectionPreferences;

    public DatabaseConnectionService(DatabaseConnectionPreferences databaseConnectionPreferences) {
        this.databaseConnectionPreferences = databaseConnectionPreferences;
        databaseConnectionStatusProperty = new SimpleObjectProperty<>(FAILED);
    }

    public ObjectProperty<DatabaseConnectionStatus> getDatabaseConnectionStatusProperty() {
        return databaseConnectionStatusProperty;
    }

    public String getDatabaseURL() {
        return databaseConnectionPreferences.getDatabaseURL();
    }

    public String getUsername() {
        return databaseConnectionPreferences.getUsername();
    }

    public char[] getPassword() {
        return databaseConnectionPreferences.getPassword();
    }

    public void setDatabaseURL(String url) {
        databaseConnectionPreferences.setDatabaseURL(url);
    }

    public void setUsername(String username) {
        databaseConnectionPreferences.setUsername(username);
    }

    public void setPassword(char[] password) {
        databaseConnectionPreferences.setPassword(password);
    }

    @Override
    protected Task<Void> createTask() {
        return createDatabaseConnectionTask();
    }

    private class DatabaseConnectionTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            LOG.log(Level.INFO, "Starting database connection task...");

            var databaseConnection = new DatabaseConnection(databaseConnectionPreferences);

            updateTitle(String.format("Connecting to database %s...", databaseConnectionPreferences.getDatabaseURL()));
            updateProgress(-1L, 1L);

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException ex) {
                cancel();
            }

            try ( var connection = databaseConnection.establishConnection()) {
                updateTitle("Connected to database.");
                updateMessage(null);
            } catch (DatabaseConnectionException ex) {
                updateTitle("Connection to database failed.");
                updateMessage(ex.getMessage());
            } finally {
                updateProgress(1L, 1L);
            }

            return null;
        }

    }

}
