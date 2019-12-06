package homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setup() throws Exception{
        //Add some users to work with
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,"Ivan"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(2,"Petr"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(3,"Pavel"))))
                .andExpect(status().isOk());
    }

    @After
    public void clearAll() throws Exception{
        //Get all users to string
        MvcResult result = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //And clear the user list
        String content = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(content);
        for(int i =0; i< jsonArray.length();i++){
            mockMvc.perform(delete("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8")
                    .content(jsonArray.getString(i)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }

    @Test
    public void addValidUserTest() throws Exception{
        //Add valid user
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(4,"Egor"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Check user list after adding user
        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Ivan\"}," +
                        "{\"id\":2,\"name\":\"Petr\"}," +
                        "{\"id\":3,\"name\":\"Pavel\"},"+
                        "{\"id\":4,\"name\":\"Egor\"}]"))
                .andDo(print());
    }

    @Test
    public void addInvalidUserTest() throws Exception{
        //Add user with invalid id
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(0,"Egor"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add user with invalid name
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(4,""))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add user with invalid name and id
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(-15,"12"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add 2 users
        User[] users = {new User(4,"Qwerty"), new User(5,"qwerty")};
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(users)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add not a user
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(3,1,"ERT"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateValidUserTest() throws Exception{
        //Update user
        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,"Artem"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Check user list after updating user
        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Artem\"}," +
                        "{\"id\":2,\"name\":\"Petr\"}" +
                        ",{\"id\":3,\"name\":\"Pavel\"}]"))
                .andDo(print());
    }

    @Test
    public void updateInvalidUserTest() throws Exception{
        //Update to invalid user
        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,""))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Update not existing(add new) user
        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(4,"Egor"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Update not a user
        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(3,1,"ERT"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    public void deleteValidUserTest() throws Exception{
        //Delete existing user
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(2,"Petr"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Check user list
        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Ivan\"}," +
                        "{\"id\":3,\"name\":\"Pavel\"}]"))
                .andDo(print());
    }

    @Test
    public void deleteInvalidUserTest() throws Exception{
        //Delete not-existing user
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(17,"Egor"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete user with invalid id
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(-12,"Egor"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete user with valid id and invalid name
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(3,"ERT"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete not a user
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(3,1,"ERT"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void getAllUsersTest() throws Exception{
        //Get user list
        mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Ivan\"}," +
                        "{\"id\":2,\"name\":\"Petr\"}," +
                        "{\"id\":3,\"name\":\"Pavel\"}]"))
                .andDo(print());
    }

    @Test
    public void getValidUserByIdTest() throws Exception{
        //Valid get user by id
        mockMvc.perform(get("/user/2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":2,\"name\":\"Petr\"}"))
                .andDo(print());
    }

    @Test
    public void getInvalidUserByIdTest() throws Exception{
        //Get not existing user
        mockMvc.perform(get("/user/1235")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
        //Get user with negative id
        mockMvc.perform(get("/user/-13")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getValidUserByNameTest() throws Exception{
        //Valid get user by full name
        mockMvc.perform(get("/user/?name=Petr")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":2,\"name\":\"Petr\"}]"))
                .andDo(print());
        //Valid get users by name part
        mockMvc.perform(get("/user/?name=P")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":2,\"name\":\"Petr\"}," +
                        "{\"id\":3,\"name\":\"Pavel\"}]"))
                .andDo(print());


    }

    @Test
    public void getInvalidUserByNameTest() throws Exception{
        //Get user by empty name
        mockMvc.perform(get("/user/?name=")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Ivan\"}," +
                        "{\"id\":2,\"name\":\"Petr\"}," +
                        "{\"id\":3,\"name\":\"Pavel\"}]"))
                .andDo(print());
        //Get user by name which is not in user list
        mockMvc.perform(get("/user/?name=QWERTY")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }



}
