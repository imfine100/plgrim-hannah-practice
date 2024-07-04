package kr.co.plgrim.hannah.arraylist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyArrayList<E> implements List<E> {

	private int size;

	private static Object[] ary;
	private static final int DEFAULT_CAPACITY = 15;


	/**
	 * 생성자 (용량을 지정했을 때)
	 * @param initValue
	 */
	public MyArrayList(int initValue) {
		if (initValue > 0) {
			this.ary = new Object[initValue];

		} else if (initValue == 0) {
			this.ary = new Object[DEFAULT_CAPACITY];

		} else {
			throw new IllegalArgumentException("Illegal Capacity:" + initValue);

		}
	}


	/**
	 * 생성자 (용량을 지정하지 않았을 때)
	 */
	public MyArrayList() {
		this.ary = new Object[DEFAULT_CAPACITY];
	}


	/**
	 * 요소의 개수를 리턴한다.
	 * @return size
	 */
	@Override
	public int size() {
		return this.size;
	}


	/**
	 * 요수의 개수(size)가 0개인지 판단한다
	 *
	 * @return boolean
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0; // 0인지 아닌지 판단
	}


	/**
	 * 해당 요소가 포함 유무를 판단한다
	 *
	 * @param o element to search for
	 * @return boolean
	 */
	@Override
	public boolean contains(Object o) {
		int index = indexOf(o);

		if (index == -1) {
			return false;
		}
		return true;

	}


	/**
	 *
	 * @return
	 */
	@Override
	public Iterator<E> iterator() {
		return new MyIterator();
	}

	private class MyIterator<E> implements Iterator<E> {
		private int num = 0;

		public MyIterator() {

		}

		@Override
		public boolean hasNext() {
			return num < size;
		}

		@Override
		public E next() {
			return (E) ary[num++];
		}
	}


	/**
	 * ArrayList를 Array 로 변환한다.
	 * @return newAry
	 */
	@Override
	public Object[] toArray() {
		Object[] newAry = new Object[this.size];

		for (int i = 0; i < this.size; i++) {
			newAry[i] = get(i);

		}
		return newAry;

	}


	/**
	 * 배열의 마지막 인덱스에 요소를 추가한다.
	 * @param e element whose presence in this collection is to be ensured
	 * @return boolean
	 */
	@Override
	public boolean add(E e) {
		expandCapacity();

		this.ary[this.size] = e;

		this.size++;

		return true;

	}


	/**
	 * 배열에 인덱스를 지정하여 요소를 추가한다.
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException("Index : " + index + ", Size : " + this.size);

		}

		//index가 끝인 경우
		if (index == this.size) {
			this.ary[this.size] = element;
			this.size++;

		} else {
			expandCapacity();

			for (int i = this.size; i > index; i--) {
				this.ary[i] = this.ary[i - 1];
			}

			this.ary[index] = element;

			this.size++;

		}
	}


	/**
	 * capacity가 충분한지 비교 해서 부족한 경우 늘리기
	 */
	private void expandCapacity() {
		double initCapacity = this.ary.length;

		if (initCapacity == this.size) {
			int newCapacity = (int) Math.ceil(initCapacity * 1.7);
			Object[] newAry = new Object[newCapacity];

			for (int i = 0; i < this.size; i++) {
				newAry[i] = this.ary[i];

			}

			this.ary = newAry;

		}
	}


	/**
	 * 해당하는 첫번째 요소를 삭제한다.
	 *
	 * @param o element to search for
	 * @return boolean
	 */
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);

		if (index == -1) {
			return false;

		} else {
			this.ary[index] = null;

			for (int i = index; i < this.size - 1; i++) {
				this.ary[i] = this.ary[i + 1];
				this.ary[i + 1] = null;

			}

			this.size--;
		}

		return true;
	}


	/**
	 * 해당 인덱스의 요소를 삭제하고 삭제된 요소를 반환한다.
	 *
	 * @param index the index of the element to be removed
	 * @return initValue
	 */
	@Override
	public E remove(int index) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index : " + index + ", Size : " + this.size);
		}

		E initValue = get(index); //  삭제 데이터 저장

		this.ary[index] = null;

		for (int i = index; i < this.size -1 ; i++) {
			this.ary[i] = ary[i+1];
			this.ary[i+1] = null;
		}

		this.size--;

		return initValue;
	}


	/**
	 * 배열의 모든 요소를 비운다.
	 */
	@Override
	public void clear() {
		for (int i =0; i < this.size; i++) {
			this.ary[i] = null;

		}

		this.size = 0;
	}


	/**
	 * 해당 요소가 있는 인덱스를 찾아서 반환한다.
	 * @param o element to search for
	 * @return int
	 */
	@Override
	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < this.size; i++) {
				if (this.ary[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < this.size; i++) {
				if (o.equals(this.ary[i])) {
					return i;

				}
			}
		}

		return -1;
	}


	/**
	 * 해당 요소를 마지막부터 탐색하여 인덱스를 반환한다.
	 * @param o element to search for
	 * @return int
	 */
	@Override
	public int lastIndexOf(Object o) {
		if (o == null) {
			for (int i = this.size - 1; i >= 0; i--) {
				if (this.ary[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = this.size - 1; i >= 0; i--) {
				if (o.equals(this.ary[i])) {
					return i;
				}
			}
		}

		return -1;
	}


	/**
	 * 해당 인덱스의 요소를 가지고 온다.
	 * @param index index of the element to return
	 * @return (E) this.ary[index]
	 */
	@Override
	public E get(int index) {
		return (E) this.ary[index];
	}


	/**
	 * 해당 인덱스에 요소를 삽입한다.
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return (E) this.ary[index]
	 */
	@Override
	public E set(int index, E element) {
		E orinValue = get(index);
		this.ary[index] = element;
		return orinValue;
	}


	/**
	 * fromIndex에서부터 toIndex 앞까지의 요소를 리턴 해준다.
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex high endpoint (exclusive) of the subList
	 * @return newList
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0) {
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		}

		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		}

		List<E> newList = new MyArrayList<>();

		for (int i = fromIndex; i < toIndex; i++) {
			newList.add(get(i));

		}

		return newList;
	}


	@Override
	public <T> T[] toArray(T[] a) {
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
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("이런거 구현하지마");
	}

}
