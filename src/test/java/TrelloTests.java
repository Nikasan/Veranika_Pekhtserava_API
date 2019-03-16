import beans.TrelloBoard;
import core.TrelloBoardApi;
import core.trelloConstants.RequestMethod;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class TrelloTests {

    @Test
    public void firstTest() {
        Response response = TrelloBoardApi.with()
                .name("My awesome boeard again!")
                .callApi(TrelloBoardApi.getCreateBoardEndpoint(), RequestMethod.POST, TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());

        TrelloBoard board = TrelloBoardApi.deserialize(response);
        assertThat(board.name, equalToIgnoringCase("My awesome boeard again!"));
        System.out.println(board);
    }

    @Test
    public void checkCreatedBoard() {
        Response response = TrelloBoardApi.with()
                .callApi(TrelloBoardApi.getBoardEndpoint("5c8cf33aab53810ff296414c"), RequestMethod.GET,
                        TrelloBoardApi.getRequestSpecification());

        response.then().specification(TrelloBoardApi.getResponseSpecification());
        TrelloBoard board = TrelloBoardApi.deserialize(response);
        assertThat(board.id, equalToIgnoringCase("5c8cf33aab53810ff296414c"));

    }

}
