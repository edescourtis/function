package com.benbria.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A small functional library defining generic interface Function<I,O> and
 * usual functional stuff...
 *
 * @author wfraczak
 * @author eric
 */

public class Fun {
	/** If you want to pass a one argument function use an object with this
	 * interface which requires the existence of method "f: X -> Y" representing
	 * the function. Generics X and Y correspond to types of x and y, as in "y = f(x)"
	 *
	 * @param <X> the parameter type of "f"
	 * @param <Y> the return type of "f"
	 */
	public static interface Function<X,Y> {
		public Y f(X x);
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param f - function from type X to type Y
	 * @return List of elements of type Y, [f(a1), f(a2), f(a3) ...]
	 */
	public static <X,Y> List<Y> map(Collection<? extends X> collection, Function<X, Y> f){
		List<Y> result = new ArrayList<Y>();
		for(X e : collection)
			result.add(f.f(e));
		return result;
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param collection of elements of type Y, [b1,b2,b3,...]
	 * @param f - function from type T2<X,Y> to type Z
	 * @return List of elements of type Z, [f(a1,b1), f(a2,b2), f(a3,b3) ...]
	 */
	public static <X,Y,Z> List<Z> zipwith(Collection<? extends X> collection1, Collection<? extends Y> collection2, Function<T2<X, Y>, Z> f){
		List<Z> result = new ArrayList<Z>();

		Iterator<? extends X> i1 = collection1.iterator();
		Iterator<? extends Y> i2 = collection2.iterator();

		while(i1.hasNext() && i2.hasNext()){
			result.add(f.f(new T2<X,Y>(i1.next(), i2.next())));
		}
		return result;
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param f - function from type X to type Boolean
	 * @return List of elements of type Boolean, [f(a1), f(a2), f(a3) ...]
	 */
	public static <X> List<X> filter(Collection<? extends X> collection, Function<X, Boolean> f){
		List<X> result = new ArrayList<X>();
		for(X e : collection){
			if(f.f(e).booleanValue()) { result.add(e); }
		}
		return result;
	}

	public static List<Integer> seq(int start, int end) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i = start; i <= end; i++){
			result.add(i);
		}
		return result;
	}

