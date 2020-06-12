package start;

import swimmingContest.model.SwimmingEvent;
import swimmingContest.model.SwimmingStroke;
import swimmingContest.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.SwimmingEventsClient;


public class StartRestClient {
    private final static SwimmingEventsClient swimmingEventsClient=new SwimmingEventsClient();
    public static void main(String[] args) {
        RestTemplate restTemplate=new RestTemplate();
        System.out.println("Get swimmingEvent: ");
        SwimmingEvent swimmingEventTest=new SwimmingEvent(SwimmingStroke.backstroke,600);
        swimmingEventTest.setId(6L);
        try{
            show(()-> System.out.println(swimmingEventsClient.create(swimmingEventTest)));
            show(()->{
                System.out.println("Get all swimmingEvents: ");
                SwimmingEvent[] res=swimmingEventsClient.getAll();
                for(SwimmingEvent sw:res){
                    System.out.println(sw.getId()+": "+sw.getSwimmingStroke()+": "+sw.getDistance());
                }
            });
            show(()->{
                System.out.println("Before updating: ");
                SwimmingEvent res = swimmingEventsClient.getById(6L);
                System.out.println(res);
            });
            show(()->{
                System.out.println("After updating: ");
                SwimmingEvent update = new SwimmingEvent(SwimmingStroke.backstroke,610);
                update.setId(6L);
                swimmingEventsClient.update(update);
                SwimmingEvent res = swimmingEventsClient.getById(6L);
                System.out.println(res);
            });
            show(()->{
                swimmingEventsClient.delete(6L);
                System.out.println("Deleted swimmingEvent with id: 6");
                SwimmingEvent[] res=swimmingEventsClient.getAll();
                for(SwimmingEvent sw:res){
                    System.out.println(sw.getId()+": "+sw.getSwimmingStroke()+": "+sw.getDistance());
                }
            });

        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
