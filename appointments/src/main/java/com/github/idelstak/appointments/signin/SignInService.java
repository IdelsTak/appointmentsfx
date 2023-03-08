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
package com.github.idelstak.appointments.signin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SignInService extends Service<Optional<Credentials>> {

    public enum SignInStatus {
        SIGNED_IN, SIGNING_IN, SIGNED_OUT, SIGNING_OUT;
    }

    private static final Logger LOG = Logger.getLogger(SignInService.class.getName());
    private final ObjectProperty<SignInStatus> signInStatusProperty;
    private final List<Credentials> allCredentials;
    private Credentials credentials;

    public SignInService(Collection<Credentials> allCredentials) {
        this.allCredentials = new ArrayList<>(allCredentials);
        signInStatusProperty = new SimpleObjectProperty<>(SignInStatus.SIGNED_OUT);
    }

    public ObjectProperty<SignInStatus> getSignInStatusProperty() {
        return signInStatusProperty;
    }

    public void check(Credentials credentials) {
        this.credentials = credentials;

        super.restart();
    }

    @Override
    protected Task<Optional<Credentials>> createTask() {
        return new Task<Optional<Credentials>>() {
            @Override
            protected Optional<Credentials> call() throws Exception {
                LOG.log(Level.INFO, "Checking sign in credentials...");

                Optional<Credentials> optionalSignedInUser = Optional.empty();

                updateProgress(-1L, 1L);

                try {
                    Thread.sleep(2_000L);
                } catch (InterruptedException ex) {
                    cancel();
                }

                if (allCredentials.contains(credentials)) {
                    signInStatusProperty.set(SignInStatus.SIGNING_IN);
                    optionalSignedInUser = Optional.of(credentials);
                    signInStatusProperty.set(SignInStatus.SIGNED_IN);
                } else {
                    signInStatusProperty.set(SignInStatus.SIGNING_OUT);
                    signInStatusProperty.set(SignInStatus.SIGNED_OUT);
                }

                updateProgress(1L, 1L);

                return optionalSignedInUser;
            }
        };
    }

}
