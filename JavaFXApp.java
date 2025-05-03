
/* build+run:  
  javac --module-path /usr/lib/jvm/java-24-openjfx/lib/ \
  --add-modules javafx.controls JavaFXApp.java && java   --module-path \
   /usr/lib/jvm/java-24-openjfx/lib/ --add-modules javafx.controls JavaFXApp
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class JavaFXApp extends Application {
  public void start(Stage window) {
    System.out.println("Starting app");

    Label label = new Label("Search:");
    label.addEventHandler(MouseEvent.MOUSE_CLICKED,
        (event) -> System.out.println("Label clicked"));
    TextField searchText = new TextField();
    searchText.addEventHandler(KeyEvent.KEY_TYPED,
        (event) -> System.out.println("Key typed: " + event.getCharacter()));
    Button searchButton = new Button("Search");
    searchButton.addEventHandler(ActionEvent.ACTION,
        (event) -> System.out.println("Searching: " + searchText.getText()));
    Button closeButton = new Button("Close");
    closeButton.addEventHandler(ActionEvent.ACTION,
        (event) -> Platform.exit());

    // not needed with layout manager:
    // button.setLayoutY(25);

    // left as examples to note:
    // Circle circle = new Circle(200, 200, 20);
    // Polygon polygon = new Polygon(100, 150, 180, 90, 35, 80);
    // Group group = new Group(label, button, circle, polygon);
    // Scene scene = new Scene(group, 800, 600);

    HBox buttons = new HBox(60);

    buttons.getChildren().add(searchButton);
    buttons.getChildren().add(closeButton);

    BorderPane bp = new BorderPane();

    bp.addEventHandler(MouseEvent.MOUSE_CLICKED,
        (event) -> {
          System.out.println("BP clicked");
          event.consume();
        });

    bp.setBottom(buttons);
    bp.setCenter(searchText);
    bp.setLeft(label);

    bp.setAlignment(label, Pos.CENTER);

    bp.setMargin(label, new Insets(5, 0, 5, 5));
    bp.setMargin(searchText, new Insets(5, 5, 5, 0));
    bp.setMargin(buttons, new Insets(5, 5, 5, 5));

    Scene scene = new Scene(bp);

    // demonstration that event.consume() prevents this event reaching scene
    scene.addEventHandler(MouseEvent.MOUSE_CLICKED,
        (event) -> System.out.println("Scene clicked"));

    window.setScene(scene);

    window.setTitle("JavaFXApp");
    window.show();
  }

  public static void main(String[] args) {
    Application.launch();
  }
}
