package com.changeset.cleanup.DAO;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.changeset.cleanup.Model.Changeset;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface ChangesetDAO extends JpaRepository <Changeset,Long> {

    @Query("SELECT c FROM Changeset c WHERE c.changeset_recorded_time > :date and c.changeset_recorded_time < :to ")
    List<Changeset> getChangsetsByDate(@Param("date") Timestamp date, @Param("to") Timestamp to);




    @Query("select c from Changeset c where  c.org_id= :orgId and   c.changeset_recorded_time > :fromDate and c.changeset_recorded_time < :nextDate ")
    List<Changeset> getChangesetByPartyIDAndDate(@Param("fromDate") Timestamp fromDate, @Param("nextDate") Timestamp nextDate, @Param("orgId") Long orgId);


    @Transactional
    @Modifying
    @Query("delete  from Changeset c where  c.changeset_recorded_time > :fromDate and c.changeset_recorded_time < :nextDate ")
   public  void deleteChangesetDataWithDate(@Param("fromDate") Timestamp fromDate, @Param("nextDate") Timestamp nextDate);




    @Transactional
    @Modifying
    @Query("delete  from Changeset c where  c.org_id= :orgId and   c.changeset_recorded_time > :fromDate and c.changeset_recorded_time < :nextDate ")
    void deleteChangesetByPartyAndDate(@Param("fromDate") Timestamp fromDate, @Param("nextDate") Timestamp nextDate, @Param("orgId") Long orgId);



}
