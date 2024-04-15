package com.changeset.cleanup.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.changeset.cleanup.Model.Changeset;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface ChangesetDAO extends JpaRepository <Changeset,Long> {

    public  List<Changeset> getChangsetsByDate(Timestamp from, Timestamp to);
}
