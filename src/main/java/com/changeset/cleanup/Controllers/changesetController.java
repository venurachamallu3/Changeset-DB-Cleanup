package com.changeset.cleanup.Controllers;


import com.changeset.cleanup.DAO.ChangesetDAO;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Service.ChangesetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/cleanup/date/from/to/to")
    public List<Changeset> getChangesetsByDate(){
//        @PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to
//        System.out.println("From date  is "+  from +" to date is "+to );
        System.out.println("I'm in Controller..");
        return changesetService.getChangsetsByDate();
    }



}
