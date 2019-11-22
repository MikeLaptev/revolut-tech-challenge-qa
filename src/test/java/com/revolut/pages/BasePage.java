package com.revolut.pages;

import com.revolut.base.WebBase;
import com.revolut.exceptions.InvalidPageContentException;
import java.time.Duration;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

@Log4j2
public abstract class BasePage {

  private static final Duration QUICK_TIMEOUT = Duration.ofSeconds(2L);
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30L);

  private WebBase base;

  final WebDriver driver;

  public BasePage(final WebDriver driver) {
    this.driver = driver;
  }

  public void setBase(WebBase base) {
    this.base = base;
  }

  private boolean isElementClickable(final WebElement element) {
    return checkConditionWithFluentWaitUntil(
        ExpectedConditions.and(ExpectedConditions.elementToBeClickable(element)));
  }

  protected void inputTextTo(final WebElement input, final String textToEnter)
      throws InvalidPageContentException {
    click(input);
    input.clear();
    input.sendKeys(textToEnter);
  }

  protected void click(final WebElement elementToClick) throws InvalidPageContentException {
    if (isElementClickable(elementToClick)) {
      elementToClick.click();
    } else {
      throw new InvalidPageContentException("Unable to click on the element.");
    }
  }

  protected boolean isElementDisplayed(final WebElement elementToCheck) {
    return checkConditionWithFluentWaitUntil(
        ExpectedConditions.and(ExpectedConditions.visibilityOf(elementToCheck)));
  }

  protected boolean isElementDisplayed(final By elementToCheck) {
    return checkConditionWithFluentWaitUntil(
        ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(elementToCheck)));
  }

  protected boolean isElementNotDisplayed(final By elementToCheck) {
    return checkConditionWithFluentWaitUntil(
        ExpectedConditions.and(ExpectedConditions.invisibilityOfElementLocated(elementToCheck)),
        QUICK_TIMEOUT);
  }

  private boolean checkConditionWithFluentWaitUntil(final Function<WebDriver, Boolean> condition) {
    return checkConditionWithFluentWaitUntil(condition, DEFAULT_TIMEOUT);
  }

  private boolean checkConditionWithFluentWaitUntil(final Function<WebDriver, Boolean> condition,
      final Duration duration) {
    try {
      getFluentWait(duration)
          .ignoring(NoSuchElementException.class)
          .ignoring(StaleElementReferenceException.class)
          .until(condition);
      return condition.apply(driver);
    } catch (Exception e) {
      return false;
    }
  }

  private FluentWait<WebDriver> getFluentWait() {
    return getFluentWait(DEFAULT_TIMEOUT);
  }

  private FluentWait<WebDriver> getFluentWait(final Duration duration) {
    return new FluentWait<>(driver)
        .withTimeout(duration);
  }

  /**
   * Waits for spinner to appear and disappear.
   *
   * @return true if the spinner disappeared or was not even displayed, false otherwise
   */
  protected boolean waitForSearchSpinnerToAppearAndDisappear(final By locator) {
    Duration waitForSpinnerToAppearTimeOut = Duration.ofSeconds(5L);
    Duration waitForSpinnerToDisappearTimeOut = Duration.ofSeconds(30L);

    try {
      WebElement spinner = getFluentWait(waitForSpinnerToAppearTimeOut)
          .until(ExpectedConditions.visibilityOfElementLocated(locator));
      if (spinner != null) {
        log.debug("Spinner [locator: {}] is visible...", locator);
        try {
          boolean wait = getFluentWait(waitForSpinnerToDisappearTimeOut)
              .until(ExpectedConditions.invisibilityOf(spinner));
          log.debug("Spinner [locator: {}]  is invisible - {}", locator, wait);
          return wait;
        } catch (NoSuchElementException e) {
          log.debug("No spinner [locator: {}]  found", locator);
          return true;
        } catch (TimeoutException te) {
          log.debug("Timeout waiting for page spinner [locator: {}] to finish.", locator);
          return false;
        }
      }
    } catch (NoSuchElementException | TimeoutException e) {
      log.debug("No spinner found or spinner [locator: {}] still invisible.", locator);
    }

    return true;
  }

  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  public <T extends BasePage> T getPage(final Class<T> clazz) throws InvalidPageContentException {
    T page = base.getPage(clazz);
    page.waitForPageToBeLoaded();
    return page;
  }

  protected abstract void waitForPageToBeLoaded() throws InvalidPageContentException;
}
