# Slang Word Dictionary (JavaFX)

This is the project for the **Java Application Programming (CSC13102)** course. It is a full-featured Slang Word Dictionary built using JavaFX, Maven, and following an MVC design pattern.

## Features

This application implements all 10 required features:

1.  **Search by Slang Word:** Blazing-fast O(1) search using a `HashMap`.
2.  **Search by Definition:** Reverse search to find slangs based on keywords in their meaning.
3.  **View History:** Displays a list of previously searched terms and their results. This history is persisted after closing the app.
4.  **Add Slang Word:** Allows adding new slangs. Automatically detects duplicates and provides "Overwrite" or "Duplicate" (add new meaning) options.
5.  **Edit Slang Word:** Allows users to find a slang, then edit its name or definitions.
6.  **Delete Slang Word:** Allows deleting a slang. Includes a confirmation dialog before deletion.
7.  **Reset Dictionary:** Resets the dictionary to its original state by re-reading the original `slang.txt` file.
8.  **Random Slang Word:** Displays a random slang word (used on the Welcome screen).
9.  **Quiz (Slang -> Def):** Provides a Slang question and 4 Definition options.
10. **Quiz (Def -> Slang):** Provides a Definition question and 4 Slang options.

---

## Technical Design

To ensure high performance and data persistence, the project uses the following architecture:

* **MVC Pattern:** A clean separation between the Model (`SlangDictionary`), the View (`.fxml` files), and the Controllers.
* **Singleton Pattern:** The `SlangDictionary` (Model) is a Singleton, ensuring the entire application shares one single source of truth for the data.
* **Data Structure:** The core data is stored in a `HashMap<String, List<String>>`. This allows the primary "Search by Slang" feature to operate at **O(1)** complexity.
* **Data Persistence (Portable App):**
    * To meet the "do not re-index" requirement, the app uses **Java Serialization** (`ObjectInputStream`/`ObjectOutputStream`).
    * **On first launch:** The app reads the `slang.txt` file (located *inside* the JAR). It builds the `HashMap` in memory, then saves this optimized `HashMap` (and the empty history) to an external binary file named `slang.dat`.
    * **File Location:** This `slang.dat` file is created and saved **in the same directory as the .jar file**.
    * **On subsequent launches:** The app instantly loads the external `slang.dat` file, which includes all user changes and history.
    * **Saving:** All changes (adds, edits, deletes, and search history) are written to `slang.dat` once when the application window is closed.

---

## How to Run

The application is packaged as a "fat JAR", which includes all necessary JavaFX libraries.

1.  Ensure you have **Java (JDK/JRE) 17 or newer** installed.
2.  Find the executable `.jar` file in the `/out/artifacts/slang_dictionary_jar2/` directory.
3.  Place the `.jar` file in any folder where you have **write permissions** (e.g., your Desktop, `D:\Apps\`).
4.  Run the application by double-clicking the `.jar` file or using the terminal:

    ```bash
    java -jar your-jar-file-name.jar
    ```

5.  **Note:** On the very first run, a file named `slang.dat` will be created next to your `.jar` file. **Do not delete this file**, as it stores your changes and search history.
