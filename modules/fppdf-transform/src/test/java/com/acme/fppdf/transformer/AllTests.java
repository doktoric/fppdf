package com.acme.fppdf.transformer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all of the tests.
 * @author Janos_Gyula_Meszaros
 */
@RunWith(Suite.class)
@SuiteClasses({ InvalidConversionTest.class, ConverterTest.class })
public class AllTests {

}
