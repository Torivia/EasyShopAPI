package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Profile update(Profile updatedProfile) {
        String sql = "UPDATE profiles " +
                "SET first_name = COALESCE(?, first_name), " +
                "last_name = COALESCE(?, last_name), " +
                "phone = COALESCE(?, phone), " +
                "email = COALESCE(?, email), " +
                "address = COALESCE(?, address), " +
                "city = COALESCE(?, city), " +
                "state = COALESCE(?, state), " +
                "zip = COALESCE(?, zip) " +
                "WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Set parameters for all fields, allowing nulls to retain existing values
            ps.setString(1, updatedProfile.getFirstName());
            ps.setString(2, updatedProfile.getLastName());
            ps.setString(3, updatedProfile.getPhone());
            ps.setString(4, updatedProfile.getEmail());
            ps.setString(5, updatedProfile.getAddress());
            ps.setString(6, updatedProfile.getCity());
            ps.setString(7, updatedProfile.getState());
            ps.setString(8, updatedProfile.getZip());
            ps.setInt(9, updatedProfile.getUserId());

            ps.executeUpdate();

            // Fetch and return the updated profile
            return getProfileByUserId(updatedProfile.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Profile getProfileByUserId(int userId) {
        String sql = "SELECT user_id, first_name, last_name, phone, email, address, city, state, zip FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Profile(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip")
                );
            }

            throw new RuntimeException("Profile not found for userId: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
