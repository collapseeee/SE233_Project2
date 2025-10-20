module se233.se233_project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;


    opens se233.se233_project2 to javafx.fxml;
    exports se233.se233_project2;
}