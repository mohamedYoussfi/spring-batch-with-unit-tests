package net.youssfi.springbatchcase.config;

import net.youssfi.springbatchcase.dto.CustomerDTO;
import net.youssfi.springbatchcase.entities.Customer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CustomItemReader implements ItemReader<CustomerDTO> {
    String fileName;
    int linesToSkip;
    String delimiter;
    BufferedReader bufferedReader;
    String[] comments = new String[0];
    boolean finished = false;

    CustomItemReader(String fineName, int linesToSkip, String delimiter, String[] comments) throws IOException {
        this.fileName = fineName;
        this.linesToSkip = linesToSkip;
        this.delimiter = delimiter;
        this.comments = comments;
    }

    public CustomItemReader() {
    }

    @Override
    public CustomerDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(bufferedReader==null){
            Path path = Paths.get(new ClassPathResource(fileName).getFile().getPath());
            bufferedReader = Files.newBufferedReader(path);
            for (int i = 0; i < linesToSkip; i++) {
                bufferedReader.readLine();
            }
        }
        CustomerDTO customer = null;
        String line = null;
        boolean readNextLine = true;
        while (readNextLine == true) {
            line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                return null;
            }
            for (String comment : comments) {
                if (line.startsWith(comment)) {
                    readNextLine = true;
                    break;
                } else {
                    readNextLine = false;
                }
            }
        }

        String[] items = line.split(delimiter);
        System.out.println(Arrays.asList(items));
        customer = new CustomerDTO();
        customer.setId(Long.parseLong(items[0]));
        customer.setFirstName(items[1]);
        customer.setLastName(items[2]);
        //email,gender,contactNo,country,dob
        customer.setEmail(items[3]);
        customer.setGender(items[4]);
        customer.setContactNo(items[5]);
        customer.setCountry(items[6]);
        customer.setDob(items[7]);
        return customer;
    }

    public static class Builder{
        private CustomItemReader customItemReader=new CustomItemReader();

        public Builder filename(String fileName){
            this.customItemReader.fileName=fileName;
            return this;
        }
        public Builder linesToSkip(int linesToSkip){
            this.customItemReader.linesToSkip=linesToSkip;
            return this;
        }
        public Builder comments(String... comments){
            this.customItemReader.comments=comments;
            return this;
        }
        public Builder delimiter(String delimiter){
            this.customItemReader.delimiter=delimiter;
            return this;
        }
        public CustomItemReader build(){
            return this.customItemReader;
        }
    }
}
