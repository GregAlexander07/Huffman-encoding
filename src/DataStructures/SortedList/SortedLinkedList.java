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

	/**
	 * The add methods inserts elements to the list in a ascending order
	 * @param e elements to add to the list
	 */
	@Override
	public void add(E e) {
		Node<E> newNode = new Node<>(e);

		if (this.isEmpty()) {
			this.head = newNode;
		}

		/* Special case: Be careful when the new value is the smallest */
		else if (e.compareTo(this.head.getValue()) < 0) {
			newNode.setNext(this.head);
			this.head = newNode;

			//if the element does not enter any of the previous ifs
			//traverse the list to find where to insert the node
		} else {
			Node<E> curNode = head;
			//determines if the element has been added or not
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
			//if the whole list was traversed and the elements is not in
			//then the new node is added at the end of the list
			if (!in)
				curNode.setNext(newNode);

		}
		this.currentSize++;
	}


	/**
	 * Removes a specific element in the list
	 * @param e elements to be removed
	 * @return true if the element was removed or false if it was not
	 */
	@Override
	public boolean remove(E e) {
		/* Special case: Be careful when the value is found at the head node */
		if(isEmpty())
			return false;
		//first index tells the first occurrence of element to remove
		//if its null, element was not in the list

		return removeIndex(firstIndex(e)) != null;
	}

	/**
	 * Given an index, removed the element at said index
	 * @param index of the element to remove
	 * @return value of element that removed
	 */
	@Override
	public E removeIndex(int index) {
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

	/**
	 * Provides the index of an element in the list
	 * @param e is the target element
	 * @return index or place in the list of the target element
	 */
	@Override
	public int firstIndex(E e) {
		int index = 0;
		//move until reach index
		for (Node<E> temp = this.head; temp != null; temp = temp.getNext(), ++index) {
			if (temp.getValue().equals(e)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Provides an element in the list at a specific position
	 * @param index  to be traversed to
	 * @return value of the element at index
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index > size()) throw new IndexOutOfBoundsException("add: invalid index =" + index);

		Node<E> curr = head;
		for (int i = 0; i < index; i++) {
			curr = curr.getNext();
		}
		return curr.getValue();
	}


	/**
	 * Returns an Array version of the LinkedList contents
	 * @return theArray version of the class LinkedList
	 */
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
