package com.revolut.pages;

import com.revolut.exceptions.InvalidPageContentException;
import com.revolut.pages.help.CommunityPage;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@Log4j2
public class HomePage extends BasePage {

  private static final String REVOLUT_COOKIE_DOMAIN = "www.revolut.com";
  private static final String REVOLUT_COOKIE_PATH = "/";
  private static final long REVOLUT_COOKIE_TIME_SHIFT_IN_SECONDS = 10L;

  private static final String SITE = System.getProperty("site");
  private static final String LANDSCAPE_MENU_ITEM_XPATH =
      "//div[contains(@class, 'styles__StyledRightPart')]";
  private static final By MOBILE_MENU_ICON_LOCATOR =
      By.xpath("//div[contains(@class, 'styles__StyledMenuIcon')]");
  private static final String MOBILE_MENU_ITEM_XPATH =
      "//div[contains(@class, 'styles__StyledMobileMenu')]";

  private static final By PAGE_HEADING_OR_TITLE_WRAP_LOCATOR =
      By.xpath("//div[contains(@class, 'styles__StyledHeadingWrap') or contains(@class, 'styles__StyledTitleWrap')]");

  @FindBy(xpath = "//div[@data-testid='footer']//div[contains(@class, 'styles__StyledSelectedCountry')]")
  private WebElement changeCountryButton;

  private static final List<String> cookieNamesToAdd = List.of(
      "applePayBannerClosed",
      "careersBannerClosed",
      "productTourBannerClosed");

  public HomePage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected void waitForPageToBeLoaded() throws InvalidPageContentException {
    if (isElementNotDisplayed(PAGE_HEADING_OR_TITLE_WRAP_LOCATOR)) {
      throw new InvalidPageContentException("The Home page was not loaded correctly");
    }
  }

  public HomePage goToHomePage() {
    driver.get(SITE);
    patchCookies();
    driver.navigate().refresh();
    return this;
  }

  private void patchCookies() {
    for (String cookieName : cookieNamesToAdd) {
      driver.manage().addCookie(
          new Cookie.Builder(
              cookieName,
              String.valueOf(
                  ZonedDateTime.now()
                      .minusSeconds(REVOLUT_COOKIE_TIME_SHIFT_IN_SECONDS)
                      .toEpochSecond()))
              .domain(REVOLUT_COOKIE_DOMAIN)
              .path(REVOLUT_COOKIE_PATH)
              .expiresOn(null)
              .build());
    }
  }

  public static String getHomePageUrl() {
    return SITE;
  }

  public CommunityPage openCommunityHelpPage() throws InvalidPageContentException {
    // Expanding 'Help' menu item
    expandMainMenuItem("Help");

    // Go to community page
    clickOnMainMenuSubItem("Community");

    // Switch to a newly open tab
    String currentHandle = driver.getWindowHandle();
    for (String handle : driver.getWindowHandles()) {
      if (!handle.equals(currentHandle)) {
        driver.switchTo().window(handle);
        break;
      }
    }

    return getPage(CommunityPage.class);
  }

  public ChangeCountryPage switchToCountrySelection() throws InvalidPageContentException {
    click(changeCountryButton);
    return getPage(ChangeCountryPage.class);
  }

  private void expandMainMenuItem(final String menuItemTitle) throws InvalidPageContentException {
    WebElement menuItem = getRightPartMenuItem(LANDSCAPE_MENU_ITEM_XPATH, menuItemTitle);
    if (isElementNotDisplayed(MOBILE_MENU_ICON_LOCATOR) && isElementDisplayed(menuItem)) {
      Actions action = new Actions(driver);
      action.moveToElement(menuItem).build().perform();
    } else if (isElementDisplayed(MOBILE_MENU_ICON_LOCATOR)) {
      click(driver.findElement(MOBILE_MENU_ICON_LOCATOR));
      menuItem = getRightPartMenuItem(MOBILE_MENU_ITEM_XPATH, menuItemTitle);
      click(menuItem);
    } else {
      throw new InvalidPageContentException("Cannot navigate over main menu");
    }
  }

  private void clickOnMainMenuSubItem(final String subMenuItemName)
      throws InvalidPageContentException {
    WebElement subMenuItem = getRightPartSubMenuItem(LANDSCAPE_MENU_ITEM_XPATH, subMenuItemName);
    if (isElementNotDisplayed(By.xpath(MOBILE_MENU_ITEM_XPATH))
        && isElementDisplayed(subMenuItem)) {
      Actions action = new Actions(driver);
      action.moveToElement(subMenuItem).click().build().perform();
    } else {
      subMenuItem = getRightPartSubMenuItem(MOBILE_MENU_ITEM_XPATH, subMenuItemName);
      click(subMenuItem);
    }
  }

  private WebElement getRightPartSubMenuItem(final String menuTemplateXpath,
      final String subItemName) {
    String template = menuTemplateXpath
        + "//div[contains(@class, 'styles__StyledDropdownContent')]//a[text() = '%s']";

    return driver.findElement(By.xpath(String.format(template, subItemName)));
  }

  private WebElement getRightPartMenuItem(final String menuTemplateXpath,
      final String itemName) {
    String template = menuTemplateXpath
        + "//div[contains(@class, 'styles__StyledDropdownTrigger')][span[text() = '%s']]";

    return driver.findElement(By.xpath(String.format(template, itemName)));
  }
}
