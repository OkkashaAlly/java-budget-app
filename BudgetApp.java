import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BudgetApp extends JFrame {

    private double walletBalance;
    private double incomeBalance;
    private double expenseBalance;

    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> transactionTypeDropdown;
    private JTextArea transactionListArea;
    private JLabel walletBalanceLabel;
    private JLabel incomeBalanceLabel;
    private JLabel expenseBalanceLabel;

    private List<Transaction> transactions;

    public BudgetApp() {
        walletBalance = 0.0;
        incomeBalance = 0.0;
        expenseBalance = 0.0;
        transactions = new ArrayList<>();

        setTitle("Budget App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        amountField = new JTextField();
        descriptionField = new JTextField();
        transactionTypeDropdown = new JComboBox<>(new String[]{"Income", "Expense"});
        JButton addButton = new JButton("Add Transaction");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Transaction Type:"));
        inputPanel.add(transactionTypeDropdown);
        inputPanel.add(new JLabel()); // Empty space
        inputPanel.add(addButton);

        walletBalanceLabel = new JLabel("Wallet Balance: $" + walletBalance);
        incomeBalanceLabel = new JLabel("Income Balance: $" + incomeBalance);
        expenseBalanceLabel = new JLabel("Expense Balance: $" + expenseBalance);

        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new GridLayout(3, 1));
        balancePanel.add(walletBalanceLabel);
        balancePanel.add(incomeBalanceLabel);
        balancePanel.add(expenseBalanceLabel);

        transactionListArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(transactionListArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(balancePanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        add(panel);

        setLocationRelativeTo(null); // Center the frame
    }

    private void addTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            String transactionType = (String) transactionTypeDropdown.getSelectedItem();

            Transaction transaction = new Transaction(amount, description, transactionType);
            transactions.add(transaction);

            updateBalances();
            updateTransactionList();

            // Clear input fields
            amountField.setText("");
            descriptionField.setText("");
            transactionTypeDropdown.setSelectedIndex(0); // Set it back to "Income"
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalances() {
        walletBalance += transactions.get(transactions.size() - 1).getAmount() * (transactions.get(transactions.size() - 1).getTransactionType().equals("Income") ? 1 : -1);
        if (transactions.get(transactions.size() - 1).getTransactionType().equals("Income")) {
            incomeBalance += transactions.get(transactions.size() - 1).getAmount();
        } else {
            expenseBalance += transactions.get(transactions.size() - 1).getAmount();
        }

        walletBalanceLabel.setText("Wallet Balance: $" + walletBalance);
        incomeBalanceLabel.setText("Income Balance: $" + incomeBalance);
        expenseBalanceLabel.setText("Expense Balance: $" + expenseBalance);
    }

    private void updateTransactionList() {
        StringBuilder sb = new StringBuilder();
        for (Transaction transaction : transactions) {
            sb.append(transaction).append("\n");
        }
        transactionListArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BudgetApp app = new BudgetApp();
                app.setVisible(true);
            }
        });
    }
}

class Transaction {
    private double amount;
    private String description;
    private String transactionType;

    public Transaction(double amount, String description, String transactionType) {
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Amount: $%.2f, Description: %s", transactionType, amount, description);
    }
}
