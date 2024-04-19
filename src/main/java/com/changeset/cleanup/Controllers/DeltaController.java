package com.changeset.cleanup.Controllers;

import com.changeset.cleanup.Model.Delta;
import com.changeset.cleanup.Service.DeltaService;
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

    
    @Autowired
    public DeltaService deltaService;
    
    @GetMapping("/health")
    public String getHealth(){
        return "Delta Controller is working correct...";
    }


    @GetMapping("/cleanup/{id}")
    public List<Delta> getDeltaByChangesetID( @PathVariable("id") Long changest_id){

        System.out.println("Delta Controller get Delta Data By Changeset ID");
        return (List<Delta>) deltaService.getDeltaByChangesetID(changest_id);
    }

    @GetMapping("/cleanup/date/from/{from}/to/{to}")
    public List<Delta> getDeltaByDate(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
        System.out.println("From date  is "+  from +" to date is "+to );
        return deltaService.getDeltaByDate(from,to);
    }

    @GetMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<List<Delta>> getDeltaUsingPartyID(@PathVariable("from") Timestamp from ,
                                                           @PathVariable("to") Timestamp to,
                                                           @RequestParam(value = "orgId", required = false) Long orgId){

        if(orgId!=null){
        System.out.println("Getting the Delta By Party id "+ orgId);

        List<Delta> delData = deltaService.getDeltaByParty(from,to,orgId);
        return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
        System.out.println("party id is missing....");
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @DeleteMapping("/cleanup/{id}")
    public String deleteByChangesetID(@PathVariable("id") Long id){
        System.out.println("deleting the data by changeset ID "+id);
        return deltaService.deleteByChangesetID(id);
    }


    @DeleteMapping("/cleanup/date/from/{from}/to/{to}")
    public String deleteDataDayByDay(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
        System.out.println("From date  is "+  from +" to date is "+to );
        return deltaService.deleteDayByDayData(from,to);
    }


    @DeleteMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<String> deleteDeltaUsingPartyID(@PathVariable("from") Timestamp from ,
                                                            @PathVariable("to") Timestamp to,
                                                            @RequestParam(value = "orgId", required = false) Long orgId){

        if(orgId!=null){
            System.out.println("DELETING the Delta By Party id "+ orgId);

          String delData = deltaService.deleteDeltaByParty(from,to,orgId);
            return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
            System.out.println("party id is missing....");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }



}

