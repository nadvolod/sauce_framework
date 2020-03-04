package com.saucelabs.framework.elements;

import com.saucelabs.framework.Browser;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

@Getter
public class HTMLElement {
    private By locator;
    private Browser browser;
    private WebElement element;
    Synchronizer synchronizer;


    public HTMLElement(Browser browser, By locator) {
        this.browser = browser;
        this.locator = locator;
        this.synchronizer = new Synchronizer();
    }

    //
    // PREDICATE METHODS
    //

    // This always checks instead of relying on cache
    public boolean doesExist() {
        reset();
        try {
            locate();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public Boolean isVisible() {
        return doesExist() && element.isDisplayed();
    }

    //
    // ACTION METHODS
    //

    public void click() {
        synchronizer.act(this, () -> getElement().click());
    }

    //
    // INFORMATION METHODS
    //

    public String value() {
        return this.element.getAttribute("value");
    }

    //
    // RESTRICTED METHODS
    //

    void locate() {
        if (this.element == null) {
            this.element = browser.getDriver().findElement(locator);
        }
    }

    void reset() {
        this.element = null;
    }
}
