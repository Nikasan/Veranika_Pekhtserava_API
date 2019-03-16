import beans.Label;
import beans.TrelloBoard;
import core.LabelsApi;
import core.TrelloBoardApi;
import core.trelloConstants.RequestMethod;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static com.sun.jmx.snmp.ThreadContext.contains;
import static core.trelloConstants.TrelloOptions.BOARD_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class TrelloTests {

    @Test
    public void checkCreatedBoardExist() {
        TrelloBoard board = TrelloBoardApi.deserialize(TrelloBoardApi.with()
                .name("My awesome board!")
                .callApi(TrelloBoardApi.getCreateBoardEndpoint(), RequestMethod.POST, TrelloBoardApi.getRequestSpecification()));

        Response response = TrelloBoardApi.with()
                .callApi(TrelloBoardApi.getBoardEndpoint(board.id), RequestMethod.GET,
                        TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());
        TrelloBoard getBoard = TrelloBoardApi.deserialize(response);

        assertThat(getBoard.id, equalToIgnoringCase(board.id));
        assertThat(getBoard.name, equalToIgnoringCase(board.name));

    }

    @Test
    public void checkBoardDeleted() {

        TrelloBoard boardForDeleting = TrelloBoardApi.deserialize(TrelloBoardApi.with()
                .name("Veronika's board for deleting")
                .desc("Description for veronika's board delete")
                .callApi(TrelloBoardApi.getCreateBoardEndpoint(), RequestMethod.POST, TrelloBoardApi.getRequestSpecification()));

        Response response = TrelloBoardApi.with()
                .callApi(TrelloBoardApi.deleteBoardEndpoint(boardForDeleting.id), RequestMethod.DELETE,
                        TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());

        TrelloBoard board = TrelloBoardApi.deserialize(response);
        assertThat(board.id, is(nullValue()));
        assertThat(board.id, not(contains(boardForDeleting.id)));
    }

    @Test
    public void checkBoardDescriptionCreates() {
        Response response = TrelloBoardApi.with()
                .name("Veronika's board")
                .desc("Description for veronika's board")
                .callApi(TrelloBoardApi.getCreateBoardEndpoint(), RequestMethod.POST, TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());
        TrelloBoard board = TrelloBoardApi.deserialize(response);
        assertThat(board.name, equalToIgnoringCase("Veronika's board"));
        assertThat(board.desc, equalToIgnoringCase("Description for veronika's board"));

    }

    @Test
    public void checkBoardDescriptionUpdates() {
        Response response = TrelloBoardApi.with()
                .name("Veronika's board updated name")
                .desc("Description for veronika's board updated description")
                .callApi(TrelloBoardApi.putBoardEndpoint(BOARD_ID), RequestMethod.PUT, TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());
        TrelloBoard board = TrelloBoardApi.deserialize(response);

        assertThat(board.name, not(equalToIgnoringCase("Veronika's board")));
        assertThat(board.desc, not(equalToIgnoringCase("Description for veronika's board")));
        assertThat(board.name, equalToIgnoringCase("Veronika's board updated name"));
        assertThat(board.desc, equalToIgnoringCase("Description for veronika's board updated description"));

    }

    @Test
    public void checkDefaultCountOfLabels() {

        Response response = LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());
        List<Label> labels = LabelsApi.deserializeLabels(response);
        assertThat(labels.size(), greaterThanOrEqualTo(13));
        System.out.println(labels + " " + labels.size());
    }

    @Test
    public void checkLabelIsCreated() {

        int labelsListBeforeSize = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification())).size();

        Response response = LabelsApi.with()
                .color("red")
                .name("Name for red label")
                .idBoard(BOARD_ID)
                .callApi(LabelsApi.addLabelEndpoint(), RequestMethod.POST, LabelsApi.getRequestSpecification());

        response.then().specification(LabelsApi.getResponseSpecification());

        Label labels = LabelsApi.getTrelloAnswerPost(response);
        assertThat(labels.name, equalToIgnoringCase("Name for red label"));

        List<Label> labelsListAfter = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));
        response.then().specification(LabelsApi.getResponseSpecification());

        assertThat(labelsListAfter.get(labelsListAfter.size() - 1).name, equalToIgnoringCase("Name for red label"));
        assertThat(labelsListAfter.get(labelsListAfter.size() - 1).color, equalToIgnoringCase("red"));
        assertThat(labelsListAfter.size(), greaterThanOrEqualTo(labelsListBeforeSize + 1));

    }

    @Test
    public void checkLabelsColorsExists() {
        Response response = LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());

        List<Label> labelsList = LabelsApi.deserializeLabels(response);

        for (Label item : labelsList) {
            assertThat(item.color, notNullValue());
        }
    }

    @Test
    public void checkUpdateLabelColor() {
        List<Label> labelsListBeforeUpdate = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));

        Response response = LabelsApi.with()
                .value("red")
                .callApi(LabelsApi.updateLabelColorEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());

        Response responseAfterUpdate = LabelsApi.with()
                .value("yellow")
                .callApi(LabelsApi.updateLabelColorEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());
        Label label = LabelsApi.getTrelloAnswerPost(responseAfterUpdate);
        assertThat(label.id, equalTo(labelsListBeforeUpdate.get(0).id));

        List<Label> labelsListAfterUpdate = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));

        assertThat(labelsListAfterUpdate.get(0).color, equalTo("yellow"));
    }

    @Test
    public void checkUpdateLabelName() {
        List<Label> labelsListBeforeUpdate = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));

        Response response = LabelsApi.with()
                .value("beforeUpdateName")
                .callApi(LabelsApi.updateLabelNameEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());

        Response responseAfterUpdate = LabelsApi.with()
                .value("afterUpdateName")
                .callApi(LabelsApi.updateLabelNameEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());
        Label label = LabelsApi.getTrelloAnswerPost(responseAfterUpdate);

        assertThat(label.id, equalTo(labelsListBeforeUpdate.get(0).id));

        List<Label> labelsListAfterUpdate = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));

        assertThat(labelsListAfterUpdate.get(0).name, equalTo("afterUpdateName"));
    }

    @Test
    public void checkUpdateBothLAbelParameters() {
        List<Label> labelsListBeforeUpdate = LabelsApi.deserializeLabels(LabelsApi.with()
                .callApi(LabelsApi.getLabelEndpoint(BOARD_ID), RequestMethod.GET, LabelsApi.getRequestSpecification()));

        Response response = LabelsApi.with()
                .name("Name before Update")
                .color("purple")
                .callApi(LabelsApi.updateLabelByIdEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());

        Response responseAfterUpdate = LabelsApi.with()
                .name("Name After Update")
                .color("green")
                .callApi(LabelsApi.updateLabelByIdEndpoint(labelsListBeforeUpdate.get(0).id), RequestMethod.PUT, LabelsApi.getRequestSpecification());
        response.then().specification(LabelsApi.getResponseSpecification());
        Label label = LabelsApi.getTrelloAnswerPost(responseAfterUpdate);

        assertThat(label.id, equalTo(labelsListBeforeUpdate.get(0).id));
        assertThat(label.name, equalTo("Name After Update"));
        assertThat(label.color, equalTo("green"));
    }

}
