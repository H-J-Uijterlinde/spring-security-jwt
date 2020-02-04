package com.semafoor.as.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class SampleControllerTest {

    private MockMvc mockMvc;
    private SampleController sampleController;

    @BeforeEach
    public void setUp() {
        sampleController = new SampleController();
        this.mockMvc = MockMvcBuilders.standaloneSetup(sampleController).build();
    }

    @Test
    public void getExampleMessage() throws Exception{

        MockHttpServletResponse response = mockMvc.perform(
                get("/examples/1")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("An Example Message");
    }
}
