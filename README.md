Task Manager (JavaFX)
A desktop task management application developed to demonstrate proficiency in Java and the JavaFX framework. This project focuses on providing an intuitive user experience through custom UI components and efficient data handling.

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
