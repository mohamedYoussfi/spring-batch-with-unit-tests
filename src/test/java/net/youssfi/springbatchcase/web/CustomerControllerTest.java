package net.youssfi.springbatchcase.web;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int serverPort;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    void shouldReturnsCustomers(){
        String baseUrl="http://localhost:"+serverPort+"/customers";
        ResponseEntity<CustomerDTO[]> response = testRestTemplate.getForEntity(baseUrl, CustomerDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(0);
    }
    @Test
    void shouldAddNewCustomer(){
        String baseUrl="http://localhost:"+serverPort+"/customers";
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("Ismail")
                .lastName("Madani")
                .email("ismail@gmail.com")
                .build();
        ResponseEntity<CustomerDTO> response = testRestTemplate.postForEntity(baseUrl,customerDTO, CustomerDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(customerDTO);
    }
}