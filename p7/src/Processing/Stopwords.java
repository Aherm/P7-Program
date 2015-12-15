package Processing;

import java.util.ArrayList;
import java.util.Collection;

public class Stopwords extends ArrayList<String> {

    public Stopwords(int initialCapacity) {
        super(initialCapacity);
    }

    public Stopwords() {
    }

    public Stopwords(Collection<? extends String> c) {
        super(c);
    }
}
