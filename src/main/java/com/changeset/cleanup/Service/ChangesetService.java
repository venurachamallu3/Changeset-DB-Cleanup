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
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public List<Changeset> getChangsetsByDate() {
//        System.out.println("from "+from +"to "+to);
        Timestamp fromDate = Timestamp.valueOf("2024-02-22 04:34:53.144");
        Timestamp to = Timestamp.valueOf("2024-02-25 04:34:53.144");

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        getChangesetsDayByDay(fromDate,nextDayTimestamp, to);
        System.out.println("Done with all date ...");
        return changesetDAO.getChangsetsByDate(fromDate,to);

    }

    void getChangesetsDayByDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate){


        if(nextDate.getTime() <= endDate.getTime()){
            System.out.println("calling tha DAO with the from date is "+fromDate +" nextdate is "+nextDate);

            //call to th3e DAO
           Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(fromDate, nextDate));

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetsDayByDay(startday,nextDayTimestamp,endDate);

        }


    }
}
