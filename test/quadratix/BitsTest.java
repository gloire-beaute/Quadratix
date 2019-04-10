package quadratix;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BitsTest {
	
	Bits b1;
	Bits b2;
	
	@BeforeEach
	void setup() {
		b1 = new Bits(10, 4); // 1010
		b2 = new Bits(2, 4); // 0010
	}
	
	@Test
	@Order(1)
	void test_toString() {
		System.out.println(b1.toString());
		assertEquals("1010", b1.toString());
		
		System.out.println(b2.toString());
		assertEquals("0010", b2.toString());
	}
	
	@Test
	@Order(2)
	void getBitFromLeft() {
		assertTrue(b1.getBitFromLeft(0));
		assertFalse(b1.getBitFromLeft(1));
		assertTrue(b1.getBitFromLeft(2));
		assertFalse(b1.getBitFromLeft(3));
		
		assertFalse(b2.getBitFromLeft(0));
		assertFalse(b2.getBitFromLeft(1));
		assertTrue(b2.getBitFromLeft(2));
		assertFalse(b2.getBitFromLeft(3));
	}
	
	@Test
	@Order(3)
	void getBitFromRight() {
		assertFalse(b1.getBitFromRight(0));
		assertTrue(b1.getBitFromRight(1));
		assertFalse(b1.getBitFromRight(2));
		assertTrue(b1.getBitFromRight(3));
		
		assertFalse(b2.getBitFromRight(0));
		assertTrue(b2.getBitFromRight(1));
		assertFalse(b2.getBitFromRight(2));
		assertFalse(b2.getBitFromRight(3));
	}
	
	@Test
	@Order(4)
	void putBitFromLeft() {
		b1.putBitFromLeft(0, false); // "1010" -> "0010" = 2
		System.out.println("b1 = " + b1);
		assertEquals(2, b1.getValue());
		assertEquals("0010", b1.getBits());
	}
	
	@Test
	@Order(5)
	void putBitFromRight() {
		b1.putBitFromRight(0, true); // "1010" -> "1011" = 11
		System.out.println("b1 = " + b1);
		assertEquals(11, b1.getValue());
		assertEquals("1011", b1.getBits());
	}
	
	@Test
	@Order(6)
	void invertBitFromLeft() {
		b1.invertBitFromLeft(0); // "1010" -> "0010" = 2
		System.out.println("b1 = " + b1);
		assertEquals(2, b1.getValue());
		assertEquals("0010", b1.getBits());
	}
	
	@Test
	@Order(7)
	void invertBitFromRight() {
		b1.invertBitFromRight(0); // "1010" -> "1011" = 11
		System.out.println("b1 = " + b1);
		assertEquals(11, b1.getValue());
		assertEquals("1011", b1.getBits());
	}
	
	@Test
	@Order(8)
	void plus() {
		assertEquals(12, b1.plus(b2).getValue());
	}
	
	@Test
	@Order(9)
	void minus() {
		assertEquals(8, b1.minus(b2).getValue());
	}
	
	@Test
	@Order(10)
	void multiply() {
		assertEquals(20, b1.multiply(b2).getValue());
	}
	
	@Test
	@Order(11)
	void divideBy() {
		assertEquals(5, b1.divideBy(b2).getValue());
	}
	
	@Test
	@Order(12)
	void invertAllBits() {
		Bits i1 = b1.invertAllBits(); // i1 = "0101" = 5
		System.out.println("i1 = " + i1);
		assertEquals(5, i1.getValue());
		assertEquals("0101", i1.getBits());
	}
	
	@Test
	@Order(13)
	void getLength() {
		assertEquals(4, b1.getLength());
		assertEquals(4, b2.getLength());
		
		b2.setLength(2);
		assertEquals(2, b2.getLength());
		assertEquals(2, b2.getValue());
		assertEquals("10", b2.getBits());
	}
	
	@Test
	@Order(14)
	void generateAllPossibility() {
		ArrayList<Bits> allBits = Bits.generateAllPossibility(2);
		
		System.out.println("all possibilities with 2 bits: " + allBits);
		assertEquals(4, allBits.size());
		for (int i = 0; i < 4; i++)
			assertEquals(i, allBits.get(i).getValue());
		
		allBits = Bits.generateAllPossibility(4);
		
		System.out.println("all possibilities with 4 bits: " + allBits);
		assertEquals(16, allBits.size());
		for (int i = 0; i < 16; i++)
			assertEquals(i, allBits.get(i).getValue());
	}
	
	@Test
	@Order(15)
	void getValue() {
		assertEquals(10, b1.getValue());
		assertEquals(2, b2.getValue());
	}
	
	@Test
	@Order(16)
	void getBits() {
		assertEquals("1010", b1.getBits());
		assertEquals("0010", b2.getBits());
	}
	
	@Test
	@Order(17)
	void setBits() {
		b1.setBits("11"); // 3
		assertEquals("0011", b1.getBits());
		assertEquals(3, b1.getValue());
	}
}