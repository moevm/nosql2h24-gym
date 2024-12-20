db = db.getSiblingDB("gym-db");

db.users.insertMany([
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1001"),
        "name": "Alice",
        "surname": "Smith",
        "email": "alice.smith@example.com",
        "password": "hashed_password_1",
        "phoneNumber": "+1234567001",
        "comment": "Frequent client",
        "createdAt": ISODate("2023-08-20T10:00:00Z"),
        "updatedAt": ISODate("2024-08-20T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("2002-08-20T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 200,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-10-01T00:00:00Z"),
                    "endDate": ISODate("2025-10-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1500.00"),
                    "createdAt": ISODate("2023-10-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-10-05T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-10-01T00:00:00Z"),
                    "endDate": ISODate("2023-10-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("1500.00"),
                    "createdAt": ISODate("2023-10-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-10-05T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1002"),
        "name": "Bob",
        "surname": "Johnson",
        "email": "bob.johnson@example.com",
        "password": "hashed_password_2",
        "phoneNumber": "+1234567002",
        "comment": "New client",
        "createdAt": ISODate("2023-09-01T10:00:00Z"),
        "updatedAt": ISODate("2024-09-01T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("1996-04-01T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 100,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-11-01T00:00:00Z"),
                    "endDate": ISODate("2025-11-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1000.00"),
                    "createdAt": ISODate("2023-11-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-11-10T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-11-01T00:00:00Z"),
                    "endDate": ISODate("2023-11-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("1000.00"),
                    "createdAt": ISODate("2023-11-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-11-10T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1003"),
        "name": "Charlie",
        "surname": "Brown",
        "email": "charlie.brown@example.com",
        "password": "hashed_password_3",
        "phoneNumber": "+1234567003",
        "comment": "Has an ongoing subscription",
        "createdAt": ISODate("2023-07-15T10:00:00Z"),
        "updatedAt": ISODate("2024-07-15T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("2000-04-01T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 300,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-07-01T00:00:00Z"),
                    "endDate": ISODate("2025-07-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1800.00"),
                    "createdAt": ISODate("2023-07-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-07-05T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-07-01T00:00:00Z"),
                    "endDate": ISODate("2023-07-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("1800.00"),
                    "createdAt": ISODate("2023-07-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-07-05T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1004"),
        "name": "Diana",
        "surname": "Prince",
        "email": "diana.prince@example.com",
        "password": "hashed_password_4",
        "phoneNumber": "+1234567004",
        "comment": "Prefers group classes",
        "createdAt": ISODate("2023-06-10T10:00:00Z"),
        "updatedAt": ISODate("2024-06-10T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1995-02-01T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 250,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-06-01T00:00:00Z"),
                    "endDate": ISODate("2025-06-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1300.00"),
                    "createdAt": ISODate("2023-06-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-06-05T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-06-01T00:00:00Z"),
                    "endDate": ISODate("2023-06-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("1300.00"),
                    "createdAt": ISODate("2023-06-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-06-05T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1005"),
        "name": "Eve",
        "surname": "Adams",
        "email": "eve.adams@example.com",
        "password": "hashed_password_5",
        "phoneNumber": "+1234567005",
        "comment": "Regular visitor",
        "createdAt": ISODate("2023-05-05T10:00:00Z"),
        "updatedAt": ISODate("2024-05-05T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1991-02-01T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 180,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-05-01T00:00:00Z"),
                    "endDate": ISODate("2025-01-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1600.00"),
                    "createdAt": ISODate("2023-05-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-05-05T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-05-01T00:00:00Z"),
                    "endDate": ISODate("2023-05-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("1600.00"),
                    "createdAt": ISODate("2023-05-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-05-05T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    }
]);

db.users.insertMany([
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf2001"),
        "name": "Michael",
        "surname": "Jordan",
        "email": "michael.jordan@example.com",
        "password": "hashed_password_6",
        "phoneNumber": "+1234567101",
        "comment": "Specializes in basketball training",
        "createdAt": ISODate("2023-01-10T10:00:00Z"),
        "updatedAt": ISODate("2024-01-10T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("2000-02-01T00:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "Professional Basketball Coach",
            "hourlyRate": NumberDecimal("200.00"),
            "sections": [
                "Basketball Skills",
                "Team Strategy"
            ]
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf2002"),
        "name": "Serena",
        "surname": "Williams",
        "email": "serena.williams@example.com",
        "password": "hashed_password_7",
        "phoneNumber": "+1234567102",
        "comment": "Expert in tennis training",
        "createdAt": ISODate("2023-02-15T10:00:00Z"),
        "updatedAt": ISODate("2024-02-15T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1993-01-12T00:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "Grand Slam Winner",
            "hourlyRate": NumberDecimal("250.00"),
            "sections": [
                "Tennis Fundamentals",
                "Advanced Techniques"
            ]
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf2003"),
        "name": "Usain",
        "surname": "Bolt",
        "email": "usain.bolt@example.com",
        "password": "hashed_password_8",
        "phoneNumber": "+1234567103",
        "comment": "Specialist in sprint training",
        "createdAt": ISODate("2023-03-20T10:00:00Z"),
        "updatedAt": ISODate("2024-03-20T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("1990-01-12T00:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "Olympic Sprinting Champion",
            "hourlyRate": NumberDecimal("300.00"),
            "sections": [
                "Sprint Training",
                "Endurance Building"
            ]
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf2004"),
        "name": "Simone",
        "surname": "Biles",
        "email": "simone.biles@example.com",
        "password": "hashed_password_9",
        "phoneNumber": "+1234567104",
        "comment": "Focuses on gymnastics coaching",
        "createdAt": ISODate("2023-04-25T10:00:00Z"),
        "updatedAt": ISODate("2024-04-25T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("2001-01-12T00:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "World Champion Gymnast",
            "hourlyRate": NumberDecimal("220.00"),
            "sections": [
                "Gymnastics Basics",
                "Floor Exercises"
            ]
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf2005"),
        "name": "Tom",
        "surname": "Brady",
        "email": "tom.brady@example.com",
        "password": "hashed_password_10",
        "phoneNumber": "+1234567105",
        "comment": "Specializes in football coaching",
        "createdAt": ISODate("2023-05-10T10:00:00Z"),
        "updatedAt": ISODate("2024-05-10T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("2001-01-12T00:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "NFL MVP",
            "hourlyRate": NumberDecimal("250.00"),
            "sections": [
                "Football Techniques",
                "Team Leadership"
            ]
        }
    }
]);

db.users.insertMany([
    {
        "_id": ObjectId("653ef3a8a3e34567bfff3001"),
        "name": "Anna",
        "surname": "Smith",
        "email": "anna.smith@example.com",
        "password": "hashed_password_11",
        "phoneNumber": "+1234567201",
        "comment": "Main administrator",
        "createdAt": ISODate("2023-01-01T10:00:00Z"),
        "updatedAt": ISODate("2024-01-01T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1994-11-12T00:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3002"),
        "name": "Brian",
        "surname": "Taylor",
        "email": "brian.taylor@example.com",
        "password": "hashed_password_12",
        "phoneNumber": "+1234567202",
        "comment": "Handles user issues",
        "createdAt": ISODate("2023-02-10T10:00:00Z"),
        "updatedAt": ISODate("2024-02-10T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("1995-11-12T00:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3003"),
        "name": "Catherine",
        "surname": "Johnson",
        "email": "catherine.johnson@example.com",
        "password": "hashed_password_13",
        "phoneNumber": "+1234567203",
        "comment": "Manages schedules",
        "createdAt": ISODate("2023-03-15T10:00:00Z"),
        "updatedAt": ISODate("2024-03-15T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1995-12-12T00:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3004"),
        "name": "David",
        "surname": "Brown",
        "email": "david.brown@example.com",
        "password": "hashed_password_14",
        "phoneNumber": "+1234567204",
        "comment": "Supervises finances",
        "createdAt": ISODate("2023-04-20T10:00:00Z"),
        "updatedAt": ISODate("2024-04-20T10:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("1992-12-12T00:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    },
    {
        "_id": ObjectId("653ff3a8a3e34567bcdf3005"),
        "name": "Emily",
        "surname": "Davis",
        "email": "emily.davis@example.com",
        "password": "hashed_password_15",
        "phoneNumber": "+1234567205",
        "comment": "In charge of HR",
        "createdAt": ISODate("2023-05-25T10:00:00Z"),
        "updatedAt": ISODate("2024-05-25T10:00:00Z"),
        "gender": "FEMALE",
        "birthday": ISODate("1997-12-12T00:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    }
]);

db.rooms.insertMany([
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4201"),
        "_class": "com.example.gym.model.room.Room",
        "capacity": 25,
        "location": {
            "address": "Moscow, Tverskaya Street, 12",
            "number": 3
        },
        "name": "Downtown Fitness Center",
        "openingTime": ISODate("2024-12-13T06:30:00.000Z"),
        "closingTime": ISODate("2024-12-13T15:00:00.000Z"),
        "sections": ["Sprint Training", "Endurance Building", "Gymnastics Basics", "Floor Exercises"],
        "trainers": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2003"),
                "name": "Usain",
                "surname": "Bolt",
                "gender": "MALE",
                "qualification": "Olympic Sprinting Champion",
                "hourlyRate": 300
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2004"),
                "name": "Simone",
                "surname": "Biles",
                "gender": "MALE",
                "qualification": "World Champion Gymnast",
                "hourlyRate": 220
            }
        ],
        "updatedAt": ISODate("2024-12-12T22:32:35.780Z"),
        "workingDays": "ВТ, ЧТ, СБ"
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4002"),
        "_class": "com.example.gym.model.room.Room",
        "capacity": 15,
        "location": {
            "address": "Saint-Petersburg, Liteyny Avenue, 45",
            "number": 2
        },
        "name": "Northern Athletic Hall",
        "openingTime": ISODate("2024-12-13T08:00:00.000Z"),
        "closingTime": ISODate("2024-12-13T16:30:00.000Z"),
        "sections": ["Basketball Skills", "Team Strategy"],
        "trainers": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2001"),
                "name": "Michael",
                "surname": "Jordan",
                "gender": "MALE",
                "qualification": "Professional Basketball Coach",
                "hourlyRate": 200
            }
        ],
        "updatedAt": ISODate("2024-12-12T22:34:08.486Z"),
        "workingDays": "ПН, ВТ, СР, ЧТ, ПТ, СБ"
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
        "_class": "com.example.gym.model.room.Room",
        "capacity": 30,
        "location": {
            "address": "Kazan, Pushkin Street, 18",
            "number": 1
        },
        "name": "Central Power Gym",
        "openingTime": ISODate("2024-12-13T07:00:00.000Z"),
        "closingTime": ISODate("2024-12-13T16:00:00.000Z"),
        "sections": ["Basketball Skills", "Team Strategy", "Tennis Fundamentals", "Advanced Techniques"],
        "trainers": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2001"),
                "name": "Michael",
                "surname": "Jordan",
                "gender": "MALE",
                "qualification": "Professional Basketball Coach",
                "hourlyRate": 200
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2002"),
                "name": "Serena",
                "surname": "Williams",
                "gender": "FEMALE",
                "qualification": "Grand Slam Winner",
                "hourlyRate": 250
            }
        ],
        "updatedAt": ISODate("2024-12-12T22:30:03.896Z"),
        "workingDays": "ПН, СР, ПТ"
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4004"),
        "_class": "com.example.gym.model.room.Room",
        "capacity": 20,
        "location": {
            "address": "Sochi, Kurortny Avenue, 99",
            "number": 4
        },
        "name": "Southern Training Hall",
        "openingTime": ISODate("2024-12-13T05:00:00.000Z"),
        "closingTime": ISODate("2024-12-13T14:00:00.000Z"),
        "sections": ["Sprint Training", "Endurance Building"],
        "trainers": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2003"),
                "name": "Usain",
                "surname": "Bolt",
                "gender": "MALE",
                "qualification": "Olympic Sprinting Champion",
                "hourlyRate": 300
            }
        ],
        "updatedAt": ISODate("2024-12-12T22:34:58.388Z"),
        "workingDays": "СР, ЧТ, ПТ, ВТ, ПН"
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4005"),
        "_class": "com.example.gym.model.room.Room",
        "capacity": 18,
        "location": {
            "address": "Vladivostok, Svetlanskaya Street, 8",
            "number": 5
        },
        "name": "Eastern Fitness Arena",
        "openingTime": ISODate("2024-12-13T07:00:00.000Z"),
        "closingTime": ISODate("2024-12-13T15:00:00.000Z"),
        "sections": ["Football Techniques", "Team Leadership"],
        "trainers": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf2005"),
                "name": "Tom",
                "surname": "Brady",
                "gender": "MALE",
                "qualification": "NFL MVP",
                "hourlyRate": 250
            }
        ],
        "updatedAt": ISODate("2024-12-12T22:33:26.177Z"),
        "workingDays": "ПН, ВТ, СР, ЧТ, ПТ"
    }
]);

db.promotions.insertMany([
    {
        "_id": ObjectId("653a1f2c1c9d440000a1bc24"),
        "name": "Летний абонемент",
        "description": "Скидка 15% на летние абонементы для посещения зала.",
        "startDate": ISODate("2024-06-01T00:00:00Z"),
        "endDate": ISODate("2024-08-31T00:00:00Z"),
        "discountPercentage": 15,
        "createdBy": {
            "_id": ObjectId("653a1d4b2a7d440000a1a0e5"),
            "name": "Сергей",
            "surname": "Кузнецов"
        },
        "created": ISODate("2024-05-15T09:00:00Z")
    },
    {
        "_id": ObjectId("653a1f2c1c9d440000a1bc25"),
        "name": "Весенние скидки",
        "description": "Скидка 10% на все услуги в честь прихода весны.",
        "startDate": ISODate("2024-03-01T00:00:00Z"),
        "endDate": ISODate("2024-04-30T00:00:00Z"),
        "discountPercentage": 10,
        "createdBy": {
            "_id": ObjectId("653a1d4b2a7d440000a1a0e6"),
            "name": "Ольга",
            "surname": "Петрова"
        },
        "created": ISODate("2024-02-20T14:00:00Z")
    },
    {
        "_id": ObjectId("653a1f2c1c9d440000a1bc26"),
        "name": "Зимняя распродажа",
        "description": "Скидка 25% на годовой абонемент в зимний период.",
        "startDate": ISODate("2024-01-01T00:00:00Z"),
        "endDate": ISODate("2024-02-28T00:00:00Z"),
        "discountPercentage": 25,
        "createdBy": {
            "_id": ObjectId("653a1d4b2a7d440000a1a0e7"),
            "name": "Иван",
            "surname": "Смирнов"
        },
        "created": ISODate("2023-12-15T12:00:00Z")
    },
    {
        "_id": ObjectId("653a1f2c1c9d440000a1bc27"),
        "name": "Осенние скидки",
        "description": "Скидка 30% на первые занятия в октябре!",
        "startDate": ISODate("2024-10-01T00:00:00Z"),
        "endDate": ISODate("2024-10-31T00:00:00Z"),
        "discountPercentage": 30,
        "createdBy": {
            "_id": ObjectId("653a1d4b2a7d440000a1a0e8"),
            "name": "Мария",
            "surname": "Соколова"
        },
        "created": ISODate("2024-09-20T11:00:00Z")
    },
    {
        "_id": ObjectId("653a1f2c1c9d440000a1bc28"),
        "name": "День открытых дверей",
        "description": "Бесплатный доступ к тренажёрам и занятиям в течение одного дня.",
        "startDate": ISODate("2024-05-01T00:00:00Z"),
        "endDate": ISODate("2024-05-01T23:59:59Z"),
        "discountPercentage": 100,
        "createdBy": {
            "_id": ObjectId("653a1d4b2a7d440000a1a0e9"),
            "name": "Алексей",
            "surname": "Титов"
        },
        "created": ISODate("2024-04-10T16:00:00Z")
    }
]);

db.training_sessions.insertMany([
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3001"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 4,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1001"),
                "name": "Alice",
                "surname": "Smith",
                "loyaltyPoints": 200
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1002"),
                "name": "Bob",
                "surname": "Johnson",
                "loyaltyPoints": 100
            }
            // {
            //     "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
            //     "name": "Иван",
            //     "surname": "Петров",
            //     "loyaltyPoints": 50,
            //     "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            // }
        ],
        "startTime": ISODate("2024-12-29T10:00:00.000Z"),
        "endTime": ISODate("2024-12-29T11:30:00.000Z"),
        "createdAt": ISODate("2024-10-15T00:00:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5001"),
            "name": "Downtown Fitness Center",
            "capacity": 25
        },
        "section": {
            "name": "Basketball Skills"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2001"),
            "name": "Michael",
            "surname": "Jordan",
            "qualification": "Professional Basketball Coach",
            "hourlyRate": 200
        },
        "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3902"),
        "availableSlots": 4,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1003"),
                "name": "Charlie",
                "surname": "Brown",
                "loyaltyPoints": 300
            }
        ],
        "createdAt": ISODate("2024-12-28T00:00:00.000Z"),
        "startTime": ISODate("2024-12-28T14:00:00.000Z"),
        "endTime": ISODate("2024-12-28T15:30:00.000Z"),
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5002"),
            "name": "Eastern Fitness Arena",
            "capacity": 18
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4002"),
            "name": "Tennis Fundamentals"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2002"),
            "name": "Serena",
            "surname": "Williams",
            "qualification": "Grand Slam Winner",
            "hourlyRate": NumberDecimal("250.00")
        },
        "updatedAt": ISODate("2024-10-21T00:00:00.000Z")
    },
    // {
    //     "_id": ObjectId("653ef3a8a3e34587bcdf3003"),
    //     "_class": "com.example.gym.model.training.Training",
    //     "availableSlots": 9,
    //     "clients": [
    //         {
    //             "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
    //             "name": "Иван",
    //             "surname": "Петров",
    //             "loyaltyPoints": 50,
    //             "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
    //         }
    //     ],
    //     "createdAt": ISODate("2024-12-17T00:00:00.000Z"),
    //     "startTime": ISODate("2024-11-04T08:00:00.000Z"),
    //     "endTime": ISODate("2024-12-04T09:30:00.000Z"),
    //     "hasFreeRegistration": true,
    //     "room": {
    //         "_id": ObjectId("653ef3a8a3e34567bcdf5003"),
    //         "name": "Southern Training Hall",
    //         "capacity": 20
    //     },
    //     "section": {
    //         name: "Sprint Training"
    //     },
    //     "trainer": {
    //         "_id": ObjectId("653ef3a8a3e34567bcdf2003"),
    //         "name": "Usain",
    //         "surname": "Bolt",
    //         "qualification": "Olympic Sprinting Champion",
    //         "hourlyRate": 300
    //     },
    //     "updatedAt": ISODate("2024-12-12T23:10:23.885Z")
    // },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3334"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 2,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1004"),
                "name": "Diana",
                "surname": "Prince",
                "loyaltyPoints": 250
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1005"),
                "name": "Eve",
                "surname": "Adams",
                "loyaltyPoints": 180
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
                "name": "Иван",
                "surname": "Петров",
                "loyaltyPoints": 50,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        "createdAt": ISODate("2024-12-27T00:00:00.000Z"),
        "startTime": ISODate("2024-12-27T16:00:00.000Z"),
        "endTime": ISODate("2024-12-27T17:30:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5004"),
            "name": "Central Power Gym",
            "capacity": 30
        },
        "section": {
            name: "Gymnastics Basics"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2004"),
            "name": "Simone",
            "surname": "Biles",
            "qualification": "World Champion Gymnast",
            "hourlyRate": 220
        },
        "updatedAt": ISODate("2024-12-12T23:10:15.991Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3005"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 7,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
                "name": "Иван",
                "surname": "Петров",
                "loyaltyPoints": 50,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        "createdAt": ISODate("2024-12-29T00:00:00.000Z"),
        "startTime": ISODate("2024-12-29T08:00:00.000Z"),
        "endTime": ISODate("2024-12-29T09:30:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5005"),
            "name": "Northern Athletic Hall",
            "capacity": 15
        },
        "section": {
            name: "Football Techniques"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2005"),
            "name": "Tom",
            "surname": "Brady",
            "qualification": "NFL MVP",
            "hourlyRate": 250
        },
        "updatedAt": ISODate("2024-12-12T23:07:26.068Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf1462"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 12,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1001"),
                "name": "Alice",
                "surname": "Smith",
                "loyaltyPoints": 200,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1002"),
                "name": "Bob",
                "surname": "Johnson",
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z"),
                "loyaltyPoints": 100
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
                "name": "Иван",
                "surname": "Петров",
                "loyaltyPoints": 50,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1003"),
                "name": "Charlie",
                "surname": "Brown",
                "loyaltyPoints": 120,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        // 1
        "createdAt": ISODate("2024-12-29T00:00:00.000Z"),
        "startTime": ISODate("2024-12-29T11:00:00.000Z"),
        "endTime": ISODate("2024-12-29T13:00:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5001"),
            "name": "Downtown Fitness Center",
            "capacity": 25
        },
        "section": {
            "name": "Функциональная тренировка"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
            "name": "Дмитрий",
            "surname": "Иванов",
            "qualification": "Кандидат в мастера спорта",
            "hourlyRate": 350
        },
        "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcff1462"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 15,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf3002"),
                "name": "Brian",
                "surname": "Taylor",
                "loyaltyPoints": 200,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf3003"),
                "name": "Catherine",
                "surname": "Johnson",
                "loyaltyPoints": 100,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf3004"),
                "name": "David",
                "surname": "Brown",
                "loyaltyPoints": 50,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        // 2
        "createdAt": ISODate("2024-12-15T00:00:00.000Z"),
        "startTime": ISODate("2024-12-15T10:00:00.000Z"),
        "endTime": ISODate("2024-12-15T12:00:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
            "name": "Central Power Gym",
            "capacity": 30
        },
        "section": {
            "name": "Кардио тренировка"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
            "name": "Дмитрий",
            "surname": "Иванов",
            "qualification": "Кандидат в мастера спорта",
            "hourlyRate": 350
        },
        "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
    },

    {
        "_id": ObjectId("653ef3a8a3e34567bcff1466"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 9,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1005"),
                "name": "Eve",
                "surname": "Adams",
                "loyaltyPoints": 180,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            },
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1004"),
                "name": "Diana",
                "surname": "Prince",
                "loyaltyPoints": 250,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        // 3
        "createdAt": ISODate("2024-12-01T00:00:00.000Z"),
        "startTime": ISODate("2024-12-01T10:00:00.000Z"),
        "endTime": ISODate("2024-12-01T12:00:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
            "name": "Central Power Gym",
            "capacity": 30
        },
        "section": {
            "name": "Кардио тренировка"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
            "name": "Дмитрий",
            "surname": "Иванов",
            "qualification": "Кандидат в мастера спорта",
            "hourlyRate": 350
        },
        "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
    },

    {
        "_id": ObjectId("653ef3a8a3e34567bcff1467"),
        "_class": "com.example.gym.model.training.Training",
        "availableSlots": 5,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1002"),
                "name": "Bob",
                "surname": "Johnson",
                "loyaltyPoints": 180,
                "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
            }
        ],
        // 4
        "createdAt": ISODate("2024-10-15T00:00:00.000Z"),
        "startTime": ISODate("2024-10-15T10:00:00.000Z"),
        "endTime": ISODate("2024-10-15T12:00:00.000Z"),
        "hasFreeRegistration": true,
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
            "name": "Central Power Gym",
            "capacity": 30
        },
        "section": {
            "name": "Кардио тренировка"
        },
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
            "name": "Дмитрий",
            "surname": "Иванов",
            "qualification": "Кандидат в мастера спорта",
            "hourlyRate": 350
        },
        "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
    }

]);

