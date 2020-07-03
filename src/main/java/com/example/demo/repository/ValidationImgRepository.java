package com.example.demo.repository;


import com.example.demo.entity.ValidationImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValidationImgRepository extends JpaRepository<ValidationImg,Long> {

    List<ValidationImg> getAllByPathNotNull();
}
