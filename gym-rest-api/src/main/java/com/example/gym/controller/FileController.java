package com.example.gym.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Value("${spring.data.mongodb.uri}")
    private String uri;
    
    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> exportDatabase() throws IOException, JsonProcessingException {
        // Создаем экземпляр MongoClient
        MongoClient mongoClient = MongoClients.create(uri);
    
        // Получаем экземпляр MongoDatabase
        MongoDatabase database = mongoClient.getDatabase(uri.split("/")[uri.split("/").length - 1].split("\\?")[0]);
    
        // Читаем данные из базы данных
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    
        Map<String, List<Map<String, Object>>> exportData = new HashMap<>();
        MongoIterable<String> collectionNames = database.listCollectionNames();
        for (String collectionName : collectionNames) {
            List<Map<String, Object>> documents = new ArrayList<>();
            MongoCollection<Document> collection = database.getCollection(collectionName);
            for (Document document : collection.find()) {
                Map<String, Object> doc = new HashMap<>();
                for (Map.Entry<String, Object> entry : document.entrySet()) {
                    Object value = entry.getValue();
                    if (entry.getKey().equals("_id")) {
                        value = value.toString();
                    } else if (value instanceof Document) {
                        Map<String, Object> nestedDoc = new HashMap<>();
                        for (String nestedKey : ((Document) value).keySet()) {
                            Object nestedValue = ((Document) value).get(nestedKey);
                            if (nestedKey.equals("_id")) {
                                nestedValue = nestedValue.toString();
                            }
                            nestedDoc.put(nestedKey, nestedValue);
                        }
                        value = nestedDoc;
                    } else if (value instanceof List) {
                        List<Object> list = (List<Object>) value;
                        for (int i = 0; i < list.size(); i++) {
                            Object item = list.get(i);
                            if (item instanceof Document) {
                                Map<String, Object> nestedDoc = new HashMap<>();
                                for (String nestedKey : ((Document) item).keySet()) {
                                    Object nestedValue = ((Document) item).get(nestedKey);
                                    if (nestedKey.equals("_id")) {
                                        nestedValue = nestedValue.toString();
                                    }
                                    nestedDoc.put(nestedKey, nestedValue);
                                }
                                list.set(i, nestedDoc);
                            }
                        }
                    }
                    doc.put(entry.getKey(), value);
                }
                documents.add(doc);
            }
            exportData.put(collectionName, documents);
        }
    
        String json = mapper.writeValueAsString(exportData);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.json")
                .body(new ByteArrayResource(bytes));
    }
    
    @PostMapping("/import")
    public ResponseEntity<String> importDatabase(@RequestParam("file") MultipartFile file) throws IOException, StreamReadException, DatabindException, java.io.IOException {
        // Создаем экземпляр MongoClient
        MongoClient mongoClient = MongoClients.create(uri);

        // Получаем экземпляр MongoDatabase
        MongoDatabase database = mongoClient.getDatabase(uri.split("/")[uri.split("/").length - 1].split("\\?")[0]);

        MongoIterable<String> collectionNamesToDelete = database.listCollectionNames();
        for (String collectionName : collectionNamesToDelete) {
            database.getCollection(collectionName).drop();
        }

        // Читаем JSON-файл
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Map<String, Object>>> importData = mapper.readValue(file.getInputStream(), Map.class);

        // Заполняем базу данных
        for (Map.Entry<String, List<Map<String, Object>>> entry : importData.entrySet()) {
            String collectionName = entry.getKey();
            List<Map<String, Object>> documents = entry.getValue();

            // Проверяем, существует ли коллекция
            MongoIterable<String> collectionNames = database.listCollectionNames();
            boolean collectionExists = false;
            for (String name : collectionNames) {
                if (name.equals(collectionName)) {
                    collectionExists = true;
                    break;
                }
            }

            // Создаем коллекцию, если она не существует
            if (!collectionExists) {
                database.createCollection(collectionName);
            }

            // Заполняем коллекцию
            MongoCollection<Document> collection = database.getCollection(collectionName);
            for (Map<String, Object> document : documents) {
                Document doc = new Document();
                for (Map.Entry<String, Object> entryDoc : document.entrySet()) {
                    if (entryDoc.getKey().equals("_id")) {
                        // Если ключ "_id" существует, устанавливаем его как ObjectId
                        doc.put(entryDoc.getKey(), new ObjectId((String) entryDoc.getValue()));
                    } else {
                        doc.put(entryDoc.getKey(), entryDoc.getValue());
                    }
                }
                if (!doc.containsKey("_id")) {
                    // Если ключ "_id" не существует, устанавливаем его как новый ObjectId
                    doc.put("_id", new ObjectId());
                }
                collection.insertOne(doc);
            }
        }

        // Возвращаем ответ
        return ResponseEntity.status(HttpStatus.OK).body("База данных успешно заполнена");
    }
 
}
