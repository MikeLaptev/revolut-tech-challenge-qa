package com.revolut.exceptions;

/**
 * Quality Engineering Acceptance Exception - common exception for all of the exceptions
 */
public abstract class QeAcceptanceException extends Exception {

  public QeAcceptanceException() { }

  public QeAcceptanceException(final String message) {
    super(message);
  }

  public QeAcceptanceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public QeAcceptanceException(final Throwable cause) {
    super(cause);
  }

  public QeAcceptanceException(final String message, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
