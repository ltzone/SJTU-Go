package org.sjtugo.api.UserTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PreferenceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetPreferenceNormal() throws Exception{
        this.mockMvc.perform(post("/user/preference/get")
                .param("userID","123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("preferencelist")))
                .andExpect(content().string(containsString("banlist")));
    }

    @Test
    public void testGetPreferenceNull() throws Exception {
        this.mockMvc.perform(post("/user/preference/get")
                .param("userID", "123333"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
