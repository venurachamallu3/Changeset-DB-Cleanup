package com.changeset.cleanup.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.DateFormat;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Changeset {
    @Id
    private Long id;
    private Long org_id;
    private String changeset_type;
    private Timestamp changeset_recorded_time;
    private String changeset_source;
}
