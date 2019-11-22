package com.revolut.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.revolut.exceptions.QeAcceptanceException;
import com.revolut.pages.HomePage;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommunityHelpTests extends BaseUiTests {

  @DisplayName("Search for a text in community help page")
  @Test
  void searchInCommunityHelpByText() throws QeAcceptanceException {
    // Arrange
    final HomePage home = base.getPage(HomePage.class);
    final String textToSearch = "We got a banking licence";

    // Act
    List<String> topics =
        home.goToHomePage()
            .openCommunityHelpPage()
            .openSearchWindow()
            .inputTextToSearch(textToSearch)
            .getSearchTopicTitles();

    // Assert
    assertThat("Topics should contains expected title",
        topics, is(hasItem(textToSearch)));
  }

  @DisplayName("Link to a page with shortcuts should be presented in hamburger menu on community help page")
  @Test
  void keyboardShortcutsInHamburgerMenuShouldBePresent() throws QeAcceptanceException {
    // Arrange
    final HomePage home = base.getPage(HomePage.class);

    // Act & Assert
    assertThat("Link on keyboard shortcuts in hamburger menu should be present",
        home.goToHomePage()
            .openCommunityHelpPage()
            .openHamburgerMenu()
            .isLinkOnKeyboardShortcutsPresent(),
        is(true));
  }
}
