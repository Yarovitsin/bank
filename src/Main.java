import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class BankClient {
    private String firstName;
    private String lastName;
    private Double balance;
    private String password_hash;

    public BankClient(String firstName, String lastName, Double balance, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.password_hash = getHash(password);
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    private String getHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean checkPassword(String password) {
        return password_hash.equals(getHash(password));
    }

    public boolean withdrawable(double amount) {
        return amount <= balance;
    }
}

class Bank {
    public Bank() {
    }
    public boolean sendMoney(BankClient from, String password, BankClient to, double amount) {
        if (from.checkPassword(password) && from.withdrawable(amount)) {
            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);
            return true;
        }
        return false;
    }
}

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        BankClient client1 = new BankClient("John", "Doe", 1000.0, "1234");
        BankClient client2 = new BankClient("Jane", "Doe", 2000.0, "4321");
        BankClient client3 = new BankClient("John", "Smith", 10000.0, "1111");

        //write some example code here
        bank.sendMoney(client1, "1234", client2, 100);
        bank.sendMoney(client2, "4321", client3, 200);
        bank.sendMoney(client3, "1111", client1, 300);

        System.out.println(client1.getBalance());
        System.out.println(client2.getBalance());
        System.out.println(client3.getBalance());
    }
}