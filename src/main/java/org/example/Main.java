package org.example;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {
        DbConfig.initializeDatabase();

        Thread session1 = new Thread(() -> runTransactionA());
        Thread session2 = new Thread(() -> runTransactionB());

        session1.start();
        session2.start();

        session1.join();
        session2.join();
    }

    private static void runTransactionA() {
        try (Connection conn = DbConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtA = conn.prepareStatement("SELECT * FROM TableA WHERE id = 1 FOR UPDATE");
                 PreparedStatement stmtB = conn.prepareStatement("SELECT * FROM TableB WHERE id = 1 FOR UPDATE")) {

                System.out.println("Session 1: Locking TableA...");
                stmtA.executeQuery();

                Thread.sleep(1000); // Wait to create timing overlap

                System.out.println("Session 1: Attempting to lock TableB...");
                stmtB.executeQuery();

                conn.commit();
            } catch (SQLException e) {
                System.out.println("Session 1: Deadlock or error occurred - " + e.getMessage());
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTransactionB() {
        try (Connection conn = DbConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtB = conn.prepareStatement("SELECT * FROM TableB WHERE id = 1 FOR UPDATE");
                 PreparedStatement stmtA = conn.prepareStatement("SELECT * FROM TableA WHERE id = 1 FOR UPDATE")) {

                System.out.println("Session 2: Locking TableB...");
                stmtB.executeQuery();

                Thread.sleep(1000); // Wait to create timing overlap

                System.out.println("Session 2: Attempting to lock TableA...");
                stmtA.executeQuery();

                conn.commit();
            } catch (SQLException e) {
                System.out.println("Session 2: Deadlock or error occurred - " + e.getMessage());
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}