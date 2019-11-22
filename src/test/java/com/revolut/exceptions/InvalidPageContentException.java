package com.revolut.exceptions;

/**
 * Internal exception to make messages more meaningful when one or another element is not found by
 * xpath, id or another kind of locator.
 */
public class InvalidPageContentException extends QeAcceptanceException {

  public InvalidPageContentException(final String message) {
    super(message);
  }

  public InvalidPageContentException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
