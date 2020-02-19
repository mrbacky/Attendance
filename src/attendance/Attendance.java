/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance;

import attendance.gui.controller.DashboardController;
import attendance.gui.controller.TodayController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Attendance extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    // comment
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EASV Attendance");
        
        initRootLayout();
        showTodayScene();
    }

    
    /**
     * Shows Today scene inside the root layout.
     */
    public void showDashboardScene(){
        try {
            //  Load Today scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Attendance.class.getResource("/attendance/gui/view/Dashboard.fxml"));
            AnchorPane Dashboard = (AnchorPane) loader.load();
            
             // Set person overview into the center of root layout.
            rootLayout.setCenter(Dashboard);
            
            // Give the controller access to the main app.
            DashboardController controller = loader.getController();
            controller.setAttendance(this);
            
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
            
            
            
            
        } catch (Exception e) {
        }
        
    }
    
    /**
     * Shows Today scene inside the root layout.
     */
    public void showTodayScene(){
        try {
            //  Load Today scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Attendance.class.getResource("/attendance/gui/view/Today.fxml"));
            AnchorPane TodayScene = (AnchorPane) loader.load();
            
             // Set person overview into the center of root layout.
            rootLayout.setCenter(TodayScene);
            
            // Give the controller access to the main app.
            TodayController controller = loader.getController();
            controller.setAttendance(this);
            
        } catch (Exception e) {
        }
        
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Attendance.class.getResource("/attendance/gui/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
