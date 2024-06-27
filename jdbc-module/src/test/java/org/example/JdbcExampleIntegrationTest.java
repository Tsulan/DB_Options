package org.example;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class JdbcExampleIntegrationTest {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @BeforeClass
    public static void setUpClass() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT, name VARCHAR(255), email VARCHAR(255), PRIMARY KEY (id))";
            stmt.execute(createTableSql);
        }
    }

    @Before
    public void setUp() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE users");
        } catch (SQLException e) {
            // If the table doesn't exist, create it
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                String createTableSql = "CREATE TABLE users (id INT AUTO_INCREMENT, name VARCHAR(255), email VARCHAR(255), PRIMARY KEY (id))";
                stmt.execute(createTableSql);
            }
        }
    }

    @Test
    public void testAddUser() {
        JdbcExample.addUser("TestName1", "test1@example.com");

        String sql = "SELECT * FROM users WHERE name = 'TestName1' AND email = 'test1@example.com'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next()); // Ensure that we have a result
            assertThat("User name doesn't match", rs.getString("name"), is("TestName1"));
            assertThat("User email doesn't match", rs.getString("email"), is("test1@example.com"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException occurred");
        }
    }

    @Test
    public void testUpdateUser() {
        JdbcExample.addUser("TestName", "test@example.com");
        JdbcExample.updateUser(1, "UpdatedName", "updated@example.com");

        String sql = "SELECT * FROM users WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next());
            assertThat("User name is not updated", rs.getString("name"), is("UpdatedName"));
            assertThat("User email is not updated", rs.getString("email"), is("updated@example.com"));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException occurred");
        }
    }

    @Test
    public void testDeleteUser() {
        JdbcExample.addUser("TestName", "test@example.com");
        JdbcExample.deleteUser(1);

        String sql = "SELECT * FROM users WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertFalse(rs.next()); // Ensure that we have no result
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException occurred");
        }
    }

    @Test
    public void testGetAllUsers() {
        JdbcExample.addUser("TestName1", "test1@example.com");
        JdbcExample.addUser("TestName2", "test2@example.com");

        String sql = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                count++;
            }
            assertThat("Number of all users don't match", count, is(2));
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException occurred");
        }
    }
}