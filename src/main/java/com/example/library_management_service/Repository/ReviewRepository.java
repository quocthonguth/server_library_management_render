package com.example.library_management_service.Repository;

import com.example.library_management_service.Entity.ReturnDetail;
import com.example.library_management_service.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
}
