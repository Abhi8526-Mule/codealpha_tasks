import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;
    double price;

    Room(int number, String category, double price) {
        this.roomNumber = number;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }

    public String toString() {
        return "Room " + roomNumber + " - " + category + " - Rs. " + price + " - " + (isBooked ? "Booked" : "Available");
    }
}

class Reservation {
    String guestName;
    Room room;
    boolean isPaid;
    String paymentMethod;

    Reservation(String guestName, Room room, boolean isPaid, String paymentMethod) {
        this.guestName = guestName;
        this.room = room;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
    }

    public String toString() {
        return "Guest: " + guestName +
               ", Room: " + room.roomNumber +
               ", Category: " + room.category +
               ", Price: Rs. " + room.price +
               ", Payment: " + (isPaid ? "Paid (" + paymentMethod + ")" : "Pending");
    }
}

public class HotelReservationSystem extends JFrame implements ActionListener {
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    JTextArea display;
    JButton searchBtn, bookBtn, viewBtn;

    HotelReservationSystem() {
        setTitle("Hotel Reservation System");
        setSize(650, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Room Data (with prices)
        rooms.add(new Room(101, "Deluxe", 2500));
        rooms.add(new Room(102, "Suite", 4000));
        rooms.add(new Room(103, "Standard", 1500));
        rooms.add(new Room(104, "Deluxe", 2500));
        rooms.add(new Room(105, "Standard", 1500));

        display = new JTextArea();
        display.setEditable(false);
        add(new JScrollPane(display), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        searchBtn = new JButton("Search Available Rooms");
        bookBtn = new JButton("Make Reservation");
        viewBtn = new JButton("View All Bookings");

        panel.add(searchBtn);
        panel.add(bookBtn);
        panel.add(viewBtn);
        add(panel, BorderLayout.SOUTH);

        searchBtn.addActionListener(this);
        bookBtn.addActionListener(this);
        viewBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String category = JOptionPane.showInputDialog(this, "Enter room category (Deluxe, Suite, Standard):");
            display.setText("Available Rooms:\n");
            for (Room r : rooms) {
                if (!r.isBooked && r.category.equalsIgnoreCase(category)) {
                    display.append(r.toString() + "\n");
                }
            }

        } else if (e.getSource() == bookBtn) {
            String name = JOptionPane.showInputDialog(this, "Enter guest name:");
            String roomNumStr = JOptionPane.showInputDialog(this, "Enter room number to book:");
            int roomNum;
            try {
                roomNum = Integer.parseInt(roomNumStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid room number!");
                return;
            }

            Room selectedRoom = null;
            for (Room r : rooms) {
                if (r.roomNumber == roomNum && !r.isBooked) {
                    selectedRoom = r;
                    break;
                }
            }

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(this, "Room not available or already booked!");
                return;
            }

            // Show room price before payment
            JOptionPane.showMessageDialog(this, "Room Price: Rs. " + selectedRoom.price);

            // Payment method selection
            String[] options = {"Cash", "Card", "Cancel"};
            int paymentOption = JOptionPane.showOptionDialog(this,
                    "Select payment method:",
                    "Payment",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            boolean isPaid = false;
            String paymentMethod = "";

            if (paymentOption == 0) {
                paymentMethod = "Cash";
                isPaid = true;
            } else if (paymentOption == 1) {
                paymentMethod = "Card";
                isPaid = true;
            } else {
                JOptionPane.showMessageDialog(this, "Payment cancelled. Reservation not completed.");
                return;
            }

            selectedRoom.isBooked = true;
            reservations.add(new Reservation(name, selectedRoom, isPaid, paymentMethod));
            JOptionPane.showMessageDialog(this, "Reservation successful for " + name + "!");
        }

        else if (e.getSource() == viewBtn) {
            display.setText("Current Bookings:\n");
            for (Reservation r : reservations) {
                display.append(r.toString() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        new HotelReservationSystem().setVisible(true);
    }
}