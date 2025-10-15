module se233.se233_project2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.se233_project2 to javafx.fxml;
    exports se233.se233_project2;
}