package project.logManager.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.logManager.LogManagerApplication;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ContextConfiguration(classes = {
        LogManagerApplication.class
})
@Transactional
class UserControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    
    /*@Test
    void testDeleteAll() {
        MvcResult result = mockMvc.perform(
                        ("/api/process/")
                                .contentType("application/json")
                                .header("X-Request-ID", "6844461681")
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn());
    }*/



}