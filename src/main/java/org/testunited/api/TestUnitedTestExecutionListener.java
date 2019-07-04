package org.testunited.api;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestUnitedTestExecutionListener implements TestExecutionListener {
	
	List<TestResult> tests = new ArrayList<TestResult>();

	@Override
	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {		
		if (testIdentifier.isTest()) {
			boolean isSuccessful = (testExecutionResult.getStatus() == Status.SUCCESSFUL);
			
			String reason = "";
			if (testExecutionResult.getThrowable().isPresent()) {
				reason = testExecutionResult.getThrowable().get().getMessage()
						.replaceAll("\n", "").replaceAll("\'","");
			}
			
			String suite = "";
			String separator = "class:";
			if (testIdentifier.getParentId().isPresent()) {
				suite = testIdentifier.getParentId().get();
				suite = suite.substring(suite.indexOf(separator) + separator.length(), suite.lastIndexOf("]"));
			}

			String name = testIdentifier.getDisplayName();
			if (name.indexOf("(") != -1) {
				name = name.substring(0, name.lastIndexOf("("));
			}

			TestResult testRun = new TestResult(String.format("%s.%s", suite, name), isSuccessful, reason);

			this.tests.add(testRun);
		}
	}

	@Override
	public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {

		// TODO Auto-generated method stub
		TestExecutionListener.super.reportingEntryPublished(testIdentifier, entry);
	}

	@Override
	public void testPlanExecutionFinished(TestPlan testPlan) {

		StringBuilder payloadBuilder = new StringBuilder();
//		payloadBuilder.append("[");
//
//		for(int i=0;i< this.tests.size();i++) {
//			payloadBuilder.append(this.tests.get(i).toJson());
//			
//			if(i < this.tests.size() - 1)
//				payloadBuilder.append(",");
//		}
//
//		payloadBuilder.append("]");
		ObjectMapper mapper = new ObjectMapper();

		try {
			payloadBuilder.append(mapper.writeValueAsString(this.tests));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpClient httpclient = HttpClients.createDefault();
		String payload = payloadBuilder.toString();
		StringEntity requestEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
		
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		try {
			String formattedPayload = mapper.writeValueAsString(this.tests);
			System.out.println("----------TESTUNITED PAYLOAD------------");
			System.out.println(formattedPayload);
			System.out.println("----------------------------------------");
			
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpPost postMethod = new HttpPost(PropertyReader.getPropValue("testunited.service.url") + "/testresults/bulk");
		postMethod.setEntity(requestEntity);
		HttpResponse rawResponse = null;

		try {
			rawResponse = httpclient.execute(postMethod);
			System.out.println(rawResponse.getStatusLine());
			HttpEntity entity2 = rawResponse.getEntity();
			EntityUtils.consume(entity2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
