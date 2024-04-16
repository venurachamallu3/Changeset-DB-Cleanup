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

    @Query("SELECT c FROM Changeset c WHERE c.changeset_recorded_time > :date and c.changeset_recorded_time < :to ")
    List<Changeset> getChangsetsByDate(@Param("date") Timestamp date, @Param("to") Timestamp to);


}
