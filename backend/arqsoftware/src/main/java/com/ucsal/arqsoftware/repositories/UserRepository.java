package com.ucsal.arqsoftware.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.projections.UserDetailsProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(nativeQuery = true, value = """
			SELECT tb_user.login AS login, tb_user.password, tb_role.id AS 
				roleId, tb_role.authority 
				FROM tb_user INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id 
				INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id 
				WHERE tb_user.login = :login

			""")
	List<UserDetailsProjection> searchUserAndRolesByLogin(String login);
	
	Optional<User> findByLogin(String login);

	boolean existsByLogin(String login);

	Page<User> findByLoginIgnoreCaseContaining(String login, Pageable pageable);
	
}
