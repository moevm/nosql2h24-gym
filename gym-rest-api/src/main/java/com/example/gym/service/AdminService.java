package com.example.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.admin.UpdateAdminDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final Mapper modelMapper;

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
        return userRepository.findAllByRoles(UserRoleType.ROLE_ADMIN.name()).stream()
                .map(a -> modelMapper.toAdminDto(a))
                .toList();
    }

    public ResponseAdminDto findAdminById(String id) throws ResourceNotFoundException {
        User admin = getById(id);
        return modelMapper.toAdminDto(admin);
    }

    public void deleteAdmin(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public ResponseAdminDto updateAdmin(
            String id, 
            UpdateAdminDto dto
    ) throws ResourceNotFoundException, UniquenessViolationException {
        User admin = getById(id);
        
        if (dto.getName() != null && !admin.getName().equals(dto.getName())) {
            admin.setName(dto.getName());
        }

        if (dto.getSurname() != null && !admin.getSurname().equals(dto.getSurname())) {
            admin.setSurname(dto.getSurname());
        }

        if (dto.getEmail() != null && !admin.getEmail().equals(dto.getEmail())) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с электронной почтой %s уже существует"
                        .formatted(dto.getEmail()));
            } 
            
            admin.setEmail(dto.getEmail());
        }

        User updatedAdmin = userRepository.save(admin);
        return modelMapper.toAdminDto(updatedAdmin);
    }

    public User getById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Админ с ID %s не найден"
                        .formatted(id)));
    }

}
