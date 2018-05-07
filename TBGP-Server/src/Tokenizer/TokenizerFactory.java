package Tokenizer;

/**
 * A factory for creating Tokenizer objects.
 *
 * @param <T> the generic type
 */
public interface TokenizerFactory<T> {
   
   /**
    * Creates the tokenizer.
    *
    * @return the message tokenizer
    */
   MessageTokenizer<T> create();
}
