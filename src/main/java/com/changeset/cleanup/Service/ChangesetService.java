package com.changeset.cleanup.Service;

import com.changeset.cleanup.Controllers.changesetController;
import com.changeset.cleanup.Exception.IDNotFoundException;
import com.changeset.cleanup.Model.Changeset;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.changeset.cleanup.DAO.ChangesetDAO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ChangesetService {

    private static  final Logger logger = (Logger) LoggerFactory.getLogger(ChangesetService.class);


    @Autowired
    public  ChangesetDAO changesetDAO;

    public Changeset getChangesetByID(Long id){
        logger.info("Getting the data from the DB for the ID "+id);
        System.out.println("ID is "+ id );
        System.out.println("fetching the data from the DB ...I'm in Service..");
        Optional<Changeset> cs = Optional.ofNullable(changesetDAO.findById(id).orElseThrow(() -> new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  " + id)));
//        if(!cs.isPresent()){
//            System.out.println("ID IS NOT FOUND...");
//            throw new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  "+ id);
//        }
        System.out.println("after getting the db the data is "+cs.get().getId());
        return cs.get();
    }

    public List<Long> getChangsetsByDate(Timestamp fromDate , Timestamp to) {

        List<Long> allChangesetIds = new ArrayList<>();
////        System.out.println("from "+from +"to "+to);
//        Timestamp fromDate = Timestamp.valueOf("2024-02-22 04:34:53.144");
//        Timestamp to = Timestamp.valueOf("2024-02-25 04:34:53.144");


        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


//        if(fromDate)

//        if(nextDayTimestamp)

        return getChangesetsDayByDay(fromDate,nextDayTimestamp, to,allChangesetIds);
//
//
//        return changesetDAO.getChangsetsByDate(fromDate,to);

    }

    public List<Long> getChangesetsDayByDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate, List<Long> allChangesetIDs){

//        List<Long> allChangesetIDs = new ArrayList<>();
        if (nextDate.getTime() > endDate.getTime()) {
            return allChangesetIDs;
        }
        if(nextDate.getTime() <= endDate.getTime()) {
            System.out.println("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate);

            //call to the DAO
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(fromDate, nextDate));

            if (cs.isPresent()) {
                System.out.println("Total count in the current API call " + cs.get().size());
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                allChangesetIDs.addAll(changesetIDs);
//                System.out.println("Total count is " + allChangesetIDs.size());
//                System.out.println(allChangesetIDs);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetsDayByDay(startday, nextDayTimestamp, endDate,allChangesetIDs);
        }

//        System.out.println("Total size is "+allChangesetIDs.size() +"\n"+" IDs are"+allChangesetIDs);
        return allChangesetIDs;
    }



    public List<Long> getchangesetDataByPartyDate(Timestamp fromDate, Timestamp to, Long orgId){

        List<Long> allChangesetIDs = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

         return  getChangesetDataAPIDatePArty(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
//
//        System.out.println("Done with all date ...");

    }


    public List<Long> getChangesetDataAPIDatePArty(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId, List<Long> allChangesetIDs){


        if (nextDate.getTime() > endDate.getTime()) {
            return allChangesetIDs;
        }
        if(nextDate.getTime() <= endDate.getTime()) {
            System.out.println("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate + "partyID  " +orgId);

            //call to th3e DAO
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(fromDate, nextDate,orgId));

            if (cs.isPresent()) {
                System.out.println("Total count in the current API call " + cs.get().size());
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                allChangesetIDs.addAll(changesetIDs);
//                System.out.println("Total count is " + allChangesetIDs.size());
//                System.out.println(allChangesetIDs);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetDataAPIDatePArty(startday, nextDayTimestamp, endDate,orgId,allChangesetIDs);
        }

        System.out.println("Total size is "+allChangesetIDs.size() +"\n"+" IDs are"+allChangesetIDs);
        return allChangesetIDs;


    }



    public void deleteChangesetByID(Long id) {
        System.out.println("Deleting the changeset ID By ID "+id);
        Optional<Changeset> cs = Optional.ofNullable(changesetDAO.findById(id).orElseThrow(() -> new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  " + id)));
        changesetDAO.deleteById(id);
    }

    public void deleteChangesetByDate(Timestamp fromDate, Timestamp to) {

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        deleteChangesetDataWithDate(fromDate,nextDayTimestamp, to);
        System.out.println("Done with all date ...");

//        return "DELETED SUCCESSFULLYY........";
    }

    public void deleteChangesetDataWithDate(Timestamp fromDate,Timestamp nextDate,Timestamp endDate){

        if (nextDate.getTime() <= endDate.getTime()) {

            System.out.println("calling tha DAO to Delete with the from date is " + fromDate + " nextdate is " + nextDate);
            changesetDAO.deleteChangesetDataWithDate(fromDate,nextDate);
            System.out.println("API CALL IS DONE and deleted the data.... ");

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteChangesetDataWithDate(startday, nextDayTimestamp, endDate);

        }
    }


    public String deleteChangesetByDateAndParty(Timestamp fromDate, Timestamp to, Long orgId) {

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        deleteChangesetByPartyIDAndByDate(fromDate,nextDayTimestamp, to,orgId);
        System.out.println("Done with all date ...");

        return "Successfully Deleted with the party ID "+orgId;
    }

    private void deleteChangesetByPartyIDAndByDate(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId) {


        if (nextDate.getTime() <= endDate.getTime()) {
            System.out.println("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate);

            changesetDAO.deleteChangesetByPartyAndDate(fromDate, nextDate,orgId);
            System.out.println("API CALL IS DONE .... ");

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteChangesetByPartyIDAndByDate(startday, nextDayTimestamp, endDate , orgId);
        }


    }
}
