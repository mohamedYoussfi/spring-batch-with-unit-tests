package net.youssfi.springbatchcase.services;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import net.youssfi.springbatchcase.mappers.CustomerMapper;
import net.youssfi.springbatchcase.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    //private AutoCloseable autoCloseable;
    @InjectMocks
    private CustomerServiceImpl underTest;
    private List<CustomerDTO> givenCustomersDTO;
    private List<Customer> givenCustomers;
    @Mock
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        //autoCloseable = MockitoAnnotations.openMocks(this);
        //underTest=new CustomerServiceImpl(customerRepository);
        // Given
        givenCustomersDTO=List.of(
                CustomerDTO.builder().id(1L).firstName("A").lastName("AA").email("a@gmail.com").build(),
                CustomerDTO.builder().id(2L).firstName("B").lastName("BB").email("b@gmail.com").build()
        );
        givenCustomers=List.of(
                Customer.builder().id(1L).firstName("A").lastName("AA").email("a@gmail.com").build(),
                Customer.builder().id(2L).firstName("B").lastName("BB").email("b@gmail.com").build()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        //autoCloseable.close();
    }
    @Test
    void saveCustomer() {
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .email("med@youssfi.net")
                .build();
        Customer customer=Customer.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .email("med@youssfi.net")
                .build();
        when(customerMapper.from(any(CustomerDTO.class))).thenReturn(customer);
        when(customerMapper.from(any(Customer.class))).thenReturn(customerDTO);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO savedCustomer = underTest.saveCustomer(customerDTO);
        ArgumentCaptor<Customer> argumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(argumentCaptor.capture());
        Customer capturedCustomer = argumentCaptor.getValue();
        assertThat(capturedCustomer).isEqualTo(customer);
        assertThat(savedCustomer).isEqualTo(customerDTO);
    }

    @Test
    void saveCustomerEmailTaking() {
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .email("med@youssfi.net")
                .build();
        given(customerRepository.verifyIfEmailExists(anyString())).willReturn(true);
        assertThatThrownBy(()->underTest.saveCustomer(customerDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("This email already exists");
         verify(customerRepository,never()).save(any());
    }

    @Test
    void saveCustomerWithEmailNull() {
        CustomerDTO customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .email(null)
                .build();
        assertThatThrownBy(()->underTest.saveCustomer(customerDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email is required");
    }

    @Test
    void getAllCustomers() {
        when(customerRepository.findAll()).thenReturn(givenCustomers);
        for(int i=0;i<givenCustomers.size();i++){
            when(customerMapper.from(givenCustomers.get(i))).thenReturn(givenCustomersDTO.get(i));
        }
        // When
        List<CustomerDTO> allCustomers = underTest.getAllCustomers();
        System.out.println(allCustomers);
        System.out.println(givenCustomersDTO);
        // Then
        verify(customerRepository,timeout(1)).findAll();
        for(int i=0;i<allCustomers.size();i++)
           assertThat(allCustomers.get(i)).isEqualTo(givenCustomersDTO.get(i));
    }
    @Test
    @DisplayName("Should return a customer by id")
    void testCustomerById(){
        CustomerDTO customerDTO=givenCustomersDTO.get(0);
        Customer customer=givenCustomers.get(0);
        when(customerMapper.from(any(Customer.class))).thenReturn(customerDTO);
        Long customerId=customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        CustomerDTO result = underTest.getCustomerById(customerId);
        assertThat(result).isEqualTo(customerDTO);
    }
    @Test
    @DisplayName("Should throw exception when searching customer by id")
    void testCustomerByIdThrowingException(){
        Long customerId=2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.getCustomerById(customerId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Not found");
    }
    @Test
    @DisplayName("Should delete a given customer")
    void testDeleteCustomer(){
        Customer customer=givenCustomers.get(0);
        Long customerId=customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(customerId);
        underTest.deleteCustomer(customerId);
        verify(customerRepository,times(1)).deleteById(customerId);
    }
    @Test
    @DisplayName("Should throw exception when deleting customer by id")
    void testDeleteCustomerThrowingException(){
        Long customerId=2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.deleteCustomer(customerId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer Not found");
    }
}