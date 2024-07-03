package demoCrudApp.example.DemoCrudApp.controller;

import demoCrudApp.example.DemoCrudApp.model.Item;
import demoCrudApp.example.DemoCrudApp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/items")
public class ItemController {
   @Autowired
   private ItemService itemService;
   @PostMapping
   public ResponseEntity<Item> createItem(@RequestBody Item item) {
       Item savedItem = itemService.save(item);
       return ResponseEntity.ok(savedItem);
   }
   @GetMapping("/{id}")
   public ResponseEntity<Item> getItemById(@PathVariable String id) {
       Optional<Item> item = itemService.getById(id);
       return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
   }
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteItem(@PathVariable String id) {
       itemService.delete(id);
       return ResponseEntity.noContent().build();
   }
}
