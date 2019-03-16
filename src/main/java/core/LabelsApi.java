package core;

import beans.Label;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import core.trelloConstants.RequestMethod;
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
import java.util.List;

import static core.trelloConstants.TrelloOptions.*;
import static core.trelloConstants.TrelloOptions.Endpoints.*;
import static io.restassured.RestAssured.given;

public class LabelsApi {
    private HashMap<String, String> queryParams = new HashMap<String, String>() {{
        put("key", KEY);
        put("token", TOKEN);
    }};

    private LabelsApi() {
    }

    public static String getLabelEndpoint(String id) {
        String labelUrl = String.format(GET_LABELS, id);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, labelUrl);
    }

    public static String addLabelEndpoint() {
        String labelUrl = String.format(POST_LABEL);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, labelUrl);
    }

    public static String updateLabelColorEndpoint(String id) {
        String labelUrl = String.format(UPDATE_LABEL, id);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, labelUrl);
    }

    public static String updateLabelNameEndpoint(String id) {
        String labelUrl = String.format(UPDATE_LABEL_NAME, id);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, labelUrl);
    }

    public static String updateLabelByIdEndpoint(String id) {
        String labelUrl = String.format(UPDATE_LABEL_BY_ID, id);
        return String.format("%1$s/%2$s", TRELLO_API_BASE_URL, labelUrl);
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

    public static List<Label> deserializeLabels(Response responseLabels) {
        // Get JSON string
        String jsonString = responseLabels.asString();
        // Create Serializer
        Gson gson = new Gson();
        //Deserialize json to object

        List<Label> labels = gson.fromJson(jsonString.trim(), new TypeToken<List<Label>>() {
        }.getType());
        // return object

        return labels;
    }

    public static Label getTrelloAnswerPost(Response response) {
        String jsonString = response.asString();
        // Create Serializer
        Gson gson = new Gson();
        //Deserialize json to object
        Label label = gson.fromJson(jsonString, Label.class);
        // return object
        return label;
    }


    public static LabelsApi.ApiBuilder with() {

        return new LabelsApi.ApiBuilder(new LabelsApi());
    }

    public static class ApiBuilder {
        LabelsApi api;

        private ApiBuilder(LabelsApi api) {
            this.api = api;
        }


        public ApiBuilder name(String value) {
            api.queryParams.put("name", value);
            return this;
        }

        public ApiBuilder color(String value) {
            api.queryParams.put("color", value);
            return this;
        }

        public ApiBuilder idBoard(String value) {
            api.queryParams.put("idBoard", value);
            return this;
        }

        public ApiBuilder id(String value) {
            api.queryParams.put("id", value);
            return this;
        }

        public ApiBuilder value(String value) {
            api.queryParams.put("value", value);
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
                case DELETE: {
                    response = spec.delete(endpoint);
                    break;
                }
                case PUT: {
                    response = spec.put(endpoint);
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
