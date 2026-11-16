package it.unibo.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.controller.DrawNumberControllerImpl;
import it.unibo.mvc.model.DrawNumberImpl;

/**
 * Application entry-point.
 */
public final class LaunchApp {

    private static final int CLASSES_TO_LOAD = 3;
    private static final String VIEW_CLASS_NAME = "it.unibo.mvc.view.DrawNumber";
    private static final String VIEW = "View";

    private LaunchApp() { }

    /**
     * Runs the application.
     *
     * @throws ClassNotFoundException if the fetches class does not exist
     * @throws NoSuchMethodException if the 0-ary constructor do not exist
     * @throws InvocationTargetException if the constructor throws exceptions
     * @throws InstantiationException if the constructor throws exceptions
     * @throws IllegalAccessException in case of reflection issues
     * @throws IllegalArgumentException in case of reflection issues
     */
    public static void main()
        throws
        ClassNotFoundException,
        NoSuchMethodException,
        InvocationTargetException,
        InstantiationException,
        IllegalAccessException {
        final var model = new DrawNumberImpl();
        final DrawNumberController app = new DrawNumberControllerImpl(model);
        /*app.addView(new DrawNumberSwingView());
        app.addView(new DrawNumberStandardOutputView());*/
        for (final String shortViewName : List.of("StandardOutput", "Swing")) {
            final String actViewName = VIEW_CLASS_NAME + shortViewName + VIEW;
            for (int i = 0; i < CLASSES_TO_LOAD; i++) {
                final Class<?> viewClass = Class.forName(actViewName);
                final Object viewIstance = viewClass.getConstructor().newInstance();
                if (DrawNumberView.class.isAssignableFrom(viewClass)) {
                    app.addView((DrawNumberView) viewIstance);
                } else {
                    throw new IllegalAccessException(
                        "Is not possible to create a DrawNumberView istance from"
                        + viewClass.toString()
                    );
                }
            }
        }
    }
}
