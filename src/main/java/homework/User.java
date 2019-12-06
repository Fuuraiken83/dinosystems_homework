package homework;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class User {
    @Positive
    private long id;
    @Size(min=3, message = "Name is 3 characters minimum!")
    private String name;
}
