package attendance.gui.controller;

import attendance.be.User;
import attendance.gui.model.ModelCreator;
import attendance.gui.model.ModelException;
import attendance.gui.model.interfaces.ICourseModel;
import attendance.gui.model.interfaces.IUserModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pwPassword;
    @FXML
    private JFXButton btnLogIn;

    private final String ROOT_STUDENT = "/attendance/gui/view/RootStudent.fxml";
    private final String SUBJECT_CHOOSER = "/attendance/gui/view/CourseSelectionForTeacher.fxml";
    private User user;
    private IUserModel userModel;
    private ICourseModel courseModel;

    public LoginController() throws Exception {
        userModel = ModelCreator.getInstance().getUserModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fieldValidator();
    }

    private void showRoot(String ROOT_TO_SHOW) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ROOT_TO_SHOW));
            Parent root = fxmlLoader.load();
            courseModel = ModelCreator.getInstance().getCourseModel();
            if (ROOT_TO_SHOW.equals(ROOT_STUDENT)) {
                RootStudentController controller = fxmlLoader.getController();
                controller.setUser(user);
                controller.injectModel(courseModel);
            }
            if (ROOT_TO_SHOW.equals(SUBJECT_CHOOSER)) {
                CourseSelectionForTeacherController controller = fxmlLoader.getController();
                controller.setUser(user);
                controller.injectModel(courseModel);
                controller.initializeComboBox();
            }
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fieldValidator() {//   could be also decoupled 
        RequiredFieldValidator usernameValidator = new RequiredFieldValidator();
        RequiredFieldValidator passwordValidator = new RequiredFieldValidator();
        txtUsername.getValidators().add(usernameValidator);
        usernameValidator.setMessage("Please fill out username.");
        pwPassword.getValidators().add(passwordValidator);
        passwordValidator.setMessage("Please fill out password");
        txtUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtUsername.validate();
            }
        });
        pwPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                pwPassword.validate();
            }
        });
    }

    private void authentication() throws Exception {
        try {
            this.user = userModel.login(txtUsername.getText(), pwPassword.getText());
        } catch (ModelException ex) {
            showAlert(ex);
        }
        if (user != null) {
            if (user.getType() == User.UserType.TEACHER) {
                showRoot(SUBJECT_CHOOSER);
                closeLogin();
            } else if (user.getType() == User.UserType.STUDENT) {
                showRoot(ROOT_STUDENT);
                closeLogin();
            }
        }
    }

    private void showAlert(Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.show();
        if (a.getResult() == ButtonType.OK) {
        }
    }

    @FXML//   could be also decoupled 
    private void BtnPressed(ActionEvent event) throws IOException, Exception {
        authentication();
    }

    @FXML//   could be also decoupled 
    private void EnterPressed(javafx.scene.input.KeyEvent event) throws IOException, InterruptedException, Exception {
        if (event.getCode() == KeyCode.ENTER) {
            authentication();
        }
    }

    private void closeLogin() {
        Stage loginStage;
        loginStage = (Stage) btnLogIn.getScene().getWindow();
        loginStage.close();
    }

}
