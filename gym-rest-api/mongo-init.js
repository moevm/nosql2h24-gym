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
                    "startDate": ISODate("2023-10-01T00:00:00Z"),
                    "endDate": ISODate("2024-10-01T00:00:00Z"),
                    "status": "ACTIVE",
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
                    "startDate": ISODate("2023-11-01T00:00:00Z"),
                    "endDate": ISODate("2024-11-01T00:00:00Z"),
                    "status": "ACTIVE",
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
                    "startDate": ISODate("2023-07-01T00:00:00Z"),
                    "endDate": ISODate("2024-07-01T00:00:00Z"),
                    "status": "ACTIVE",
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
                    "startDate": ISODate("2023-06-01T00:00:00Z"),
                    "endDate": ISODate("2024-06-01T00:00:00Z"),
                    "status": "ACTIVE",
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
                    "startDate": ISODate("2023-05-01T00:00:00Z"),
                    "endDate": ISODate("2024-05-01T00:00:00Z"),
                    "status": "ACTIVE",
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
        "_id": ObjectId("653ef3a8a3e34567bcdf3001"),
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
        "_id": ObjectId("653ef3a8a3e34567bcdf3005"),
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
        "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
        "name": "Downtown Fitness Center",
        "capacity": 25,
        "location": {
            "address": "Moscow, Tverskaya Street, 12",
            "number": 3
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4002"),
        "name": "Northern Athletic Hall",
        "capacity": 15,
        "location": {
            "address": "Saint-Petersburg, Liteyny Avenue, 45",
            "number": 2
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
        "name": "Central Power Gym",
        "capacity": 30,
        "location": {
            "address": "Kazan, Pushkin Street, 18",
            "number": 1
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4004"),
        "name": "Southern Training Hall",
        "capacity": 20,
        "location": {
            "address": "Sochi, Kurortny Avenue, 99",
            "number": 4
        }
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf4005"),
        "name": "Eastern Fitness Arena",
        "capacity": 18,
        "location": {
            "address": "Vladivostok, Svetlanskaya Street, 8",
            "number": 5
        }
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
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2001"),
            "name": "Michael",
            "surname": "Jordan",
            "qualification": "Professional Basketball Coach",
            "hourlyRate": NumberDecimal("200.00")
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4001"),
            "name": "Basketball Skills"
        },
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5001"),
            "name": "Basketball Court A",
            "capacity": 15
        },
        "startTime": ISODate("2024-11-02T10:00:00Z"),
        "endTime": ISODate("2024-11-02T11:30:00Z"),
        "availableSlots": 5,
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
        ],
        "createdAt": ISODate("2024-10-15T00:00:00Z"),
        "updatedAt": ISODate("2024-10-20T00:00:00Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3002"),
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2002"),
            "name": "Serena",
            "surname": "Williams",
            "qualification": "Grand Slam Winner",
            "hourlyRate": NumberDecimal("250.00")
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4002"),
            "name": "Tennis Fundamentals"
        },
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5002"),
            "name": "Tennis Court B",
            "capacity": 10
        },
        "startTime": ISODate("2024-11-03T14:00:00Z"),
        "endTime": ISODate("2024-11-03T15:30:00Z"),
        "availableSlots": 4,
        "clients": [
            {
                "_id": ObjectId("653ef3a8a3e34567bcdf1003"),
                "name": "Charlie",
                "surname": "Brown",
                "loyaltyPoints": 300
            }
        ],
        "createdAt": ISODate("2024-10-16T00:00:00Z"),
        "updatedAt": ISODate("2024-10-21T00:00:00Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3003"),
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2003"),
            "name": "Usain",
            "surname": "Bolt",
            "qualification": "Olympic Sprinting Champion",
            "hourlyRate": NumberDecimal("300.00")
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4003"),
            "name": "Sprint Training"
        },
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5003"),
            "name": "Outdoor Track",
            "capacity": 20
        },
        "startTime": ISODate("2024-11-04T08:00:00Z"),
        "endTime": ISODate("2024-11-04T09:30:00Z"),
        "availableSlots": 10,
        "clients": [],
        "createdAt": ISODate("2024-10-17T00:00:00Z"),
        "updatedAt": ISODate("2024-10-22T00:00:00Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3004"),
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2004"),
            "name": "Simone",
            "surname": "Biles",
            "qualification": "World Champion Gymnast",
            "hourlyRate": NumberDecimal("220.00")
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4004"),
            "name": "Gymnastics Basics"
        },
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5004"),
            "name": "Gymnastics Hall",
            "capacity": 12
        },
        "startTime": ISODate("2024-11-05T16:00:00Z"),
        "endTime": ISODate("2024-11-05T17:30:00Z"),
        "availableSlots": 3,
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
            }
        ],
        "createdAt": ISODate("2024-10-18T00:00:00Z"),
        "updatedAt": ISODate("2024-10-23T00:00:00Z")
    },
    {
        "_id": ObjectId("653ef3a8a3e34567bcdf3005"),
        "trainer": {
            "_id": ObjectId("653ef3a8a3e34567bcdf2005"),
            "name": "Tom",
            "surname": "Brady",
            "qualification": "NFL MVP",
            "hourlyRate": NumberDecimal("250.00")
        },
        "section": {
            "_id": ObjectId("653ef3a8a3e34567bcdf4005"),
            "name": "Football Techniques"
        },
        "room": {
            "_id": ObjectId("653ef3a8a3e34567bcdf5005"),
            "name": "Football Field",
            "capacity": 25
        },
        "startTime": ISODate("2024-11-06T18:00:00Z"),
        "endTime": ISODate("2024-11-06T19:30:00Z"),
        "availableSlots": 8,
        "clients": [],
        "createdAt": ISODate("2024-10-19T00:00:00Z"),
        "updatedAt": ISODate("2024-10-24T00:00:00Z")
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
            "hourlyRate": NumberDecimal("150.00"),
            "sections": [
                "Функциональная тренировка",
                "Кардио тренировка"
            ]
        }
    }
]);