db.users.insertMany([
    // Отладочный Клиент
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
        "name": "Иван",
        "surname": "Петров",
        "email": "client@gmail.com",
        "password": "client",
        "phoneNumber": "+79001234567",
        "comment": "Отладочный клиент",
        "createdAt": ISODate("2024-01-01T10:00:00Z"),
        "updatedAt": ISODate("2024-01-02T10:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 50,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-01-01T00:00:00Z"),
                    "endDate": ISODate("2025-01-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1000.00"),
                    "createdAt": ISODate("2024-01-01T00:00:00Z"),
                    "updatedAt": ISODate("2024-01-02T00:00:00Z")
                },
                {
                    "startDate": ISODate("2022-01-01T00:00:00Z"),
                    "endDate": ISODate("2023-01-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("15000.00"),
                    "createdAt": ISODate("2024-01-01T00:00:00Z"),
                    "updatedAt": ISODate("2024-01-02T00:00:00Z")
                },
                {
                    "startDate": ISODate("2021-01-01T00:00:00Z"),
                    "endDate": ISODate("2022-01-01T00:00:00Z"),
                    "status": "INACTIVE",
                    "price": NumberDecimal("13000.00"),
                    "createdAt": ISODate("2024-01-01T00:00:00Z"),
                    "updatedAt": ISODate("2024-01-02T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },

    // Отладочный Админ
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4002"),
        "name": "Анна",
        "surname": "Смирнова",
        "email": "admin@gmail.com",
        "password": "admin",
        "phoneNumber": "+79001234568",
        "comment": "Отладочный администратор",
        "createdAt": ISODate("2024-01-01T10:00:00Z"),
        "updatedAt": ISODate("2024-01-02T10:00:00Z"),
        "roles": ["ROLE_ADMIN"],
        "clientInfo": null,
        "trainerInfo": null
    },

    // Отладочный Тренер
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
        "name": "Дмитрий",
        "surname": "Иванов",
        "email": "trainer@gmail.com",
        "password": "trainer",
        "phoneNumber": "+79001234569",
        "comment": "Отладочный тренер",
        "createdAt": ISODate("2024-01-01T10:00:00Z"),
        "updatedAt": ISODate("2024-01-02T10:00:00Z"),
        "roles": ["ROLE_TRAINER"],
        "clientInfo": null,
        "trainerInfo": {
            "qualification": "Кандидат в мастера спорта",
            "hourlyRate": NumberDecimal("350.00"),
            "sections": [
                "Функциональная тренировка",
                "Кардио тренировка"
            ]
        }
    }
]);


