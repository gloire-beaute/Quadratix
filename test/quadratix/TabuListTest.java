package quadratix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadratix.tabu.TabuList;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class TabuListTest {
	
	TabuList<Integer, Integer> tabu;
	Function<Integer, Integer> f1, f2, f3, f4, f5;
	
	@BeforeEach
	void setup() {
		tabu = new TabuList<>(3);
		
		f1 = Function.identity();
		f2 = x -> 2 * x + 3;
		f3 = x -> ((int) Math.pow(x, 2)) + 2 * x + 3;
		f4 = x -> 0;
		f5 = x -> 1 - x;
		
		tabu.add(f1);
	}
	
	@Test
	void add() {
		assertEquals(3, tabu.getFixedSize());
		assertEquals(1, tabu.size());
		assertEquals(f1, tabu.get(0));
		
		tabu.add(f2);
		System.out.println(tabu);
		assertEquals(2, tabu.size());
		assertEquals(3, tabu.getFixedSize());
		assertEquals(f2, tabu.get(1));
		
		tabu.add(f3);
		assertEquals(3, tabu.size());
		assertEquals(3, tabu.getFixedSize());
		assertEquals(f3, tabu.get(2));
		
		tabu.add(f4);
		assertEquals(3, tabu.size());
		assertEquals(3, tabu.getFixedSize());
		assertEquals(f2, tabu.get(0));
		assertEquals(f3, tabu.get(1));
		assertEquals(f4, tabu.get(2));
		
		tabu.add(f5);
		assertEquals(3, tabu.size());
		assertEquals(3, tabu.getFixedSize());
		assertEquals(f3, tabu.get(0));
		assertEquals(f4, tabu.get(1));
		assertEquals(f5, tabu.get(2));
	}
}