package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import swimmingContest.model.User;
import swimmingContest.services.ISwimmingContestServices;
import swimmingContest.services.SwimmingContestException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInFXML implements Initializable {
    @FXML
    TextField UserFirstName;
    @FXML
    TextField UserLastName;
    @FXML
    PasswordField UserPassword;
    @FXML
    Button LogInButton;
    @FXML
    Label PasswordConfirmation;

    private ISwimmingContestServices server;
    private SwimmingContestFXML swimmingContestFXML;
    private User crtUser;

    private Stage logInStage ;

    public LogInFXML() { }
    public void setServer(ISwimmingContestServices s){
        server=s;
    }

    Parent swimmingContestParent;

    public void setParent(Parent p){
        swimmingContestParent=p;
    }


    public void setLogInStage(Stage logInStage){this.logInStage=logInStage;}
    public  void handleLogIn(){
        try {
            User userLoggingIn=new User(UserFirstName.getText(),UserLastName.getText(),UserPassword.getText());
            crtUser=userLoggingIn;
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/SwimmingContestView.fxml"));
            AnchorPane root;
            root = fxmlLoader.load();
            swimmingContestFXML=fxmlLoader.getController();;
            server.login(crtUser,swimmingContestFXML);
            System.out.println(swimmingContestFXML);
            PasswordConfirmation.setText("Your password has been confirmed");
            PasswordConfirmation.setTextFill(Color.rgb(21, 117, 84));
            Stage swimmingContestStage=new Stage();
            swimmingContestStage.setTitle(crtUser.getFirstName()+" "+crtUser.getLastName());
            swimmingContestStage.initModality(Modality.WINDOW_MODAL);
            Scene scene =new Scene(root);
            swimmingContestStage.setScene(scene);
            swimmingContestFXML.setStage(swimmingContestStage);
            swimmingContestStage.setTitle(crtUser.getFirstName()+" "+crtUser.getLastName());
            swimmingContestStage.initModality(Modality.WINDOW_MODAL);
            swimmingContestFXML.setServer(server);
            swimmingContestFXML.setUser(crtUser);
            swimmingContestFXML.initModel();
            swimmingContestStage.show();
        } catch (SwimmingContestException | IOException e) {
            e.printStackTrace();
        }
    }

//    public void showSwimmingContestDialog(){
//        FXMLLoader fxmlLoader=new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("/views/SwimmingContestView.fxml"));
//        AnchorPane root;
//        try {
//            root = fxmlLoader.load();
//            swimmingContestFXML=fxmlLoader.getController();;
//            swimmingContestFXML.setServer(server);
//            swimmingContestFXML.setUser(crtUser);
//            swimmingContestFXML.initModel();
//            Stage swimmingContestStage=new Stage();
//            swimmingContestStage.setTitle(crtUser.getFirstName()+" "+crtUser.getLastName());
//            swimmingContestStage.initModality(Modality.WINDOW_MODAL);
//            Scene scene =new Scene(root);
//            swimmingContestStage.setScene(scene);
//            swimmingContestFXML.setStage(swimmingContestStage);
//
//            swimmingContestStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void setUser(User user) {
        this.crtUser = user;
    }

    public void setSwimmingContestFXML(SwimmingContestFXML swimmingContestFXML){this.swimmingContestFXML=swimmingContestFXML;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PasswordConfirmation.setText("Your password is not completed!");
        PasswordConfirmation.setTextFill(Color.rgb(210, 39, 30));
        UserPassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleLogIn();
            }
        });
    }
}
