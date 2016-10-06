package lesson.controller;

import com.google.gson.Gson;
import lesson.App;
import lesson.request.TaskArguments;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@Transactional
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGet() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/task/?id=1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testGetList() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/task/getByLesson?id=1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreate() throws Exception {
        TaskArguments task = new TaskArguments();
        task.name ="Test Name 3";
        task.lessonID = 1L;
        task.description = "desc";

        Gson gson = new Gson();
        String json = gson.toJson(task);

        MvcResult result = this.mockMvc.perform(post("/task/add")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateByUser() throws Exception {
        TaskArguments task = new TaskArguments();
        task.name ="Test Error Create";
        task.lessonID = 1L;
        Gson gson = new Gson();
        String json = gson.toJson(task);

        MvcResult result = this.mockMvc.perform(post("/task/create")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is5xxServerError())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}