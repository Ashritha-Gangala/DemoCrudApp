package demoCrudApp.example.DemoCrudApp.repository;

import demoCrudApp.example.DemoCrudApp.model.Item;
import demoCrudApp.example.DemoCrudApp.service.ItemService;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.util.Optional;
@Service
public class ItemRepository implements ItemService {
   private final DynamoDbEnhancedClient enhancedClient;
   private final DynamoDbTable<Item> itemTable;
   
   public ItemRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbTable<Item> itemTable) {
	super();
	this.enhancedClient = enhancedClient;
	this.itemTable = itemTable;
}
public Item save(Item item) {
       try {
           itemTable.putItem(PutItemEnhancedRequest.builder(Item.class).item(item).build());
       } catch (DynamoDbException e) {
           e.printStackTrace();
       }
       return item;
   }
   public Optional<Item> getById(String id) {
       try {
           return Optional.ofNullable(itemTable.getItem(r -> r.key(k -> k.partitionValue(id))));
       } catch (DynamoDbException e) {
           e.printStackTrace();
           return Optional.empty();
       }
   }
   public void delete(String id) {
       try {
           itemTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
       } catch (DynamoDbException e) {
           e.printStackTrace();
       }
   }
}