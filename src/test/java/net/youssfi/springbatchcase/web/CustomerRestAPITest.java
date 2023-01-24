package net.youssfi.springbatchcase.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import net.youssfi.springbatchcase.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerRestAPITest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> givenCustomers;

    @BeforeEach
    void setUp() {
        givenCustomers= List.of(
                CustomerDTO.builder().id(1L).firstName("A").lastName("AA").email("a@gmail.com").build(),
                CustomerDTO.builder().id(1L).firstName("B").lastName("BB").email("b@gmail.com").build()
        );
    }

    @Test
    @DisplayName("should save and return the saved customer")
    void shouldAddCustomer() throws Exception {
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .email(null)
                .build();
        ArgumentCaptor<CustomerDTO> argumentCaptor=ArgumentCaptor.forClass(CustomerDTO.class);

        when(customerService.saveCustomer(argumentCaptor.capture())).thenReturn(customerDTO);

        ResultActions perform = this.mockMvc
                .perform(post("/customers").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO))
                );
        perform.andExpect(status().isCreated());
        perform.andExpect(jsonPath("$.firstName",is(customerDTO.getFirstName())));
        perform.andExpect(jsonPath("$.lastName",is(customerDTO.getLastName())));

    }

    @Test
    @DisplayName("Should return given customers")
    void testGetCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(givenCustomers);
        this.mockMvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(givenCustomers.size())));
    }

    @Test
    @DisplayName("Should return given customer")
    void testGetCustomerById() throws Exception {
        CustomerDTO customerDTO=givenCustomers.get(0);
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        this.mockMvc.perform(get("/customers/{id}",1L).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(customerDTO.getFirstName())));
    }
    @Test
    @DisplayName("Should return Error when requesting a customer by id")
    void testCustomerById() throws Exception {
        CustomerDTO customerDTO=givenCustomers.get(0);
        when(customerService.getCustomerById(2L)).thenThrow(new RuntimeException("customer not found"));
        this.mockMvc.perform(get("/customers/{id}",2L).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(content().bytes("customer not found".getBytes()));
    }
    @Test
    @DisplayName("should delete a given customer")
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer=givenCustomers.get(0);
        doNothing().when(customerService).deleteCustomer(anyLong());
        this.mockMvc.perform(delete("/customers/{id}",customer.getId()))
                .andExpect(status().isNoContent());
    }
    @Test
    @DisplayName("should return internal error when delete a given customer")
    void testDeleteCustomerWithError() throws Exception {
        Long customerId=3L;
        String errorMessage=String.format("Customer %d Not found",customerId);
        doThrow(new RuntimeException(errorMessage)).when(customerService).deleteCustomer(customerId);
        this.mockMvc.perform(delete("/customers/{id}",customerId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().bytes(errorMessage.getBytes()));
    }
}
