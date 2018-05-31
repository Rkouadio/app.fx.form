/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class FormFx extends Application {
   private FXMLLoader loader;
    
    @Override
    public void start(Stage primaryStage) {
       /* Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show(); */
       
       try {
         
            loader = new FXMLLoader();
           // loader.setLocation(getClass().getResource("/formuly/view/frontend/acceuille.fxml"));
            loader.setLocation(getClass().getResource("/vue/fxml/commandes.fxml"));
            Parent root = loader.load();
            
            primaryStage.setTitle("App Commandes");
              primaryStage.setScene(new Scene(root));
               primaryStage.show();
          
        } catch (IOException ex) {        
         Logger.getLogger(FormFx.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
