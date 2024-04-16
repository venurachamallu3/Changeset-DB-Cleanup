package com.changeset.cleanup.Service;


import com.changeset.cleanup.DAO.DeltaDAO;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Model.Delta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeltaService {

    @Autowired
    private DeltaDAO deltaDAO;

    public List<Delta> getDeltaByDate(Timestamp fromDate,Timestamp to) {
//        Timestamp fromDate = Timestamp.valueOf("2023-02-22 04:34:53.144");
//        Timestamp to = Timestamp.valueOf("2023-02-25 04:34:53.144");

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        getChangesetsDayByDay(fromDate,nextDayTimestamp, to);
        System.out.println("Done with all date ...");
        return deltaDAO.getDeltaDayByDay(fromDate,to);
    }

    public List<Delta> getDeltaByChangesetID(Long cID){
         Optional <List<Delta>> d = Optional.ofNullable((List<Delta>) deltaDAO.findByChangesetId(cID));
        if(d.isPresent()){
            System.out.println("Changeset with the cID"+ cID +"is not present");
        }
        System.out.println("for the given chnageset id data is "+ d.get());
            return d.get();
    }


    void getChangesetsDayByDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate) {

        if (nextDate.getTime() <= endDate.getTime()) {
            System.out.println("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate);

            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaDayByDay(fromDate, nextDate));
            System.out.println("API CALL IS DONE .... ");
            if (d.isPresent()) {
                List<Delta> deltaList = d.get();
                int totalObjects = deltaList.size();
                System.out.println("Total number of objects in d: " + totalObjects);
            } else {
                System.out.println("No objects found in d.");
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetsDayByDay(startday, nextDayTimestamp, endDate);


        }

    }


    public String  deleteByChangesetID(Long id) {

       deltaDAO.deleteByChangesetId(id);

       return "Deleted successfully.....";
    }

    public String deleteDayByDayData(Timestamp fromDate, Timestamp to) {

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        deleteDayToDay(fromDate,nextDayTimestamp, to);
        System.out.println("Done with all date ...");

        return "DELETED SUCCESSFULLYY........";
    }

    public  void deleteDayToDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate){

        if (nextDate.getTime() <= endDate.getTime()) {

            System.out.println("calling tha DAO to Delete with the from date is " + fromDate + " nextdate is " + nextDate);
            deltaDAO.deleteByDeltaByDate(fromDate,nextDate);
            System.out.println("API CALL IS DONE and deleted the data.... ");

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteDayToDay(startday, nextDayTimestamp, endDate);

        }


    }
}
