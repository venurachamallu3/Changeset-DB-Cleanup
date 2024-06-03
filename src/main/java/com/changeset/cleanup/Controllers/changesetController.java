package com.changeset.cleanup.Controllers;


import com.changeset.cleanup.DAO.ChangesetDAO;
import com.changeset.cleanup.Exception.IDNotFoundException;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Model.Delta;
import com.changeset.cleanup.Service.ChangesetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("changeset")
@AllArgsConstructor
@NoArgsConstructor
public class changesetController {

    private static  final Logger logger = (Logger) LoggerFactory.getLogger(changesetController.class);



    @Autowired
    public ChangesetService changesetService;

    @GetMapping("/health")
    public  String getCustomerID(){
        System.out.println("Health Controller...");
        return "Health Controller...";
    }

    @GetMapping("/cleanup/{id}")
    public Changeset getChangesetByID(@PathVariable("id") Long Id){
        logger.info("fetching the changeset data for the ID "+Id);
        return changesetService.getChangesetByID(Id);
    }


    @GetMapping("/cleanup/date/from/{from}/to/{to}")
    public List<Long> getChangesetsByDate(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
//        @PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to
//        System.out.println("From date  is "+  from +" to date is "+to );
        logger.info("Changeset Clean up data from {} to{} ",from, to);
//        System.out.println("I'm in Controller..");
        return changesetService.getChangsetsByDate(from,to);
    }


    @GetMapping("/cleanup/party/from/{from}/to/{to}")
    public List<Long> getChangesetPartyAndDate(@PathVariable("from") Timestamp from ,
                                               @PathVariable("to") Timestamp to,
                                               @RequestParam(value = "orgId", required = true) Long orgId){
        logger.info("Changeset Data for party id {} from {} to {}  ",orgId,from,to);
         return changesetService.getchangesetDataByPartyDate(from,to,orgId);
    }


    @DeleteMapping("/cleanup/{id}")
    public Changeset deleteChangesetByID(@PathVariable("id") Long Id){
//        if(Id==null) throw new IDNotFoundException("Changeset ID is Missing in the URL, please provide the Changeset ID ");
        logger.info("Calling the Service to delete the changeset Data  with ID is "+Id);
        return changesetService.deleteChangesetByID(Id);
//        logger.info("CHANGESET DATA DELETED SUCCESSFULLY with ID is {}",Id);
//        return "DELETED SUCCESSFULLY.......";
    }

    @DeleteMapping("/cleanup/date/from/{from}/to/{to}")
    public String deleteChangesetByDate(@PathVariable("from") Timestamp from, @PathVariable("to") Timestamp to){

        logger.info("Deleting the changeset Data from {} to {} ", from,to);
        changesetService.deleteChangesetByDate(from,to);
        logger.info("Successfully deleted the Changeset Data from {} to {} ",from,to);
        return "deleted changeset Data from "+ from + "to "+to ;
    }



    @DeleteMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<String> deleteChangesetByDateAndPartyID(@PathVariable("from") Timestamp from ,
                                                          @PathVariable("to") Timestamp to,
                                                          @RequestParam(value = "orgId", required = true) Long orgId){

        if(orgId!=null){
//            System.out.println("DELETING the Changeset By Party id "+ orgId);


            logger.info("Deleting the Changeset Data for Party ID {} from {} to {}",orgId,from
                    ,to);
            String delData = changesetService.deleteChangesetByDateAndParty(from,to,orgId);

            logger.info("Deleted the Changeset Data for Party ID {} from {} to {}",orgId,from
                    ,to);
            return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
            System.out.println("party id is missing....");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }







}
