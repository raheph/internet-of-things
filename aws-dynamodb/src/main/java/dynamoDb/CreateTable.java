package dynamoDb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreateTable {

//    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
//            .build();

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();

    static DynamoDB dynamoDB = new DynamoDB(client);
    static Table table = dynamoDB.getTable("offer_tuesday");

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

//    static String productCatalogTableName = "ProductCatalog";
//    static String forumTableName = "Forum";
//    static String threadTableName = "Thread";
//    static String replyTableName = "Reply";
    static String offer_tuesday = "offer_tuesday";

    public static void main(String[] args) throws Exception {
        JsonParser parser = new JsonFactory().createParser(new File("/Users/re/Desktop/internet-of-things/aws-dynamodb/sample.json"));
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();

            String id = currentNode.path("id").asText();
           // String title = currentNode.path("title").asText();

            System.out.println("id ==== " + id );

            try {
                table.putItem(new Item().withPrimaryKey("id", id).withJSON("offerName",
                        currentNode.path("offerName").toString()).withJSON("description",
                        currentNode.path("description").toString()));
                System.out.println("PutItem succeeded: " + id);

            }
            catch (Exception e) {
                System.err.println("Unable to add movie: " + id);
                System.err.println(e.getMessage());
                break;
            }
        }
        parser.close();


















        try {

//            deleteTable(productCatalogTableName);
//            deleteTable(forumTableName);
//            deleteTable(threadTableName);
//            deleteTable(replyTableName);

            // Parameter1: table name
            // Parameter2: reads per second
            // Parameter3: writes per second
            // Parameter4/5: partition key and data type
            // Parameter6/7: sort key and data type (if applicable)

//            createTable(productCatalogTableName, 10L, 5L, "Id", "N");
//            createTable(forumTableName, 10L, 5L, "Name", "S");
//            createTable(threadTableName, 10L, 5L, "ForumName", "S", "Subject", "S");
//            createTable(replyTableName, 10L, 5L, "Id", "S", "ReplyDateTime", "S");

//            loadSampleOffer(offer_tuesday);
//            loadSampleForums(forumTableName);
//            loadSampleThreads(threadTableName);
//            loadSampleReplies(replyTableName);

        }
        catch (Exception e) {
            System.err.println("Program failed:");
            System.err.println(e.getMessage());
        }
        System.out.println("Success.");
    }

//    private static void deleteTable(String tableName) {
//        Table table = dynamoDB.getTable(tableName);
//        try {
//            System.out.println("Issuing DeleteTable request for " + tableName);
//            table.delete();
//            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
//            table.waitForDelete();
//
//        }
//        catch (Exception e) {
//            System.err.println("DeleteTable request failed for " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }
//
//    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
//                                    String partitionKeyName, String partitionKeyType) {
//
//        createTable(tableName, readCapacityUnits, writeCapacityUnits, partitionKeyName, partitionKeyType, null, null);
//    }

//    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
//                                    String partitionKeyName, String partitionKeyType, String sortKeyName, String sortKeyType) {
//
//        try {
//
//            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
//            keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH)); // Partition
//            // key
//
//            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
//            attributeDefinitions
//                    .add(new AttributeDefinition().withAttributeName(partitionKeyName).withAttributeType(partitionKeyType));
//
//            if (sortKeyName != null) {
//                keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName).withKeyType(KeyType.RANGE)); // Sort
//                // key
//                attributeDefinitions
//                        .add(new AttributeDefinition().withAttributeName(sortKeyName).withAttributeType(sortKeyType));
//            }
//
//            CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
//                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits)
//                            .withWriteCapacityUnits(writeCapacityUnits));
//
//            // If this is the Reply table, define a local secondary index
//            if (replyTableName.equals(tableName)) {
//
//                attributeDefinitions
//                        .add(new AttributeDefinition().withAttributeName("PostedBy").withAttributeType("S"));
//
//                ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
//                localSecondaryIndexes.add(new LocalSecondaryIndex().withIndexName("PostedBy-Index")
//                        .withKeySchema(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH), // Partition
//                                // key
//                                new KeySchemaElement().withAttributeName("PostedBy").withKeyType(KeyType.RANGE)) // Sort
//                        // key
//                        .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY)));
//
//                request.setLocalSecondaryIndexes(localSecondaryIndexes);
//            }

//            request.setAttributeDefinitions(attributeDefinitions);
//
//            System.out.println("Issuing CreateTable request for " + tableName);
//            Table table = dynamoDB.createTable(request);
//            System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
//            table.waitForActive();
//
//        }
//        catch (Exception e) {
//            System.err.println("CreateTable request failed for " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }

    private static void loadSampleOffer(String tableName) {

        Table table = dynamoDB.getTable(tableName);

        try {

            System.out.println("Adding data to " + tableName);

            Item item = new Item().withPrimaryKey("Id", "101").withString("Offer", "offer 1")
                    .withString("description", "the first offer");
            table.putItem(item);

//            item = new Item().withPrimaryKey("Id", "102").withString("Offer", "offer 2")
//                    .withString("description", "the second offer");
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("Id", "103").withString("Offer", "offer 3")
//                    .withString("description", "the third offer");
//            table.putItem(item);

        }
        catch (Exception e) {
            System.err.println("Failed to create item in " + tableName);
            System.err.println(e.getMessage());
        }

    }

