package homework.data;

import homework.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcPhoneBookRepository implements BasicRepository<PhoneNumber>,PhonebookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcPhoneBookRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PhoneNumber add(PhoneNumber phonenumber) {
        jdbcTemplate.update(
                "insert into Phonebook (id,owner_id,number) values (?,?,?)",
                phonenumber.getId(),
                phonenumber.getOwner_id(),
                phonenumber.getNumber()
        );
        return phonenumber;
    }

    @Override
    public PhoneNumber update(PhoneNumber phonenumber) {
        int i = jdbcTemplate.update(
                "update Phonebook set owner_id=?,number=? where id=?",
                phonenumber.getOwner_id(),
                phonenumber.getNumber(),
                phonenumber.getId()
        );
        return (i == 0 ? null: phonenumber);
    }

    @Override
    public PhoneNumber delete(PhoneNumber phonenumber) {
        int i = jdbcTemplate.update(
                "delete from Phonebook where id=? and owner_id=? and number=?",
                phonenumber.getId(),
                phonenumber.getOwner_id(),
                phonenumber.getNumber()
        );
        return (i == 0 ? null: phonenumber);
    }

    @Override
    public PhoneNumber findById(long id) {
        return jdbcTemplate.queryForObject(
                "select * from Phonebook where id=?",
                this::mapRowToPhonebook,
                id);
    }

    @Override
    public Iterable<PhoneNumber> findByOwnerId(long id) {
        return jdbcTemplate.query("select * from Phonebook where owner_id=?",
                this::mapRowToPhonebook,
                id);
    }

    @Override
    public Iterable<PhoneNumber> findByNumber(String number){
        return jdbcTemplate.query(
                "select * from Phonebook where number like ?",
                this::mapRowToPhonebook,
                "%"+number+"%");
    }

    @Override
    public Iterable<PhoneNumber> findAll() {
        return jdbcTemplate.query(
                "select * from Phonebook",
                this::mapRowToPhonebook);
    }

    private PhoneNumber mapRowToPhonebook(ResultSet resultSet, long rowNums) throws SQLException {
        System.out.println();
        return new PhoneNumber(resultSet.getLong("id"),resultSet.getLong("owner_id"),
                resultSet.getString("number"));

    }
}
