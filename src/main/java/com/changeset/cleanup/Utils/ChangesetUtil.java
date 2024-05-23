package com.changeset.cleanup.Utils;

import com.changeset.cleanup.DAO.ChangesetDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ChangesetUtil {

    @Autowired
    private ChangesetDAO changesetDAO;


    public Timestamp dayIncrement(Timestamp nextDate){

        LocalDateTime start = nextDate.toLocalDateTime();
        LocalDateTime nextDay = start.plusDays(1);
        Timestamp nextDayTimestamp = Timestamp.valueOf(nextDay);
        Timestamp startday = Timestamp.valueOf(start);
        return startday;
    }
}
