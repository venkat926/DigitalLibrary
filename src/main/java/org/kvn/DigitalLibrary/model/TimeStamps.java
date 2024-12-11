package org.kvn.DigitalLibrary.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;

@Getter
@Setter
@MappedSuperclass
public class TimeStamps {
    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;
}
