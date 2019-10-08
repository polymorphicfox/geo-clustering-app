package com.akos.testservice.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "geo_objects")
@Data
public class GeoObject {

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Id
    private String id;

    @Column(length = 12, precision = 7)
    @Basic
    private Double latitude;

    @Column(length = 12, precision = 7)
    @Basic
    private Double longitude;

    @Column(columnDefinition = "text")
    @Basic
    private String address;

    @Column(columnDefinition = "text")
    @Basic
    private String city;

    @Column(columnDefinition = "text")
    @Basic
    private String name;

    @Basic
    @Column(columnDefinition="text")
    private String quadkey;
}
