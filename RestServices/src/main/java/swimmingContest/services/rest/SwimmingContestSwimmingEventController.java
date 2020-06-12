package swimmingContest.services.rest;


import swimmingContest.model.SwimmingEvent;
import swimmingContest.persistence.repository.RepositoryException;
import swimmingContest.persistence.SwimmingEventInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/swimmingContest/swimmingEvents")
public class SwimmingContestSwimmingEventController {

    @Autowired
    private SwimmingEventInterface swimmingEventRepository;

    @RequestMapping( method=RequestMethod.GET)
    public SwimmingEvent[] getAll(){
        return swimmingEventRepository.getSwimmingEvents();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id){

        SwimmingEvent swimmingEvent=swimmingEventRepository.findOne(id);
        if (swimmingEvent==null)
            return new ResponseEntity<String>("SwimmingEvent not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<SwimmingEvent>(swimmingEvent, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST)
    public SwimmingEvent create(@RequestBody SwimmingEvent swimmingEvent){
        swimmingEventRepository.save(swimmingEvent);
        return swimmingEvent;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SwimmingEvent update(@RequestBody SwimmingEvent swimmingEvent) {
        System.out.println("Updating swimmingEvent ...");
        swimmingEventRepository.update(swimmingEvent.getId(),swimmingEvent);
        return swimmingEvent;

    }

    @RequestMapping(value="/{swimmingEventId}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long swimmingEventId){
        System.out.println("Deleting swimmingEvent ... "+swimmingEventId);
        try {
            swimmingEventRepository.delete(swimmingEventId);
            return new ResponseEntity<SwimmingEvent>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete swimmingEvent exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{swimmingEvent}/distance")
    public Integer distance(@PathVariable Long swimmingEvent){
        SwimmingEvent result=swimmingEventRepository.findOne(swimmingEvent);
        System.out.println("Result ..."+result);

        return result.getDistance();
    }



    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String swimmingEventError(RepositoryException e) {
        return e.getMessage();
    }
}
