public class TaskQueue<E>{
    private final SampleLinkedList<E> list;
    
    public TaskQueue(){
        list = new SampleLinkedList<>();
    }
    
    public void enqueue(E data){
        list.addLast(data);
    }
    
    public E dequeue(){
        return list.removeFirst();
    }
    
    public E getFront(){
        return list.getFirst();
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }
    
    public SampleLinkedList<E> getList(){
        return list;
    }
}