//    private static void loadSampleForums(String tableName) {
//
//        Table table = dynamoDB.getTable(tableName);
//
//        try {
//
//            System.out.println("Adding data to " + tableName);
//
//            Item item = new Item().withPrimaryKey("Name", "Amazon DynamoDB")
//                    .withString("Category", "Amazon Web Services").withNumber("Threads", 2).withNumber("Messages", 4)
//                    .withNumber("Views", 1000);
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("Name", "Amazon S3").withString("Category", "Amazon Web Services")
//                    .withNumber("Threads", 0);
//            table.putItem(item);
//
//        }
//        catch (Exception e) {
//            System.err.println("Failed to create item in " + tableName);
//            System.err.println(e.getMessage());
//        }
//    }

//    private static void loadSampleThreads(String tableName) {
//        try {
//            long time1 = (new Date()).getTime() - (7 * 24 * 60 * 60 * 1000); // 7
//            // days
//            // ago
//            long time2 = (new Date()).getTime() - (14 * 24 * 60 * 60 * 1000); // 14
//            // days
//            // ago
//            long time3 = (new Date()).getTime() - (21 * 24 * 60 * 60 * 1000); // 21
//            // days
//            // ago
//
//            Date date1 = new Date();
//            date1.setTime(time1);
//
//            Date date2 = new Date();
//            date2.setTime(time2);
//
//            Date date3 = new Date();
//            date3.setTime(time3);
//
//            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            Table table = dynamoDB.getTable(tableName);
//
//            System.out.println("Adding data to " + tableName);
//
//            Item item = new Item().withPrimaryKey("ForumName", "Amazon DynamoDB")
//                    .withString("Subject", "DynamoDB Thread 1").withString("Message", "DynamoDB thread 1 message")
//                    .withString("LastPostedBy", "User A").withString("LastPostedDateTime", dateFormatter.format(date2))
//                    .withNumber("Views", 0).withNumber("Replies", 0).withNumber("Answered", 0)
//                    .withStringSet("Tags", new HashSet<String>(Arrays.asList("index", "primarykey", "table")));
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("ForumName", "Amazon DynamoDB").withString("Subject", "DynamoDB Thread 2")
//                    .withString("Message", "DynamoDB thread 2 message").withString("LastPostedBy", "User A")
//                    .withString("LastPostedDateTime", dateFormatter.format(date3)).withNumber("Views", 0)
//                    .withNumber("Replies", 0).withNumber("Answered", 0)
//                    .withStringSet("Tags", new HashSet<String>(Arrays.asList("index", "partitionkey", "sortkey")));
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("ForumName", "Amazon S3").withString("Subject", "S3 Thread 1")
//                    .withString("Message", "S3 Thread 3 message").withString("LastPostedBy", "User A")
//                    .withString("LastPostedDateTime", dateFormatter.format(date1)).withNumber("Views", 0)
//                    .withNumber("Replies", 0).withNumber("Answered", 0)
//                    .withStringSet("Tags", new HashSet<String>(Arrays.asList("largeobjects", "multipart upload")));
//            table.putItem(item);
//
//        }
//        catch (Exception e) {
//            System.err.println("Failed to create item in " + tableName);
//            System.err.println(e.getMessage());
//        }
//
//    }

//    private static void loadSampleReplies(String tableName) {
//        try {
//            // 1 day ago
//            long time0 = (new Date()).getTime() - (1 * 24 * 60 * 60 * 1000);
//            // 7 days ago
//            long time1 = (new Date()).getTime() - (7 * 24 * 60 * 60 * 1000);
//            // 14 days ago
//            long time2 = (new Date()).getTime() - (14 * 24 * 60 * 60 * 1000);
//            // 21 days ago
//            long time3 = (new Date()).getTime() - (21 * 24 * 60 * 60 * 1000);
//
//            Date date0 = new Date();
//            date0.setTime(time0);
//
//            Date date1 = new Date();
//            date1.setTime(time1);
//
//            Date date2 = new Date();
//            date2.setTime(time2);
//
//            Date date3 = new Date();
//            date3.setTime(time3);
//
//            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            Table table = dynamoDB.getTable(tableName);
//
//            System.out.println("Adding data to " + tableName);
//
//            // Add threads.
//
//            Item item = new Item().withPrimaryKey("Id", "Amazon DynamoDB#DynamoDB Thread 1")
//                    .withString("ReplyDateTime", (dateFormatter.format(date3)))
//                    .withString("Message", "DynamoDB Thread 1 Reply 1 text").withString("PostedBy", "User A");
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("Id", "Amazon DynamoDB#DynamoDB Thread 1")
//                    .withString("ReplyDateTime", dateFormatter.format(date2))
//                    .withString("Message", "DynamoDB Thread 1 Reply 2 text").withString("PostedBy", "User B");
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("Id", "Amazon DynamoDB#DynamoDB Thread 2")
//                    .withString("ReplyDateTime", dateFormatter.format(date1))
//                    .withString("Message", "DynamoDB Thread 2 Reply 1 text").withString("PostedBy", "User A");
//            table.putItem(item);
//
//            item = new Item().withPrimaryKey("Id", "Amazon DynamoDB#DynamoDB Thread 2")
//                    .withString("ReplyDateTime", dateFormatter.format(date0))
//                    .withString("Message", "DynamoDB Thread 2 Reply 2 text").withString("PostedBy", "User A");
//            table.putItem(item);
//
//        }
//        catch (Exception e) {
//            System.err.println("Failed to create item in " + tableName);
//            System.err.println(e.getMessage());
//
//        }
//    }

}