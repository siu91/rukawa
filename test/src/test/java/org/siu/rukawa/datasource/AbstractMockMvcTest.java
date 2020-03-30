package org.siu.rukawa.datasource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("rukawa-test")
@SpringBootTest(classes = RukawaTestApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractMockMvcTest {

    @Autowired
    private WebApplicationContext webApplicationContext;


    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }


    public ExpectedException getExpectedException() {
        return expectedException;
    }
}
