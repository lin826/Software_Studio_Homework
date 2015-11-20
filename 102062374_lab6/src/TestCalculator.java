import static org.junit.Assert.*;

import org.junit.*;


public class TestCalculator {
	
	private Calculator calc = new Calculator();
	
	@BeforeClass
	public static void setup(){
		System.out.println("Start test Calculator");
	}
	
	@Before
	public void prepare(){
		System.out.println("Prepree for test");
		this.calc.setNumber(2);
	}
	
	@After
	public void resultOfTest(){
		System.out.println(this.calc.getResult());
	}
	
	@Test
	public void testInitState() {
		Calculator calc = new Calculator();
		assertNotNull(calc);
		assertTrue(calc.getResult()==0.0);
		assertFalse(calc.getResult()==2.0);
		//fail("Not yet implemented");
	}
	
	@Test
	public void testMinus(){
		this.calc.minus(1);
		assertTrue(this.calc.getResult()==1.0);
		assertFalse(this.calc.getResult()==-1.0);
	}
	
	@Test
	public void testTimes(){
		this.calc.times(3);
		assertTrue("not 6.0",this.calc.getResult()==6.0);
	}
	
	@Test
	public void testPlus(){
		this.calc.plus(1);
		assertTrue(this.calc.getResult()==3.0);
	}
	
	@Test
	public void testPowerOf(){
		this.calc.powerOf(3);
		assertTrue(this.calc.getResult()==8.0);
	}	
	
	@Test
	public void testDivideBy(){
		this.calc.dividedBy(1);
		//System.out.println("2/5 = "+this.calc.getResult()); //the result will be erro
		assertTrue(this.calc.getResult()==2.0);
	}
	
	@Test
	public void testPowerOf4(){
		this.calc.powerOf(4);
		assertTrue(this.calc.getResult()==16.0);
	}
	
	@Test
	public void testSquare(){
		this.calc.square();
		assertTrue(this.calc.getResult()==4.0);
	}

	


}
