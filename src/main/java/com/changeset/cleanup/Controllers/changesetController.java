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
    public ResponseEntity<Changeset> getChangesetByID(@PathVariable("id") Long Id){
        logger.info("fetching the changeset data for the ID "+Id);
        return new ResponseEntity<>(changesetService.getChangesetByID(Id), HttpStatus.OK);
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
    public ResponseEntity<Changeset> deleteChangesetByID(@PathVariable("id") Long Id){
        logger.info("Calling the Service to delete the changeset Data  with ID is "+Id);
        return new ResponseEntity<>(changesetService.deleteChangesetByID(Id), HttpStatus.OK);
    }


    @DeleteMapping("/cleanup/date/from/{from}/to/{to}")
    public ResponseEntity <List<Long>>
    deleteChangesetByDate(@PathVariable("from") Timestamp from, @PathVariable("to") Timestamp to){
        logger.info("Deleting the changeset Data from {} to {} ", from,to);
        return new ResponseEntity<>(changesetService.deleteChangesetByDate(from,to),HttpStatus.OK);
    }



    @DeleteMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<List<Long>> deleteChangesetByDateAndPartyID(@PathVariable("from") Timestamp from ,
                                                          @PathVariable("to") Timestamp to,
                                                          @RequestParam(value = "orgId", required = true) Long orgId){

        if(orgId!=null){
            logger.info("Deleting the Changeset Data for Party ID {} from {} to {}",orgId,from
                    ,to);
            List<Long> ChangesetIDS= changesetService.deleteChangesetByDateAndParty(from,to,orgId);

            return  ResponseEntity.status(HttpStatus.OK).body(ChangesetIDS);
        }
        else{
            logger.error("party id is missing....");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }







}
