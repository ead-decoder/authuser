package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existByUserAndCourseId(UserModel userModel, UUID courseId);
    UserCourseModel save(UserCourseModel userCourseModel);
}
