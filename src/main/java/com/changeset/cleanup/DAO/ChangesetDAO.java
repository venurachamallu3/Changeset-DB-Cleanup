package com.changeset.cleanup.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.changeset.cleanup.Model.Changeset;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface ChangesetDAO extends JpaRepository <Changeset,Long> {


//    @Query("select * from Changeset c where changeset_recorded_time between ${from} and ${to}")


//    @Query("select c1_0.id,c1_0.changeset_recorded_time,c1_0.changeset_source,c1_0.changeset_type,c1_0.org_id from changeset c1_0 where c1_0.id in (14214553)")
//    @Query(value = "SELECT c FROM Changeset c WHERE c.changeset_recorded_time > 2024-02-22 05:34:53.144 ")
//    List<Changeset> getChangsetsByDate();

    @Query("SELECT c FROM Changeset c WHERE c.changeset_recorded_time > :date and c.changeset_recorded_time < :to ")
    List<Changeset> getChangsetsByDate(@Param("date") Timestamp date, @Param("to") Timestamp to);

}
