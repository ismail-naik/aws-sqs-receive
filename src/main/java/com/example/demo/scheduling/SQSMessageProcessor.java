package com.example.demo.scheduling;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
public class SQSMessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SQSMessageProcessor.class);

	String queueUrl = "https://sqs.us-east-1.amazonaws.com/addyourawsaccount id/sqs-test-standard";
	final SqsClient sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
	
	
	@Scheduled(cron = "*/10 * * * * ?")
	public void processMessages() {
		getMessages();
		LOGGER.info("Executing Message Processor-time based");
	}

	private List<Message> getMessages() {

		List<Message> messages = null;

		try {
			
			messages = receiveMessages(sqsClient, queueUrl);
			// ObjectMapper objectMapper = new ObjectMapper();
			
			int count = 0;
			LOGGER.info("Message Size" + messages.size());
			
			for (Message mess : messages) {

				LOGGER.info("Message Received-->:" + mess.body().toString());
				//LOGGER.info("Count =" + count++ + ":" + mess.toString());
			}
		} catch (SqsException e) {
			LOGGER.error(e.awsErrorDetails().errorMessage());

		} /*
			 * catch (JsonProcessingException jpe) {
			 * System.err.println(jpe.getLocalizedMessage());
			 * 
			 * }
			 */

		return messages;

	}

	private static List<Message> receiveMessages(SqsClient sqsClient, String queueUrl) {

		System.out.println("\nReceive messages");
		List<Message> messages = null;
		try {

			ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl)
					.visibilityTimeout(60)
					.waitTimeSeconds(20)
					.maxNumberOfMessages(10).build(); 
			
			 ReceiveMessageResponse receiveResult = sqsClient.receiveMessage(receiveMessageRequest);
			 
			 if (receiveResult.sdkHttpResponse().statusCode() != 200) {
				    LOGGER.error("got error response from SQS queue {}: {}",
				    queueUrl,
				    receiveResult.sdkHttpResponse());
				    return Collections.emptyList();
				  }
			// receiveResult.messages().siz
			messages = receiveResult.messages();
			LOGGER.info("Message count " + messages.size());
			// return messages;
		} catch (SqsException e) {
			System.err.println(e.awsErrorDetails().errorMessage());

		}
		return messages;

	}

}
