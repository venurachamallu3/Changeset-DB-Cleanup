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


    private static  final Logger logger = (Logger) LoggerFactory.getLogger(DeltaService.class);

    @Autowired
    private DeltaDAO deltaDAO;

    @Autowired
    private ChangesetDAO changesetDAO;

    public List<Delta> getDeltaByChangesetID(Long cID){
        Optional <List<Delta>> d = Optional.ofNullable((List<Delta>) deltaDAO.findByChangesetId(cID));
        if(!d.isPresent()){
            throw new IDNotFoundException("Data is found in the Delta Table with the changeset ID "+cID);
        }
        logger.info("Delta Data for the changeset ID is "+cID+ ": "+d.get());
        return d.get();
    }



    public List<Long> getDeltaByDate(Timestamp fromDate,Timestamp to) {

        List<Long> allChangesetIDs= new ArrayList<>();

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
        logger.info("Calling the method getDelatDayByDay to get the data from {} to {}  ",from,to);
        List<Long> finalChangesetIDs =  getDelatDayByDay(fromDate,nextDayTimestamp, to,allChangesetIDs);
        logger.info("Total Delta Data from {} to {} and count is : {} , the changeset IDS are {} ",from,to,finalChangesetIDs.size(),finalChangesetIDs);
        return finalChangesetIDs;
    }


    public  List<Long> getDelatDayByDay(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, List<Long> allChangesetIDs) {
        if (nextDate.getTime() > endDate.getTime()) {
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} ",beforeDate,endDate);
            Optional <List<Delta>> df = Optional.ofNullable(deltaDAO.getDeltaDayByDay(beforeDate, endDate));
            if (df.isPresent()) {
                List<Long> changesetIDs = df.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data count for last api call  from {} to {} is {}",beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
            return allChangesetIDs;
        }
        else if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("Calling the DAO to get the delta data from {} to {} ", fromDate,nextDate);
            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaDayByDay(fromDate, nextDate));
            logger.info("API CALL IS DONE.......");
            if (d.isPresent()) {
                List<Long> changesetIDs = d.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                allChangesetIDs.addAll(changesetIDs);
                logger.info("Total delta data from {} to {} count is {} " , fromDate,nextDate,changesetIDs.size());
            } else {
                logger.info("No delta data is found from {} to {}", fromDate, nextDate);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getDelatDayByDay(startday, nextDayTimestamp, endDate,allChangesetIDs);
        }
        return  allChangesetIDs;
    }


    public List<Long> getDeltaByParty(Timestamp fromDate, Timestamp to, Long orgId) {

        List<Long> allChangesetIDs= new ArrayList<>();
        logger.info("Fetching the Delta Data for party id {} from {}  to  {} ",fromDate,to);
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("Calling the getDeletaByPartyID ");
        List<Long> finalChangesetIDs = getDeltaByPartyID(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
        logger.info("Total delta data for party {} from {} to {} and changesetID count is  {} and IDs are {} ",orgId,fromDate,to,finalChangesetIDs.size(), finalChangesetIDs);
        return finalChangesetIDs;

    }


    private List<Long> getDeltaByPartyID(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId, List<Long> allChangesetIDs) {

        if (nextDate.getTime() > endDate.getTime()) {
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} for party ID {}  ",beforeDate,endDate, orgId);
            Optional <List<Delta>> df = Optional.ofNullable(deltaDAO.getDeltaByPartyID(beforeDate, endDate,orgId));
            if (df.isPresent()) {
                List<Long> changesetIDs = df.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data count for last api call  from {} to {} is {}",beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
            return allChangesetIDs;
        }
        else if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("calling tha DAO  from date is {} to {} ", fromDate,nextDate);
            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaByPartyID(fromDate, nextDate,orgId));
            logger.info("API CALL IS DONE .... ");
            if (d.isPresent()) {
                List<Long> changesetIDs = d.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data count for party id {} is {}  from {} to {} and changesetid count is {}",orgId,d.stream().count(),fromDate,nextDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }else{
                logger.info("NO delta data is found from {} to {} ", fromDate,nextDate);
            }

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            getDeltaByPartyID(startday, nextDayTimestamp, endDate , orgId,allChangesetIDs);
        }
    return allChangesetIDs;
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

    public List<Long> deleteDayByDayData(Timestamp fromDate, Timestamp to) {

        List<Long> allChangesetIDs = new ArrayList<>();

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("calling the method deleteDayToDay from {} to {} ", fromDate,to);
        List<Long> finalChangesetIDs = deleteDayToDay(fromDate,nextDayTimestamp, to,allChangesetIDs);
        logger.info("total delta data is deleted from {} to {} , and changeset id count is and changeset IDs are {}", fromDate,to,finalChangesetIDs.size(),finalChangesetIDs);
        return finalChangesetIDs;
    }

    public  List<Long> deleteDayToDay(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, List<Long> allChangesetIDs){

        if(nextDate.getTime()> endDate.getTime()){
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} for party ID {}  ",beforeDate,endDate);
            Optional <List<Delta>> df = Optional.ofNullable(deltaDAO.getDeltaDayByDay(beforeDate,endDate));
            if (df.isPresent()) {
                List<Long> changesetIDs = df.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data count is {} for last api call  from {} to {} and changeset ID count is {}",df.stream().count(),beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }else{
                logger.info("NO delta data is found from {} to {} ", beforeDate,endDate);
            }
            deltaDAO.deleteByDeltaByDate(beforeDate,endDate);
            logger.info("Delete api call is done for the last day from {} to {} ", beforeDate,endDate);
            return allChangesetIDs;
        }
        else if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("calling tha DAO to get the Delta Data with the from {} to {} ",fromDate,nextDate);
            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaDayByDay(fromDate, nextDate));
            if (d.isPresent()) {
                List<Long> changesetIDs = d.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data count is {}  from {} to {} and changesetid count is {}",d.stream().count(),fromDate,nextDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }else{
                logger.info("NO delta data is found from {} to {} ", fromDate,nextDate);
            }
            logger.info("calling the delete api call from {} to {}", fromDate,nextDate);
            deltaDAO.deleteByDeltaByDate(fromDate,nextDate);
            logger.info("Delete api call is done for the last day from {} to {} ", fromDate,nextDate);


            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteDayToDay(startday, nextDayTimestamp, endDate,allChangesetIDs);
        }
        return allChangesetIDs;
    }



    public List<Long> deleteDeltaByParty(Timestamp fromDate, Timestamp to, Long orgId) {

        List<Long> allChangesetIDs = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
         logger.info("Calling the method deleteDeltaByPartyIDDataByDate from {} to {} ",fromDate,to);
        List<Long> finalChangesetIDs = deleteDeltaByPartyIDDataByDate(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
        logger.info("Deleted the delta data for party id {} , from {} to {} , changeset ID count is  {} and changesetIDS are",orgId,fromDate,to,finalChangesetIDs.size(),finalChangesetIDs);
        return finalChangesetIDs;
    }

    public List<Long> deleteDeltaByPartyIDDataByDate(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId , List<Long> allChangesetIDs){

        if(nextDate.getTime()> endDate.getTime()){
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} for party ID {}  ",beforeDate,endDate);
            Optional <List<Delta>> df = Optional.ofNullable(deltaDAO.getDeltaByPartyID(beforeDate,endDate,orgId));
            if (df.isPresent()) {
                List<Long> changesetIDs = df.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data for party id {} count is {} for last api call  from {} to {} and changeset ID count is {}",orgId,df.stream().count(),beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }else{
                logger.info("NO delta data is found from {} to {} ", beforeDate,endDate);
            }
            deltaDAO.deleteDeltaByParty(beforeDate,endDate,orgId);
            logger.info("Delete api call is done for the last day from {} to {} ", beforeDate,endDate);
            return allChangesetIDs;
        }
        if (nextDate.getTime() <= endDate.getTime()) {

            logger.info("calling tha DAO for party id {} to get the Delta Data with the from {} to {} ",orgId,fromDate,nextDate);
            Optional<List<Delta>> d = Optional.ofNullable(deltaDAO.getDeltaByPartyID(fromDate, nextDate,orgId));
            if (d.isPresent()) {
                List<Long> changesetIDs = d.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Delta::getChangeset_id) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());
                logger.info("Delta Data for party id {}  count is {}  from {} to {} and changesetid count is {}",orgId,d.stream().count(),fromDate,nextDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }else{
                logger.info("NO delta data is found from {} to {} ", fromDate,nextDate);
            }
            logger.info("calling the delete api call from {} to {}", fromDate,nextDate);
            deltaDAO.deleteDeltaByParty(fromDate,nextDate,orgId);
            logger.info("Delete api call is done for the last day from {} to {} ", fromDate,nextDate);

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteDeltaByPartyIDDataByDate(startday, nextDayTimestamp, endDate , orgId,allChangesetIDs);
        }
        return  allChangesetIDs;
    }

}
