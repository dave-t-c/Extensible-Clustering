package parser;

/**
 * Exception that is thrown if two positions cannot be compared
 * due to having different dimensional components. E.g. one may have more 
 * dimensions than the other.
 * @author David Cook
 *
 */
public class IncomparableComponentsException extends Exception {
  private static final long serialVersionUID = 1L;

  public IncomparableComponentsException() {
    super("Could not calculate distance. Components have different numbers of dimenions");
  }
}
