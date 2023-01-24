package net.youssfi.springbatchcase.web;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.MockitoMethod;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = JobRestController.class)
class JobRestControllerTest {
    @MockBean
    private JobLauncher jobLauncher;
    @MockBean
    private Job job;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void testJobLauncher() throws Exception {
        JobExecution jobExecution=new JobExecution(212L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class),ArgumentMatchers.any(JobParameters.class))).thenReturn(jobExecution);
        mockMvc.perform(MockMvcRequestBuilders.get("/startJob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status",is("COMPLETED")));

    }

}