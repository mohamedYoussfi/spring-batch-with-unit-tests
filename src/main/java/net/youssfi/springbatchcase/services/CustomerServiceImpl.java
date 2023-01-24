package net.youssfi.springbatchcase.services;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import net.youssfi.springbatchcase.mappers.CustomerMapper;
import net.youssfi.springbatchcase.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO){
        if (customerDTO.getEmail()==null)
            throw new RuntimeException("Email is required");
        Boolean emailExists = customerRepository.verifyIfEmailExists(customerDTO.getEmail());
        if(emailExists) throw new RuntimeException(String.format("This email already exists : %s",customerDTO.getEmail()));
        Customer customer = customerRepository.save(customerMapper.from(customerDTO));
        return customerMapper.from(customer);
    }
    @Override
    public List<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll().stream().map(customerMapper::from).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer==null)
         throw new RuntimeException("Customer Not found");
        return customerMapper.from(customer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new RuntimeException("Customer Not found");
        customerRepository.deleteById(customerId);
    }
}
