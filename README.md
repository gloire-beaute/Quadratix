# Quadratix

Quadratix is a student project that assigns a place to a set of machines using quadratic assignement methods.

## Project Structure

In this document, the type `P` denotes the **parameter type** of the fitness function, that can be any elements (bits, combination, number,
...). `R` represents the **return type** of the fitness function. It is often a number (integer or real).

Classes for General Purposes:

* `ISearch`: Interface that implements the `search` function. It must be used by all classes that use an optimisation
search.

* `NumberOperations<T>`: Interface that implements methods to use an object as a number, with different operations:
	* `plus()`
	* `minus()`
	* `multiply()`
	* `divide()`
	* `getZero()`: [The neutral element][Wikipedia-IdentityElement] or the vector space.
The `T` represents the type of the number-like object.

* `MathFunction<P, R>`: Interface that inherits from [`Function`][JavaDoc-Function]. It add the method
`Function<R, P> invert();` in addition to (`R apply(T t)`)[JavaDoc-Function.apply()]. `invert()` return the inverse of
the function defined by `apply()`.

* `ElementaryFunction<P>`: Interface that inherits from `MathFunction<P, P>`. It represents an
[endomorph function][Wikipedia-Endomorphism]. It also add a static method to get the identity.

Classes for Tabu Search:

* `TabuList<P, R>`: Class that inherits from
[`ArrayList<Function<P, R>>`][JavaDoc-ArrayList]. It is a list with a fixed size, defined when it is constructed: When
the list is at full capacity, and a new element is added, it will overwrite the **first** element. The elements listed
are only functions (through the interface [`Function`][JavaDoc-Function]). Be careful not to get confuse between
`size()` which is the actual size of the list (exactly like in [`List`][JavaDoc-List.size()]), and `getFixedSize()`,
which is the fixed length of the list. `size()` ≤ `getFixedSize()`.

* `Tabu<P, R>`: Class that implement the tabu search. The functions `search()` and its overload search the optimal point
in the space of solution.

* `Bits`: Class that represents an array of bits.

* `Combination`: Class that represents a combination of number.

* `BitLengthExceededException`: Exception thrown when an invalid size of bits have been given in `Bits`.

[JavaDoc-ArrayList]: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
[JavaDoc-Function]: https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html
[JavaDoc-Function.apply()]: https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html#apply-T-
[JavaDoc-List.size()]: https://docs.oracle.com/javase/8/docs/api/java/util/List.html#size--
[Wikipedia-Endomorphism]: https://en.wikipedia.org/wiki/Endomorphism
[Wikipedia-IdentityElement]: https://en.wikipedia.org/wiki/Identity_element