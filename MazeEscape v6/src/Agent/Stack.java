/*package Agent;

public class Stack {
    private class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top; // Top of the stack
    private int size; // Track size for max stack depth metric

    public Stack() {
        top = null;
        size = 0;
    }

    // Push an item onto the stack
    public void push(String item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Pop an item from the stack
    public String pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        String item = top.data;
        top = top.next;
        size--;
        return item;
    }

    // Peek at the top item without removing it
    public String peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    // Get the current size of the stack
    public int size() {
        return size;
    }

    // Convert stack to string for logging
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}*/

