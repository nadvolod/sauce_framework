package com.saucelabs.framework.elements;

import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

class Synchronizer {
    public int timeout;

    public Synchronizer() {
        this(desiredWait());
    }

    public Synchronizer(Integer i) {
        this.timeout = i;
    }

    void act(HTMLElement element, Runnable block) {
        do {
            try {
                element.locate();
                block.run();
                break;
            } catch (NoSuchElementException e) {
                waitUntilExists(element);
            } catch (ElementNotVisibleException e) {
                waitUntilVisible(element);
            }
        } while (true);
    }

    private static Integer desiredWait() {
        // TODO: this needs a better way to get set
        String propertyWait = System.getProperty("automatic.wait");
        return propertyWait == null ? 30 : Integer.parseInt(propertyWait);
    }

    private void waitUntilExists(HTMLElement element) {
        try {
            await().atMost(timeout, SECONDS).until(element::doesExist);
        } catch (ConditionTimeoutException e) {
            throw new NoSuchElementException("This element was not located: " + element.getLocator().toString());
        }
    }

    private void waitUntilVisible(HTMLElement element) {
        try {
            await().atMost(timeout, SECONDS).until(element::isVisible);
        } catch (ConditionTimeoutException e) {
            throw new ElementNotVisibleException("This element was located, but is not displayed: " + element.getLocator().toString());
        }
    }
}