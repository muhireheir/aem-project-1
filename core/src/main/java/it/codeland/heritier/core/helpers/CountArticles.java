package it.codeland.heritier.core.helpers;

import java.util.Iterator;

public class CountArticles {
    int i = 0;
    public CountArticles(Iterator<String[]> iterator) {
        while (iterator.hasNext()) {
            this.i = this.i + 1;
            iterator.next();
        }
    }
    public int getSize(){
        return this.i;
    }
}
