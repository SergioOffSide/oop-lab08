package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

/**
 * CUI implementation of {@link DrawNumberView}.
 */
public final class DrawNumberStandardOutputView implements DrawNumberView {

    /**
     * Builds a new CUI-based non-interactie view.
     */
    public DrawNumberStandardOutputView() {
        /*
         * This constructor is here to write the Javadoc and avoid Java 21+:
         * `warning: use of default constructor, which does not provide a comment`
         */
    }

    @Override
    public void result(final DrawResult res) {
       IO.println(res.getDescription());
    }

    @Override
    public void setController(final DrawNumberController observer) { }

    @Override
    public void start() { }
}
