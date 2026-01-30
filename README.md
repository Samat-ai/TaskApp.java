Task Manager (JavaFX)
A desktop task management application developed to demonstrate proficiency in Java and the JavaFX framework. This project focuses on providing an intuitive user experience through custom UI components and efficient data handling.


Images:

<img width="260" height="460" alt="Screenshot 2026-01-30 000003" src="https://github.com/user-attachments/assets/60987e21-52e5-4db1-9707-55f9e7dc79e6" />
<img width="500" height="337" alt="Screenshot 2026-01-30 000101" src="https://github.com/user-attachments/assets/85173560-c9af-44b1-a6ab-deab0a1acfa5" />
<img width="260" height="460" alt="Screenshot 2026-01-30 000149" src="https://github.com/user-attachments/assets/aaf44cbf-e364-4735-b1e2-e237843575aa" />
<img width="260" height="460" alt="Screenshot 2026-01-30 000128" src="https://github.com/user-attachments/assets/1feeb6c4-f5ea-4f21-9c1b-e621a1dd02a0" />


🚀 Key Features
  Dynamic Task Management: Create, edit, and delete tasks with specific names, descriptions, deadlines, and priorities.
  Intuitive Reordering: Drag-and-drop functionality allows for easy mouse-driven task reordering.
  Smart Filtering: Toggle between "All", "Open", and "Done" tasks instantly using FilteredList predicates.
  Custom UI Architecture: Uses a custom CellFactory to render tasks as "cards" with checkboxes, toggleable descriptions, and priority-based styling.
  Automated Sorting: Completion logic automatically sinks finished tasks to the bottom of the list to keep active items prioritized.

🛠️ Tech Stack
  Language: Java 
  Framework: JavaFX (Application lifecycle, UI controls, Event handling) 
  Styling: External CSS (style.css) for UI/UX consistency 
  Data Structures: ObservableList and FilteredList for real-time UI synchronization 

🏗️ Project Structure
  TaskApp.java: The main entry point containing the application lifecycle, UI layout, and core logic.
  Task: A static inner class (Model) that encapsulates task properties using an Enum for priority safety.
  style.css: Handles visual aesthetics, including card shadows, rounded corners, and priority-specific color overlays.

🎨 UI & Styling
  The application utilizes an external stylesheet to separate design from logic:
  Priority Classes: .priority-high, .priority-medium, and .priority-low provide immediate visual feedback.
  Responsive Cards: .list-cell cards use subtle drop-shadows and padding for a modern, clean look.
  Animations: Inline expand/collapse for task descriptions to maintain a clean interface.

🔧 Future Enhancements
  Implementing persistent storage via file handling or a database.
  Adding a search bar for larger task lists.
  System tray notifications for upcoming deadlines.
