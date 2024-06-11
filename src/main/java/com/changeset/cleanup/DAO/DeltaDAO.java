package com.changeset.cleanup.DAO;

import com.changeset.cleanup.Model.Delta;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface DeltaDAO extends JpaRepository <Delta,Long> {

    @Query("select d from Delta d where d.changeset_id = :cid")
//    changeset_id = :cid
    public List< Delta> findByChangesetId(Long cid);


    @Query("select d from Delta d where d.changeset_id IN (SELECT c.id FROM Changeset c WHERE c.changeset_recorded_time > :from and c.changeset_recorded_time < :to)")
    public List<Delta> getDeltaDayByDay(@Param("from") Timestamp from , @Param("to") Timestamp to);


    @Transactional
    @Modifying
    @Query("delete  from Delta d where d.changeset_id = :cid ")
    public void deleteByChangesetId(@Param("cid") Long cid);



    @Transactional
    @Modifying
    @Query("delete  from Delta d where d.changeset_id in  (SELECT c.id FROM Changeset c WHERE c.changeset_recorded_time > :from and c.changeset_recorded_time < :to) ")
    public void deleteByDeltaByDate(@Param("from") Timestamp from, @Param("to") Timestamp to);



    @Query("select d from Delta d where d.changeset_id IN (SELECT c.id FROM Changeset c WHERE c.org_id = :orgId and c.changeset_recorded_time > :from and c.changeset_recorded_time < :to)")
    List<Delta> getDeltaByPartyID(@Param("from") Timestamp from, @Param("to") Timestamp to,@Param("orgId") Long orgId);



    @Transactional
    @Modifying
    @Query("delete  from Delta d where d.changeset_id in  (SELECT c.id FROM Changeset c WHERE c.org_id = :orgId and  c.changeset_recorded_time > :from and c.changeset_recorded_time < :to) ")
    public  void deleteDeltaByParty(@Param("from") Timestamp from, @Param("to") Timestamp to,@Param("orgId") Long orgId);
}
