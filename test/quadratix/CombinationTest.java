package quadratix;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.combination.Combination;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
	void swap2() {
		Combination c = new Combination(combination);
		c.swap();
		System.out.println("swap(c) = " + c);
		assertNotEquals(combination, c);
		assertEquals(combination.size(), c.size());
		c.containsAll(combination);
	}
	
	@Test
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
}