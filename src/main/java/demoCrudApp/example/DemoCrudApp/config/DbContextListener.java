package demoCrudApp.example.DemoCrudApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import demoCrudApp.example.DemoCrudApp.model.Item;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Component
public class DbContextListener {

	@Autowired
	private DynamoDbClient dynamoDbClient;

	@EventListener({ ContextStartedEvent.class })
	public void onApplicationEvent(ContextStartedEvent event) {
		
		System.out.println("Inside the application listener");
		DynamoDbWaiter ddw = dynamoDbClient.waiter();
		CreateTableRequest createTableRequest = CreateTableRequest.builder()
				.attributeDefinitions(AttributeDefinition.builder().attributeName("ItemId")
						.attributeType(ScalarAttributeType.S).build())
				.keySchema(KeySchemaElement.builder().attributeName("ItemId").keyType(KeyType.HASH).build())
				.provisionedThroughput(
						ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
				.tableName("Item").build();
		String newTable = null;

		try {
			System.out.println("Trying the table create");
			CreateTableResponse createTableResponse = dynamoDbClient.createTable(createTableRequest);
			DescribeTableRequest describeTableRequest = DescribeTableRequest.builder().tableName("Item")
					.build();
			// Waiting for table creation
			System.out.println("Waiting for the table create");
			WaiterResponse<DescribeTableResponse> waiterResponse = ddw.waitUntilTableExists(describeTableRequest);
			waiterResponse.matched().response().ifPresent(System.out::println);
			newTable = createTableResponse.tableDescription().tableName();
			System.out.println("New Table Created:: " + newTable);

		} catch (DynamoDbException e) {

			if (StringUtils.startsWithIgnoreCase(e.getMessage(), "Table already exists")) {
				System.out.println(e.getMessage());
			} else {
				System.out.println("Exception is: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

}

