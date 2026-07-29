package com.thuyen.bakeryshop.modules.auth.infrastructure.persistence;

import com.thuyen.bakeryshop.modules.auth.domain.UserCredentialRepository;
import com.thuyen.bakeryshop.modules.auth.domain.model.UserCredential;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.entity.UserCredentialEntity;
import com.thuyen.bakeryshop.modules.auth.infrastructure.persistence.mapper.UserCredentialMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserCredentialRepositoryImpl implements UserCredentialRepository {

    private final UserCredentialJpaRepository userCredentialJpaRepository;
    private final UserCredentialMapper userCredentialMapper;

    public UserCredentialRepositoryImpl(
            UserCredentialJpaRepository userCredentialJpaRepository,
            UserCredentialMapper userCredentialMapper
    ) {
        this.userCredentialJpaRepository = userCredentialJpaRepository;
        this.userCredentialMapper = userCredentialMapper;
    }

    @Override
    public Optional<UserCredential> findByUserId(UUID userId) {
        return userCredentialJpaRepository.findById(userId)
                .map(userCredentialMapper::toDomain);
    }

    @Override
    public UserCredential save(UserCredential credential) {
        UserCredentialEntity entity = userCredentialMapper.toEntity(credential);
        UserCredentialEntity savedEntity = userCredentialJpaRepository.save(entity);
        return userCredentialMapper.toDomain(savedEntity);
    }
}
