package net.youssfi.springbatchcase.mappers;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CustomerMapperImpl implements CustomerMapper  {
    private ModelMapper modelMapper;

    public CustomerMapperImpl(){
        modelMapper=new ModelMapper();
        TypeMap<CustomerDTO,Customer>  propertyMapperCustomerToCustomerDTO=modelMapper.createTypeMap(CustomerDTO.class,Customer.class);
        TypeMap<Customer,CustomerDTO>  propertyMapperCustomerDTOToCustomer=modelMapper.createTypeMap(Customer.class,CustomerDTO.class);
        Converter<String,Date> stringDateConverter=c->{
            LocalDate localDate = LocalDate.parse(c.getSource(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        };
        Converter<Date,String> dateStringConverter=c->{
            LocalDate localDate=c.getSource().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
            String strDate=String.format("%02d",localDate.getDayOfMonth())+"-"
                         + String.format("%02d",localDate.getMonthValue())+"-"
                         +localDate.getYear();
            return strDate;
        };
        propertyMapperCustomerToCustomerDTO.addMappings(mapper->mapper.using(stringDateConverter).map(CustomerDTO::getDob,Customer::setDob));
        propertyMapperCustomerToCustomerDTO.addMappings(mapper->mapper.map(CustomerDTO::getContactNo,Customer::setPhoneNumber));

        propertyMapperCustomerDTOToCustomer.addMappings(mapper->mapper.using(dateStringConverter).map(Customer::getDob,CustomerDTO::setDob));
        propertyMapperCustomerDTOToCustomer.addMappings(mapper->mapper.map(Customer::getPhoneNumber,CustomerDTO::setContactNo));
    }
    @Override
    public Customer from(CustomerDTO customerDTO){
      return modelMapper.map(customerDTO,Customer.class);
    }
    @Override
    public CustomerDTO from(Customer customer){
        return modelMapper.map(customer,CustomerDTO.class);
    }
}
