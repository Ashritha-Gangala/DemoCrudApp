package demoCrudApp.example.DemoCrudApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demoCrudApp.example.DemoCrudApp.model.Item;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
@Configuration
public class DynamoDbConfig {
   @Bean
   public DynamoDbClient dynamoDbClient() {
       return DynamoDbClient.builder().build();
   }
   
   @Bean
   public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
	   return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient()).build();
   }
   
   @Bean
   public DynamoDbTable<Item> dyanmoDbtable(){
	   return dynamoDbEnhancedClient().table("Item", TableSchema.fromBean(Item.class));
   }
   
}