package com.github.cliffdurden.postgresandjpatimedemo.repository;

import com.github.cliffdurden.postgresandjpatimedemo.entity.TimeDemoEntity;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;

//@DynamicInsert
public interface TimeDemoRepository extends JpaRepository<TimeDemoEntity, Long> {
}
