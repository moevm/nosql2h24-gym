package com.example.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.gym.exception.ResourceNotFoundException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.admin.ResponseAdminDto;
import com.example.gym.model.admin.UpdateAdminDto;
import com.example.gym.model.dto.statistics.admin.FinanceStatisticsDto;
import com.example.gym.model.dto.statistics.admin.PurchasedSubcriptions;
import com.example.gym.model.dto.statistics.admin.RoomActive;
import com.example.gym.model.dto.statistics.admin.RoomsActive;
import com.example.gym.model.dto.statistics.admin.SectionStatistics;
import com.example.gym.model.dto.statistics.admin.SubscriptionDetailDto;
import com.example.gym.model.promotion.CreatedBy;
import com.example.gym.model.promotion.Promotion;
import com.example.gym.model.room.Room;
import com.example.gym.model.settings.LoyaltySettings;
import com.example.gym.model.settings.dto.CreateLoyaltySettingsDto;
import com.example.gym.model.subscription.SubscriptionStatus;
import com.example.gym.model.training.Training;
import com.example.gym.model.user.ResponseUserDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.model.user.pojo.Subscription;
import com.example.gym.repository.LoyaltySettingsRepository;
import com.example.gym.repository.PromotionRepository;
import com.example.gym.repository.RoomRepository;
import com.example.gym.repository.TrainingRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.util.Mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UniquenessCheckService uniquenessCheckService;
    private final UserRepository userRepository;
    private final Mapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final PromotionRepository promotionRepository;
    private final LoyaltySettingsRepository loyaltySettingsRepository;
    private final TrainingRepository trainingRepository;
    private final RoomRepository roomRepository;

    // @Transactional
    // public ResponseAdminDto createAdmin(CreateAdminDto adminDto) {
    //     if (findAdminByEmail(adminDto.getEmail()).isPresent()) {
    //         throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
    //     }

    //     Admin admin = modelMapper.toModel(adminDto);
    //     Admin createdAdmin = adminRepository.save(admin);
    //     return modelMapper.toDto(createdAdmin);
    // }

    public Map<String, Map<String, Object>> getApplicationStatistics() {
        List<Training> trainings = trainingRepository.findAllByEndTimeBefore(LocalDateTime.now());

        Map<String, Map<String, Object>> statistics = new HashMap<>();
        statistics.put("today", new HashMap<String, Object>());
        statistics.put("week", new HashMap<String, Object>());
        statistics.put("month", new HashMap<String, Object>());

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);

        for (Training training : trainings) {
            System.out.println(training);
            LocalDate trainingDate = training.getStartTime().toLocalDate();
            System.out.println(training.getClients().size());
            System.out.println(training.getAvailableSlots());
            double trainingLoad = ((double) training.getClients().size() / (double) training.getAvailableSlots()) * 100;

            if (trainingDate.isEqual(today)) {
                updateStatistics(statistics, "today", trainingLoad);
            }

            if (trainingDate.isAfter(startOfWeek) && trainingDate.isBefore(today.plusDays(7))) {
                updateStatistics(statistics, "week", trainingLoad);
            }

            if (trainingDate.getMonth() == today.getMonth() && trainingDate.getYear() == today.getYear()) {
                updateStatistics(statistics, "month", trainingLoad);
            }
        }

        return statistics;
    }

    private void updateStatistics(Map<String, Map<String, Object>> statistics, String period, double load) {
        Map<String, Object> periodStats = statistics.get(period);

        int currentCount = (Integer) periodStats.getOrDefault("trainingCount", 0) + 1;
        double currentLoadSum = (Double) periodStats.getOrDefault("loadSum", 0.0) + load;

        System.out.println(load);
        System.out.println(currentCount);
        System.out.println(currentLoadSum);
        System.out.println(currentLoadSum / currentCount);

        periodStats.put("trainingCount", currentCount);
        periodStats.put("loadSum", currentLoadSum);
        periodStats.put("averageLoad", currentLoadSum / currentCount);
    }

    // public List<TrainerStatistics> getTrainersStatistics(LocalDate startDate, LocalDate endDate) {
    //     List<User> trainers = userRepository.findAllByRoles("ROLE_TRAINER");

    //     List<TrainerStatistics> statistics = trainers.stream()
    //             .map(t -> {
    //                 TrainingStatistics trainingStatistics = trainerService.getTrainingsStatistics(t.getId());
    //                 TrainerStatistics trainerStatistics = new TrainerStatistics();
    //                 trainerStatistics.setTrainer(modelMapper.toTrainerDto(t));
    //                 trainerStatistics.setTrainings(trainingStatistics);
    //                 return trainerStatistics;
    //             })
    //             .toList();

    //     return statistics;

    //     return null;
    // }

    public void setAcceptanceRate(CreateLoyaltySettingsDto dto) {
        loyaltySettingsRepository.deleteAll();
        List<LoyaltySettings> loyaltySettings = loyaltySettingsRepository.findAll();

        if (!loyaltySettings.isEmpty()) {
            LoyaltySettings loyaltySetting = new LoyaltySettings(null, dto.getAcceptanceRate());
            loyaltySettingsRepository.save(loyaltySetting);
        }
    }

    public List<ResponseUserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted(Comparator.comparing(user -> user.getRoles().stream()
                        .findFirst()
                        .orElse("")))
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ResponseAdminDto> findAllAdmins(
            String name,
            String surname,
            LocalDateTime birthdayFrom,
            LocalDateTime birthdayBefore
    ) {
        Query query = new Query();
        if (name != null && !name.isEmpty() && !name.isBlank())
            query.addCriteria(Criteria.where("name").regex(name, "i"));

        if (surname != null && !surname.isEmpty() && !surname.isBlank())
            query.addCriteria(Criteria.where("surname").regex(surname, "i"));

        if (birthdayFrom != null)
            query.addCriteria(Criteria.where("birthday").gte(birthdayFrom));

        if (birthdayBefore != null)
            query.addCriteria(Criteria.where("birthday").lt(birthdayBefore));

        query.addCriteria(Criteria.where("roles").in(UserRoleType.ROLE_ADMIN));

        List<User> admins = mongoTemplate.find(query, User.class);

        return admins.stream()
                .map(modelMapper::toAdminDto)
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

        Boolean needUpdate = false;

        if (dto.getName() != null && !admin.getName().equals(dto.getName())) {
            admin.setName(dto.getName());
            needUpdate = true;
        }

        if (dto.getSurname() != null && !admin.getSurname().equals(dto.getSurname())) {
            admin.setSurname(dto.getSurname());
            needUpdate = true;
        }

        if (dto.getEmail() != null && !admin.getEmail().equals(dto.getEmail())) {
            if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
                throw new UniquenessViolationException("Пользователь с электронной почтой %s уже существует"
                        .formatted(dto.getEmail()));
            }

            admin.setEmail(dto.getEmail());
        }

        if (dto.getGender() != null && admin.getGender() != dto.getGender()) {
            admin.setGender(dto.getGender());
        }

        if (dto.getBirthday() != null && (admin.getBirthday() != null && !admin.getBirthday().equals(dto.getBirthday()))) {
            admin.setBirthday(dto.getBirthday());
        }

        User updatedAdmin = userRepository.save(admin);

        if (needUpdate) {
            List<Promotion> promotions = promotionRepository.findAllByCreatedById(updatedAdmin.getId());
            promotions.forEach(promotion -> {
                CreatedBy createdBy = promotion.getCreatedBy();

                if (createdBy != null) {
                    if (dto.getName() != null && !createdBy.getName().equals(updatedAdmin.getName())) {
                        createdBy.setName(updatedAdmin.getName());
                    }

                    if (dto.getSurname() != null && !createdBy.getSurname().equals(updatedAdmin.getSurname())) {
                        createdBy.setSurname(updatedAdmin.getSurname());
                    }

                    promotionRepository.save(promotion);
                }
            });
        }
        return modelMapper.toAdminDto(updatedAdmin);
    }

    public User getById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Админ с ID %s не найден"
                        .formatted(id)));
    }

    public List<SectionStatistics> getFinishedTrainings(LocalDate startDate, LocalDate endDate, int page, int size) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<Training> trainings = trainingRepository.findAllByEndTimeBeforeAndStartTimeBetween(LocalDateTime.now(), startDateTime, endDateTime);

        log.info("Start Date: {}", startDateTime);
        log.info("End Date: {}", endDateTime);
        log.info("Found trainings: {}", trainings.size());

        Map<String, SectionStatistics> sectionStatisticsMap = new HashMap<>();

        for (Training training : trainings) {
            String sectionName = training.getSection().getName();
            SectionStatistics sectionStats = sectionStatisticsMap.getOrDefault(sectionName, new SectionStatistics());

            sectionStats.setTrainingCount(sectionStats.getTrainingCount() == null ? 1 : sectionStats.getTrainingCount() + 1);

            int currentClientCount = training.getClients().size();
            sectionStats.setClientCount(sectionStats.getClientCount() == null ? currentClientCount : sectionStats.getClientCount() + currentClientCount);

            int totalSlots = training.getRoom().getCapacity();
            double loadPercentage = totalSlots > 0 ? (currentClientCount / (double) totalSlots) * 100 : 0;
            sectionStats.setLoadPercentage(loadPercentage);

            sectionStats.setSectionName(sectionName);
            sectionStatisticsMap.put(sectionName, sectionStats);
        }

        Map<String, SectionStatistics> sortedMap = new TreeMap<>(sectionStatisticsMap);
        List<String> sortedKeys = new ArrayList<>(sortedMap.keySet());

        int start = page * size;
        int end = Math.min(start + size, sortedKeys.size());

        List<String> paginatedKeys = new ArrayList<>();
        if (size != 0 && start < sortedKeys.size()) {
            paginatedKeys = sortedKeys.subList(start, end);
        } else {
            paginatedKeys = sortedKeys;
        }

        return paginatedKeys.stream()
                .map(k -> sortedMap.get(k))
                .toList();
    }

    public PurchasedSubcriptions getPurchasedSubscriptions(
            String clientId,
            LocalDate startDate,
            LocalDate endDate,
            SubscriptionStatus status,
            int page,
            int size
    ) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        PurchasedSubcriptions purchasedSubcriptions = new PurchasedSubcriptions();
        List<User> clients = userRepository.findAllByRoles("ROLE_USER");
        List<User> filteredClients = clients.stream()
                .filter(client -> {
                    if (clientId != null && !client.getId().equals(clientId)) {
                        return false;
                    }

                    ClientInfo clientInfo = client.getClientInfo();
                    if (clientInfo != null) {
                        List<Subscription> subscriptions = clientInfo.getSubscriptions();
                        return subscriptions.stream().anyMatch(subscription -> {
                            LocalDateTime subscriptionStartDate = subscription.getStartDate();
                            LocalDateTime subscriptionEndDate = subscription.getEndDate();
                            boolean dateInRange = subscriptionStartDate.isBefore(endDateTime) && subscriptionEndDate.isAfter(startDateTime);
                            boolean statusMatches = (status == null || subscription.getStatus() == status);
                            return dateInRange && statusMatches;
                        });
                    }

                    return false;
                })
                .toList();

        System.out.println("Filtered Clients Count: " + filteredClients.size());

        Double totalPrice = filteredClients.stream()
                .flatMap(client -> client.getClientInfo().getSubscriptions().stream()
                        .filter(subscription -> (status == null || subscription.getStatus() == status)))
                .map(Subscription::getPrice)
                .reduce(0.0, Double::sum);

        purchasedSubcriptions.setTotalPrice(totalPrice);
