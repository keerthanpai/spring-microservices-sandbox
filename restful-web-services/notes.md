
# RESTful Web Services

## Social Media Application Resource Mappings

### User -> Posts

- Retrieve all Users      - GET  /users
- Create a User           - POST /users
- Retrieve one User       - GET  /users/{id} -> /users/1   
- Delete a User           - DELETE /users/{id} -> /users/1

- Retrieve all posts for a User - GET /users/{id}/posts 
- Create a posts for a User - POST /users/{id}/posts
- Retrieve details of a post - GET /users/{id}/posts/{post_id}

## Error in the Log
```
Resolved exception caused by Handler execution: 
org.springframework.http.converter.HttpMessageNotWritableException: 
No converter found for return value of type: 
class com.in28minutes.rest.webservices.restfulwebservices.HelloWorldBean
```
- This happened because there were no getters in HelloWorldBean class

## Questions to Answer

- What is dispatcher servlet?
- Who is configuring dispatcher servlet?
- What does dispatcher servlet do? 
- How does the HelloWorldBean object get converted to JSON?
- Who is configuring the error mapping?

- Mapping servlet: 'dispatcherServlet' to [/]

- Mapped "{[/hello-world],methods=[GET]}" onto 
public java.lang.String com.in28minutes.rest.webservices.restfulwebservices.HelloWorldController.helloWorld()
- Mapped "{[/hello-world-bean],methods=[GET]}" onto 
public com.in28minutes.rest.webservices.restfulwebservices.HelloWorldBean com.in28minutes.rest.webservices.restfulwebservices.HelloWorldController.helloWorldBean()
- Mapped "{[/error]}" onto 
public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
- Mapped "{[/error],produces=[text/html]}" onto 
public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)


### Example Requests

#### GET http://localhost:8080/users
```json
[
    {
        "id": 1,
        "name": "Adam",
        "birthDate": "2019-11-21T04:40:20.796+0000"
    },
    {
        "id": 2,
        "name": "Eve",
        "birthDate": "2019-11-21T04:40:20.796+0000"
    },
    {
        "id": 3,
        "name": "Jack",
        "birthDate": "2019-11-21T04:40:20.796+0000"
    }
]
```
#### GET http://localhost:8080/users/1
```json
{
    "id": 1,
    "name": "Adam",
    "birthDate": "2019-11-21T04:40:20.796+0000"
}
```
#### POST http://localhost:8080/users
```json
{
    "name": "Ranga",
    "birthDate": "2000-07-19T04:29:24.054+0000"
}
```

#### GET http://localhost:8080/users/1000
- Get request to a non existing resource. 
- The response shows default error message structure auto configured by Spring Boot.

```json
{
    "timestamp": "2019-11-21T05:28:37.534+0000",
    "status": 404,
    "error": "Not Found",
    "message": "id-500",
    "path": "/users/500"
}
```

#### GET http://localhost:8080/users/1000
- Get request to a non existing resource. 
- The response shows a Customized Message Structure
```json
{
    "timestamp": "2019-11-21T05:31:01.961+0000",
    "message": "id-500",
    "details": "Any details you would want to add"
}
```

#### POST http://localhost:8080/users with Validation Errors

##### Request
```json
{
    "name": "R",
    "birthDate": "2000-07-19T04:29:24.054+0000"
}
```
##### Response - 400 Bad Request
```json
{
    "timestamp": "2019-11-21T09:00:27.912+0000",
    "message": "Validation Failed",
    "details": "org.springframework.validation.BeanPropertyBindingResult: 1 errors\nField error in object 'user' on field 'name': rejected value [R]; codes [Size.user.name,Size.name,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.name,name]; arguments []; default message [name],2147483647,2]; default message [Name should have atleast 2 characters]"
}
```
#### GET http://localhost:8080/users/1 with HATEOAS
```json
{
    "id": 1,
    "name": "Adam",
    "birthDate": "2019-11-21T14:25:26.787+0000",
    "_links": {
        "self": {
            "href": "http://localhost:8080/users/1"
        },
        "all-users": {
            "href": "http://localhost:8080/users"
        }
    }
}
```
#### Internationalization

##### Configuration 
- LocaleResolver
   - Default Locale - Locale.US
- ResourceBundleMessageSource

##### Usage
- Autowire MessageSource
- @RequestHeader(value = "Accept-Language", required = false) Locale locale
- messageSource.getMessage("helloWorld.message", null, locale)

### XML Representation of Resources

#### GET http://localhost:8080/users
- Accept application/xml
```xml
<List>
    <item>
        <id>2</id>
        <name>Eve</name>
        <birthDate>2019-11-21T10:25:20.450+0000</birthDate>
    </item>
    <item>
        <id>3</id>
        <name>Jack</name>
        <birthDate>2019-11-21T10:25:20.450+0000</birthDate>
    </item>
    <item>
        <id>4</id>
        <name>Govinda</name>
        <birthDate>2019-11-21T10:25:20.450+0000</birthDate>
    </item>
</List>
```

#### POST http://localhost:8080/users
- Accept : application/xml
- Content-Type : application/xml

Request
```xml
<item>
        <name>Govinda</name>
        <birthDate>2019-11-21T10:25:20.450+0000</birthDate>
</item>
```

Response
- Status - 201 Created
