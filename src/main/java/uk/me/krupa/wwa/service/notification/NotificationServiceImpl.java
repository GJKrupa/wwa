package uk.me.krupa.wwa.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.krupa.wwa.dto.detail.GameDetail;
import uk.me.krupa.wwa.dto.nfy.Notification;
import uk.me.krupa.wwa.dto.nfy.NotificationType;
import uk.me.krupa.wwa.entity.game.Game;
import uk.me.krupa.wwa.entity.game.Round;
import uk.me.krupa.wwa.entity.user.User;
import uk.me.krupa.wwa.repository.game.GameRepository;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Override
    @Transactional(readOnly = true)
    public void notifyGameCreated(User user, Long id, String name) {
        Notification notification = new Notification(id, user.getId(), NotificationType.CREATED, user.getFullName(), name);
        simpMessagingTemplate.convertAndSend("/topic/user/all", notification);
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyGameJoined(User user, long id) {
        Game game = gameRepository.findOne(id);
        Notification notification = new Notification(id, user.getId(), NotificationType.JOINED, user.getFullName(), game.getName());
        simpMessagingTemplate.convertAndSend("/topic/user/all", notification);
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyGameStarted(User user, long id) {
        Game game = gameRepository.findOne(id);
        Notification notification = new Notification(id, user.getId(), NotificationType.STARTED, game.getName());
        simpMessagingTemplate.convertAndSend("/topic/user/all", notification);
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyJudgingReady(User user, long id) {
        Game game = gameRepository.findOne(id);
        Notification notification = new Notification(id, user.getId(), NotificationType.ALL_PLAYED, game.getName());
        simpMessagingTemplate.convertAndSend("/topic/user/" + game.getCurrentRound().getCzar().getUser().getId(), notification);
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyJudged(User user, long id) {
        Game game = gameRepository.findOne(id);
        Round round = neo4jTemplate.fetch(game.getCurrentRound().getPrevious());
        String winner = round.getWinningPlay().getPlayer().getUser().getFullName();
        Notification notification = new Notification(id, user.getId(), NotificationType.WINNER_CHOSEN, winner, game.getName());
        game.getPlayers().stream()
                .filter(p -> !p.getUser().equals(user))
                .forEach(p -> simpMessagingTemplate.convertAndSend("/topic/user/" + p.getUser().getId(), notification));
    }
}
