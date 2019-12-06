package homework.data;

import homework.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcUserRepository implements BasicRepository<User>,UserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        jdbcTemplate.update(
                "insert into Users (id,name) values (?,?)",
                user.getId(),
                user.getName()
        );
        return user;
    }

    @Override
    public User update(User user) {
        int i = jdbcTemplate.update(
                "update Users set name=? where id=?",
                user.getName(),
                user.getId()
        );
        return (i == 0 ? null : user);
    }

    @Override
    public User delete(User user) {
        int i = jdbcTemplate.update(
                "delete from Users where id=? and name=?",
                user.getId(),
                user.getName());
        return (i == 0 ? null : user);
    }

    @Override
    public User findById(long id) {
        return jdbcTemplate.queryForObject(
                "select * from Users where id=?",
                this::mapRowToUser,
                id);
    }

    @Override
    public Iterable<User> findByName(String name) {
        return jdbcTemplate.query(
                "select * from Users where name like ?",
                this::mapRowToUser,
                "%"+name+"%");
    }

    @Override
    public Iterable<User> findAll() {
        Iterable<User> result =  jdbcTemplate.query(
                "select * from Users",
                this::mapRowToUser);

        return result;
    }

    private User mapRowToUser(ResultSet resultSet,long rowNums) throws SQLException {
        return new User(resultSet.getLong("id"),resultSet.getString("name"));
    }

}
