package ServerThreadPerClient.tokenizer;

public interface TokenizerFactory<T> {
   MessageTokenizer<T> create();
}
