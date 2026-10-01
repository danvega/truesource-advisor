package example.truesource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    public record Order(String id, String total, String currency) {}

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        if (!"1001".equals(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new Order("1001", "42.00", "USD"));
    }
}
