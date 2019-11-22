package com.revolut.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.revolut.exceptions.QeAcceptanceException;
import com.revolut.pages.HomePage;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ChangeCountryTests extends BaseUiTests {

  private static Stream<Arguments> countryToUrlPath() {
    return Stream.of(
        Arguments.of("United Kingdom", ""),
        Arguments.of("United States", "en-US"),
        Arguments.of("Australia", "en-AU"),
        Arguments.of("Canada", "en-CA")
    );
  }

  @ParameterizedTest
  @MethodSource("countryToUrlPath")
  @DisplayName("Change country option should be displayed on home page")
  void changeCountryOnHomePageTest(final String country, final String urlPath)
      throws QeAcceptanceException {
    // Arrange
    final HomePage home = base.getPage(HomePage.class);

    // Act & Assert
    assertThat("Url should be concatenation of home url and country specific path",
        home.goToHomePage()
            .switchToCountrySelection()
            .selectCountryToSwitch(country)
            .getCurrentUrl(),
        is(equalTo(HomePage.getHomePageUrl() + "/" + urlPath)));
  }
}
