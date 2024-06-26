import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(int userId, String username, String password) {
        super(userId, username, password, "Admin");
    }

    public void viewAllBookings() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Reservations";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("User ID: " + rs.getInt("user_id"));
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Check-in Date: " + rs.getDate("check_in_date"));
                System.out.println("Check-out Date: " + rs.getDate("check_out_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("-----");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateReservationStatus(int reservationId, String status) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Reservations SET status = ? WHERE reservation_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            stmt.executeUpdate();
            System.out.println("Reservation status updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
      }
}

    public void viewReservationRequests() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Reservations WHERE status = 'pending'";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("User ID: " + rs.getInt("user_id"));
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Check-in Date: " + rs.getDate("check_in_date"));
                System.out.println("Check-out Date: " + rs.getDate("check_out_date"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("-----");
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
}
    public void addRoom(String roomType) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Rooms (room_type, is_reserved) VALUES (?, 'N')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomType);
            stmt.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(int reservationId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Reservations WHERE reservation_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();

            // Optionally, set the room as not reserved
            String updateRoomQuery = "UPDATE Rooms SET is_reserved = 'N' WHERE room_id = (SELECT room_id FROM Reservations WHERE reservation_id = ?)";
            PreparedStatement updateStmt = conn.prepareStatement(updateRoomQuery);
            updateStmt.setInt(1, reservationId);
            updateStmt.executeUpdate();

            System.out.println("Booking canceled successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMenu() {
        System.out.println("Admin Menu:");
        System.out.println("1. View All Bookings");
        System.out.println("2. Add New Room");
        System.out.println("3. Cancel a Booking");
        System.out.println("4. View Pending Reservation Requests");
        System.out.println("5. Accept/Reject Reservation Request");
        System.out.println("6. Logout");
      }
}
