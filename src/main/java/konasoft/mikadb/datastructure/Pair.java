package konasoft.mikadb.datastructure;

public class Pair<E, T> {
    private E first;
    private T second;

    public Pair(E e, T t) {
        first = e;
        second = t;
    }

    public E getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
