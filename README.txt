==========================================================
  Masroofy — Budget Management Desktop App
  CS251 Introduction to Software Engineering — 2026
  STARTER TEMPLATE (Task 3 — Implementation)
==========================================================

Team Members & Ownership:
  • Mahmoud Mokhtar Mohamed (Team Leader) — 20242320
      DatabaseHelper.java, LocalStorageRepository.java,
      BudgetCalculator.java, NotificationService.java , DashboardService.java ,Main.java
  • Mazen Mahmoud Abd El-Moez             — 20242258
      BudgetCycle.java, Transaction.java, Category.java,
      CategoryType.java, User.java, 
  • Mahmoud Mohamed Elsawy                — 20240558
      CycleSetupController.java, ExpenseController.java,
      DashboardController.java, HistoryController.java
  • Mohamed Arafa Khalaf                  — 20240517
      DateUtils.java, AlertUtils.java, StateManager.java

----------------------------------------------------------
WHAT IS THIS TEMPLATE?
----------------------------------------------------------
Every Java file contains:
  - Full Javadoc on every class and method (Task 4 ready)
  - The correct method signatures matching the SDS class diagram
  - Step-by-step TODO comments inside each method body
  - throw new UnsupportedOperationException(...) guards so the
    code compiles but reminds you what's unfinished

The FXML files (cycle-setup, dashboard, expense-log, history),
CSS stylesheet, pom.xml, and .gitignore are ALREADY COMPLETE
— do not change them unless you know what you're doing.

----------------------------------------------------------
HOW TO START
----------------------------------------------------------
Prerequisites:
  • Java 17+   (https://adoptium.net)
  • Maven 3.8+ (https://maven.apache.org)

Verify setup:
  java -version     # must say 17 or higher
  mvn -version

Run the app (it will throw UnsupportedOperationException until
you implement the methods):
  mvn javafx:run

----------------------------------------------------------
IMPLEMENTATION ORDER (recommended)
----------------------------------------------------------
Start from the bottom up — each layer depends on the one below:

STEP 1 — Utilities (Mohamed Arafa)
  DateUtils.java    → 5 one-liner methods
  AlertUtils.java   → 4 alert methods
  StateManager.java → 8 state transitions

STEP 2 — Model (Mazen)
  CategoryType.java → already complete (enum)
  Category.java     → 2 constructors + setType()
  User.java         → 2 constructors
  Transaction.java  → 2 constructors + 3 methods
  BudgetCycle.java  → initialize(), validateInputs(),
                       getRemainingDays(), recalculateLimit(),
                       applyDailyRollover()

STEP 3 — Repository (Mahmoud Mokhtar)
  DatabaseHelper.java         → DB_URL static block, connect(), initializeDatabase()
  LocalStorageRepository.java → getInstance(), seedDefaultUser(),
                                 saveCycle(), updateCycle(), getActiveCycle(),
                                 clearCycleData(), saveTransaction(),
                                 getTransactions(), updateTransaction(),
                                 deleteTransaction(), filterTransactions()

STEP 4 — Services (Mahmoud Mokhtar + Mazen)
  BudgetCalculator.java   → 3 calculation methods (Mahmoud Mokhtar)
  NotificationService.java → checkAndNotify() + 2 alert methods (Mahmoud Mokhtar)
  DashboardService.java   → getDashboardSummary(), getPieChartData() (Mazen)

STEP 5 — Main + Controllers (Mahmoud Mokhtar + Mahmoud Elsawy)
  Main.java               → start() method (Mahmoud Mokhtar)
  CycleSetupController.java → US#1 (Mahmoud Elsawy)
  ExpenseController.java    → US#2 + US#6 (Mahmoud Elsawy)
  DashboardController.java  → US#3 + US#4 (Mahmoud Elsawy)
  HistoryController.java    → US#7 (Mahmoud Elsawy)

----------------------------------------------------------
TECHNOLOGY STACK
----------------------------------------------------------
  Language   : Java 17
  UI         : JavaFX 21 + FXML
  Styling    : JavaFX CSS (styles.css — provided, do not edit)
  Database   : SQLite via sqlite-jdbc 3.45.1
  Build      : Apache Maven
  Docs       : JavaDoc (mvn javadoc:javadoc → target/site/apidocs/)

----------------------------------------------------------
PROJECT STRUCTURE
----------------------------------------------------------
  src/main/java/com/masroofy/
    Main.java                    — App entry point
    model/
      BudgetCycle.java           — Mazen
      Transaction.java           — Mazen
      Category.java              — Mazen
      CategoryType.java          — Mazen (complete)
      User.java                  — Mazen
    repository/
      DatabaseHelper.java        — Mahmoud Mokhtar
      LocalStorageRepository.java — Mahmoud Mokhtar (Singleton)
    service/
      BudgetCalculator.java      — Mahmoud Mokhtar (Strategy)
      NotificationService.java   — Mahmoud Mokhtar (Observer)
      DashboardService.java      — Mahmoud Mokhtar (Facade)
    controller/
      CycleSetupController.java  — Mahmoud Elsawy
      DashboardController.java   — Mahmoud Elsawy
      ExpenseController.java     — Mahmoud Elsawy
      HistoryController.java     — Mahmoud Elsawy
    util/
      AlertUtils.java            — Mohamed Arafa
      DateUtils.java             — Mohamed Arafa
      StateManager.java          — Mohamed Arafa

  src/main/resources/
    fxml/   (4 screens — COMPLETE, do not edit)
    css/    (styles.css — COMPLETE, do not edit)

----------------------------------------------------------
GENERATE JAVADOC (Task 4)
----------------------------------------------------------
  mvn javadoc:javadoc
  Output: target/site/apidocs/index.html

----------------------------------------------------------
DESIGN PATTERNS (for SDS reference)
----------------------------------------------------------
  1. Singleton  — LocalStorageRepository.getInstance()
  2. Strategy   — BudgetCalculator (interchangeable algorithms)
  3. Observer   — NotificationService ↔ BudgetCycle/Transaction
  4. Facade     — DashboardService (hides backend from UI)
  5. MVC        — Overall architecture

----------------------------------------------------------
DATABASE
----------------------------------------------------------
  SQLite file created at: ~/.masroofy/masroofy.db
  Tables: users, budget_cycles, transactions
  Data persists between sessions automatically.

==========================================================
