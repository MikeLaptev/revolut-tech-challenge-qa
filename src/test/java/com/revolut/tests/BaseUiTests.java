package com.revolut.tests;

import com.revolut.base.WebBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseUiTests {

  WebBase base;

  @BeforeEach
  void setUpBase() {
    WebBase.setUpBase();
    base = new WebBase();
  }

  @AfterEach
  void tearDownBase() {
    WebBase.tearDownBase();
  }
}
