# WeekendKanban 📝

A lightweight, interactive Task Management Board (Kanban style) built to demonstrate the power of **Apache Wicket** for component-oriented UIs and **Spring Boot** for a robust backend.

**Goal:** Build a functional Trello clone in a single weekend without writing custom JavaScript.

## 🛠 Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Frontend:** Apache Wicket 9.x (or 10.x)
* **Database:** H2 (In-Memory for dev)
* **Glue:** `wicket-spring-boot-starter` (by Marc Giffing)
* **Tools:** Maven, Lombok

## 🚀 Getting Started

### Prerequisites
* JDK 17 or higher installed.
* Maven installed (or use the wrapper included in the project).
* An IDE (IntelliJ IDEA or VS Code recommended).

### Installation & Run

1.  **Clone the repo** (or initialize your folder):
    ```bash
    git clone https://github.com/yourusername/weekend-kanban.git
    cd weekend-kanban
    ```

2.  **Run the application**:
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Access the Board**:
    Open your browser to http://localhost:8080.
    * *H2 Console:* http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb)

## 🏗 Architecture & Mental Model

Unlike standard MVC (where a Controller passes DTOs to a Template), Wicket is **Component Oriented**.

* **The Page (BoardPage)**: The canvas. It holds the high-level layout.
* **The Panel (ColumnPanel)**: A reusable vertical lane (Todo, Doing, Done). We place 3 distinct instances of this class on the Page.
* **The Component (TaskCard)**: A small panel representing a single task. It handles its own "Delete" or "Move" events.
* **The Model (IModel)**: The most important part. The UI components do not hold data; they hold a *reference* (Model) to how to get the data from the Spring Service.

## 🗺 Weekend Roadmap

### 📅 Phase 1: The Setup (Friday)
- [ ] Initialize Spring Boot project with Web, JPA, H2, Lombok.
- [ ] Add `wicket-spring-boot-starter` dependency.
- [ ] Create `HomePage.html` and `HomePage.java`.
- [ ] Verify "Hello World" renders.

### 📅 Phase 2: The Core (Saturday)
- [ ] Create `Task` Entity (`id`, `title`, `status`).
- [ ] Create `TaskRepository` and `TaskService`.
- [ ] Build `TaskCardPanel` (renders a single task title).
- [ ] Build `ColumnPanel` (uses `ListView` to render a list of TaskCards).
- [ ] **Milestone:** You can see tasks loaded from `data.sql` on the screen.

### 📅 Phase 3: Interactivity (Sunday)
- [ ] Add `AjaxLink` buttons to TaskCard ("Move Right", "Delete").
- [ ] implement `onClick` events to call `TaskService`.
- [ ] Use `AjaxRequestTarget` to refresh only the specific Column Panel (not the whole page).
- [ ] Add a Modal Window to create new tasks.

## 🧠 Wicket + Spring Cheat Sheet

### 1. Injecting Spring Services
Do not use `@Autowired` in Wicket components. Use `@SpringBean`.

```java
public class BoardPage extends WebPage {
    @SpringBean
    private TaskService taskService;
}
```

### 2. The Golden Rule of Models
**NEVER** store a JPA Entity directly in a Component field. It will cause a `NotSerializableException`.
**ALWAYS** use a `LoadableDetachableModel`.

**❌ Bad:**
```java
// This will crash during serialization
public class TaskPanel extends Panel {
    private Task task; // Forbidden!
    public TaskPanel(String id, Task task) { ... }
}
```

**✅ Good:**
```java
// This loads fresh data only when rendering
public class TaskPanel extends Panel {
    public TaskPanel(String id, IModel<Task> taskModel) {
        super(id, taskModel);
        add(new Label("title", new PropertyModel<>(taskModel, "title")));
    }
}
```

### 3. AJAX Updates
To update a part of the screen without a reload:
1.  The container to be updated must have `.setOutputMarkupId(true)`.
2.  In the event handler, add that component to the target.

```java
// Inside an AjaxLink onClick:
taskService.moveTask(task);
target.add(columnPanel); // Refreshes the UI
```
