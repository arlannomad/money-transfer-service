package kz.almaty.moneytransferservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.almaty.moneytransferservice.dto.TransactionDto;
import kz.almaty.moneytransferservice.enums.Status;
import kz.almaty.moneytransferservice.enums.TransactionType;
import kz.almaty.moneytransferservice.service.impl.TransactionServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
@Testcontainers
class TransactionControllerTestIT {

    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setContext(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private TransactionServiceImpl userService;

    @Test
    void getAllTransactions() throws Exception {
        TransactionDto transactionDto1 = TransactionDto.builder()
                .transactionType(TransactionType.TRANSFER)
                .transactionAmount(BigDecimal.valueOf(100))
                .status(Status.SUCCESS)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        TransactionDto transactionDto2 = TransactionDto.builder()
                .transactionType(TransactionType.CREDIT)
                .transactionAmount(BigDecimal.valueOf(1))
                .status(Status.SUCCESS)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        List<TransactionDto> list = new ArrayList<>();
        list.add(transactionDto1);
        list.add(transactionDto2);

        String JsonRequest = objectMapper.writeValueAsString(list);

        mockMvc.perform(get("/api/transaction")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).getAll();
    }
}