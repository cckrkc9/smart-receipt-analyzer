package com.cancikrikci.receiptprocessor.repository;

import com.cancikrikci.receiptprocessor.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;


    private DynamoDbTable<User> userTable() {
        return dynamoDbEnhancedClient.table("User", TableSchema.fromBean(User.class));
    }

    public Optional<User> findByUsername(String username) {
        return userTable().scan().items()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void save(User user) {
        userTable().putItem(user);
    }
}
