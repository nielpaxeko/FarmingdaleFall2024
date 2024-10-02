package org.example;
import java.io.PushbackReader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyScannerTest {

    // NOTES
    // The token and buffer string are printed in the MyScannerClass
    // Print statements for separating tests and checking the buffer string in all tests
    // Didn't know wether to include SCANEOF in the tests but decided not to based on the wording of the assignment's description except in the last one
    @Test
    public void test1() throws Exception {
        String test_input = "declare x int";
        System.out.println("Test 1 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans

        assertEquals(MyScanner.TOKEN.DECLARE, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.INTDATATYPE, scanner.scan());
    }

    @Test
    public void test2() throws Exception {
        String test_input = "set x = 5";
        System.out.println("\nTest 2 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans
        assertEquals(MyScanner.TOKEN.SET, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(MyScanner.TOKEN.INTLITERAL, scanner.scan());
    }

    @Test
    public void test3() throws Exception {
        String test_input = "calc x + y";
        System.out.println("\nTest 3 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans
        assertEquals(MyScanner.TOKEN.CALC, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.PLUS, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
    }

    @Test
    public void test4() throws Exception {
        String test_input = "print x";
        System.out.println("\nTest 4 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans
        assertEquals(MyScanner.TOKEN.PRINT, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
    }

    @Test
    public void test5() throws Exception {
        String test_input = "if x = y then endif";
        System.out.println("\nTest 5 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans
        assertEquals(MyScanner.TOKEN.IF, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.THEN, scanner.scan());
        assertEquals(MyScanner.TOKEN.ENDIF, scanner.scan());
    }

    @Test
    public void test6() throws Exception {
        String test_input = "if x = y then\n print x\n endif";
        System.out.println("\nTest 6 -> " + test_input);
        PushbackReader pbr = new PushbackReader(new StringReader(test_input));
        MyScanner scanner = new MyScanner(pbr);
        // Scans
        assertEquals(MyScanner.TOKEN.IF, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.THEN, scanner.scan());
        assertEquals(MyScanner.TOKEN.PRINT, scanner.scan());
        assertEquals(MyScanner.TOKEN.ID, scanner.scan());
        assertEquals(MyScanner.TOKEN.ENDIF, scanner.scan());
    }

    @Test
    public void test7() throws Exception {
        // Empty string test
        System.out.println("\nTest 7 -> ''");
        PushbackReader empty = new PushbackReader(new StringReader(""));
        MyScanner scanner = new MyScanner(empty);
        // SCAN EOF
        assertEquals(MyScanner.TOKEN.SCANEOF, scanner.scan());
    }
}