import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.request.CreateUser;
import model.rerponse.CreateUserResp;
import model.rerponse.GetUserListResp;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApiTests {
    public final String BASE_URI = "https://reqres.in";
    // Пример выполнения гет запроса, без проверок
    @Test
    public void getListUser_1(){
        given()//выполнить запрос
                .when()
//                .log().all()// чтобы показали нам структуру запроса = get.... th. = answer body
                .get("https://reqres.in/api/users?page=2")
                .then();
//                .log().all();// выполняется после того как выполняется запрос = answer body
    }

    //Пример выполнения гет запроса, с проверкой полей с помошью TestNg
    @Test
    public void getListUser_2(){
        Response response = (Response) given()//выполнить запрос
                .when()
                .log().all()// чтобы показали нам структуру запроса = get.... th. = answer body
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()// выполняется после того как выполняется запрос = answer body
                .extract().response();// методы которые нам говорят, забери весь ответ и сохранят в переменую response
        Assert.assertEquals(response.getStatusCode(), 200, "The actual statusCode in not 200");// приходяший резудьтат и ожидаемый// текст не обязательно указывать
        Assert.assertEquals(response.body().jsonPath().getInt("data[0].id"), 7);// путь от дата и сравниваем с результатом id = 7
        Assert.assertEquals(response.body().jsonPath().getString("data[1].email"), "lindsay.ferguson@reqres.in");// email = "lindsay.ferguson@reqres.in"
    }


    // С помошью Rest Assured// Пример выполнения гет запроса, c проверка с помошью Rest Assured (название библиотеке, добавили ее в pom.xml)
    @Test
    public void getListUser_3(){
        given()
                .when()
                .baseUri(BASE_URI)
                .log().all()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .assertThat() // говорим что мы будем делать проверки
                .statusCode(200)
                .body("data[0].id", Matchers.equalTo(7))
                .body("data[1].email", Matchers.equalTo("lindsay.ferguson@reqres.in"));

    }
    @Test
    public void createUser_1(){
        String user = "{\n " +                    //будет тут храниться тело запроса
                "   \"name\": \"morpheus\",\n" +
                "   \"job\": \"leader\"\n" +
                "}";
        RestAssured.given()
                //.header("Content-type", "application/jason") // стандартный заголовок, jason формате - первы метод
                .contentType(ContentType.JSON) // тоже самое, что предыдушая строчка
                .body(user)// тело нашего запроса
                .when()
                .baseUri(BASE_URI) // базовый ури, который мы вынесли отдельно
                .log().all() // хотим увидеть логи
                .post("/api/users")//указываем метод, который хотим испольовать (нужно указать endPoint)
                .then() // что мы хотим увидеть послк всего выполненого
                .log().all()
                .assertThat() //всегда если хотим вызвать проверку
                .statusCode(201);
    }

    // conversation on JSON
    @Test
    public void createUser_2(){
        String name = "morpheus";
        String job = "leader";
        CreateUser user = new CreateUser(name, job); // нужна сперва получить екземпляр созданого класса (CreateUser) и вставить name and job

        RestAssured.given()
                .contentType(ContentType.JSON) // тоже самое, что предыдушая строчка
                .body(user)// тело нашего запроса
                .when()
                .baseUri(BASE_URI) // базовый ури, который мы вынесли отдельно
                .log().all() // хотим увидеть логи
                .post("/api/users")//указываем метод, который хотим испольовать (нужно указать endPoint)
                .then() // что мы хотим увидеть послк всего выполненого
                .log().all()
                .assertThat() //всегда если хотим вызвать проверку
                .statusCode(201)
                .body("name", Matchers.equalTo("morpheus"))// в теле проверям поля которые пришли
                .body("job",Matchers.equalTo("leader"));
    }

    //как можно записывать ответы в клас и испольщовать их для проверки

    /*
    Первый основной способ
     */
    @Test
    public void createUser_3(){
        CreateUser user = new CreateUser("morpheus", "leader"); // нужна сперва получить екземпляр созданого класса (CreateUser) и вставить name and job

        CreateUserResp createUserResp = RestAssured.given()// ответ записываем в экземпляр класса createUserResp
                .contentType(ContentType.JSON) // тоже самое, что предыдушая строчка
                .body(user)// тело нашего запроса
                .when()
                .baseUri(BASE_URI) // базовый ури, который мы вынесли отдельно
                .log().all() // хотим увидеть логи
                .post("/api/users")//указываем метод, который хотим испольовать (нужно указать endPoint)
                .then() // что мы хотим увидеть послк всего выполненого
                .log().all()
                .extract().as(CreateUserResp.class); // (Указываем-забери ответ, как экземпляр этого класса CreateUserResp)для того чтобы передать для записи  в class CreateUserResp
        Assert.assertEquals(createUserResp.name, "morpheus");
        Assert.assertEquals(createUserResp.job, "leader");

        Assert.assertTrue(createUserResp.createdAt.contains(LocalDate.now().toString()));//эта строчка говорит, что
        // проверь то, что наш обьект который мы записали из ответа, его плде createdAT, CONTAINTS(содержит),
        // сегоднишнюю дату LocalDate.now().toString() // Этот метод возврашает True oder False

    }

    /*
    Второй основной способ
     */
        @Test
        public void getUserListRespClass(){
            GetUserListResp getUserListResp = RestAssured.given()
                    .when()
                    .baseUri(BASE_URI)
                    .log().all() // Body
                    .get("/api/users?page=2")
                    .then()
                    .log().all()
                    .extract().as(GetUserListResp.class);

            Assert.assertEquals(getUserListResp.data.get(3).first_name, "Byron"); // достаем имя с определенного елемента(index 3)
            // - пишем getUserListResp.выбираем с класса datum а потом get - получить
            // - и нам перечисляеться все с класса CreateUserListResp
            System.out.println(getUserListResp.data.get(0).first_name);// примеры, правильно ли нам показывает(Это проверка)
            System.out.println(getUserListResp.data.get(4).id);
            System.out.println(getUserListResp.data.get(1).last_name);
        }

        // Повтор предыдушего теста, но с созданием, как мы ранее создавали обьект// но можно делать все быстрее, сокрашая строчки
        @Test
        public void getUserListRespClass_1(){
            GetUserListResp getUserListResp = new GetUserListResp();
            getUserListResp = RestAssured.given()
                    .when()
                    .baseUri(BASE_URI)
                    .log().all() // Body
                    .get("/api/users?page=2")
                    .then()
                    .log().all()
                    .extract().as(GetUserListResp.class);

            Assert.assertEquals(getUserListResp.data.get(3).first_name, "Byron"); // достаем имя с определенного елемента(index 3)
            // - пишем getUserListResp.выбираем с класса datum а потом get - получить
            // - и нам перечисляеться все с класса CreateUserListResp


            //Примеры
            System.out.println(getUserListResp.data.get(0).first_name);// примеры, правильно ли нам показывает(Это проверка)
            System.out.println(getUserListResp.data.get(4).id);
            System.out.println(getUserListResp.data.get(1).last_name);
            System.out.println(getUserListResp.page);
            System.out.println(getUserListResp.per_page);
            System.out.println(getUserListResp.total);
            System.out.println(getUserListResp.support.url);
        }
}
