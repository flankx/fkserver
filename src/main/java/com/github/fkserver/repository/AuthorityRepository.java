package com.github.fkserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.fkserver.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
