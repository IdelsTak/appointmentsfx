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

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;

public class FxmlWithControllerLoader extends FXMLLoader {

    public static <T> T load(String fxmlPath) {
        return new FxmlWithControllerLoader(fxmlPath, Optional.empty()).load();
    }

    public static <T, U> T load(String fxmlPath, U controller) {
        return new FxmlWithControllerLoader(fxmlPath, Optional.of(controller)).load();
    }

    private <U> FxmlWithControllerLoader(String fxmlPath, Optional<U> optionalController) {
        super(FxmlWithControllerLoader.class.getResource(fxmlPath));

        setController(optionalController);
    }

    private <U> void setController(Optional<U> optionalController) {
        optionalController
                .stream()
                .limit(1)
                .forEach(this::setController);
    }

    @Override
    public <T> T load() {
        try {
            return super.load();
        } catch (IOException ex) {
            throw new FxmlLoadException(ex);
        }
    }

}
