package core;

import beans.TrelloBoard;
import com.google.gson.Gson;
import core.trelloConstants.RequestMethod;
import core.trelloConstants.TrelloOptions;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.HashMap;

import static core.trelloConstants.TrelloOptions.*;
import static core.trelloConstants.TrelloOptions.Endpoints.GET_BOARD;
import static io.restassured.RestAssured.given;

public class TrelloBoardApi {
    private HashMap<String, String> queryParams = new HashMap<String, String>() {{
        put("key", KEY);
        put("token", TOKEN);
    }};

    private TrelloBoardApi() {
    }

    // Endpoints
    public static String getCreateBoardEndpoint() {
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, TrelloOptions.Endpoints.CREATE_BOARD);
    }

    public static String getBoardEndpoint(String id) {
        String boardUrl = String.format(GET_BOARD, id);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, boardUrl);
    }

    /* Specs */
    public static ResponseSpecification getResponseSpecification() {
        ResponseSpecification result = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .build();

        return result;
    }

    public static RequestSpecification getRequestSpecification() {
        RequestSpecification result = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(TRELLO_API_BASE_URL)
                .build();

        return result;
    }
    /* ------------------------------------------------------------------ */

    public static TrelloBoard deserialize(Response response) {
        // Get JSON string
        String jsonString = response.asString();
        // Create Serializer
        Gson gson = new Gson();
        //Deserialize json to object
        TrelloBoard board = gson.fromJson(jsonString, TrelloBoard.class);
        // return object
        return board;
    }

    public static ApiBuilder with() {
//            TrelloBoardApi api = new TrelloBoardApi();
//            return new ApiBuilder(api);
        return new ApiBuilder(new TrelloBoardApi());
    }

    public static class ApiBuilder {
        TrelloBoardApi api;

        private ApiBuilder(TrelloBoardApi api) {
            this.api = api;
        }

        public ApiBuilder name(String value) {
            api.queryParams.put("name", value);
            return this;
        }


        public Response callApi(String endpoint, RequestMethod method, RequestSpecification specification) {
            RestAssured.defaultParser = Parser.JSON;

            RequestSpecification _spec = specification == null ? given() : given().spec(specification);

            RequestSpecification spec = _spec.headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                    with().queryParams(api.queryParams).when();

            Response response = null;
            switch (method) {
                case GET: {
                    response = spec.get(endpoint);
                    break;
                }
                case POST: {
                    response = spec.post(endpoint);
                    break;
                }
                default:
                    response = spec.get(endpoint);
                    break;
            }

            return response.then().contentType(ContentType.JSON).extract().response();
        }
    }

}

