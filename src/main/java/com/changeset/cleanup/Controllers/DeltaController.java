package com.changeset.cleanup.Controllers;

import com.changeset.cleanup.Model.Delta;
import com.changeset.cleanup.Service.DeltaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/delta")
public class DeltaController {


    private static  final Logger logger = (Logger) LoggerFactory.getLogger(DeltaController.class);


    @Autowired
    public DeltaService deltaService;
    
    @GetMapping("/health")
    public String getHealth(){
        logger.info("DELETE controller is working correct....");
        return "Delta Controller is working correct...";
    }


    @GetMapping("/cleanup/{id}")
    public List<Delta> getDeltaByChangesetID( @PathVariable("id") Long changest_id){

        logger.info("Getting the Delta Data for the Changeset ID is {}",changest_id);
        return (List<Delta>) deltaService.getDeltaByChangesetID(changest_id);
    }

    @GetMapping("/cleanup/date/from/{from}/to/{to}")
    public List<Long> getDeltaByDate(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
//        System.out.println("From date  is "+  from +" to date is "+to );
        logger.info("Getting the Delta Data from {} to {} is ",from,to);
        return deltaService.getDeltaByDate(from,to);
    }

    @GetMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<List<Delta>> getDeltaUsingPartyID(@PathVariable("from") Timestamp from ,
                                                           @PathVariable("to") Timestamp to,
                                                           @RequestParam(value = "orgId", required = false) Long orgId){

        if(orgId!=null){
        System.out.println("Getting the Delta By Party id "+ orgId);

        List<Delta> delData = deltaService.getDeltaByParty(from,to,orgId);
        logger.info("Got the Delta Data for the party ID {} from {} to {} is {}  ",orgId,from,to, delData);
        return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
        logger.error("Party ID is missing please add party ID ");
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @DeleteMapping("/cleanup/{id}")
    public List<Delta> deleteByChangesetID(@PathVariable("id") Long id){
//        System.out.println("deleting the data by changeset ID "+id);
        logger.info("Deleting the Delta Data with the Changeset ID as {}",id);
        return  deltaService.deleteByChangesetID(id);

    }


    @DeleteMapping("/cleanup/date/from/{from}/to/{to}")
    public List<Delta> deleteDataDayByDay(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
        System.out.println("From date  is "+  from +" to date is "+to );
        logger.info("Delete the Delta Data from {} to {} ",from,to);
        return deltaService.deleteDayByDayData(from,to);
    }


    @DeleteMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<String> deleteDeltaUsingPartyID(@PathVariable("from") Timestamp from ,
                                                            @PathVariable("to") Timestamp to,
                                                            @RequestParam(value = "orgId", required = false) Long orgId){

        if(orgId!=null){
            System.out.println("DELETING the Delta By Party id "+ orgId);
            logger.info("Deleting the Delta Data for the party ID {} from {} to {} ",orgId,from,to);

          String delData = deltaService.deleteDeltaByParty(from,to,orgId);
            return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
            System.out.println("party id is missing....");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }



}

