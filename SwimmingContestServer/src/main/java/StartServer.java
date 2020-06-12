import swimmingContest.network.utils.AbstractServer;
import swimmingContest.network.utils.ServerException;
import swimmingContest.network.utils.SwimmingContestConcurrentServer;
import swimmingContest.persistence.repository.ParticipantJdbcRepository;
import swimmingContest.persistence.repository.SignUpJdbcRepository;
import swimmingContest.persistence.repository.SwimmingEventJdbcRepository;
import swimmingContest.persistence.repository.UserJdbcRepository;
import swimmingContest.server.SwimmingContestServerImpl;
import swimmingContest.services.ISwimmingContestServices;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/swimmingContestServer.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find swimmingContestServer.properties "+e);
            return;
        }
        UserJdbcRepository userJdbcRepository=new UserJdbcRepository(serverProps);
        ParticipantJdbcRepository participantJdbcRepository=new ParticipantJdbcRepository(serverProps);
        SwimmingEventJdbcRepository swimmingEventJdbcRepository=new SwimmingEventJdbcRepository(serverProps);
        SignUpJdbcRepository signUpJdbcRepository=new SignUpJdbcRepository(serverProps);
        ISwimmingContestServices swimmingContestServerImpl=new SwimmingContestServerImpl(userJdbcRepository, participantJdbcRepository,swimmingEventJdbcRepository,signUpJdbcRepository);
        int swimmingContestServerPort=defaultPort;
        try {
            swimmingContestServerPort = Integer.parseInt(serverProps.getProperty("swimmingContest.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+swimmingContestServerPort);
        AbstractServer server = new SwimmingContestConcurrentServer(swimmingContestServerPort, swimmingContestServerImpl) {
        };
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
