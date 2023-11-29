package edu.brown.cs.student.main.server.Exceptions;

public class ShuttleDataException extends Exception {
  public ShuttleDataException(String message) {
    super(message);
  }

  public ShuttleDataException(String message, Throwable cause) {
    super(message);
  }
}
