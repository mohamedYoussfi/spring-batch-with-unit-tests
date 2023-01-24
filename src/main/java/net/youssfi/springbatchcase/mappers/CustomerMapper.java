package net.youssfi.springbatchcase.mappers;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;

public interface CustomerMapper {

    Customer from(CustomerDTO customerDTO);

    CustomerDTO from(Customer customer);
}
