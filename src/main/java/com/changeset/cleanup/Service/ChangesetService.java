package com.changeset.cleanup.Service;

import com.changeset.cleanup.Model.Changeset;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.changeset.cleanup.DAO.ChangesetDAO;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ChangesetService {

    @Autowired
    public  ChangesetDAO changesetDAO;

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

    private int i = 14214553;
    public List<Changeset> getChangsetsByDate() {
//        System.out.println("from "+from +"to "+to);
        Timestamp fromDate = Timestamp.valueOf("2024-02-22 04:34:53.144");
        Timestamp to = Timestamp.valueOf("2024-02-22 06:34:53.144");

        return changesetDAO.getChangsetsByDate(fromDate,to);

    }
}
