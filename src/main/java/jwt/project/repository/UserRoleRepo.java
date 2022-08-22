package jwt.project.repository;

import jwt.project.entity.Role;
import jwt.project.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

}
