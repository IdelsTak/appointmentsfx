package com.github.idelstak.appointments;

import com.github.idelstak.appointments.ui.MainWindow;
import com.sun.javafx.application.PlatformImpl;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class SchedulingAppTest extends ApplicationTest {

    private static final Logger LOG = Logger.getLogger(SchedulingAppTest.class.getName());
    private final MainWindow mainWindow = new MainWindow();

    @BeforeClass
    public static void initPlatform() {
        PlatformImpl.startup(() -> {
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(mainWindow));
        stage.show();
    }

    @Test
    public void shouldAttemptToConnectToDatabaseWhenStarted() {
        var node = targetWindow(mainWindow).lookup("#checking-database-label").query();
        
        assertThat(node.isVisible(), is(true));
    }

}
