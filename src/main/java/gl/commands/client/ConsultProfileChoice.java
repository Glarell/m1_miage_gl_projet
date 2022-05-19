package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.ClientDAO;
import gl.database.model.Client;

import java.util.Scanner;

public class ConsultProfileChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Consulter mon profil------]\n");
        Client client = ClientDAO.getClient(Application.currentClient.getId_client());
        System.out.println(client);
        return Application.RETURN_SUCCESS;
    }
}
