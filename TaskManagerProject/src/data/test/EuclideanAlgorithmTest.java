package data.test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.EuclideanAlgorithm;

//@author A0065475X
public class EuclideanAlgorithmTest {

    private void computeReversePrime() {
        EuclideanAlgorithm result;
        // Actual computation of reverse prime.
        result = new EuclideanAlgorithm(363767, 20280);
        System.out.println(result.m);
    }

    @Test
    public void test() {
        
        EuclideanAlgorithm result;
        
        result = new EuclideanAlgorithm(13, 11);
        assertEquals(-5, result.m);
        assertEquals(6, result.q);
        assertEquals(1, result.gcd);
        
        result = new EuclideanAlgorithm(65, 40);
        assertEquals(-3, result.m);
        assertEquals(5, result.q);
        assertEquals(5, result.gcd);
        
        result = new EuclideanAlgorithm(40, 65);
        assertEquals(5, result.m);
        assertEquals(-3, result.q);
        assertEquals(5, result.gcd);

        //computeReversePrime();
    }
}
