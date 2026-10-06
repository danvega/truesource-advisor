package com.broadcom;

import com.broadcom.OrdersController.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersContractTest {
    @Autowired private TestRestTemplate client;

    @Test
    void returnsTheExpectedOrderContractOverHttp() {
        var response = client.getForEntity("/api/orders/1001", Order.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new Order("1001", "42.00", "USD"));
    }

    @Test
    void unknownOrderReturnsNotFoundOverHttp() {
        var response = client.getForEntity("/api/orders/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
