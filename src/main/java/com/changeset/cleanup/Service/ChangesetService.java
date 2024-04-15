package com.changeset.cleanup.Service;

import com.changeset.cleanup.DAO.ImpChangesetDAO;
import com.changeset.cleanup.Model.Changeset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.changeset.cleanup.DAO.ChangesetDAO;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ChangesetService {





    @Autowired
    public ImpChangesetDAO changesetDAO;

    public Changeset getChangesetByID(Long id){
        System.out.println("ID is "+ id );
        System.out.println("fetching the data from the DB ...I'm in Service..");
        Optional<Changeset> cs = changesetDAO.findById(id);
        if(!cs.isPresent()){
            System.out.println("ID IS NOT FOUND...");
        }
        System.out.println("after getting the db the data is "+cs.get().getId());
        return cs.get();
    }

//    public List<Changeset> getChangsetsByDate(Timestamp from, Timestamp to) {
//
//    }
}
