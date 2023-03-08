package com.github.idelstak.appointments.signin;

import java.util.Arrays;

public class Credentials {

    private final String username;
    private final char[] password;

    public Credentials(String username, char[] password) {
        this.username = username;
        this.password = copyOf(password);
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return copyOf(password);
    }

    private static char[] copyOf(char[] original) {
        return Arrays.copyOf(original, original.length);
    }

}
