package org.testunited.api;

import java.util.List;

public class TestResultSubmission {
private String session;
public String getSession() {
	return session;
}
public void setSession(String session) {
	this.session = session;
}
public List<TestResult> getTestResults() {
	return testResults;
}
public void setTestResults(List<TestResult> testResults) {
	this.testResults = testResults;
}
private List<TestResult> testResults;
}
