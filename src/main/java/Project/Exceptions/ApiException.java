package Project.Exceptions;

/**
 * Purpose of this class is to make an Exception with a HTTP status code
 * Author: Thomas Hartmann
 */
public class ApiException extends RuntimeException {
        private int statusCode;
        public ApiException(int statusCode, String message) {
            super(message);
            this.statusCode = statusCode;
        }
    }
