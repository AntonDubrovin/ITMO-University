package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.User;

public interface FriendshipRepository {
    void save(User user1, User user2);
    void delete(User user1, User user2);
    boolean find(User user1, User user2);
}
