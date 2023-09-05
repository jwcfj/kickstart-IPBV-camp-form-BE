package com.blessedbytes.campform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import com.blessedbytes.campform.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Adicione métodos personalizados de consulta, se necessário
    // Por padrão, você terá todos os métodos de CRUD do JpaRepository
    UserDetails findByLogin(String login);

}
