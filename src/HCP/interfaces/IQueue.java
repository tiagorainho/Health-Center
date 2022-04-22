package HCP.interfaces;

public interface IQueue<E> {

    void add(E obj);

    E pop();
    
}
