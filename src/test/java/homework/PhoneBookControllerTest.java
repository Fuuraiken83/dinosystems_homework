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
public class PhoneBookControllerTest {

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
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(4,"Semyon"))))
                .andExpect(status().isOk());

        //Add some phones to work with
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,1,"+88001231212"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(2,2,"+88001232323"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(3,3,"+88001233434"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(4,3,"+88001234444"))))
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
    public void addValidPhoneNumberTest() throws Exception{
        //Add valid phone number
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(5,1,"+88008001234"))))
                .andExpect(status().isOk())
                .andDo(print());

        //Check phonenumber after adding
        mockMvc.perform(get("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}," +
                        "{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}," +
                        "{\"id\":5,\"owner_id\":1,\"number\":\"+88008001234\"}]"))
                .andDo(print());

    }

    @Test
    public void addInvalidPhoneNumberTest() throws Exception{
        PhoneNumber[] phoneNumbers = {new PhoneNumber(5,1,"+88008001234"),
                new PhoneNumber(6,1,"+88008001234")};
        //Add phonenumber with invalid id
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(-12,1,"+88008001234"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add phonenumber with invalid number
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(8,1,"+98008001234"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add not a phone number
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(6,"Pavlin"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Add 2 numbers
        mockMvc.perform(post("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(phoneNumbers)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateValidPhoneNumberTest() throws Exception{
        //Update phone number
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,1,"+78008001234"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Change owner
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,3,"+78008001234"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Check phone book after updating number
        mockMvc.perform(get("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":3,\"number\":\"+78008001234\"}," +
                        "{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());
    }

    @Test
    public void updateInvalidPhoneNumberTest() throws Exception{
        //Update to invalid number
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,1,"+98008001234"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Update not existing(add new) number
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(8,1,"+88008001111"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Set owner id to not existing user id
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(9,11,"+88008001111"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Update not phone number
        mockMvc.perform(put("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,"Ivan"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void deleteValidPhoneNumberTest() throws Exception{
        //Delete existing phone number
        mockMvc.perform(delete("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,1,"+88001231212"))))
                .andExpect(status().isOk())
                .andDo(print());
        //Check phonebook
        mockMvc.perform(get("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());
    }

    @Test
    public void deleteInvalidPhoneNumberTest() throws Exception{
        //Delete not-existing phone number
        mockMvc.perform(delete("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(10,1,"+81231231212"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete number with errors
        mockMvc.perform(delete("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(-10,-1,"+18001231212"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete user with valid id,owner_id and invalid number
        mockMvc.perform(delete("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new PhoneNumber(1,1,"+88011231212"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        //Delete not a number
        mockMvc.perform(delete("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,"Ivan"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void deleteUserTest() throws Exception{
        //delete user
        mockMvc.perform(delete("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(new User(1,"Ivan"))))
                .andExpect(status().isOk())
                .andDo(print());
        //check phonebook of deleted user
        mockMvc.perform(get("/user/1/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getPhonebookTest() throws Exception{
        //Get all numbers from phonebook
        mockMvc.perform(get("/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}," +
                        "{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());
    }

    @Test
    public void getValidPhonebookByIdTest() throws Exception{
        //Get number by id
        mockMvc.perform(get("/phonebook/1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}"))
                .andDo(print());
    }

    @Test
    public void getInvalidPhonebookByIdTest() throws Exception{
        //Get number by not existing id
        mockMvc.perform(get("/phonebook/134")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
        //Get number by invalid id
        mockMvc.perform(get("/phonebook/-134")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getValidPhonebookByOwnerIdTest() throws Exception{
        //Get numbers by owner id
        mockMvc.perform(get("/user/3/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());
    }

    @Test
    public void getInvalidPhonebookByOwnerIdTest() throws Exception{
        //Get numbers by not existing owner id
        mockMvc.perform(get("/user/1567/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
        //Get numbers by invalid owner id
        mockMvc.perform(get("/user/-1567/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
        //Get numbers by owner with no numbers in his phonebook
        mockMvc.perform(get("/user/4/phonebook")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getValidPhonebookByNumberTest() throws Exception{
        //Get numbers by full number
        mockMvc.perform(get("/phonebook/?number=+88001231212")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}]"))
                .andDo(print());
        //Get numbers by number part
        mockMvc.perform(get("/phonebook/?number=+8")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}," +
                        "{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());
    }
    @Test
    public void getInvalidPhonebookByNumberTest() throws Exception{
        //Get numbers by empty
        mockMvc.perform(get("/phonebook/?number=")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"owner_id\":1,\"number\":\"+88001231212\"}," +
                        "{\"id\":2,\"owner_id\":2,\"number\":\"+88001232323\"}," +
                        "{\"id\":3,\"owner_id\":3,\"number\":\"+88001233434\"}," +
                        "{\"id\":4,\"owner_id\":3,\"number\":\"+88001234444\"}]"))
                .andDo(print());

        //Get numbers by not existing number
        mockMvc.perform(get("/phonebook/?number=qwerty")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
