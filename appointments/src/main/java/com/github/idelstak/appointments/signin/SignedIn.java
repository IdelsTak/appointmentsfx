package com.github.idelstak.appointments.signin;

import java.util.Optional;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SignedIn {

    private final ObjectProperty<Optional<Credentials>> credentialsProperty;

    public SignedIn() {
        credentialsProperty = new SimpleObjectProperty<>(Optional.empty());
    }

    public ObjectProperty<Optional<Credentials>> getCredentialsProperty() {
        return credentialsProperty;
    }

}
