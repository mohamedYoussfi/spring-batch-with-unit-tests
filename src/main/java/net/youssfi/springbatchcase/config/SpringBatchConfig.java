package net.youssfi.springbatchcase.config;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import net.youssfi.springbatchcase.mappers.CustomerMapper;
import net.youssfi.springbatchcase.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
public class SpringBatchConfig {
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public SpringBatchConfig(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("job1",jobRepository)
                .start(step)
                //.flow(step).end()
                .incrementer(new RunIdIncrementer())
                .build();
     }
    @Bean
    public Step Step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws IOException {
        return new StepBuilder("step1", jobRepository)
                .<CustomerDTO,Customer>chunk(5,platformTransactionManager)
                //.reader(reader())
                //.reader(new CustomItemReader("customers.csv",1,",",new String[]{"//","..","#"}))
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .transactionManager(platformTransactionManager)
                .build();
    }
    @Bean
    public ItemReader<CustomerDTO> reader(){
        return new FlatFileItemReaderBuilder<CustomerDTO>()
                .name("csvReader")
                .resource(new ClassPathResource("customers.csv"))
                .comments("..","//","#")
                .delimited()
                .names("id","firstName","lastName","email","gender","contactNo","country","dob")
                .linesToSkip(1)
                .targetType(CustomerDTO.class)
                .build();

    }
    public ItemReader<CustomerDTO> customerItemReader(){
        return new CustomItemReader.Builder()
                .filename("customers.csv")
                .linesToSkip(1)
                .comments("..","#","//")
                .delimiter(",")
                .build();
    }
    @Bean
    public ItemWriter<Customer> writer(){
        return chunk -> customerRepository.saveAll(chunk.getItems());
        /*
        return new RepositoryItemWriterBuilder<Customer>()
                .repository(customerRepository)
                .methodName("save")
                .build();
         */
    }
    @Bean
    public ItemProcessor<CustomerDTO,Customer> processor(){
        return customerDTO -> {
            return customerMapper.from(customerDTO);
        };
    }
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor=new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}

