package com.changeset.cleanup.Service;

import com.changeset.cleanup.Controllers.changesetController;
import com.changeset.cleanup.Exception.IDNotFoundException;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Model.Delta;
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
        Optional<Changeset> cs = Optional.ofNullable(changesetDAO.findById(id).orElseThrow(() -> new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  " + id)));
        logger.info("Got the Changeset Data with Id is " + id + cs.get());
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

        logger.info("Calling the method getChangesetsDayByDay to get the changeset Data from {} to {}", fromDate,to);
        return getChangesetsDayByDay(fromDate,nextDayTimestamp, to,allChangesetIds);
    }

    public List<Long> getChangesetsDayByDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate, List<Long> allChangesetIDs){

        if (nextDate.getTime() > endDate.getTime()) {
            logger.info("Got all the changeset Data from {} to {}  is {}", fromDate,endDate,allChangesetIDs);
            return allChangesetIDs;
        }
        if(nextDate.getTime() <= endDate.getTime()) {
            logger.info("Calling the DAO with the from date is {} to {} ",fromDate, nextDate);
//
            //call to the DAO
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(fromDate, nextDate));

            if (cs.isPresent()) {
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data count  from {} to {} is {}",fromDate,nextDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetsDayByDay(startday, nextDayTimestamp, endDate,allChangesetIDs);
        }

        return allChangesetIDs;
    }



    public List<Long> getchangesetDataByPartyDate(Timestamp fromDate, Timestamp to, Long orgId){

        List<Long> allChangesetIDs = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("Calling the method getChangesetDataAPIDateParty to get the Changest Data for Party ID {} from {} to {} ",orgId, fromDate,to);
         return  getChangesetDataAPIDateParty(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
//
//        System.out.println("Done with all date ...");

    }


    public List<Long> getChangesetDataAPIDateParty(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId, List<Long> allChangesetIDs){


        if (nextDate.getTime() > endDate.getTime()) {
            logger.info("Total Changeset Data for Party {} from {} to {} count  is {} ",orgId,fromDate,endDate,allChangesetIDs.size());
            logger.info("Data is {}",allChangesetIDs);
            return allChangesetIDs;
        }
        if(nextDate.getTime() <= endDate.getTime()) {

            logger.info("calling tha DAO with the from  {} to {} with party ID {}",fromDate,nextDate,orgId);
            //call to th3e DAO
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(fromDate, nextDate,orgId));

            if (cs.isPresent()) {

//                System.out.println("Total count in the current API call " + cs.get().size());
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data from {} to {} is {} ", fromDate, endDate,changesetIDs.size());

                allChangesetIDs.addAll(changesetIDs);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getChangesetDataAPIDateParty(startday, nextDayTimestamp, endDate,orgId,allChangesetIDs);
        }

//        System.out.println("Total size is "+allChangesetIDs.size() +"\n"+" IDs are"+allChangesetIDs);
        return allChangesetIDs;


    }



    public Changeset deleteChangesetByID(Long id) {
//        System.out.println("Deleting the changeset ID By ID "+id);
        Optional<Changeset> cs = Optional.ofNullable(changesetDAO.findById(id).orElseThrow(() -> new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  " + id)));
        changesetDAO.deleteById(id);
        logger.info("DELTED SUCCESSFULLY...");
        return cs.get();
    }

    public void deleteChangesetByDate(Timestamp fromDate, Timestamp to) {

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        logger.debug("Calling the method deleteChangesetDataWithDate with Dates from {} to {}",fromDate,to);
        deleteChangesetDataWithDate(fromDate,nextDayTimestamp, to);
        logger.info("ALL DATA DELETED SUCCESSFULLYY from {} to {] ........", fromDate,to);
//        return "DELETED SUCCESSFULLYY........";
    }

    public void deleteChangesetDataWithDate(Timestamp fromDate,Timestamp nextDate,Timestamp endDate){

        if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("Calling the DAO to Delete with the from date is \" + fromDate + \" nextdate is \" + nextDate ");


//            System.out.println("calling tha DAO to Delete with the from date is " + fromDate + " nextdate is " + nextDate);
            changesetDAO.deleteChangesetDataWithDate(fromDate,nextDate);
//            System.out.println("API CALL IS DONE and deleted the data.... ");

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


        logger.info("Calling the Method deleteChangesetByPartyIDAndByDate , to delete the Changeset Data for Party ID {} from {} to ", orgId, fromDate,to);
        deleteChangesetByPartyIDAndByDate(fromDate,nextDayTimestamp, to,orgId);

        logger.info("Successfully Deleted with the party ID {}",orgId);

        return "Successfully Deleted with the party ID "+orgId;
    }

    private void deleteChangesetByPartyIDAndByDate(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId) {


        if (nextDate.getTime() <= endDate.getTime()) {
//            System.out.println("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate);

            logger.info("Calling the DAO for party ID {} from {} to {} ",orgId,fromDate,nextDate);
            changesetDAO.deleteChangesetByPartyAndDate(fromDate, nextDate,orgId);
//            System.out.println("API CALL IS DONE .... ");

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteChangesetByPartyIDAndByDate(startday, nextDayTimestamp, endDate , orgId);
        }


    }
}
