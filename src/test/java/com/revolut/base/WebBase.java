package com.revolut.base;

import com.revolut.pages.BasePage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

public class WebBase {

  private static WebDriver driver;

  private static final int IMPLICIT_WAIT_TIMEOUT = 15;
  private static final int PAGE_LOAD_TIMEOUT = 30;
  private static final String CHROME_DRIVER_PATH =
      "/src/test/resources/drivers/mac_os/chromedriver";

  private final Map<Class<? extends BasePage>, BasePage> pageObjects = new HashMap<>();

  private <T> T instantiatePage(final WebDriver driver, Class<T> clazz) {
    try {
      try {
        Constructor<T> constructor = clazz.getConstructor(WebDriver.class);
        return constructor.newInstance(driver);
      } catch (NoSuchMethodException e) {
        // rethrowing the exception
        throw new RuntimeException(e);
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      // rethrowing the exception
      throw new RuntimeException(e);
    }
  }

  private <T extends BasePage> T initializePage(final Class<T> clazz) {
    T page = instantiatePage(driver, clazz);
    PageFactory.initElements(driver, page);
    page.setBase(this);
    return page;
  }

  public <T extends BasePage> T getPage(final Class<T> clazz) {
    return clazz.cast(pageObjects.computeIfAbsent(clazz, p -> initializePage(clazz)));
  }

  public static void setUpBase() {
    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, getChromeDriverMacPath());
    ChromeOptions options = new ChromeOptions();
    driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
    driver.manage().deleteAllCookies();
    driver.manage().window().fullscreen();
  }

  private static String getChromeDriverMacPath() {
    return System.getProperty("user.dir") + CHROME_DRIVER_PATH;
  }

  public static void tearDownBase() {
    driver.quit();
  }
}
