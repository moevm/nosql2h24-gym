package com.example.gym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.admin.UpdateAdminDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final Mapper modelMapper;

    private final static Integer ROLE_ADMIN_INDEX = UserRoleType.ROLE_ADMIN.ordinal();

    // @Transactional
    // public ResponseAdminDto createAdmin(CreateAdminDto adminDto) {
    //     if (findAdminByEmail(adminDto.getEmail()).isPresent()) {
    //         throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
    //     }

    //     Admin admin = modelMapper.toModel(adminDto);
    //     Admin createdAdmin = adminRepository.save(admin);
    //     return modelMapper.toDto(createdAdmin);
    // }

    public List<ResponseAdminDto> findAllAdmins() {
        return userRepository.findAllByRoleIndex(ROLE_ADMIN_INDEX).stream()
                .map(a -> modelMapper.toAdminDto(a))
                .toList();
    }

    public ResponseAdminDto findAdminById(String id) {
        User admin = getById(id);
        return modelMapper.toAdminDto(admin);
    }

    public void deleteAdmin(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public ResponseAdminDto updateAdmin(String id, UpdateAdminDto dto) {
        User admin = getById(id);
        
        if (!admin.getName().equals(dto.getName()) && dto.getName() != null) {
            admin.setName(dto.getName());
        }

        if (!admin.getSurname().equals(dto.getSurname()) && dto.getSurname() != null) {
            admin.setSurname(dto.getSurname());
        }

        if (!admin.getEmail().equals(dto.getEmail()) && dto.getEmail() != null) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Пользователь с такой электронной почтой существует");
            } 
            
            admin.setEmail(dto.getEmail());
        }

        User updatedAdmin = userRepository.save(admin);
        return modelMapper.toAdminDto(updatedAdmin);
    }

    public User getById(String id) {
        Optional<User> optionalAdmin = userRepository.findById(id);
        if (optionalAdmin.isEmpty()) {
            throw new NoResultException("Админа с id %s не найден".formatted(id));
        }

        return optionalAdmin.get();
    }

}
