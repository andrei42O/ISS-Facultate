package persistance;

import model.JobType;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryDBUsers implements IRepositoryDBUsers {
    private final JdbcUtils databaseInfo;

    public RepositoryDBUsers(Properties databaseInfo) {
        this.databaseInfo = new JdbcUtils(databaseInfo);
    }

    @Override
    public User save(User entity) {
        Connection connection = databaseInfo.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password, job_type, name) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getJob().toString());
            ps.setString(4, entity.getName());
            int res = ps.executeUpdate();
            if(res == 1){
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setID(generatedKeys.getLong(1));
                    }
                    else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
                return entity;
            }
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
            return null;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> ret = new ArrayList<>();
        Connection connection = databaseInfo.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE job_type like 'Agent'");
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                ret.add(extractUserFromResultSet(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
        return ret;
    }

    @Override
    public User delete(User entity) {
        Connection connection = databaseInfo.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id = ?")){
            ps.setLong(1, entity.getID());
            int res = ps.executeUpdate();
            if(res == 1){
                return entity;
            }
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
            return null;
        }
        return null;
    }

    @Override
    public User update(User entity, Long id) {
        Connection connection = databaseInfo.getConnection();
        try(PreparedStatement ps = connection.prepareStatement("UPDATE users SET username = ?, name = ?, password = ?, job_type = ?  WHERE id = ?")){
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getPassword());
            ps.setString(4, entity.getJob().toString());
            ps.setLong(5, id);
            int res = ps.executeUpdate();
            if(res == 1){
                entity.setID(id);
                return entity;
            }
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
            return null;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Connection connection = databaseInfo.getConnection();
        try(PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM users WHERE username like '%s'", username));
            ResultSet rs = ps.executeQuery()){
            if(rs.next()) {
                return extractUserFromResultSet(rs);
            }
            return null;
        } catch (SQLException ex) {
            System.err.println("Error DB" + ex);
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        Long id = (long) rs.getInt("id");
        String _username = rs.getString("username");
        String password = rs.getString("password");
        String name = rs.getString("name");
        JobType job = JobType.valueOf(rs.getString("job_type"));
        User radu = new User(_username, password, name, job);
        radu.setID(id);
        return radu;
    }
}
