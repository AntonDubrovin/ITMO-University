package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.FriendshipRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class FriendDataPage {
    // INSERT INTO Friends VALUES (userId1, userId2)
    // DELETE FROM Friends WHERE userId1=? AND userId2=?
    // SELECT COUNT(*) FROM Friends WHERE userId1=? AND userId2=?

    private FriendshipRepository friendshipRepository;
    private UserRepository userRepository;


    private void add(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user1 = (User)request.getSession().getAttribute("user");
        if (user1 != null) {
            long userId = Long.parseLong(request.getParameter("userId"));
            User user2 = userRepository.find(userId);
            if (user2 != null) {
                friendshipRepository.save(user1, user2);
            }
        }

        throw new RedirectException("/users");
    }
    
    public void remove() {
        
    }
}
