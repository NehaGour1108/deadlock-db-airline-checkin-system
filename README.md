To simulate and observe a deadlock in a database using Java and Maven with an embedded database (like H2), we can set up a project that establishes two sessions and causes a deadlock through a circular dependency on table locks.

Here’s a full example:

### Project Structure

```
airline-checkin-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── DeadlockSimulation.java
│       │           └── DbConfig.java
│       └── resources/
│           └── application.properties
├── pom.xml
```

### Step 1: Set up `pom.xml`

Add the following dependencies to `pom.xml` for H2 and basic project setup.

### Step 2: Database Configuration in `DbConfig.java`

This class initializes the H2 database and sets up two tables to simulate the deadlock.

### Step 3: Deadlock Simulation in `DeadlockSimulation.java`

This class starts two threads that create transactions to induce a deadlock.

### Explanation of `DeadlockSimulation.java`

1. **Thread session1** locks a row in `TableA` and tries to lock `TableB`.
2. **Thread session2** locks a row in `TableB` and tries to lock `TableA`.
3. The circular wait results in a deadlock, which will throw an exception in one of the transactions, as H2 automatically handles deadlocks by rolling back one of the conflicting transactions.

### Step 4: Running the Application

1. Compile the code with Maven:


2. Run the application:

### Expected Output

You should see logs similar to:

```
Session 1: Locking TableA...
Session 2: Locking TableB...
Session 1: Attempting to lock TableB...
Session 2: Attempting to lock TableA...
Session 1: Deadlock or error occurred - Deadlock detected
```

The output confirms that a deadlock was detected, and one of the transactions rolled back. This approach demonstrates how to simulate and observe deadlocks in a Java Maven project with H2.
