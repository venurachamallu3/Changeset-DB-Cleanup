package com.changeset.cleanup.Service;

import com.changeset.cleanup.Exception.IDNotFoundException;
import com.changeset.cleanup.Model.Changeset;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.changeset.cleanup.DAO.ChangesetDAO;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Changeset> csa = new ArrayList<>();

        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        if(nextDayTimestamp.getTime()>=to.getTime()){
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(fromDate, to));
            if(cs.isPresent()){
                  List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                          .orElseGet(Stream::empty) // Return an empty stream if the list is null
                          .map(Changeset::getId) // Extract the changesetID from each Changeset object
                          .collect(Collectors.toList());

                  logger.info("Changeset Data count  from {} to {} is {}",fromDate,to,changesetIDs.size());
                  allChangesetIds.addAll(changesetIDs);
              }
            return allChangesetIds;
        }else {
            logger.info("Calling the method getChangesetsDayByDay to get the changeset Data from {} to {}", fromDate, to);
            List<Long> finalchangesetIDs =  getChangesetsDayByDay(fromDate, nextDayTimestamp, to, allChangesetIds);
            return finalchangesetIDs;
        }
    }

    public List<Long> getChangesetsDayByDay(Timestamp fromDate,Timestamp nextDate,Timestamp endDate, List<Long> allChangesetIDs){

        if (nextDate.getTime() > endDate.getTime()) {
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} ",beforeDate,endDate);
            Optional <List<Changeset>> csf = Optional.ofNullable(changesetDAO.getChangsetsByDate(beforeDate, endDate));
            if (csf.isPresent()) {
                List<Long> changesetIDs = csf.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data count  from {} to {} is {}",beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
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
         List<Long> finalChangesetIDS = getChangesetDataAPIDateParty(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
         return finalChangesetIDS;
    }


    public List<Long> getChangesetDataAPIDateParty(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId, List<Long> allChangesetIDs){


        if (nextDate.getTime() > endDate.getTime()) {
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call from {} to {} ",beforeDate,endDate);
            Optional <List<Changeset>> csf = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(beforeDate, endDate,orgId));
            if (csf.isPresent()) {
                List<Long> changesetIDs = csf.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data count  from {} to {} is {}",beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
            logger.info("Total Changeset Data for Party {} from {} to {} count  is {} ",orgId,fromDate,endDate,allChangesetIDs.size());
            return allChangesetIDs;
        }
        else if(nextDate.getTime() <= endDate.getTime()) {

            logger.info("calling tha DAO with the from  {} to {} with party ID {}",fromDate,nextDate,orgId);
            //call to th3e DAO
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(fromDate, nextDate,orgId));

            if (cs.isPresent()) {
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
        return allChangesetIDs;
    }



    public Changeset deleteChangesetByID(Long id) {
        Optional<Changeset> cs = Optional.ofNullable(changesetDAO.findById(id).orElseThrow(() -> new IDNotFoundException("DATA IS NOT FOUND WITH THE ID  " + id)));
        changesetDAO.deleteById(id);
        logger.info("Changeset Data is deleted successfully with ID as  {} and data is {} ",id, cs.get());
        return cs.get();
    }

    public List<Long> deleteChangesetByDate(Timestamp fromDate, Timestamp to) {

        List<Long> allChangesetIds = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.debug("Calling the method deleteChangesetDataWithDate with Dates from {} to {}",fromDate,to);
        List<Long> finalChangesetIDs =  deleteChangesetDataWithDate(fromDate,nextDayTimestamp, to,allChangesetIds);
        logger.info("Successfully Deleted the Changeset Data from {} to {}  , Changeset IDS : {} ", fromDate,to,finalChangesetIDs);
        return finalChangesetIDs;
    }

    public List<Long> deleteChangesetDataWithDate(Timestamp fromDate,Timestamp nextDate,Timestamp endDate, List<Long> allChangesetIds){

        if(nextDate.getTime()>endDate.getTime()){
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call to get the delta data from {} to {} ",beforeDate,endDate);
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(beforeDate, endDate));
            if (cs.isPresent()) {
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Last API call Changeset Data count  from {} to {} is {}",beforeDate,endDate,changesetIDs.size());
                allChangesetIds.addAll(changesetIDs);
            }
            changesetDAO.deleteChangesetDataWithDate(beforeDate,endDate);
            logger.info("Deleted the changeset data from {} to {} ", beforeDate,endDate);
            return allChangesetIds;
        }
        else if (nextDate.getTime() <= endDate.getTime()) {
            logger.info("Calling the DAO to Delete with the from {} to {} ", fromDate, nextDate);
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangsetsByDate(fromDate, nextDate));
            if (cs.isPresent()) {
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data count  from {} to {} is {}",fromDate,nextDate,changesetIDs.size());
                allChangesetIds.addAll(changesetIDs);
            }

            //To delete the data
            changesetDAO.deleteChangesetDataWithDate(fromDate,nextDate);

            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);
            deleteChangesetDataWithDate(startday, nextDayTimestamp, endDate,allChangesetIds);
        }

        logger.info("Total deleted changeset ids count is {} within this range {} to {}",allChangesetIds.size(), fromDate,endDate);
        return allChangesetIds;
    }


    public List<Long> deleteChangesetByDateAndParty(Timestamp fromDate, Timestamp to, Long orgId) {

        List<Long> allChangesetIDs = new ArrayList<>();
        LocalDateTime from = fromDate.toLocalDateTime();
        LocalDateTime nextDay = from.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);

        logger.info("Calling the Method deleteChangesetByPartyIDAndByDate , to delete the Changeset Data for Party ID {} from {} to ", orgId, fromDate,to);

        List<Long> finalIds = deleteChangesetByPartyIDAndByDate(fromDate,nextDayTimestamp, to,orgId,allChangesetIDs);
        logger.info("Successfully Deleted  the changeset Data for  the party ID {} from {} to {} , changeset IDS : {}",orgId,from,to,finalIds);
        return finalIds;
    }

    private List<Long> deleteChangesetByPartyIDAndByDate(Timestamp fromDate, Timestamp nextDate, Timestamp endDate, Long orgId , List<Long> allChangesetIDs) {
        if(nextDate.getTime()>endDate.getTime()){
            Timestamp beforeDate = Timestamp.valueOf(nextDate.toLocalDateTime().minusDays(1));
            logger.info("Calling the last api call to get the delta data from {} to {} for party id {} ",beforeDate,endDate, orgId);
            Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(beforeDate, endDate,orgId));
            if (cs.isPresent()) {
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Last API call for party id {} to get the  Changeset Data count  from {} to {} is {}",orgId,beforeDate,endDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
            changesetDAO.deleteChangesetByPartyAndDate(beforeDate,endDate,orgId);
            logger.info("Deleted the changeset data for party id {} from {} to {} ", orgId, beforeDate,endDate);
            return allChangesetIDs;
        }
        else if (nextDate.getTime() <= endDate.getTime()) {
          logger.info("Calling the DAO to get the changeset data for party ID {} from {} to {} ",orgId,fromDate,nextDate);
          Optional <List<Changeset>> cs = Optional.ofNullable(changesetDAO.getChangesetByPartyIDAndDate(fromDate, nextDate,orgId));
            if (cs.isPresent()) {
                List<Long> changesetIDs = cs.map(List::stream)  // Convert the list to a stream
                        .orElseGet(Stream::empty) // Return an empty stream if the list is null
                        .map(Changeset::getId) // Extract the changesetID from each Changeset object
                        .collect(Collectors.toList());

                logger.info("Changeset Data count  from {} to {} is {}",fromDate,nextDate,changesetIDs.size());
                allChangesetIDs.addAll(changesetIDs);
            }
            changesetDAO.deleteChangesetByPartyAndDate(fromDate,nextDate,orgId);
            LocalDateTime start = nextDate.toLocalDateTime();
            LocalDateTime nextDay = start.plusDays(1);
            Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
            Timestamp startday = Timestamp.valueOf(start);

            deleteChangesetByPartyIDAndByDate(startday, nextDayTimestamp, endDate , orgId,allChangesetIDs);
        }
        return allChangesetIDs;
    }
}
