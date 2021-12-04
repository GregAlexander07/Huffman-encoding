package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author Greg A. Viera Pérez
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}
	} // End of Node class


	private Node<E> head; // First DATA node (This is NOT a dummy header node)

	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		Node<E> newNode = new Node<>(e);

		if (this.isEmpty()) {
			this.head = newNode;
		}
		/* Special case: Be careful when the new value is the smallest */


		else if (e.compareTo(this.head.getValue()) < 0) {
			newNode.setNext(this.head);
			this.head = newNode;
		} else {
			Node<E> curNode = head;
			boolean in = false;

			while (curNode.getNext() != null) {
				if (e.compareTo(curNode.getNext().getValue()) < 0) {
					//insert between current and next
					newNode.setNext(curNode.getNext());
					curNode.setNext(newNode);
					curNode = newNode;
					in = true;
					break;

				} else
					curNode = curNode.getNext();

			}
			if (!in)
				curNode.setNext(newNode);

		}
		this.currentSize++;
	}
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		if(isEmpty())
			return false;

		return removeIndex(firstIndex(e)) != null;
	}

	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		if (index < 0 || index > size()) throw new IndexOutOfBoundsException("add: invalid index =" + index);


		/* Special case: Be careful when index = 0 */
		if(index == 0) {
			Node<E> ntr = this.head;
			E result = this.head.getValue();
			this.head = this.head.getNext();
			ntr.clear();
			this.currentSize--;
			return result;
		}


		//traverse towards the index we want removed
		Node<E> curr = this.head;
		Node<E> nextNode = this.head.getNext();
		for (int i = 0; i < index - 1; i++) {
			curr = curr.getNext();
			nextNode = nextNode.getNext();
		}

		//eliminate node
		E result = nextNode.getValue();
		curr.setNext(nextNode.getNext());
		nextNode.clear();
		this.currentSize--;

		return result;
	}

	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */

		int count = 0;
		for (Node<E> temp = this.head; temp != null; temp = temp.getNext(), ++count) {
			if (temp.getValue().equals(e)) {
				return count;
			}
		}

		return -1;

	}

	@Override
	public E get(int index) {
		/* TODO ADD CODE HERE */
		if (index < 0 || index > size()) throw new IndexOutOfBoundsException("add: invalid index =" + index);

		Node<E> curr = head;
		for (int i = 0; i < index; i++) {
			curr = curr.getNext();
		}

		return curr.getValue();
	}



	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}
