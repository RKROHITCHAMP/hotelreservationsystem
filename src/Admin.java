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
    public void addRoom(String roomType, int price) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Rooms (room_type, price) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomType);
            stmt.setInt(2, price);
            stmt.executeUpdate();
            System.out.println("Room added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void cancelBooking(int reservationId) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String checkQuery = "SELECT COUNT(*) FROM Reservations WHERE reservation_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, reservationId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {

                String deleteQuery = "DELETE FROM Reservations WHERE reservation_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, reservationId);
                deleteStmt.executeUpdate();
                System.out.println("Booking canceled successfully.");
            } else {

                System.out.println("The given reservation ID does not exist.");
            }
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
