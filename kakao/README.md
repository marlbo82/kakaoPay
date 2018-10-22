1. in-memory DB : Redis 사용
   application.properties에 host/port 세팅

2. Back-End Framework : Spring Boot 사용

3. Application 구동
   1) RunAs - Spring Boot App
   
4. 문제해결 전략
   1) 참조를 개별 할일 별 path를 사용하여 계층화 함
   2) path 값을 이용해 순환 참조 구조를 막음 