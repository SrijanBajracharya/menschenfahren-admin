package com.achiever.menschenfahren.admin.data.service;

import com.achiever.menschenfahren.admin.data.entity.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}