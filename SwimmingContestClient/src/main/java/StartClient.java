import GUI.LogInFXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import swimmingContest.network.SwimmingContestProxy;
import swimmingContest.services.ISwimmingContestServices;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/swimmingContestClient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find swimmingContestClient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("swimmingContest.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("swimmingContest.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISwimmingContestServices server = new SwimmingContestProxy(serverIP, serverPort);

//        primaryStage.setTitle("LogIn");
//        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
//        Pane pane = fxmlLoader.load();
//        LogInFXML logInFXML =fxmlLoader.getController();
//        logInFXML.setServer(server);
//
//
//        Scene scene=new Scene(pane);
//        primaryStage.setScene(scene);
//        logInFXML.setLogInStage(primaryStage);
//
//        FXMLLoader loaderContest=new FXMLLoader(getClass().getResource("/views/SwimmingContestView.fxml"));
//        Parent swimminContestParent=  loaderContest.load();
//        SwimmingContestFXML swimmingContestFXML = loaderContest.getController();
//        swimmingContestFXML.setServer(server);
//        logInFXML.setSwimmingContestFXML(swimmingContestFXML);
//        logInFXML.setParent(swimminContestParent);
//        primaryStage.show();

        primaryStage.setTitle("LogIn");
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        System.out.println(fxmlLoader);
        Pane pane = fxmlLoader.load();
        LogInFXML logInFXML =fxmlLoader.getController();
        logInFXML.setServer(server);

        Scene scene=new Scene(pane);
        primaryStage.setScene(scene);
        logInFXML.setLogInStage(primaryStage);
        primaryStage.show();

    }


}
