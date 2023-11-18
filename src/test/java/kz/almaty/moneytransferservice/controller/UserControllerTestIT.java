package kz.almaty.moneytransferservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.almaty.moneytransferservice.dto.CreditDebitRequest;
import kz.almaty.moneytransferservice.dto.TransferRequest;
import kz.almaty.moneytransferservice.dto.UserDto;
import kz.almaty.moneytransferservice.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Testcontainers
class UserControllerTestIT {

    @Autowired
    private WebApplicationContext context;
    @Before
    public void setContext(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Container
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private static UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .accountNumber("1")
                .accountBalance(BigDecimal.valueOf(100))
                .email("email")
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .build();
    }

    @Test
    public void testAddAccount() throws Exception {

        String JsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/user")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).addAccount(userDto);
    }

    @Test
    void findByAccountNumber() throws Exception {
        CreditDebitRequest request = CreditDebitRequest.builder()
                .accountNumber("1")
                .amount(BigDecimal.valueOf(100))
                .build();

        String JsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(get("/api/user/getBalanceByAccountNumber")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).findByAccountNumber(request);
    }

    @Test
    void creditSAccount() throws Exception {
        CreditDebitRequest request = CreditDebitRequest.builder()
                .accountNumber("1")
                .amount(BigDecimal.valueOf(100))
                .build();

        String JsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/user/creditAccountByAccountNumber")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).creditAccount(request);
    }

    @Test
    void debitAccount() throws Exception {
        CreditDebitRequest request = CreditDebitRequest.builder()
                .accountNumber("1")
                .amount(BigDecimal.valueOf(100))
                .build();

        String JsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/user/debitAccountByAccountNumber")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).debitAccount(request);
    }

    @Test
    void transfer() throws Exception {
        TransferRequest request = TransferRequest.builder()
                .fromAccount("1")
                .toAccount("2")
                .transferAmount(BigDecimal.valueOf(100))
                .build();

        String JsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/user/transferByAccountNumbers")
                        .content(JsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService).transfer(request);
    }

//    @Test
//    void getAllUsers() throws Exception {
//
//        UserDto userDto1 = UserDto.builder()
//                .firstName("1")
//                .lastName("1")
//                .email("1")
//                .build();
//
//        UserDto userDto2 = UserDto.builder()
//                .firstName("1")
//                .lastName("1")
//                .email("1")
//                .build();
//
//        List<UserDto> list = new ArrayList<>();
//        list.add(userDto1);
//        list.add(userDto2);
//
//        String JsonRequest = objectMapper.writeValueAsString(list);
//
//        mockMvc.perform(get("/api/user/getAllAccounts")
//                        .content(JsonRequest)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        Mockito.verify(userService).getAll();
//    }

}
