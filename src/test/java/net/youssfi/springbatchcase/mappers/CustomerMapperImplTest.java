package net.youssfi.springbatchcase.mappers;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import net.youssfi.springbatchcase.entities.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(MockitoExtension.class)
class CustomerMapperImplTest {
    @InjectMocks
    private CustomerMapperImpl underTest;
    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer=Customer.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .email("a@gmail.com")
                .dob(Date.from(LocalDate.of(2000,11,23).atStartOfDay().toInstant(ZoneOffset.UTC)))
                .gender(Gender.MALE)
                .phoneNumber("+2126543212")
                .build();
        customerDTO=CustomerDTO.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .email("a@gmail.com")
                .dob("23-11-2000")
                .gender("MALE")
                .contactNo("+2126543212")
                .build();
    }

    @Test
    void testMapCustomerToCustomerDTO(){
        Customer expected=customer;
        Customer customerResult = underTest.from(customerDTO);
        assertThat(customerResult.getId()).isEqualTo(customer.getId());
        assertThat(customerResult.getGender().toString()).isEqualTo(customerDTO.getGender());
        LocalDate localDate=customerResult.getDob().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        String dateStr=String.format("%02d",localDate.getDayOfMonth());
        dateStr+="-"+String.format("%02d",localDate.getMonthValue());
        dateStr+="-"+localDate.getYear();
        assertThat(dateStr).isEqualTo(customerDTO.getDob());
        assertThat(customerResult.getPhoneNumber()).isEqualTo(customerDTO.getContactNo());
    }
    @Test
    void testMapCustomerDTOToCustomer(){
        CustomerDTO expected=customerDTO;
        CustomerDTO customerDTOResult = underTest.from(customer);
        assertThat(customerDTOResult.getId()).isEqualTo(customer.getId());
        assertThat(customerDTOResult.getGender().toString()).isEqualTo(customer.getGender().toString());
        assertThat(customerDTOResult.getContactNo()).isEqualTo(customer.getPhoneNumber());
        LocalDate localDate=customer.getDob().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        String dateStr=String.format("%02d",localDate.getDayOfMonth());
        dateStr+="-"+String.format("%02d",localDate.getMonthValue());
        dateStr+="-"+localDate.getYear();
        assertThat(dateStr).isEqualTo(customerDTOResult.getDob());

    }
}