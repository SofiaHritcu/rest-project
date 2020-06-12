package swimmingContest.server;

import swimmingContest.model.*;
import swimmingContest.persistence.repository.ParticipantJdbcRepository;
import swimmingContest.persistence.repository.SignUpJdbcRepository;
import swimmingContest.persistence.repository.SwimmingEventJdbcRepository;
import swimmingContest.persistence.repository.UserJdbcRepository;
import swimmingContest.services.ISwimmingContestObserver;
import swimmingContest.services.ISwimmingContestServices;
import swimmingContest.services.SwimmingContestException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SwimmingContestServerImpl implements ISwimmingContestServices {
    private UserJdbcRepository userJdbcRepository;
    private ParticipantJdbcRepository participantJdbcRepository;
    private SwimmingEventJdbcRepository swimmingEventJdbcRepository;
    private SignUpJdbcRepository signUpJdbcRepository;
    private Map<Long, ISwimmingContestObserver> loggedClients;

    public SwimmingContestServerImpl(UserJdbcRepository userJdbcRepository,ParticipantJdbcRepository participantJdbcRepository,SwimmingEventJdbcRepository swimmingEventJdbcRepository,SignUpJdbcRepository signUpJdbcRepository) {
        this.userJdbcRepository=userJdbcRepository;
        this.participantJdbcRepository=participantJdbcRepository;
        this.swimmingEventJdbcRepository=swimmingEventJdbcRepository;
        this.signUpJdbcRepository=signUpJdbcRepository;
        loggedClients=new ConcurrentHashMap<>();;
    }


    @Override
    public synchronized void login(User user, ISwimmingContestObserver client) throws SwimmingContestException {
        User userR=userJdbcRepository.findOneByNamePassword(user.getFirstName(),user.getLastName(),user.getPassword());
        if (userR!=null){
            if(loggedClients.get(userR.getId())!=null)
                throw new SwimmingContestException("User already logged in.");
            loggedClients.put(userR.getId(), client);
        }else
            throw new SwimmingContestException("Authentication failed.");
    }

    @Override
    public void logout(User user, ISwimmingContestObserver client) throws SwimmingContestException {
        ISwimmingContestObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new SwimmingContestException("User "+user.getId()+" is not logged in.");
    }

    @Override
    public synchronized void addSignUp(SignUpDTO signUpDTO) throws SwimmingContestException {
        signUpJdbcRepository.save(signUpDTO);
        signUpDTO.setSignUps((List<SignUp>) signUpJdbcRepository.findAllSignUps());
        signUpDTO.setParticipants((List<Participant>) participantJdbcRepository.findAll());
        signUpDTO.setSwimmingEvents((List<SwimmingEvent>) swimmingEventJdbcRepository.findAll());
        notifyUsers(signUpDTO);
    }

    private final int defaultThreadsNo=5;
    public void notifyUsers(SignUpDTO signUpDTO){
        Iterable<User> users=getUsers();
        System.out.println("Logged "+users);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(User us :users){
            ISwimmingContestObserver swimmingContestClient = loggedClients.get(us.getId());
            if (swimmingContestClient !=null){
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us.getId()+ "] other user added a signUp  ["+signUpDTO.getId()+"] .");
                        System.out.println(swimmingContestClient);
                        swimmingContestClient.addedSignUp(signUpDTO);
                    } catch (SwimmingContestException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
            }
        }
        executor.shutdown();
    }

    private synchronized Iterable<User> getUsers(){
        return userJdbcRepository.findAll();
    }

    @Override
    public Iterable<Participant> getParticipantsSwimmingEvent(SwimmingEvent swimmingEvent) throws SwimmingContestException {
        System.out.println("Participants for swimmingEvent: "+swimmingEvent.getId());
        Iterable<Participant> result=signUpJdbcRepository.findParticipantsBySwimmingEvent(swimmingEvent.getId());
        int size=0,poz=0;
        for (Participant p : result){
            size+=1;
        }
        System.out.println("Number of participants for swimming event: "+size);
        return result;
    }

    @Override
    public Iterable<SwimmingEvent> getSwimmingEventsParticipant(Participant participant) throws SwimmingContestException {
        System.out.println("SwimmingEvents for participant: "+participant.getId());
        Iterable<SwimmingEvent> result=signUpJdbcRepository.findSwimmingEventsByParticipant(participant.getId());
        int size=0,poz=0;
        for (SwimmingEvent sw : result){
            size+=1;
        }
        System.out.println("Number of swimmingEvents for participant: "+size);
        return result;
    }

    @Override
    public Iterable<Participant> findAllParticipants() throws SwimmingContestException {
        System.out.println("participants: ");
        Iterable<Participant> result=participantJdbcRepository.findAll();
        int size=0,poz=0;
        for (Participant p : result){
            size+=1;
        }
        System.out.println("Number of participants: "+size);
        return result;
    }



    @Override
    public Participant findParticipant(String firstName, String lastName, Integer age) throws SwimmingContestException {
        System.out.println("Participant with : "+firstName+" "+lastName+" "+age);
        Participant result=participantJdbcRepository.findParticipantByNameAge(firstName,lastName,age);
        System.out.println("Found participant: "+result);
        return result;
    }

    @Override
    public void addParticipant(ParticipantDTO participant) throws SwimmingContestException {
        System.out.println("Adding participant: "+participant.getId());
        participantJdbcRepository.save(participant);
        System.out.println("Participant saved");
    }

    @Override
    public Integer filterNumberOfParticipantsBySwimmingEvent(SwimmingEvent swimmingEvent) throws SwimmingContestException {
        System.out.println("Number of participants for swimmingEvent: "+swimmingEvent.getId());
        Integer result=signUpJdbcRepository.findNumberOfParticipantsBySwimmingEvent(swimmingEvent.getId());
        System.out.println("Number of participants for swimming event: "+result);
        return result;
    }

    @Override
    public Iterable<SignUp> findAllSignUps() throws SwimmingContestException {
        System.out.println("SignUps : ");
        Iterable<SignUp> result=signUpJdbcRepository.findAll();
        int size=0,poz=0;
        for (SignUp p : result){
            size+=1;
        }
        System.out.println("Number of signUps: "+size);
        return result;
    }

    @Override
    public Iterable<SwimmingEvent> findAllSwimmingEvents() {
        System.out.println("Swimming Events: ");
        Iterable<SwimmingEvent> result=swimmingEventJdbcRepository.findAll();
        int size=0,poz=0;
        for (SwimmingEvent sw : result){
            size+=1;
        }
        System.out.println("Number of swimmingEvents: "+size);
        return result;
    }

    @Override
    public SwimmingEvent findSwimmingEventByStrokeDistance(SwimmingStroke swimmingStroke, Integer distance) throws SwimmingContestException {
        System.out.println("SwimmingEvent with : "+swimmingStroke.toString()+" "+distance);
        SwimmingEvent result=swimmingEventJdbcRepository.findSwimmingEventByStyleDistance(swimmingStroke,distance);
        System.out.println("SwimmingEvent: "+result.getId());
        return result;
    }


    @Override
    public User findOneUserNamePassword(User user) throws SwimmingContestException {
        System.out.println("User with name and password :"+user.getFirstName()+" "+user.toString()+" "+user.getPassword());
        User userFound=userJdbcRepository.findOneByNamePassword(user.getFirstName(),user.getLastName(),user.getPassword());
        return userFound;
    }

    @Override
    public User findOneUserName(User user) throws SwimmingContestException {
        System.out.println("User with name  :"+user.getFirstName()+" "+user.toString()+" ");
        User userFound=userJdbcRepository.findOneByName(user.getFirstName(),user.getLastName());
        return userFound;
    }
}
