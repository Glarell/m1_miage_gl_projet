package gl.database.dao;

import gl.database.model.Client;
import gl.database.model.Notification;
import gl.database.model.TypeNotification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.notIn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class NotificationDAOTest {

    @BeforeAll
    static void init() throws SQLException {
        Client client = new Client("test", "test", "test", "test", "test", "test", "test");
        client.setId_client(999);
        ClientDAO.registrerClientWithId(client);
    }

    @Test
    public void testInsertNewNotification() throws SQLException {
        Notification notification = new Notification(999, "contenu_test", 999, TypeNotification.TYPE_MAIL);
        NotificationDAO.insertNewNotificationWithId(notification);

        assertThat(NotificationDAO.getNotificationByClient(999))
                .filteredOn(Notification::getContenu, "contenu_test")
                .filteredOn(Notification::getId_client, 999)
                .filteredOn(Notification::getId_typeNotification, TypeNotification.TYPE_MAIL)
                .hasSize(1);

        NotificationDAO.deleteOldNotification(999);
    }

    @Test
    public void testInsertNewNotificationWithUnknowClient() {
        assertThatThrownBy(() -> {
            Notification notification = new Notification(999, "contenu_test", 998, TypeNotification.TYPE_MAIL);
            NotificationDAO.insertNewNotificationWithId(notification);
        }).isInstanceOf(PSQLException.class);
    }

    @Test
    public void testDeleteNotification() throws SQLException {
        Notification notification = new Notification(999, "contenu_test", 999, TypeNotification.TYPE_MAIL);
        NotificationDAO.insertNewNotificationWithId(notification);
        NotificationDAO.deleteOldNotification(999);

        List<Notification> listOfNotification = NotificationDAO.getAllNotification();
        assertThat(listOfNotification)
                .filteredOn(Notification::getId_notification, notIn(999))
                .filteredOn(Notification::getContenu, notIn("contenu_test"))
                .filteredOn(Notification::getId_client, notIn(999))
                .hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    public void testGetAllEstAssocie() throws SQLException {
        Notification notification = new Notification(999, "contenu_test", 999, TypeNotification.TYPE_MAIL);
        Notification notification2 = new Notification(998, "contenu_test2", 999, TypeNotification.TYPE_MAIL);
        Notification notification3 = new Notification(997, "contenu_test3", 999, TypeNotification.TYPE_MAIL);
        NotificationDAO.insertNewNotificationWithId(notification);
        NotificationDAO.insertNewNotificationWithId(notification2);
        NotificationDAO.insertNewNotificationWithId(notification3);

        List<Notification> listOfNotification = NotificationDAO.getAllNotification();
        assertThat(listOfNotification)
                .extracting(Notification::getId_notification)
                .contains(999)
                .contains(998)
                .contains(997)
                .hasSizeGreaterThanOrEqualTo(3);

        NotificationDAO.deleteOldNotification(999);
        NotificationDAO.deleteOldNotification(998);
        NotificationDAO.deleteOldNotification(997);
    }

    @Test
    public void testGetEstAssocieByClient() throws SQLException {
        Notification notification = new Notification(999, "contenu_test", 999, TypeNotification.TYPE_MAIL);
        Notification notification2 = new Notification(998, "contenu_test2", 999, TypeNotification.TYPE_MAIL);
        Notification notification3 = new Notification(997, "contenu_test3", 999, TypeNotification.TYPE_MAIL);
        NotificationDAO.insertNewNotificationWithId(notification);
        NotificationDAO.insertNewNotificationWithId(notification2);
        NotificationDAO.insertNewNotificationWithId(notification3);

        List<Notification> listOfNotification = NotificationDAO.getNotificationByClient(999);
        assertThat(listOfNotification)
                .extracting(Notification::getId_notification)
                .contains(999)
                .contains(998)
                .contains(997)
                .hasSizeGreaterThanOrEqualTo(3);

        NotificationDAO.deleteOldNotification(999);
        NotificationDAO.deleteOldNotification(998);
        NotificationDAO.deleteOldNotification(997);
    }

    @AfterAll
    static void end() throws SQLException {
        ClientDAO.deleteClient(999);
    }
}
