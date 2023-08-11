# hellow-world-java-spring

| Operation | API Endpoint | HTTP Method | Response Status |
|-----------|--------------|-------------|-----------------|
| Create    | /person      | POST        | 201 (CREATED)   |
| Read      | /person{id}  | GET         | 200 (OK)        |
| Update    | /person{id}  | PUT         | 204 (NO DATA)   |
| Delete    | /person{id}  | DELETE      | 204 (NO DATA)   |

## Layered Architecture

- **Controller Layer** is the layer near the Client (as it receives and responds to web requests).

- **Repository Layer** is the layer near the data store (as it reads from and writes to the data store). 