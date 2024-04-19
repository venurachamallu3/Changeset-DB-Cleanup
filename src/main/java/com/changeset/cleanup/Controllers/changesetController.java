package com.changeset.cleanup.Controllers;


import com.changeset.cleanup.DAO.ChangesetDAO;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Service.ChangesetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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



    @Autowired
    public ChangesetService changesetService;

    @GetMapping("/health")
    public  String getCustomerID(){
        System.out.println("Health Controller...");
        return "Health Controller...";
    }

    @GetMapping("/cleanup/{id}")
    public Changeset getChangesetByID(@PathVariable("id") Long Id){

        System.out.println("ID is "+ Id );
        System.out.println("fetching the data ...I'm in Controller..");
        return changesetService.getChangesetByID(Id);
    }


    @GetMapping("/cleanup/date/from/{from}/to/{to}")
    public List<Long> getChangesetsByDate(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
//        @PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to
//        System.out.println("From date  is "+  from +" to date is "+to );
        System.out.println("I'm in Controller..");
        return changesetService.getChangsetsByDate(from,to);
    }


    @GetMapping("/cleanup/party/from/{from}/to/{to}")
    public List<Long> getChangesetPartyAndDate(@PathVariable("from") Timestamp from ,
                                               @PathVariable("to") Timestamp to,
                                               @RequestParam(value = "orgId", required = false) Long orgId){



        return changesetService.getchangesetDataByPartyDate(from,to,orgId);
    }


    @DeleteMapping("/cleanup/{id}")
    public String deleteChangesetByID(@PathVariable("id") Long Id){
        changesetService.deleteChangesetByID(Id);
        return "DELETED SUCCESSFULLY.......";
    }

    @DeleteMapping("/cleanup/date/from/{from}/to/{to}")
    public String deleteChangesetByDate(@PathVariable("from") Timestamp from, @PathVariable("to") Timestamp to){

        System.out.println("deleting the Changeset Data from "+ from + " to "+ to);
        changesetService.deleteChangesetByDate(from,to);
        return "deleted changeset Data from "+ from + "to "+to ;
    }



    @DeleteMapping("/cleanup/party/from/{from}/to/{to}")
    public ResponseEntity<String> deleteChangesetByDateAndPartyID(@PathVariable("from") Timestamp from ,
                                                          @PathVariable("to") Timestamp to,
                                                          @RequestParam(value = "orgId", required = false) Long orgId){

        if(orgId!=null){
            System.out.println("DELETING the Changeset By Party id "+ orgId);

            String delData = changesetService.deleteChangesetByDateAndParty(from,to,orgId);
            return  ResponseEntity.status(HttpStatus.OK).body(delData);
        }
        else{
            System.out.println("party id is missing....");
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }







}
