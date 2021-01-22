package ru.madrabit.webscrapper_spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LauncherTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void toDoTest() throws Exception {
        this.mvc
                .perform(get("/api/launcher/A_1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getStatus() throws Exception {
        this.mvc
                .perform(get("/api/status"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
