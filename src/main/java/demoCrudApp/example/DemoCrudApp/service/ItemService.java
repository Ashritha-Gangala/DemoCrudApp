package demoCrudApp.example.DemoCrudApp.service;

import demoCrudApp.example.DemoCrudApp.model.Item;
//import demoCrudApp.example.DemoCrudApp.repository.ItemRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import java.util.Optional;

public interface ItemService {

   //private ItemRepository itemRepository;
   public Item save(Item item);
   public Optional<Item> getById(String id);
   public void delete(String id);
}