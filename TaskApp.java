package com.example.sceneapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TaskApp extends Application {

    public enum Priority { HIGH, MEDIUM, LOW }

    public static class Task {
        private String name;
        private String description;
        private LocalDate deadline;
        private Priority priority;
        private boolean completed = false;

        public Task(String name, String description, LocalDate deadline, Priority priority) {
            this.name = name;
            this.description = description;
            this.deadline = deadline;
            this.priority = priority;
        }
        public String getName()           { return name; }
        public String getDescription()    { return description; }
        public LocalDate getDeadline()    { return deadline; }
        public Priority getPriority()     { return priority; }
        public boolean isCompleted()      { return completed; }
        public void setName(String n)     { name = n; }
        public void setDescription(String d) { description = d; }
        public void setDeadline(LocalDate d) { deadline = d; }
        public void setPriority(Priority p) { priority = p; }
        public void setCompleted(boolean c) { completed = c; }

        @Override
        public String toString() { return name; }
    }

    private final ObservableList<Task> masterList =
            FXCollections.observableArrayList();
    private final FilteredList<Task> filteredList =
            new FilteredList<>(masterList, t -> true);

    @Override
    public void start(Stage stage) {
        // --- ListView & CellFactory with inline toggle ---
        ListView<Task> listView = new ListView<>(filteredList);
        listView.setCellFactory(lv -> new ListCell<Task>() {
            private final CheckBox check      = new CheckBox();
            private final Label nameLabel     = new Label();
            private final Label metaLabel     = new Label();
            private final Label descLabel     = new Label();
            private final Button toggleBtn    = new Button("▼");
            private final VBox textBox        = new VBox(nameLabel, metaLabel);
            private final HBox header         = new HBox(8, check, textBox, toggleBtn);
            private final VBox content        = new VBox(4, header, descLabel);

            {
                // initial styling
                textBox.setAlignment(Pos.CENTER_LEFT);
                header.setAlignment(Pos.CENTER_LEFT);
                header.setPadding(new Insets(2, 0, 2, 0));
                descLabel.getStyleClass().add("task-desc");
                descLabel.setWrapText(true);
                descLabel.setVisible(false);
                toggleBtn.setFocusTraversable(false);

                toggleBtn.setOnAction(e -> {
                    boolean showing = descLabel.isVisible();
                    descLabel.setVisible(!showing);
                    toggleBtn.setText(showing ? "▼" : "▲");
                });

                // Checkbox action: complete & reorder
                check.setOnAction(e -> {
                    Task t = getItem();
                    if (t == null) return;
                    t.setCompleted(check.isSelected());
                    masterList.remove(t);
                    if (t.isCompleted()) {
                        masterList.add(t);
                    } else {
                        int idx = 0;
                        while (idx < masterList.size() && !masterList.get(idx).isCompleted()) {
                            idx++;
                        }
                        masterList.add(idx, t);
                    }
                    listView.refresh();
                });

                // Drag & drop reordering
                setOnDragDetected(event -> {
                    if (getItem() == null) return;
                    Dragboard db = startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(getItem().getName());
                    db.setContent(cc);
                    event.consume();
                });
                setOnDragOver(event -> {
                    if (event.getGestureSource() != this &&
                            event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });
                setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        String key = db.getString();
                        Task dragged = masterList.stream()
                                .filter(x -> x.getName().equals(key))
                                .findFirst().orElse(null);
                        if (dragged != null) {
                            int thisIdx = getIndex();
                            masterList.remove(dragged);
                            masterList.add(Math.min(thisIdx, masterList.size()), dragged);
                            success = true;
                        }
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });

                setGraphic(content);
            }

            @Override
            protected void updateItem(Task t, boolean empty) {
                super.updateItem(t, empty);
                if (empty || t == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(t.getName());
                    metaLabel.setText("Due " + t.getDeadline() +
                            "  •  " + t.getPriority());
                    descLabel.setText(t.getDescription());
                    descLabel.setVisible(false);
                    toggleBtn.setText("▼");
                    check.setSelected(t.isCompleted());

                    // priority styling via CSS classes
                    getStyleClass().removeAll(
                            "priority-high","priority-medium","priority-low","completed");
                    if (t.isCompleted()) {
                        getStyleClass().add("completed");
                    } else {
                        switch (t.getPriority()) {
                            case HIGH:   getStyleClass().add("priority-high");   break;
                            case MEDIUM: getStyleClass().add("priority-medium"); break;
                            case LOW:    getStyleClass().add("priority-low");    break;
                        }
                    }
                    setGraphic(content);
                }
            }
        });

        // --- Filter bar ---
        ToggleGroup tg = new ToggleGroup();
        RadioButton allBtn = new RadioButton("All");
        RadioButton openBtn = new RadioButton("Open");
        RadioButton doneBtn = new RadioButton("Done");
        allBtn.setToggleGroup(tg);
        openBtn.setToggleGroup(tg);
        doneBtn.setToggleGroup(tg);
        allBtn.setSelected(true);
        tg.selectedToggleProperty().addListener((obs,o,n)->{
            if (n==openBtn)      filteredList.setPredicate(t->!t.isCompleted());
            else if (n==doneBtn) filteredList.setPredicate(Task::isCompleted);
            else                 filteredList.setPredicate(t->true);
        });
        HBox filterBar = new HBox(12, allBtn, openBtn, doneBtn);
        filterBar.getStyleClass().add("filter-bar");

        // --- Controls & Dialog ---
        ImageView addIcon = new ImageView(new Image("file:add.png"));
        addIcon.setFitWidth(65); addIcon.setFitHeight(65);
        Button addBtn = new Button("", addIcon);
        addBtn.getStyleClass().addAll("btn-icon", "btn-add");
        addBtn.setOnAction(e -> showDialog(null));

        ImageView editIcon = new ImageView(new Image("file:edit.png"));
        editIcon.setFitWidth(32); editIcon.setFitHeight(32);
        Button editBtn = new Button("", editIcon);
        editBtn.getStyleClass().addAll("btn-icon", "btn-edit");
        editBtn.setOnAction(e -> {
            Task sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) showDialog(sel);
        });

        ImageView delIcon = new ImageView(new Image("file:delete.png"));
        delIcon.setFitWidth(30); delIcon.setFitHeight(30);
        Button delBtn = new Button("", delIcon);
        delBtn.getStyleClass().addAll("btn-icon", "btn-delete");
        delBtn.setOnAction(e -> {
            Task sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) masterList.remove(sel);
        });

        Button clrBtn = new Button("Clear");
        clrBtn.getStyleClass().addAll("btn-text", "btn-clear");
        clrBtn.setOnAction(e -> masterList.clear());
        HBox controls = new HBox(8, addBtn, editBtn, delBtn, clrBtn);
        controls.setPadding(new Insets(10,0,0,0));

        // --- Layout ---
        VBox root = new VBox(10, filterBar, listView, controls);
        root.setPadding(new Insets(15));
        Scene scene = new Scene(root, 450, 550);
        scene.getStylesheets().add("file:style.css");

        stage.setTitle("Task Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void showDialog(Task edit) {
        boolean isEdit = edit != null;
        Dialog<Task> dlg = new Dialog<>();
        dlg.setTitle(isEdit ? "Edit Task" : "New Task");
        dlg.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10); g.setVgap(10); g.setPadding(new Insets(20,20,10,20));

        TextField nameF = new TextField(isEdit ? edit.getName() : "");
        TextArea descA  = new TextArea(isEdit ? edit.getDescription() : "");
        descA.setPrefRowCount(2);
        DatePicker dp    = new DatePicker(isEdit ? edit.getDeadline() : LocalDate.now());
        ComboBox<Priority> cb = new ComboBox<>(
                FXCollections.observableArrayList(Priority.values()));
        cb.getSelectionModel().select(isEdit ? edit.getPriority() : Priority.MEDIUM);
        cb.setConverter(new StringConverter<>() {
            public String toString(Priority p){ return p.name(); }
            public Priority fromString(String s){ return Priority.valueOf(s); }
        });

        g.add(new Label("Title:"),   0,0);
        g.add(nameF,                 1,0);
        g.add(new Label("Description:"), 0,1);
        g.add(descA,                 1,1);
        g.add(new Label("Deadline:"), 0,2);
        g.add(dp,                    1,2);
        g.add(new Label("Priority:"),0,3);
        g.add(cb,                    1,3);

        dlg.getDialogPane().setContent(g);
        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK && !nameF.getText().isEmpty()) {
                if (isEdit) {
                    edit.setName(nameF.getText());
                    edit.setDescription(descA.getText());
                    edit.setDeadline(dp.getValue());
                    edit.setPriority(cb.getValue());
                    return edit;
                } else {
                    return new Task(
                            nameF.getText(),
                            descA.getText(),
                            dp.getValue(),
                            cb.getValue()
                    );
                }
            }
            return null;
        });

        dlg.showAndWait().ifPresent(t -> {
            if (!isEdit) masterList.add(t);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
