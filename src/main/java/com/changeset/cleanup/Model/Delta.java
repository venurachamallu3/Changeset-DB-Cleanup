package com.changeset.cleanup.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Delta {

    @Id
    private Long id;
    private Long changeset_id;
    private Long product_id;
    private Long license_id;
    private Character action;


}
