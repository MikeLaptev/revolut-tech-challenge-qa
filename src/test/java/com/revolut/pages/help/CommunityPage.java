package com.revolut.pages.help;

import com.revolut.exceptions.InvalidPageContentException;
import com.revolut.pages.BasePage;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CommunityPage extends BasePage {

  @FindBy(id = "search-button")
  private WebElement searchButton;

  @FindBy(id = "search-term")
  private WebElement inputSearchTerm;

  @FindBy(css = ".search-result-topic .topic-title")
  private List<WebElement> searchTopicTitles;

  @FindBy(id = "toggle-hamburger-menu")
  private WebElement hamburgerMenuToggle;

  @FindBy(className = "panel-body")
  private WebElement hamburgerMenu;

  @FindBy(css = ".keyboard-shortcuts-link")
  private WebElement keyboardShortcutsLink;

  private static final By SEARCHING_SPINNER = By.className("spinner");

  public CommunityPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected void waitForPageToBeLoaded() throws InvalidPageContentException {
    if (!isElementDisplayed(hamburgerMenuToggle)) {
      throw new InvalidPageContentException("The Community page was not loaded correctly");
    }
  }

  public CommunityPage openSearchWindow() throws InvalidPageContentException {
    click(searchButton);
    return this;
  }

  public CommunityPage inputTextToSearch(final String textToSearch)
      throws InvalidPageContentException {
    inputTextTo(inputSearchTerm, textToSearch);
    if (!waitForSearchSpinnerToAppearAndDisappear(SEARCHING_SPINNER)) {
      throw new InvalidPageContentException("Search spinner still displayed on the page");
    }
    return this;
  }

  public List<String> getSearchTopicTitles() {
    return searchTopicTitles.stream().map(WebElement::getText).collect(Collectors.toList());
  }

  public CommunityPage openHamburgerMenu() throws InvalidPageContentException {
    click(hamburgerMenuToggle);
    return this;
  }

  public boolean isLinkOnKeyboardShortcutsPresent() {
    return isElementDisplayed(keyboardShortcutsLink);
  }
}
