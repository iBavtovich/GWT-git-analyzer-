package com.epam.gwt;

import com.epam.gwt.client.GithubAnalyzerTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GithubAnalyzerSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for GithubAnalyzer");
    suite.addTestSuite(GithubAnalyzerTest.class);
    return suite;
  }
}