// новые клиенты
db.users.insertMany([
    {
        "_id": ObjectId("613ef3a8a3e34567bcdf1002"),
        "name": "Bob",
        "surname": "Johnson",
        "email": "bob.johnson@example.com",
        "password": "hashed_password_2",
        "phoneNumber": "+1234567002",
        "comment": "New client",
        "createdAt": ISODate("2023-09-01T12:00:00Z"),
        "updatedAt": ISODate("2024-09-01T12:00:00Z"),
        "gender": "MALE",
        "birthday": ISODate("1990-01-15T00:00:00Z"),
        "roles": ["ROLE_USER"],
        "clientInfo": {
            "loyaltyPoints": 50,
            "subscriptions": [
                {
                    "startDate": ISODate("2024-11-01T00:00:00Z"),
                    "endDate": ISODate("2025-11-01T00:00:00Z"),
                    "status": "ACTIVE",
                    "price": NumberDecimal("1200.00"),
                    "createdAt": ISODate("2023-11-01T00:00:00Z"),
                    "updatedAt": ISODate("2023-11-05T00:00:00Z")
                }
            ]
        },
        "trainerInfo": null
    },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1003"),
            "name": "Carol",
            "surname": "Williams",
            "email": "carol.williams@example.com",
            "password": "hashed_password_3",
            "phoneNumber": "+1234567003",
            "comment": "Prefers morning sessions",
            "createdAt": ISODate("2023-05-15T08:00:00Z"),
            "updatedAt": ISODate("2024-05-15T08:00:00Z"),
            "gender": "FEMALE",
            "birthday": ISODate("1985-05-25T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 300,
                "subscriptions": [
                    {
                        "startDate": ISODate("2023-01-01T00:00:00Z"),
                        "endDate": ISODate("2024-01-01T00:00:00Z"),
                        "status": "INACTIVE",
                        "price": NumberDecimal("1500.00"),
                        "createdAt": ISODate("2023-01-01T00:00:00Z"),
                        "updatedAt": ISODate("2023-01-10T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1004"),
            "name": "David",
            "surname": "Brown",
            "email": "david.brown@example.com",
            "password": "hashed_password_4",
            "phoneNumber": "+1234567004",
            "comment": "Active participant in group classes",
            "createdAt": ISODate("2023-03-20T15:00:00Z"),
            "updatedAt": ISODate("2024-03-20T15:00:00Z"),
            "gender": "MALE",
            "birthday": ISODate("1988-03-30T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 150,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-06-01T00:00:00Z"),
                        "endDate": ISODate("2025-06-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("1700.00"),
                        "createdAt": ISODate("2024-06-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-06-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1005"),
            "name": "Emily",
            "surname": "Davis",
            "email": "emily.davis@example.com",
            "password": "hashed_password_5",
            "phoneNumber": "+1234567005",
            "comment": "Regular yoga practitioner",
            "createdAt": ISODate("2023-07-10T10:00:00Z"),
            "updatedAt": ISODate("2024-07-10T10:00:00Z"),
            "gender": "FEMALE",
            "birthday": ISODate("1995-07-20T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 400,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-09-01T00:00:00Z"),
                        "endDate": ISODate("2025-09-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("2000.00"),
                        "createdAt": ISODate("2024-09-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-09-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1006"),
            "name": "Frank",
            "surname": "Miller",
            "email": "frank.miller@example.com",
            "password": "hashed_password_6",
            "phoneNumber": "+1234567006",
            "comment": "Occasional visitor",
            "createdAt": ISODate("2023-11-01T09:00:00Z"),
            "updatedAt": ISODate("2024-11-01T09:00:00Z"),
            "gender": "MALE",
            "birthday": ISODate("1980-11-05T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 30,
                "subscriptions": []
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1007"),
            "name": "Grace",
            "surname": "Taylor",
            "email": "grace.taylor@example.com",
            "password": "hashed_password_7",
            "phoneNumber": "+1234567007",
            "comment": "Prefers personal training",
            "createdAt": ISODate("2023-01-15T11:00:00Z"),
            "updatedAt": ISODate("2024-01-15T11:00:00Z"),
            "gender": "FEMALE",
            "birthday": ISODate("1992-01-20T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 500,
                "subscriptions": [
                    {
                        "startDate": ISODate("2023-08-01T00:00:00Z"),
                        "endDate": ISODate("2024-08-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("2200.00"),
                        "createdAt": ISODate("2023-08-01T00:00:00Z"),
                        "updatedAt": ISODate("2023-08-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1008"),
            "name": "Henry",
            "surname": "Wilson",
            "email": "henry.wilson@example.com",
            "password": "hashed_password_8",
            "phoneNumber": "+1234567008",
            "comment": "Focuses on cardio workouts",
            "createdAt": ISODate("2023-03-05T07:00:00Z"),
            "updatedAt": ISODate("2024-03-05T07:00:00Z"),
            "gender": "MALE",
            "birthday": ISODate("1998-03-15T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 250,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-04-01T00:00:00Z"),
                        "endDate": ISODate("2025-04-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("1800.00"),
                        "createdAt": ISODate("2024-04-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-04-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1009"),
            "name": "Ivy",
            "surname": "Moore",
            "email": "ivy.moore@example.com",
            "password": "hashed_password_9",
            "phoneNumber": "+1234567009",
            "comment": "Enjoys group fitness",
            "createdAt": ISODate("2023-06-25T13:00:00Z"),
            "updatedAt": ISODate("2024-06-25T13:00:00Z"),
            "gender": "FEMALE",
            "birthday": ISODate("2000-06-15T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 100,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-07-01T00:00:00Z"),
                        "endDate": ISODate("2025-07-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("1400.00"),
                        "createdAt": ISODate("2024-07-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-07-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1010"),
            "name": "Jack",
            "surname": "Anderson",
            "email": "jack.anderson@example.com",
            "password": "hashed_password_10",
            "phoneNumber": "+1234567010",
            "comment": "Looking to improve strength",
            "createdAt": ISODate("2023-04-30T18:00:00Z"),
            "updatedAt": ISODate("2024-04-30T18:00:00Z"),
            "gender": "MALE",
            "birthday": ISODate("1991-04-20T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 200,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-05-01T00:00:00Z"),
                        "endDate": ISODate("2025-05-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("1600.00"),
                        "createdAt": ISODate("2024-05-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-05-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        },
        {
            "_id": ObjectId("613ef3a8a3e34567bcdf1011"),
            "name": "Katie",
            "surname": "White",
            "email": "katie.white@example.com",
            "password": "hashed_password_11",
            "phoneNumber": "+1234567011",
            "comment": "Prefers weekend sessions",
            "createdAt": ISODate("2023-12-01T10:00:00Z"),
            "updatedAt": ISODate("2024-12-01T10:00:00Z"),
            "gender": "FEMALE",
            "birthday": ISODate("1993-12-15T00:00:00Z"),
            "roles": ["ROLE_USER"],
            "clientInfo": {
                "loyaltyPoints": 350,
                "subscriptions": [
                    {
                        "startDate": ISODate("2024-01-01T00:00:00Z"),
                        "endDate": ISODate("2025-01-01T00:00:00Z"),
                        "status": "ACTIVE",
                        "price": NumberDecimal("1900.00"),
                        "createdAt": ISODate("2024-01-01T00:00:00Z"),
                        "updatedAt": ISODate("2024-01-05T00:00:00Z")
                    }
                ]
            },
            "trainerInfo": null
        }
    ])

db.training_sessions.insertMany(
    [
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1467"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1002"),
                    "name": "Bob",
                    "surname": "Johnson",
                    "loyaltyPoints": 180,
                    "registrationDate": ISODate("2024-01-01T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-15T00:00:00.000Z"),
            "startTime": ISODate("2024-10-15T10:00:00.000Z"),
            "endTime": ISODate("2024-10-15T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1468"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1003"),
                    "name": "Carol",
                    "surname": "Williams",
                    "loyaltyPoints": 250,
                    "registrationDate": ISODate("2024-01-15T10:00:00.000Z")
                },
                {
                    "_id": ObjectId("653ef3a8a3e34567bcdf3002"),
                    "name": "Brian",
                    "surname": "Taylor",
                    "loyaltyPoints": 250,
                    "registrationDate": ISODate("2024-01-15T10:00:00.000Z")
                },

            ],
            "createdAt": ISODate("2024-10-16T00:00:00.000Z"),
            "startTime": ISODate("2024-10-16T10:00:00.000Z"),
            "endTime": ISODate("2024-10-16T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1469"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1004"),
                    "name": "David",
                    "surname": "Brown",
                    "loyaltyPoints": 200,
                    "registrationDate": ISODate("2024-01-20T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-17T00:00:00.000Z"),
            "startTime": ISODate("2024-10-17T10:00:00.000Z"),
            "endTime": ISODate("2024-10-17T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1470"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1005"),
                    "name": "Emily",
                    "surname": "Davis",
                    "loyaltyPoints": 400,
                    "registrationDate": ISODate("2024-01-25T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-18T00:00:00.000Z"),
            "startTime": ISODate("2024-10-18T10:00:00.000Z"),
            "endTime": ISODate("2024-10-18T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1471"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1006"),
                    "name": "Frank",
                    "surname": "Miller",
                    "loyaltyPoints": 30,
                    "registrationDate": ISODate("2024-02-01T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-19T00:00:00.000Z"),
            "startTime": ISODate("2024-10-19T10:00:00.000Z"),
            "endTime": ISODate("2024-10-19T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1472"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1007"),
                    "name": "Grace",
                    "surname": "Taylor",
                    "loyaltyPoints": 500,
                    "registrationDate": ISODate("2024-02-05T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-20T00:00:00.000Z"),
            "startTime": ISODate("2024-10-20T10:00:00.000Z"),
            "endTime": ISODate("2024-10-20T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1473"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1008"),
                    "name": "Henry",
                    "surname": "Wilson",
                    "loyaltyPoints": 250,
                    "registrationDate": ISODate("2024-02-10T10:00:00.000Z")
                },
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1009"),
                    "name": "Ivy",
                    "surname": "Moore",
                    "loyaltyPoints": 250,
                    "registrationDate": ISODate("2024-02-10T10:00:00.000Z")
                },

            ],
            "createdAt": ISODate("2024-10-21T00:00:00.000Z"),
            "startTime": ISODate("2024-10-21T10:00:00.000Z"),
            "endTime": ISODate("2024-10-21T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1474"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1009"),
                    "name": "Ivy",
                    "surname": "Moore",
                    "loyaltyPoints": 100,
                    "registrationDate": ISODate("2024-02-15T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-22T00:00:00.000Z"),
            "startTime": ISODate("2024-10-22T10:00:00.000Z"),
            "endTime": ISODate("2024-10-22T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        },
        {
            "_id": ObjectId("693ef3a8a3e34567bcff1475"),
            "_class": "com.example.gym.model.training.Training",
            "availableSlots": 5,
            "clients": [
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1010"),
                    "name": "Jack",
                    "surname": "Anderson",
                    "loyaltyPoints": 200,
                    "registrationDate": ISODate("2024-02-20T10:00:00.000Z")
                },
                {
                    "_id": ObjectId("613ef3a8a3e34567bcdf1011"),
                    "name": "Katie",
                    "surname": "White",
                    "loyaltyPoints": 200,
                    "registrationDate": ISODate("2024-02-20T10:00:00.000Z")
                }
            ],
            "createdAt": ISODate("2024-10-23T00:00:00.000Z"),
            "startTime": ISODate("2024-10-23T10:00:00.000Z"),
            "endTime": ISODate("2024-10-23T12:00:00.000Z"),
            "hasFreeRegistration": true,
            "room": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4303"),
                "name": "Central Power Gym",
                "capacity": 30
            },
            "section": {
                "name": "Кардио тренировка"
            },
            "trainer": {
                "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
                "name": "Дмитрий",
                "surname": "Иванов",
                "qualification": "Кандидат в мастера спорта",
                "hourlyRate": 350
            },
            "updatedAt": ISODate("2024-12-12T23:06:43.713Z")
        }])


