package gl.database.dao;

import gl.database.model.VariableApplication;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableApplicationDAOTest {

    @Test
    public void testGetAllVariableApplication() {
        List<VariableApplication> listOfVariableApplication = VariableApplicationDAO.getAllVariableApplication();
        assertThat(listOfVariableApplication)
                .extracting(VariableApplication::getNom)
                .contains(VariableApplication.WAITING_TIME)
                .hasSize(1);
    }

    @Test
    public void testGetVariableApplicationByName() {
        VariableApplication variableApplication = VariableApplicationDAO.getVariableApplicationByName(VariableApplication.WAITING_TIME);

        assertThat(variableApplication.getNom())
                .isEqualTo(VariableApplication.WAITING_TIME);
    }

    @Test
    public void testGetVariableApplicationFromUnknowName() {
        VariableApplication variableApplication = VariableApplicationDAO.getVariableApplicationByName("test");

        assertThat(variableApplication.getNom())
                .isNull();
    }

    @Test
    public void testUpdateVariableApplicationValue() throws SQLException {
        VariableApplication variableApplication = VariableApplicationDAO.getVariableApplicationByName(VariableApplication.WAITING_TIME);
        int valueVariableApplicationSave = variableApplication.getValeur();

        //Test sur le changement de la variable d'application du délai d'attente
        VariableApplicationDAO.updateVariableApplicationValue(variableApplication, 999);
        assertThat(VariableApplicationDAO.getVariableApplicationByName(VariableApplication.WAITING_TIME).toString())
                .isEqualTo(variableApplication.toString());

        //Restoration de la variable d'application du délai d'attente
        VariableApplicationDAO.updateVariableApplicationValue(variableApplication, valueVariableApplicationSave);
        assertThat(VariableApplicationDAO.getVariableApplicationByName(VariableApplication.WAITING_TIME).getValeur())
                .isEqualTo(valueVariableApplicationSave);
    }
}
