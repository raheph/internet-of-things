package com.serverless;


import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
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



import java.util.*;

public class DDBEventProcessor implements
        RequestHandler<DynamodbEvent, String> {


    public static SpeechletClass speechletClass;

    public DDBEventProcessor(){
        if (speechletClass == null){
            speechletClass = new SpeechletClass();
        }
    }


     class SpeechletClass implements SpeechletV2 {

        @Override
        public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        }

        @Override
        public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
            return getWelcomeResponse();
        }

        @Override
        public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {


            System.out.print("Size of the map ==== " + offerList.size());
            // Create the Simple card content.
            SimpleCard card = getSimpleCard("HelloWorld", "it works .... ");

            // Create the plain text output.
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech("it works .... ");

            return SpeechletResponse.newTellResponse(speech, card);
        }

        @Override
        public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        }

        /**
         * Creates and returns a {@code SpeechletResponse} with a welcome message.
         *
         * @return SpeechletResponse spoken and visual response for the given intent
         */
        private SpeechletResponse getWelcomeResponse() {
            String speechText = "Welcome to the Alexa Skills Kit, you can say hello";
            return getAskResponse("HelloWorld", speechText);
        }

        /**
         * Creates a {@code SpeechletResponse} for the hello intent.
         *
         * @return SpeechletResponse spoken and visual response for the given intent
         */
        private SpeechletResponse getHelloResponse() {
            String speechText = "Hello world";

            // Create the Simple card content.
            SimpleCard card = getSimpleCard("HelloWorld", speechText);

            // Create the plain text output.
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

            return SpeechletResponse.newTellResponse(speech, card);
        }

        /**
         * Creates a {@code SpeechletResponse} for the help intent.
         *
         * @return SpeechletResponse spoken and visual response for the given intent
         */
        private SpeechletResponse getHelpResponse() {
            String speechText = "You can say hello to me!";
            return getAskResponse("HelloWorld", speechText);
        }

        /**
         * Helper method that creates a card object.
         * @param title title of the card
         * @param content body of the card
         * @return SimpleCard the display card to be sent along with the voice response.
         */
        private SimpleCard getSimpleCard(String title, String content) {
            SimpleCard card = new SimpleCard();
            card.setTitle(title);
            card.setContent(content);

            return card;
        }

        /**
         * Helper method for retrieving an OutputSpeech object when given a string of TTS.
         * @param speechText the text that should be spoken out to the user.
         * @return an instance of SpeechOutput.
         */
        private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(speechText);

            return speech;
        }

        /**
         * Helper method that returns a reprompt object. This is used in Ask responses where you want
         * the user to be able to respond to your speech.
         * @param outputSpeech The OutputSpeech object that will be said once and repeated if necessary.
         * @return Reprompt instance.
         */
        private Reprompt getReprompt(OutputSpeech outputSpeech) {
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(outputSpeech);

            return reprompt;
        }

        /**
         * Helper method for retrieving an Ask response with a simple card and reprompt included.
         * @param cardTitle Title of the card that you want displayed.
         * @param speechText speech text that will be spoken to the user.
         * @return the resulting card and speech text.
         */
        private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
            SimpleCard card = getSimpleCard(cardTitle, speechText);
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
            Reprompt reprompt = getReprompt(speech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }
    }














    private static Map<String, String> offerList = new HashMap<String, String>();
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    DynamoDBMapper mapper = new DynamoDBMapper(client);

    ScanRequest scanRequest = new ScanRequest()
            .withTableName("offer_tuesday");


    public String handleRequest(DynamodbEvent ddbEvent, Context context) {
        ScanResult result = client.scan(scanRequest);

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
}
