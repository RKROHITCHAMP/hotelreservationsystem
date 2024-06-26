import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormalUser extends User {

    public NormalUser(int userId, String username, String password) {
        super(userId, username, password, "User");
    }

    public void makeReservation(int roomId, Date checkInDate, Date checkOutDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {


            // Check if the room is available for the specified date range
            String checkQuery = "SELECT COUNT(*) FROM Reservations WHERE room_id = ? AND status='accepted' AND ((check_in_date <= ? AND check_out_date >= ?) OR (check_in_date <= ? AND check_out_date >= ?))";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, roomId);
            checkStmt.setDate(2, checkOutDate);
            checkStmt.setDate(3, checkInDate);
            checkStmt.setDate(4, checkInDate);
            checkStmt.setDate(5, checkOutDate);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Room is not available for booking in the specified date range.");
            } else {
                // Reserve the room
                String insertQuery = "INSERT INTO Reservations (user_id, room_id, check_in_date, check_out_date,status) VALUES (?, ?, ?, ?,'pending')";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery, new String[]{"reservation_id"});
                insertStmt.setInt(1, this.userId);
                insertStmt.setInt(2, roomId);
                insertStmt.setDate(3, checkInDate);
                insertStmt.setDate(4, checkOutDate);
                insertStmt.executeUpdate();

                // Retrieve the generated reservation_id
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int reservationId = generatedKeys.getInt(1);
                    System.out.println("Reservation made successfully. Your reservation ID is " + reservationId + ".");
                } else {
                    System.out.println("Reservation was made but could not retrieve the reservation ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void viewAllRooms() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query to get all rooms
            String query = "SELECT room_id, room_type FROM Rooms";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Display all rooms
            System.out.println("All Rooms:");
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String roomType = rs.getString("room_type");
                System.out.println("Room ID: " + roomId + ", Room Type: " + roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkReservationStatus(int reservationId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Reservations WHERE reservation_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("User ID: " + rs.getInt("user_id"));
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Check-in Date: " + rs.getDate("check_in_date"));
                System.out.println("Check-out Date: " + rs.getDate("check_out_date"));
                System.out.println("Status: " + rs.getString("status"));
            } else {
                System.out.println("No reservation found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
 }
}

    public void checkout(int reservationId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update reservation status to 'Checked Out'
            String updateReservationQuery = "UPDATE Reservations SET status = 'checkedOut' WHERE reservation_id = ?";
            PreparedStatement updateReservationStmt = conn.prepareStatement(updateReservationQuery);
            updateReservationStmt.setInt(1, reservationId);
            int rowsUpdated = updateReservationStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Checked out successfully. Room is now available.");
            } else {
                System.out.println("No reservation found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showMenu() {
        System.out.println("User Menu:");
        System.out.println("1. Make a Reservation");
        System.out.println("2. Check Reservation Status");
        System.out.println("3. Logout");
        // Implement the menu options to call respective methods
    }
}
