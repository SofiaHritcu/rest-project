package GUI;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import swimmingContest.model.*;
import swimmingContest.services.ISwimmingContestObserver;
import swimmingContest.services.ISwimmingContestServices;
import swimmingContest.services.SwimmingContestException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SwimmingContestFXML implements ISwimmingContestObserver {
    private Stage stage;
    private ISwimmingContestServices server;
    private User user;
    private ObservableList<SwimmingEventDTO> modelSwimmingEventDTO = FXCollections.observableArrayList();
    private ObservableList<ParticipantDTO> modelParticipantDTO = FXCollections.observableArrayList();
    private ObservableList<SwimmingEvent> modelSwimmingEvent=FXCollections.observableArrayList();
    private ObservableList<CheckBox> checkBoxes= FXCollections.observableArrayList();
    private ObservableList<SignUp> modelSignUp=FXCollections.observableArrayList();

    @FXML
    TableView<SwimmingEventDTO> SwimmingEventsTableView;

    @FXML
    TableColumn<SwimmingEventDTO,Integer> DistanceColumn;

    @FXML
    TableColumn<SwimmingEventDTO, SwimmingStroke> SwimmingStrokeColumn;

    @FXML
    TableColumn<SwimmingEventDTO,Integer> NumberOfParticipantsColumn;
    //----------------------end TableView fx:id----------------


    @FXML
    TableView<ParticipantDTO> ParticipantsTableView;

    @FXML
    TableColumn<ParticipantDTO,String> FirstNameColumn;

    @FXML
    TableColumn<ParticipantDTO,String> LastNameColumn;

    @FXML
    TableColumn<ParticipantDTO,Integer> AgeColumn;

    @FXML
    TableColumn<ParticipantDTO, List<SwimmingEvent>> SignUpsColumn;
    //----------------------end TableView fx:id----------------

    @FXML
    Label FirstNameLabel;

    @FXML
    Label LastNameLabel;

    @FXML
    Label AgeLabel;

    @FXML
    TextField FirstNameField;

    @FXML
    TextField LastNameField;

    @FXML
    TextField AgeField;

    @FXML
    Button addSignUpButton;

    @FXML
    Button reloadButton;

    //-------------------------------------
    @FXML
    GridPane gridPaneSignUp;



    public void setServer(ISwimmingContestServices s) {
        server = s;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void setStage(Stage stage){this.stage=stage;}

    @FXML
    public void initialize(){
        DistanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        SwimmingStrokeColumn.setCellValueFactory(new PropertyValueFactory<>("swimmingStroke"));
        NumberOfParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));

        FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        AgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        SignUpsColumn.setCellValueFactory(new PropertyValueFactory<>("signedUpFor"));

        SwimmingEventsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SwimmingEventDTO>() {
            @Override
            public void changed(ObservableValue<? extends SwimmingEventDTO> observable, SwimmingEventDTO oldValue, SwimmingEventDTO newValue) {
                try {
                    if(newValue != null) {
                        modelParticipantDTO.setAll(toDTOParticipant(server.getParticipantsSwimmingEvent(newValue)));
                    }
                    else{
                        modelSignUp .setAll((Collection<? extends SignUp>) server.findAllSignUps());

                        modelParticipantDTO.setAll(toDTOParticipant(server.getParticipantsSwimmingEvent(oldValue)));
                    }
                    System.out.println(modelParticipantDTO.toString());
                } catch (SwimmingContestException e) {
                    e.printStackTrace();
                }
                if(modelParticipantDTO.size()==0){
                   ParticipantsTableView.setPlaceholder(new Label("No participants are signed up \n for this swimming Event"));
               }else{
                   ParticipantsTableView.setItems(modelParticipantDTO);
               }
            }
        });
        if(modelSwimmingEvent.size()!=0){
            initCheckBoxes();
        }
    }

    public void initModel(){
        try {
            modelSwimmingEvent.setAll((Collection<? extends SwimmingEvent>) server.findAllSwimmingEvents());
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        try {
            modelSignUp.setAll((Collection<? extends SignUp>) server.findAllSignUps());
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        modelSwimmingEventDTO.setAll(toDTOSwimmimngEvent());
        SwimmingEventsTableView.setItems(modelSwimmingEventDTO);
        try {
            modelParticipantDTO.setAll(toDTOParticipant(server.findAllParticipants()));
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        ParticipantsTableView.setItems(modelParticipantDTO);
        initCheckBoxes();
    }

    public void initModelSwimmingEvents(){
        try {
            modelSwimmingEvent.setAll((Collection<? extends SwimmingEvent>) server.findAllSwimmingEvents());
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        try {
            modelSignUp.setAll((Collection<? extends SignUp>) server.findAllSignUps());
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        System.out.println("Aici");
        modelSwimmingEventDTO.setAll(toDTOSwimmimngEvent());
        System.out.println("Aici2");
        SwimmingEventsTableView.setItems(modelSwimmingEventDTO);
        System.out.println("Aici3");
        try {
            modelParticipantDTO.setAll(toDTOParticipant(server.findAllParticipants()));
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        ParticipantsTableView.setItems(modelParticipantDTO);
    }

    private void initCheckBoxes(){
        modelSwimmingEvent.forEach(x->{
            CheckBox checkBox=new CheckBox(x.toString());
            checkBoxes.add(checkBox);
            gridPaneSignUp.add(checkBox,1,gridPaneSignUp.getRowCount());
        });

    }


    public List<SwimmingEventDTO> toDTOSwimmimngEvent(){
        List<SwimmingEventDTO> swimmingEventDTOS=new ArrayList<>();
        modelSwimmingEvent.forEach(x->
        {
            SwimmingEventDTO swimmEv= null;
            System.out.println(x);
            swimmEv = new SwimmingEventDTO(x.getSwimmingStroke(),x.getDistance(),filterNumberOfParticipantsBySwimmingEvent(x));
            swimmEv.setId(x.getId());
            swimmingEventDTOS.add(swimmEv);
        });
        return  swimmingEventDTOS;
    }

    public List<SwimmingEventDTO> toDTOSwimmimngEventFrom(Iterable<SwimmingEvent> swimmingEvents){
        List<SwimmingEventDTO> swimmingEventDTOS=new ArrayList<>();
        swimmingEvents.forEach(x->
        {
            SwimmingEventDTO swimmEv= null;
            System.out.println(x);
            swimmEv = new SwimmingEventDTO(x.getSwimmingStroke(),x.getDistance(),filterNumberOfParticipantsBySwimmingEvent(x));
            swimmEv.setId(x.getId());
            swimmingEventDTOS.add(swimmEv);
        });
        return  swimmingEventDTOS;
    }

    private Integer filterNumberOfParticipantsBySwimmingEvent(SwimmingEvent swimmingEvent){
        Integer numberOfParticipants=0;
        for (SignUp signUp : modelSignUp){
            if(signUp.getId().getValue() ==swimmingEvent.getId()){
                numberOfParticipants+=1;
            }
        }
        return numberOfParticipants;
    }


    public List<ParticipantDTO> toDTOParticipant(Iterable<Participant> participants){
        List<ParticipantDTO> participantDTOS=new ArrayList<>();
        participants.forEach(x->
        {
            ParticipantDTO participantDTO= null;
            List<SwimmingEvent> signedUpFor = new ArrayList<>();
            for ( SignUp signUp : modelSignUp){
                for (SwimmingEvent sw : modelSwimmingEvent)
                {
                    if (signUp.getId().getKey().equals(x.getId()) &&  signUp.getId().getValue().equals(sw.getId())){
                        signedUpFor.add(sw);
                    }
                }
            }
            participantDTO = new ParticipantDTO(x.getFirstName(),x.getLastName(),x.getAge(), signedUpFor);
            participantDTO.setId(x.getId());
            participantDTOS.add(participantDTO);
        });
        return participantDTOS;
    }

    private Iterable<SwimmingEvent> getSwimmingEventsByParticipant(Participant x) throws SwimmingContestException {
        List<SwimmingEvent> swimmingEvents=new ArrayList<>();
        for ( SignUp signUp :modelSignUp){
            if (signUp.getId().getKey()==x.getId()){
                for (SwimmingEvent swimmingEvent:modelSwimmingEvent){
                    if (signUp.getId().getValue()==swimmingEvent.getId()){
                        swimmingEvents.add(swimmingEvent);
                    }
                }
            }
        }
        return swimmingEvents;
    }

    public void handleAddSignUp(){
        List<SwimmingEvent> swimmingEvents=new ArrayList<>();
        for (CheckBox checkBox : checkBoxes){
            if (checkBox.isSelected()){
                try {
                    swimmingEvents.add(server.findSwimmingEventByStrokeDistance(SwimmingStroke.valueOf(checkBox.getText().split(" ")[0]),Integer.parseInt(checkBox.getText().split(" ")[1])));
                } catch (SwimmingContestException e) {
                    e.printStackTrace();
                }
            }
        }
        if(swimmingEvents.size()==0 || FirstNameField.getText()==null || LastNameField.getText()==null || AgeField.getText()==null ){
            MessageAlert.showErrorMessage(null,"Cannot complete sign up !Information missing!");

        }else {
            for (SwimmingEvent swimmingEvent : swimmingEvents) {
                SignUpDTO signUpDTO = new SignUpDTO();
                try {
                    if (server.findParticipant(FirstNameField.getText(), LastNameField.getText(), Integer.parseInt(AgeField.getText())) == null) {
                        ParticipantDTO participantToSignUp = new ParticipantDTO(FirstNameField.getText(), LastNameField.getText(), Integer.parseInt(AgeField.getText()),swimmingEvents);
                        try {
                            server.addParticipant(participantToSignUp);
                        } catch (SwimmingContestException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SwimmingContestException e) {
                    e.printStackTrace();
                }
                try {
                    signUpDTO.setId(new Pair<>(server.findParticipant(FirstNameField.getText(), LastNameField.getText(), Integer.parseInt(AgeField.getText())).getId(), swimmingEvent.getId()));
                    server.addSignUp(signUpDTO);
                } catch (SwimmingContestException e) {
                    e.printStackTrace();
                }
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "SignUp", "SignUp completed successfully!");
        }
    }

    public void handleLogOut(){
        try {
            server.logout(user,this);
        } catch (SwimmingContestException e) {
            e.printStackTrace();
        }
        stage.close();
    }

    @Override
    public synchronized void addedSignUp(SignUpDTO signUpDTO) throws SwimmingContestException {
        Platform.runLater(()->{
            SwimmingEventsTableView.getItems().removeAll(SwimmingEventsTableView.getItems());
            List<SwimmingEvent> swimmingEvents=new ArrayList<>();
            for (SwimmingEvent swimmingEvent:signUpDTO.getSwimmingEvents()){
                swimmingEvents.add(swimmingEvent);
            }
            this.modelSwimmingEvent=FXCollections.observableArrayList(swimmingEvents);

            List<SignUp> signUps=new ArrayList<>();
            for(SignUp signUp:signUpDTO.getSignUps()){
                signUps.add(signUp);
            }
            this.modelSignUp=FXCollections.observableArrayList(signUps);

            List<SwimmingEventDTO> swimmingEventDTOS=new ArrayList<>();
            for(SwimmingEvent swimmingEvent:modelSwimmingEvent){
                SwimmingEventDTO swimmingEventDTO=new SwimmingEventDTO(swimmingEvent.getSwimmingStroke(),swimmingEvent.getDistance(),filterNumberOfParticipantsBySwimmingEvent(swimmingEvent));
                swimmingEventDTO.setId(swimmingEvent.getId());
                swimmingEventDTOS.add(swimmingEventDTO);
            }
            this. modelSwimmingEventDTO=FXCollections.observableArrayList(swimmingEventDTOS);

            List<ParticipantDTO> participantDTOS=new ArrayList<>();
            for (SignUp signUp :signUps){
                if(signUp.getId().getValue().equals(signUpDTO.getId().getValue())){
                    for(Participant p:signUpDTO.getParticipants()){
                        if(p.getId().equals(signUpDTO.getId().getKey())){
                            System.out.println(signUp.getId());
                            List<SwimmingEvent> signedUpFor = new ArrayList<>();
                            ParticipantDTO participantDTO=new ParticipantDTO(p.getFirstName(),p.getLastName(),p.getAge(), signedUpFor);
                            participantDTO.setId(p.getId());
                            signedUpFor = participantDTO.getSignedUpFor();
                            for(SwimmingEvent sw:signUpDTO.getSwimmingEvents()){
                                if(sw.getId().equals(signUp.getId().getValue())){
                                    signedUpFor.add(sw);
                                    break;
                                }
                            }
                            participantDTO.setSignedUpFor(signedUpFor);
                            participantDTOS.add(participantDTO);
                        }
                    }
                }
            }
            participantDTOS.forEach(x-> System.out.println(x));
            this.modelParticipantDTO=FXCollections.observableArrayList(participantDTOS);
            SwimmingEventsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SwimmingEventDTO>() {
                @Override
                public void changed(ObservableValue<? extends SwimmingEventDTO> observable, SwimmingEventDTO oldValue, SwimmingEventDTO newValue) {
                    try {
                        if(newValue != null) {
                            modelParticipantDTO.setAll(toDTOParticipant(server.getParticipantsSwimmingEvent(newValue)));
                            System.out.println(modelParticipantDTO.toString());
                        }
                        else{
                            modelParticipantDTO.setAll(toDTOParticipant(server.getParticipantsSwimmingEvent(oldValue)));
                            System.out.println(modelParticipantDTO.toString());
                        }
                    } catch (SwimmingContestException e) {
                        e.printStackTrace();
                    }
                    if(modelParticipantDTO.size()==0){
                        ParticipantsTableView.setPlaceholder(new Label("No participants are signed up \n for this swimming Event"));
                    }else{
                        ParticipantsTableView.setItems(modelParticipantDTO);
                    }
                }
            });

            SwimmingEventsTableView.setItems(modelSwimmingEventDTO);
            //SwimmingEventsTableView.getItems().forEach(x-> System.out.println(x));
            ParticipantsTableView.setItems(modelParticipantDTO);
            ParticipantsTableView.getItems().forEach(x-> System.out.println(x));
        });
    }

    public void ReloadButton() {
        System.out.println(this.modelSwimmingEventDTO);
        System.out.println(this.modelParticipantDTO);
        System.out.println(this.SwimmingEventsTableView);
        System.out.println(this.ParticipantsTableView);
        SwimmingEventsTableView.setItems(modelSwimmingEventDTO);
        SwimmingEventsTableView.getItems().forEach(x-> System.out.println(x));
        ParticipantsTableView.setItems(modelParticipantDTO);
        ParticipantsTableView.getItems().forEach(x-> System.out.println(x));
    }
}
