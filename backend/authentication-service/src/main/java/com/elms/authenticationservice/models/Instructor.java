package com.elms.authenticationservice.models;

import java.math.BigInteger;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "instructor")
@Data
public class Instructor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "instructor_id",unique = true)
	private int id;
	@Column(name = "instructor_name", length = 50, nullable = false, unique = false)
	private String name;
	@Column(name = "instructor_email", length = 50, nullable = false, unique = true)
	private String email;
	@Column(name = "instructor_password", length = 30, nullable = false, unique = false)
	private String password;
	@Column(name="instructor_image",columnDefinition = "blob default 'https://www.personality-insights.com/wp-content/uploads/2017/12/default-profile-pic-e1513291410505.jpg'")
	private Blob image;
	@Column(name="bank_ifsc_code")
	private String bankIfscCode;
	@Column(name="account_number")
	private BigInteger accountNumber;

}

//ALTER TABLE `elms`.`instructor` 
//CHANGE COLUMN `account_number` `account_number` BIGINT NULL DEFAULT NULL ;
//ALTER TABLE `elms`.`instructor` 
//CHANGE COLUMN `bank_ifsc_code` `bank_ifsc_code` VARCHAR(20) NULL DEFAULT NULL ;
