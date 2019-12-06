package homework;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class PhoneNumber {
    @Positive
    private long id;
    @Positive
    private long owner_id;
    //Russian phone number pattern
    @Pattern(regexp ="^[+][8|7][0-9]{10}$")
    private String number;
}
