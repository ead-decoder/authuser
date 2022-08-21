package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>, JpaSpecificationExecutor<UserCourseModel> {

    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

}
