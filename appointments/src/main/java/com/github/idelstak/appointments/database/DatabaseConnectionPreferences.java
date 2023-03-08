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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DatabaseConnectionPreferences {

    private static final String DATABASE_URL = "database-url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final Preferences preferences;

    public DatabaseConnectionPreferences() {
        this(Preferences.userNodeForPackage(DatabaseConnectionPreferences.class));
    }

    public DatabaseConnectionPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getDatabaseURL() {
        return preferences.get(DATABASE_URL, "");
    }

    public void setDatabaseURL(String url) {
        preferences.put(DATABASE_URL, url);
    }

    public String getUsername() {
        return preferences.get(USERNAME, "");
    }

    public void setUsername(String username) {
        preferences.put(USERNAME, username);
    }

    public char[] getPassword() {
        return preferences.get(PASSWORD, "").toCharArray();
    }

    public void setPassword(char[] password) {
        var nonNullPassword = new String(nullOrEmptyArray(password) ? new char[]{} : password);
        preferences.put(PASSWORD, nonNullPassword);
    }

    public void clear() {
        try {
            preferences.clear();
        } catch (BackingStoreException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean nullOrEmptyArray(char[] chars) {
        return chars == null || chars.length == 0;
    }

    @Override
    public String toString() {
        return String.format("Username: %s; URL: %s", getUsername(), getDatabaseURL());
    }

}
