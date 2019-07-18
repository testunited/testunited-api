package org.testunited.api;

import java.util.UUID;

public class TestResultSubmissionSummary{
	
	private UUID testSessionId;
	private int testCaseCount;

	public UUID getTestSessionId() {
		return testSessionId;
	}

	public void setTestSessionId(UUID testSessionId) {
		this.testSessionId = testSessionId;
	}

	public int getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(int testCaseCount) {
		this.testCaseCount = testCaseCount;
	}
	
	
}