package org.testunited.sdk;

import java.util.List;

public class TestResultSubmission {
private String sessionName;
public String getSessionName() {
	return sessionName;
}
public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
}
public List<TestResult> getTestResults() {
	return testResults;
}
public void setTestResults(List<TestResult> testResults) {
	this.testResults = testResults;
}
private List<TestResult> testResults;
}
