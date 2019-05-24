package quadratix;

import org.junit.jupiter.api.*;
import quadratix.combination.Combination;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CombinationTest {
	
	static Combination combination;
	
	@BeforeAll
	static void globalSetup() {
		combination = new Combination(1, 3, 4, 5, 2);
		System.out.println("     c  = " + combination);
	}
	
	@BeforeEach
	 void setup() {
		combination = new Combination(1, 3, 4, 5, 2);
	}
	
	@Test
	@Order(1)
	void swap() {
		Combination c = new Combination(combination);
		c.swap(1, 3);
		System.out.println("swap(c) = " + c);
		assertNotEquals(combination, c);
		assertEquals(combination.size(), c.size());
		assertEquals(1, c.get(0));
		assertEquals(5, c.get(1));
		assertEquals(4, c.get(2));
		assertEquals(3, c.get(3));
		assertEquals(2, c.get(4));
	}
	
	@Test
	@Order(2)
	void swap1() {
		Combination c = new Combination(combination);
		c.swap(1);
		System.out.println("swap(c) = " + c);
		assertNotEquals(combination, c);
		assertNotEquals(combination.get(1), c.get(1));
		assertEquals(combination.size(), c.size());
		c.containsAll(combination);
	}
	
	@Test
	@Order(3)
	void swap2() {
		Combination c = new Combination(combination);
		c.swap();
		System.out.println("swap(c) = " + c);
		assertNotEquals(combination, c);
		assertEquals(combination.size(), c.size());
		c.containsAll(combination);
	}
	
	@Test
	@Order(4)
	void generateAllPossibility() {
		ArrayList<Combination> possibilities = Combination.generateAllPossibility(2);
		System.out.println(possibilities.stream().map(Combination::toString).collect(Collectors.joining("\n")));
		assertEquals(2, possibilities.size());
		assertEquals(new Combination(1, 2), possibilities.get(0));
		assertEquals(new Combination(2, 1), possibilities.get(1));
		
		possibilities = Combination.generateAllPossibility(3);
		System.out.println(possibilities.stream().map(Combination::toString).collect(Collectors.joining("\n")));
		assertEquals(4, possibilities.size());
		assertEquals(new Combination(1, 2, 3), possibilities.get(0));
		assertEquals(new Combination(2, 1, 3), possibilities.get(1));
		assertEquals(new Combination(3, 2, 1), possibilities.get(2));
		assertEquals(new Combination(1, 3, 2), possibilities.get(3));
	}
	
	@Test
	@Order(5)
	void generateRandom() {
		Combination c = Combination.generateRandom(1);
		assertEquals(1, c.size());
		assertTrue(c.get(0) == 0 || c.get(0) == 1);
		
		Set<Combination> ci = new HashSet<>(100);
		for (int i = 0; i < 100; i++) {
			c = Combination.generateRandom(4);
			ci.add(c);
			assertEquals(4, c.size());
		}
		System.out.println("Number of elements: " + ci.size());
		ci.forEach(System.out::println);
		assertEquals(12, ci.size());
	}
}