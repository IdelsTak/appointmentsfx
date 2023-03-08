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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {

    private final DatabaseConnectionPreferences databaseConnectionPreferences;
    private Connection connection;
    
    public DatabaseConnection(DatabaseConnectionPreferences databaseConnectionPreferences) {
        this.databaseConnectionPreferences = databaseConnectionPreferences;
    }

    public Connection establishConnection() throws DatabaseConnectionException {
        if (connection == null) {
            var url = databaseConnectionPreferences.getDatabaseURL();
            var username = databaseConnectionPreferences.getUsername();
            var password = databaseConnectionPreferences.getPassword();

            try {
                connection = DriverManager.getConnection(url, username, new String(password));
            } catch (SQLException ex) {
                throw new DatabaseConnectionException(ex);
            }
        }
        return connection;
    }

    

    @Override
    public void close() throws DatabaseConnectionException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseConnectionException(ex);
        }
    }

}
