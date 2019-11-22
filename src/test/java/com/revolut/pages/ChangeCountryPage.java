package com.revolut.pages;

import com.revolut.exceptions.InvalidPageContentException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChangeCountryPage extends BasePage {

  @FindBy(xpath = "//div[contains(@class, 'styles__StyledCountriesRegionBlock')]")
  private List<WebElement> regionBlocks;

  public ChangeCountryPage(WebDriver driver) {
    super(driver);
  }

  @Override
  protected void waitForPageToBeLoaded() throws InvalidPageContentException {
    for (WebElement regionBlock: regionBlocks) {
      if (!isElementDisplayed(regionBlock)) {
        throw new InvalidPageContentException("The Change Country page was not loaded correctly");
      }
    }
  }

  public HomePage selectCountryToSwitch(String country) throws InvalidPageContentException {
    String countryToSelectXPathTemplate =
        "//div[contains(@class, 'styles__StyledCountryText') and text() = '%s']";

    try {
      WebElement selectedCountry =
          driver.findElement(By.xpath(String.format(countryToSelectXPathTemplate, country)));
      click(selectedCountry);
    } catch (NoSuchElementException e) {
      throw new InvalidPageContentException(
          String.format("Cannot identify country %s in the list of available ones", country), e);
    }

    return getPage(HomePage.class);
  }
}
