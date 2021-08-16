package com.monoplus.mcd.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoginUserRepository extends CrudRepository<LoginUser, String> {

    List<LoginUser> findByEmail(String email);
}
