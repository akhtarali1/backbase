package com.backbase.assesment.movies.persistance.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Academy award details entity
 *
 * @author Akhtar
 */
@Entity
@Data
@Table(name = "ACADEMY_AWARDS")
public class AcademyAward {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String fromYear;
    private String category;
    private String nominee;
    private String additionalInfo;
    private boolean won;
}
