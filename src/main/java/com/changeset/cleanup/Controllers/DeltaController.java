package com.changeset.cleanup.Controllers;

import com.changeset.cleanup.Model.Delta;
import com.changeset.cleanup.Service.DeltaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @DeleteMapping("/cleanup/{id}")
    public String deleteByChangesetID(@PathVariable("id") Long id){
        System.out.println("deleting the data by changeset ID "+id);
        return deltaService.deleteByChangesetID(id);
    }


    @DeleteMapping("/cleanup/from/{from}/to/{to}")
    public String deleteDataDayByDay(@PathVariable("from") Timestamp from , @PathVariable("to") Timestamp to){
        System.out.println("From date  is "+  from +" to date is "+to );
        return deltaService.deleteDayByDayData(from,to);

    }
}

