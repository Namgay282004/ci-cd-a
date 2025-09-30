package sg.edu.nus.iss.cicddemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import sg.edu.nus.iss.cicddemo.Controller.VulnerableController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VulnerableController.class)
class VulnerableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/demo/user/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("User data for ID: 123"));
    }

    @Test
    void testGetConfig() throws Exception {
        mockMvc.perform(get("/demo/config"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Database password length:")));
    }

    @Test
    void testHashPassword() throws Exception {
        mockMvc.perform(post("/demo/hash")
                .contentType("application/json")
                .content("testpassword"))
                .andExpect(status().isOk());
    }

    @Test
    void testSystemInfo() throws Exception {
        mockMvc.perform(get("/demo/info"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("System Properties:")));
    }

    @Test
    void testRiskyOperation() throws Exception {
        mockMvc.perform(get("/demo/risky"))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation completed"));
    }

    @Test
    void testExecuteCommand() throws Exception {
        mockMvc.perform(get("/demo/system?command=test"))
                .andExpect(status().isOk());
    }

    @Test
    void testReadFile() throws Exception {
        mockMvc.perform(get("/demo/file?fileName=test.txt"))
                .andExpect(status().isOk());
    }
}
