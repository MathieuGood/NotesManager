module com.example.treeviewnotes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.treeviewnotes to javafx.fxml;
    exports com.example.treeviewnotes;
}