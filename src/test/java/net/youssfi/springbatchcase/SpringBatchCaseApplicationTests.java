package net.youssfi.springbatchcase;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//@SpringBootTest
class SpringBatchCaseApplicationTests {

    Calculator underTest=new Calculator();
    @Test
    void shouldAddNumbers() {
        // Given
        int a=20;
        int b=31;
        // When
        int result= underTest.add(a,b);
        // Then
        int expected=51;
        assertThat(result).isEqualTo(expected);
    }

    class Calculator {
          int add(int a , int b) {
              return a + b;
          }
    }

}
