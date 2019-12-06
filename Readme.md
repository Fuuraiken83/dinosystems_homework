#What is this?
This is a simple REST API phonebook application which do:
* get all users
* get all phone numbers in users' phonebook
* create, read (by id or name), update, delete users
* create, read (by id, number), update, delete phone numbers in phonebook
#How to run
Run Main.java class in your IDE or execute jar file from the folder "target".
#REST request examples
Add user via POST request:
`curl -d '{"id":"1", "name":"Ivan"}' -H "Content-Type: application/json" -X POST http://localhost:8080/user`
Add phone number to phonebook via POST request:
`curl -d '{"id":"1","owner_id":"1", "number":"+88001231234"}' -H "Content-Type: application/json" -X POST http://localhost:8080/phonebook`
Get all users via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/user`
Get user with id = 1 via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/user/1`
Get user with name "Ivan" via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/user/?name=Ivan`
Get all phone numbers from phonebook via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/phonebook`
Get phone numbers with id = 1 via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/phonebook/1`
Get phone number "+88001231234" via GET request:
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/phonebook/?number=%2B88001231234`
Get phonebook of the user with id=1 via GET request
`curl -H "Content-Type: application/json" -X GET http://localhost:8080/user/1/phonebook`
Edit user via PUT request:
`curl -d '{"id":"1", "name":"Alexandr"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/user`
Edit phone number via PUT request:
`curl -d '{"id":"1", "owner_id":"1","number":"+81231231234"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/phonebook`
Delete user via DELETE request:
`curl -d '{"id":"1", "name":"Ivan"}' -H "Content-Type: application/json" -X DELETE http://localhost:8080/user`
Delete phone number via DELETE request:
`curl -d '{"id":"1","owner_id":"1", "number":"+88001231234"}' -H "Content-Type: application/json" -X POST http://localhost:8080/phonebook`