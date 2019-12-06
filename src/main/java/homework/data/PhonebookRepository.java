package homework.data;

import homework.PhoneNumber;

public interface PhonebookRepository {
    Iterable<PhoneNumber> findByOwnerId(long id);
    Iterable<PhoneNumber> findByNumber(String number);
}
