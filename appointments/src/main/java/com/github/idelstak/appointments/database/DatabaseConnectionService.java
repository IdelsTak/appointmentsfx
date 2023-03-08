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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DatabaseConnectionService extends Service<Void> {

    public enum DatabaseConnectionStatus {
        SUCCESSFUL, FAILED;
    }

    private final ObjectProperty<DatabaseConnectionStatus> databaseConnectionStatusProperty;
    private final DatabaseConnectionPreferences databaseConnectionPreferences;

    public DatabaseConnectionService(DatabaseConnectionPreferences databaseConnectionPreferences) {
        databaseConnectionStatusProperty = new SimpleObjectProperty<>(FAILED);
        this.databaseConnectionPreferences = databaseConnectionPreferences;
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

    @Override
    protected Task<Void> createTask() {
        return new DatabaseConnectionTask();
    }

    private class DatabaseConnectionTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
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