	public static List<Integer> seq(int start, int end, final int step) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i = start; i <= end; i += step){
			result.add(i);
		}
		return result;
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param f - function from type X to type Boolean
	 * @return List of elements of type X, [f(a1), f(a2), f(a3) ...]
	 */
	public static <X> List<X> takewhile(Collection<? extends X> collection, Function<X, Boolean> f){
		List<X> result = new ArrayList<X>();
		for(X e : collection){
			if(f.f(e).booleanValue()) { result.add(e); }
			else { return result; }
		}
		return result;
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param f - function from type X to type Boolean
	 * @return Boolean if all f(aX) was true
	 */
	public static <X> boolean all(Collection<? extends X> collection, Function<X, Boolean> f){
		for(X e : collection){
			if(f.f(e).booleanValue() == false){
				return false; 
			}
		}
		return true;
	}

	/**
	 * @param collection of elements of type X, [a1,a2,a3,...]
	 * @param f - function from type X to type Boolean
	 * @return Boolean if f(aX) was true
	 */
	public static <X> boolean any(Collection<? extends X> collection, Function<X, Boolean> f){
		for(X e : collection){
			if(f.f(e).booleanValue() == true){
				return true; 
			}
		}
		return false;
	}

	/**
	 * @param accumulator initial accumulator
	 * @param collection of elements of type X
	 * @param f - function from type T2<X, A> to type A
	 * @return Elements of type A
	 */
	public static <X, Y, A> A foldl(A accumulator, Collection<? extends X> collection, Function<T2<X, A>, A> f){
		for(X x : collection){ accumulator = f.f(new T2<X, A>(x, accumulator)); }
		return accumulator;
	}

	/**
	 * @param accumulator initial accumulator
	 * @param collection of elements of type X
	 * @param f - function from type T2<X, A> to type A
	 * @return Elements of type A
	 */
	public static <X, Y, A> A foldr(A accumulator, Collection<? extends X> collection, Function<T2<X, A>, A> f){
		ArrayList<X> list = new ArrayList<X>(collection);
		Collections.reverse(list);
		return foldl(accumulator, list, f);
	}


	/**
	 * Functions which return String (notice that the method is still called "f")
	 * @param <X> type of the argument object
	 */
	public static interface StringConverter<X> extends Function<X,String>{}

	/**
	 * Function which transforms any object into its String representation using .toString()
	 */
	public static final Function<Object,String> OBJECT_TO_STRING = new StringConverter<Object>() {
		@Override
		public String f(Object element) {
			return element.toString();
		}
	};

	public static class T0 {
		public T0() { }

		@Override
		public String toString() {
			return "()";
		}

	}

	public static class T1<A> extends T0 {
		private final A a;

		public T1(A a) {
			super();
			this.a = a;
		}

		public A getA() {
			return a;
		}

		@Override
		public String toString() {
			return "(" + getA() + ")";
		}
	}

	public static class T2<A, B> extends T1<A> {
		private final B b;

		public T2(A a, B b) {
			super(a);
			this.b = b;
		}

		public B getB() {
			return b;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ")";
		}
	}

	public static class T3<A, B, C> extends T2<A, B> {
		private final C c;

		public T3(A a, B b, C c) {
			super(a, b);
			this.c = c;
		}

		public C getC() {
			return c;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ")";
		}
	}

	public static class T4<A, B, C, D> extends T3<A, B, C> {
		private final D d;

		public T4(A a, B b, C c, D d) {
			super(a, b, c);
			this.d = d;
		}

		public D getD() {
			return d;
		}
	}

	public static class T5<A, B, C, D, E> extends T4<A, B, C, D> {
		private final E e;

		public T5(A a, B b, C c, D d, E e) {
			super(a, b, c, d);
			this.e = e;
		}

		public E getE() {
			return e;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ")";
		}
	}

	public static class T6<A, B, C, D, E, F> extends T5<A, B, C, D, E> {
		private final F f;

		public T6(A a, B b, C c, D d, E e, F f) {
			super(a, b, c, d, e);
			this.f = f;
		}

		public F getF() {
			return f;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ", " + getF() + ")";
		}
	}

	public static class T7<A, B, C, D, E, F, G> extends T6<A, B, C, D, E, F> {
		private final G g;

		public T7(A a, B b, C c, D d, E e, F f, G g) {
			super(a, b, c, d, e, f);
			this.g = g;
		}

		public G getG() {
			return g;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ", " + getF() + ", " + getG() + ")";
		}
	}

	public static class T8<A, B, C, D, E, F, G, H> extends T7<A, B, C, D, E, F, G> {
		private final H h;

		public T8(A a, B b, C c, D d, E e, F f, G g, H h) {
			super(a, b, c, d, e, f, g);
			this.h = h;
		}

		public H getH() {
			return h;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ", " + getF() + ", " + getG() + ", " + getH() + ")";
		}
	}

	public static class T9<A, B, C, D, E, F, G, H, I> extends T8<A, B, C, D, E, F, G, H> {
		private final I i;

		public T9(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
			super(a, b, c, d, e, f, g, h);
			this.i = i;
		}

		public I getI() {
			return i;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ", " + getF() + ", " + getG() + ", " + getH() + ", " + getI() + ")";
		}
	}

	public static class T10<A, B, C, D, E, F, G, H, I, J> extends T9<A, B, C, D, E, F, G, H, I> {
		private final J j;

		public T10(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
			super(a, b, c, d, e, f, g, h, i);
			this.j = j;
		}

		public J getJ() {
			return j;
		}

		@Override
		public String toString() {
			return "(" + getA() + ", " + getB() + ", " + getC() + ", " + getD() + ", " + getE() + ", " + getF() + ", " + getG() + ", " + getH() + ", " + getI() + ", " + getJ() + ")";
		}
	}
}
