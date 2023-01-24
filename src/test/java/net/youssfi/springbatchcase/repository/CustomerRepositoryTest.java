package net.youssfi.springbatchcase.repository;

import net.youssfi.springbatchcase.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;
    @Test
    void findByEmail() {
        // Given
        String email = "med@youssfi.net";
        Customer customer=Customer.builder()
                .id(1L)
                .email(email)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .build();
        underTest.save(customer);
        // When
        Customer byEmail = underTest.findByEmail(email);
        // Then
        assertThat(byEmail).isEqualTo(customer);
    }

    @Test
    void verifyIfEmailExists() {
        // Given
        String email = "med@youssfi.net";
        Customer customer=Customer.builder()
                .id(1L)
                .email(email)
                .firstName("Mohamed")
                .lastName("Youssfi")
                .build();
        underTest.save(customer);
        // When
        Boolean result = underTest.verifyIfEmailExists(email);
        // Then
        assertThat(result).isTrue();

    }
    @Test
    void verifyIfEmailNotExists() {
        // Given
        String email = "med@youssfi.net";
        // When
        Boolean result = underTest.verifyIfEmailExists(email);
        // Then
        assertThat(result).isFalse();

    }
}