package kr.co.plgrim.hannah.linkedlist;

import java.util.*;

/**
 * MyLinkedList
 *
 * <pre>
 *  1. 다음을 구현하라.
 *      패키지위치는 kr.co.plgrim.linkedlist.[닉네임] 밑으로 카피해서 작성한다.
 *      ex) kr.co.plgrim.linkedlist.hannah
 *  2. [throw new UnsupportedOperationException()] 의 메소드는 구현할 필요가 없다.
 *
 * </pre>
 *
 * &#064;date  2024.06.24
 * @author Smuft
 * @param <E>
 *
 */
public class MyLinkedList<E> implements List<E> {

	private Node<E> head;   // 특정 노드가 아닌 가리키는 pointer 개념이라고 생각하는게 나을 듯 함.

	private Node<E> tail;

	private int size = 0;

	private static class Node<E> {
		E item;
		Node<E> next;

		Node(E element, Node<E> next) {
			this.item = element;
			this.next = next;
		}

		Node(E element) {
			this.item = element;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			Node<E> x = this;
			while (x != null) {
				sb.append(x.item);
				sb.append(" ");
				x = x.next;
			}
			return sb.toString();
		}

	}

	/**
	 * 요소의 실제 갯수를 반환한다.
	 * @return size
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * 요소 존재 여부를 반환한다.
	 * @return boolean
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * 해당 요소가 포함되어 있는지 판단한다.
	 *
	 * @param o element whose presence in this list is to be tested
	 * @return
	 */
	@Override
	public boolean contains(Object o) {
		Node<E> current = this.head;

		if (o == null) {
			for (; current != null; current = current.next) {
				if (o == current.item) {
					return true;
				}
			}

		} else {
			for (; current != null; current = current.next) {
				if (o.equals(current.item)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 요소들에 대한 iterator 내용을 반환한다.
	 * @return MyIterator
	 */
	@Override
	public Iterator<E> iterator() {
		return new MyIterator();
	}

	private class MyIterator implements Iterator<E> {

		private Node<E> currentNode;


		public MyIterator() {
			this.currentNode = head;
		}

		@Override
		public boolean hasNext() {
			return currentNode != null;
		}

		@Override
		public E next() {
			E data = currentNode.item;
			currentNode = currentNode.next;
			return data;
		}
	}

	/**
	 * LinkedList의 data를 배열로 변환하여 반환한다.
	 *
	 * @return array
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size];
		Node<E> current = this.head;

		int i = 0;
		for (; current != null; current = current.next, i++) {
			array[i] = current.item;
		}
		return array;
	}


	/**
	 * 해당 요소를 제거한다.
	 * @param o element to be removed from this list, if present
	 * @return boolean
	 */
	@Override
	public boolean remove(Object o) {
		Node<E> current = this.head;
		Node<E> prevNode = null;
		E oldValue = null;

		if (o == null) {
			for ( ; current != null ; current = current.next) {
				if (o == current.item) {
					unlink(prevNode, current);
					return true;
				}
				prevNode = current;
			}
		} else {
			for ( ; current != null ; current = current.next) {
				if (o.equals(current.item)) {
					unlink(prevNode, current);
					return true;
				}
				prevNode = current;
			}
		}

		return false;
	}

	/**
	 * index에 해당하는 요소를 제거하고, 해당 요소를 반환한다.
	 * @param index the index of the element to be removed
	 * @return oldValue
	 */
	@Override
	public E remove(int index) {
		if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }

		Node<E> prevNode = null;
		Node<E> current = this.head;
		E oldValue = null;

		for (int i = 0 ; i < this.size; i++) {
			if (i == index) {
				oldValue = unlink(prevNode, current);
				break;
			}
			// 노드를 한칸씩 옮기기 위해서 필요함! (이전 <- 현재 / 현재 <-다음)
			prevNode = current;
			current = current.next;
		}


		return oldValue;
	}

	private E unlink(Node<E> prevNode, Node<E> current) {
		E oldValue = null;

		if (prevNode == null) {             // 첫번째 노드일 때 (current가 head일 때, 단 값은 하나가 아님)
			oldValue = current.item;        // 현재 노드 item 저장
			prevNode.next = current.next;   // 헤드(prevNode의 next)를 두번째 노드(current의 next)로 업데이트
		} else {                            // 중간에 삽입 할때
			oldValue = current.item;        // 현재 노드 item 저장
			this.head = current.next;       // 헤드를 현재 노드의 다음으로 업데이트
			if (this.head == null) {        // 노드가 하나라서 삭제 시 헤드가 null이 되면
				this.tail = null;
			}
		}
		this.size--;                        // 사이즈 감소
		return oldValue;
	}

	/**
	 * 요소를 모두 제거한다.
	 */
	@Override
	public void clear() {
		this.head = null;
		this.tail = null;
		this.size = 0;

	}

	/**
	 * index에 해당하는 요소를 찾아온다.
	 * @param index index of the element to return
	 * @return ptr.item
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
		}

		Node<E> ptr = this.head;

		int i = 0;
		for ( ; ptr != null; ptr = ptr.next, i++) {
			if (i == index) {
				return ptr.item;
			}
		}

		return null;
	}

	/**
	 * 해당 index에 요소를 삽입하고 기존 값을 반환한다.
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return originValue
	 */
	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
		}

		Node<E> ptr = this.head;

		int i =0;
		for (; ptr != null; ptr = ptr.next, i++){
			if (i == index) {
				E originValue = ptr.item;
				ptr.item = element;
				return originValue;
			}
		}
		return null;
	}

	/**
	 * 해당 index에 새로운 요소를 삽입한다.
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
		}

		Node<E> newNode = new Node<>(element);

		if (index == 0) {
			newNode.next = this.head;
			this.head = newNode;

			if (this.size == 0) {
				this.tail = newNode;
			}
		} else {
			Node<E> prevNode  = this.head;

			for (int i = 0; i < index-1; i++) {
				prevNode  = prevNode.next;
			}
			newNode.next = prevNode.next;
			prevNode.next = newNode;

			if (newNode.next == null) {
				this.tail = newNode;
			}
		}
		this.size++;
	}

	/**
	 * 마지막 위치(tail) 뒤에 새로운 요소를 추가한다.
	 * @param e element whose presence in this collection is to be ensured
	 * @return boolean
	 */
	@Override
	public boolean add(E e) {
		Node<E> newNode = new Node<>(e);

		if (this.head == null) {
			this.head = newNode;
			this.tail = newNode;

		} else {
			this.tail.next = newNode;
			this.tail = newNode;
		}
		this.size++;

		return true;
	}

	/**
	 * 해당 요소가 있는 index를 반환한다.
	 *
	 * @param o element to search for
	 * @return index
	 */
	@Override
	public int indexOf(Object o) {
		Node<E> ptr = this.head;
		int index = 0;

		if (o == null) {
			for (; ptr != null; ptr = ptr.next) {
				if (o == ptr.item) {
					return index;
				}
			}
		} else {
			for (; ptr != null; ptr = ptr.next) {
				if (o.equals(ptr.item)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	/**
	 * 해당 요소를 마지막부터 탐색해서 index를 반환한다.
	 *
	 * @param o element to search for
	 * @return
	 */
	@Override
	public int lastIndexOf(Object o) {
		Node<E> ptr = this.head;
		int index = -1;
		int currentIndex = 0;

		if (o == null) {
			for (; ptr != null; ptr = ptr.next) {
				if (o == ptr.item) {
					index = currentIndex;
				}
				currentIndex++;
			}
			return index;
		} else {
			for (; ptr != null; ptr = ptr.next) {
				if (o.equals(ptr.item)) {
					index = currentIndex;
				}
				currentIndex++;
			}
		}
		return index;
	}

	/**
	 * fromIndex부터 toIndex 앞까지 반환한다.
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex high endpoint (exclusive) of the subList
	 * @return
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}

		List<E> newList = new MyLinkedList<>();
		Node<E> current = this.head;

		for (int i = fromIndex; i < toIndex; i++ ) {
			newList.add(current.item);
			current = current.next;
		}

		return newList;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MyLinkedList [");

		Node<E> ptr = this.head;

		while (ptr != null) {
			sb.append(ptr.item);
			ptr = ptr.next;

			if (ptr != null) {
				sb.append(" -> ");
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public static void main(String[] args) {
		MyLinkedList<String> mll = new MyLinkedList<>();
		//mll.test();
		mll.add("A");
		mll.add("B");
		mll.add("C");
		mll.add("D");
		mll.add("E");
		mll.add("Z");
		mll.add(null);
		System.out.println("mll.remove(0) = " + mll.remove(0));
		System.out.println("mll.remove(\" B\") = " + mll.remove(null));
		System.out.println("mll.toString() = " + mll.toString());
		mll.clear();
		System.out.println("mll.size() = " + mll.size());
		System.out.println("mll.toString() = " + mll.toString());

		LinkedList list = new LinkedList<>();



	}

	/**
	 * Sample
	 */
	@SuppressWarnings("unchecked")
	private void test() {
		Node<String> node = new Node<>("10");
		this.head = (Node<E>) node;
		this.tail = (Node<E>) node;

		print();

		node = new Node<>("11");
		this.head.next = (Node<E>) node;
		this.tail = (Node<E>) node;

		print();

		node = new Node<>("12");
		this.head.next.next = (Node<E>) node;
		this.tail = (Node<E>) node;

		print();

		this.head.next = this.head.next.next;
		print();
	}

	private void print() {
		Node<E> ptr = this.head;
		for ( ; ptr != null; ptr = ptr.next) {
			System.out.print(ptr.item + " -> ");
		}

		System.out.println();
	}
}
