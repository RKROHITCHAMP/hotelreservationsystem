import java.sql.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = null;
        while(true) {
            while (true) {
                System.out.println("Welcome to the Taj Hotel");

                System.out.println("1. Login as Admin");
                System.out.println("2. Login as User");
                System.out.println("3. Register as User");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();

                if (choice == 1 || choice ==2) {
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();

                    user = User.login(username, password);

                    if (user != null) {
                        break;
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                } else if (choice == 3) {
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();

                    String userType = "User";

                    if (User.register(username, password, userType)) {
                        System.out.println("Registration successful. You can now login.");
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                } else if (choice == 4) {
                    return;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }

            while (true) {
                user.showMenu();
                int choice = scanner.nextInt();

                if (user instanceof Admin) {
                    Admin admin = (Admin) user;
                    switch (choice) {
                        case 1:
                            admin.viewAllBookings();
                            break;
                        case 2:
                            System.out.print("Enter room type to add: ");
                            String roomType = scanner.next();
                            System.out.print("Enter price of the room: ");
                            int price = scanner.nextInt();
                            admin.addRoom(roomType, price);

                            break;
                        case 3:
                            System.out.print("Enter reservation ID to cancel: ");
                            int reservationId = scanner.nextInt();
                            admin.cancelBooking(reservationId);
                            break;
                        case 4:
                            admin.viewReservationRequests();
                            break;
                        case 5:
                            System.out.print("Enter reservation ID to update: ");
                            reservationId = scanner.nextInt();
                            System.out.print("Enter status (Accepted/Rejected): ");
                            String status = scanner.next();
                            admin.updateReservationStatus(reservationId, status);
                            break;
                        case 6:
                            System.out.println("Logging out...");

                            user = null;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else if (user instanceof NormalUser) {
                    NormalUser normalUser = (NormalUser) user;
                    switch (choice) {
                        case 1:
                            normalUser.viewAllRooms();
                            System.out.print("Enter room ID to reserve: ");
                            int roomId = scanner.nextInt();
                            System.out.print("Enter check-in date (yyyy-mm-dd): ");
                            Date checkInDate = Date.valueOf(scanner.next());
                            System.out.print("Enter check-out date (yyyy-mm-dd): ");
                            Date checkOutDate = Date.valueOf(scanner.next());
                            normalUser.makeReservation(roomId, checkInDate, checkOutDate);
                            break;
                        case 2:
                            System.out.print("Enter reservation ID to check status: ");
                            int reservationId = scanner.nextInt();
                            normalUser.checkReservationStatus(reservationId);
                            break;

                        case 3:
                            System.out.println("Enter reservation ID for Check In:");
                            reservationId = scanner.nextInt();
                            normalUser.checkin(reservationId);
                            break;
                        case 4:
                            System.out.println("Enter reservation ID for Check Out:");
                            reservationId = scanner.nextInt();
                            normalUser.checkout(reservationId);
                            break;
                        case 5:
                            System.out.println("Logging out...");
                            user = null;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }

                if (user == null) {
                    break;
                }
            }

        }


    }
}
