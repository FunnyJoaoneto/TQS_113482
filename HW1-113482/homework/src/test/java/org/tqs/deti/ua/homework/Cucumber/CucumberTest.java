package org.tqs.deti.ua.homework.Cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("org/tqs/deti/ua/homework/Cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.tqs.deti.ua.homework.Cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class CucumberTest {
}
