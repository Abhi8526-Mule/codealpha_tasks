import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentGradeTrackerGUI extends JFrame {

    private ArrayList<Integer> grades = new ArrayList<>();
    private JTextField gradeInput;
    private JTextArea gradeListDisplay;
    private JLabel resultLabel;

    public StudentGradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for grade entry
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(new JLabel("Enter Grade:"));
        gradeInput = new JTextField(5);
        topPanel.add(gradeInput);

        JButton addButton = new JButton("Add Grade");
        topPanel.add(addButton);

        JButton computeButton = new JButton("Compute");
        topPanel.add(computeButton);

        add(topPanel, BorderLayout.NORTH);

        // Center panel for displaying grades
        gradeListDisplay = new JTextArea(10, 30);
        gradeListDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gradeListDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for results
        resultLabel = new JLabel("Enter grades and click Compute.");
        add(resultLabel, BorderLayout.SOUTH);

        // Event listeners
        addButton.addActionListener(e -> addGrade());
        computeButton.addActionListener(e -> computeGrades());

        setVisible(true);
    }

    private void addGrade() {
        try {
            int grade = Integer.parseInt(gradeInput.getText());
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Please enter a grade between 0 and 100.");
                return;
            }
            grades.add(grade);
            gradeListDisplay.append("Grade added: " + grade + "\n");
            gradeInput.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private void computeGrades() {
        if (grades.isEmpty()) {
            resultLabel.setText("No grades entered.");
            return;
        }

        int sum = 0;
        int highest = grades.get(0);
        int lowest = grades.get(0);

        for (int grade : grades) {
            sum += grade;
            if (grade > highest) highest = grade;
            if (grade < lowest) lowest = grade;
        }

        double average = (double) sum / grades.size();

        resultLabel.setText(String.format("Average: %.2f, Highest: %d, Lowest: %d", average, highest, lowest));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeTrackerGUI::new);
    }
}
