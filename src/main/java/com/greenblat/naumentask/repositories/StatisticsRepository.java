package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT s.count FROM Statistics s WHERE UPPER(s.person.name) = UPPER(:name) GROUP BY UPPER(:name)")
    Integer findCountByPerson_Name(String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "UPDATE Statistics  s" +
                    " SET s.count = s.count + 1" +
                    " WHERE exists (SELECT * FROM Person p WHERE UPPER(p.name) = UPPER(:name) AND p.statistics_id =s .id)")
    void updateCountByName(@Param("name") String name);

}
