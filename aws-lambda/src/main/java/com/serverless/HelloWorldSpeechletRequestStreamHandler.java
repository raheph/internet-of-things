package com.serverless;

import java.util.*;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import static com.serverless.DDBEventProcessor.speechletClass;

/**
 * This class could be the handler for an AWS Lambda function powering an Alexa Skills Kit
 * experience. To do this, simply set the handler field in the AWS Lambda console to
 * "com.amazon.asksdk.helloworld.HelloWorldSpeechletRequestStreamHandler" For this to work, you'll also need to build
 * this project using the {@code lambda-compile} Ant task and upload the resulting zip file to power
 * your function.
 */
public final class HelloWorldSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler
        implements
        RequestHandler<DynamodbEvent, String> {



    HashMap<String, String> myMap = new  HashMap<>();
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    DynamoDBMapper mapper = new DynamoDBMapper(client);

    ScanRequest scanRequest = new ScanRequest()
            .withTableName("offer_tuesday");


    public String handleRequest(DynamodbEvent ddbEvent, Context context) {
        ScanResult result = client.scan(scanRequest);
        Map<String, String> offerList = new HashMap<String, String>();

        for (Map<String, AttributeValue> item : result.getItems()){
            System.out.println(item.values());
        }
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withIndexName("offerName-index")
                .withConsistentRead(false);

        List<offer_tuesday> indexItems =  mapper.scan(offer_tuesday.class, scanExpression);

        for (offer_tuesday offers : indexItems){
            offerList.put(offers.getOfferName(), offers.getDescription());
            System.out.println(" id = " + offers.getId() + " offerName = " + offers.getOfferName() +
                    "Description = " + offers.getDescription());
        }

        return "Successfully processed " + ddbEvent.getRecords().size() + " records.";
    }




    private static final Set<String> supportedApplicationIds;
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.4d194caf-706c-4024-b1d5-4d38f14b68b3");
    }

    public HelloWorldSpeechletRequestStreamHandler() {
        super(speechletClass, supportedApplicationIds);
    }
}
