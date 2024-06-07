package com.changeset.cleanup.Service;


import com.changeset.cleanup.Controllers.changesetController;
import com.changeset.cleanup.DAO.ChangesetDAO;
import com.changeset.cleanup.DAO.DeltaDAO;
import com.changeset.cleanup.Exception.IDNotFoundException;
import com.changeset.cleanup.Model.Changeset;
import com.changeset.cleanup.Model.Delta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DeltaService {


    private static  final Logger logger = (Logger) LoggerFactory.getLogger(changesetController.class);

    @Autowired
    private DeltaDAO deltaDAO;

    @Autowired
    private ChangesetDAO changesetDAO;

    public List<Long> getDeltaByDate(Timestamp fromDate,Timestamp to) {

        List<Long> deltadata= new ArrayList<>();

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);


        logger.info("Calling the method getChangesetsDayByDay to get the data from {} to {}  ",from,to);
        return  getChangesetsDayByDay(fromDate,nextDayTimestamp, to,deltadata);
    }

    public List<Delta> getDeltaByChangesetID(Long cID){

//        deltaDAO.findByChangesetId(cID).get()
         Optional <List<Delta>> d = Optional.ofNullable((List<Delta>) deltaDAO.findByChangesetId(cID));
//         if()
        if(!d.isPresent()){
            throw new IDNotFoundException("Data is found in the Delta Table with the changeset ID "+cID);
        }
//        System.out.println("for the given chnageset id data is "+ d.get());
          logger.info("Delta Data for the changeset ID is "+cID+ ": "+d.get());
            return d.get();
    }


    public  List<Long> getChangesetsDayByDay(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, List<Long> alldeltadata) {


        if(nextDate.getTime()>=endDate.getTime()){
            logger.info("Fd0000");
            return  alldeltadata;
        }

        if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("Calling the DAO to delete the delta data from {} to {} ", fromDate,nextDate);
            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaDayByDay(fromDate, nextDate));
            logger.info("API CALL IS DONE.......");
            if (d.isPresent()) {
                List<Long> changesetIDs = d.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());


                alldeltadata.addAll(changesetIDs);
                logger.info("Total delta data from {} to {} count is {} and the changeset ID is {}" , fromDate,nextDate,changesetIDs.size(),changesetIDs);
            } else {
                logger.info("No data is found from {} to {}", fromDate, nextDate);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

              getChangesetsDayByDay(startday, nextDayTimestamp, endDate,alldeltadata);
        }
        return  alldeltadata;

    }


    public List<Delta>  deleteByChangesetID(Long id) {

         Changeset c  = changesetDAO.findById(id).orElseThrow(()-> new IDNotFoundException("Changeset ID is not found in the Changeset DB with ID "+id));

         List<Delta> d = deltaDAO.findByChangesetId(id);
         if(d.isEmpty()) {
             throw new IDNotFoundException("Changeset ID is not found in the Delta Data , with ID as  "+id);
        }
       deltaDAO.deleteByChangesetId(id);
       logger.info("Deleted sucessfully Delta data for the changeset ID is {} and delta data  is ",id,d);
       return d;
    }

    public List<Delta> deleteDayByDayData(Timestamp fromDate, Timestamp to) {

        List<Delta> allDeltaData = new ArrayList<>();

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("calling the method deleteDayToDay from {} to {} ", fromDate,to);
        return deleteDayToDay(fromDate,nextDayTimestamp, to,allDeltaData);
//        System.out.println("Done with all date ...");

//        logger.info("Deleted Done..");
//        return "DELETED SUCCESSFULLYY........";
    }

    public  List<Delta> deleteDayToDay(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, List<Delta> allDeltaData){


        if(nextDate.getTime()> endDate.getTime()){
            logger.info("Total deleted data is from {} to {}  is ",fromDate,endDate,allDeltaData);
            return allDeltaData;
        }
        if (nextDate.getTime() <= endDate.getTime()) {

            logger.info("calling tha DAO to Delete with the from {} to {} ",fromDate,nextDate);


            List<Delta> d = deltaDAO.getDeltaDayByDay(fromDate,nextDate);



             deltaDAO.deleteByDeltaByDate(fromDate,nextDate);
            logger.info("API CALL IS DONE and deleted the data.... ");

            if(d!=null){
                logger.info("Deleted the data from the Delta from {} to {} count is {} and data is {}", fromDate,nextDate,d.size(),d);
                allDeltaData.addAll(d);
            }
            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteDayToDay(startday, nextDayTimestamp, endDate,allDeltaData);

        }
        return allDeltaData;


    }

    public List<Delta> getDeltaByParty(Timestamp fromDate, Timestamp to, Long orgId) {

        logger.info("Fetching the Delta Data for party id {} from {}  to  {} ",fromDate,to);
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("Calling the getDeletaByPartyID ");
        getDeletaByPartyID(fromDate,nextDayTimestamp, to,orgId);
        logger.info("deleted done  ...");
       return deltaDAO.getDeltaByPartyID(fromDate,to,orgId);

    }


    private void getDeletaByPartyID(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId) {

        if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("calling tha DAO  from date is {} to {} ", fromDate,nextDate);

            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaByPartyID(fromDate, nextDate,orgId));
            logger.info("API CALL IS DONE .... ");
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

            getDeletaByPartyID(startday, nextDayTimestamp, endDate , orgId);
        }

    }


    public String deleteDeltaByParty(Timestamp fromDate, Timestamp to, Long orgId) {

        List<Delta> allDeletedDelta = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
         logger.info("Calling the method deleteDeltaByPartyIDDataByDate from {} to {} ",fromDate,to);
        deleteDeltaByPartyIDDataByDate(fromDate,nextDayTimestamp, to,orgId,allDeletedDelta);


        return "Successfully Deleted with the party ID "+orgId;
    }

    public List<Delta> deleteDeltaByPartyIDDataByDate(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId , List<Delta> allDeletedDelta){

        if(nextDate.getTime()>endDate.getTime()){
            return allDeletedDelta;
        }
        if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("calling tha DAO with the from date is " + fromDate + " nextdate is " + nextDate);

            List<Delta> d = deltaDAO.deleteDeltaByParty(fromDate, nextDate,orgId);
//            System.out.println("API CALL IS DONE .... ");

            if(d!=null){
                allDeletedDelta.addAll(d);
                logger.info("Deleted the data for party id {} from {} to {} and count is {} , data is {} ",orgId,fromDate,nextDate,d.size(),d);
            }else{
                logger.info("No Delta data is present in this date from {} to {} for the party id {} ",fromDate,nextDate,orgId);
            }
            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteDeltaByPartyIDDataByDate(startday, nextDayTimestamp, endDate , orgId,allDeletedDelta);
        }
        return  allDeletedDelta;
    }

}