//        purchasedSubcriptions.setCount(filteredClients.size());

        List<User> active = filteredClients.stream()
                .filter(client -> client.getClientInfo().getSubscriptions()
                        .stream().anyMatch(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE))
                .toList();
        purchasedSubcriptions.setActiveCount(active.size());

        List<User> freeze = filteredClients.stream()
                .filter(client -> client.getClientInfo().getSubscriptions()
                        .stream().anyMatch(subscription -> subscription.getStatus() == SubscriptionStatus.FREEZE))
                .toList();
        purchasedSubcriptions.setFreezeCount(freeze.size());

        List<SubscriptionDetailDto> subscriptions = filteredClients.stream()
                .flatMap(client -> client.getClientInfo().getSubscriptions()
                        .stream()
                        .filter(subscription -> (status == null || subscription.getStatus() == status))
                        .map(subscription -> modelMapper.toDto(subscription, client))) // Преобразование для всех подписок, фильтруя по статусу
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, subscriptions.size());
        if (size != 0 && start < subscriptions.size()) {
            purchasedSubcriptions.setSubscriptions(subscriptions.subList(start, end));
        } else {
            purchasedSubcriptions.setSubscriptions(subscriptions);
        }

        System.out.println("Total Subscriptions Count: " + subscriptions.size());
        purchasedSubcriptions.setCount(subscriptions.size());
        return purchasedSubcriptions;
    }

    public RoomsActive getRoomsActive(LocalDate startDate, LocalDate endDate) {
        RoomsActive roomsActive = new RoomsActive();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Room> rooms = roomRepository.findAll();
        System.out.println("Количество комнат: " + rooms.size());

        List<Training> trainings = trainingRepository.findAllByEndTimeBeforeAndStartTimeBetween(LocalDateTime.now(), startDateTime, endDateTime);
        System.out.println("Количество тренировок: " + trainings.size());

        System.out.println("Start Date: " + startDateTime);
        System.out.println("End Date: " + endDateTime);


        trainings.forEach(training -> System.out.println("Training Room ID: " + training.getRoom().getId()));

        Map<String, Long> trainingCountByRoomId = trainings.stream()
                .collect(Collectors.groupingBy(training -> training.getRoom().getId(), Collectors.counting()));

        // Выводим информацию о мапе
        trainingCountByRoomId.forEach((roomId, count) -> System.out.println("Room ID: " + roomId + ", Count: " + count));

        rooms.forEach(room -> {
            if (trainingCountByRoomId.containsKey(room.getId())) {
                System.out.println("Room ID: " + room.getId() + " will be added.");
            } else {
                System.out.println("Room ID: " + room.getId() + " will NOT be added because it has no active training.");
            }
        });

        if (trainings.isEmpty()) {
            System.out.println("Нет тренировок в указанном диапазоне дат со статусом AWAIT.");
            roomsActive.setRoomsActive(new ArrayList<>());
            return roomsActive;
        }

        List<RoomActive> roomActives = rooms.stream()
                .filter(room -> trainingCountByRoomId.containsKey(room.getId()))
                .map(room -> {
                    RoomActive roomActive = new RoomActive();
                    roomActive.setId(room.getId());
                    roomActive.setName(room.getName());
                    roomActive.setCapacity(room.getCapacity());
                    roomActive.setTrainingCount(trainingCountByRoomId.get(room.getId()));

                    if (room.getCapacity() != null && room.getCapacity() > 0) {
                        double loadPercentage = (trainingCountByRoomId.get(room.getId()).doubleValue() / room.getCapacity()) * 100;
                        roomActive.setLoadPercentage(loadPercentage);
                    } else {
                        roomActive.setLoadPercentage(0.0);
                    }

                    return roomActive;
                })
                .collect(Collectors.toList());

        roomsActive.setRoomsActive(roomActives);
        return roomsActive;
    }

    public List<FinanceStatisticsDto> getFinanceActivity(LocalDate startDate, LocalDate endDate) {
        List<User> trainers = userRepository.findAllByRoles("ROLE_TRAINER");
        List<String> trainerIds = trainers.stream().map(User::getId).toList();

        List<Training> trainings = trainingRepository.findAllByTrainerIds(trainerIds);

        LocalDateTime localEndDateTime = LocalDateTime.of(
                endDate.getYear(),
                endDate.getMonthValue(),
                endDate.getDayOfMonth(),
                23,
                59,
                59
        );

        List<FinanceStatisticsDto> filteredTrainings = trainings.stream()
                .filter(t -> !t.getStartTime().isBefore(startDate.atStartOfDay()) && !t.getEndTime().isAfter(localEndDateTime))
                .map(t -> {
                    Double profit = t.getDurationInHours() * t.getClients().size() * t.getTrainer().getHourlyRate();
                    return new FinanceStatisticsDto(t.getTrainer(), profit);
                })
                .collect(Collectors.toList());

        return filteredTrainings;

    }

}
