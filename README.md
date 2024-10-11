# 쇼핑몰(KT Commerce)

본 프로젝트는 사내 물품 구매 플랫폼인 KT Commerce의 내부 구조를 MSA로 변경하는 프로젝트입니다.
기존 모놀리식으로 운영되는 플랫폼을 MSA로 재구성하면서, 아이디어 구상이 아닌 강의에서 배운 내용을 복기하는 일에 중점을 두었습니다.


# Table of contents

- [쇼핑몰](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [폴리글랏 프로그래밍](#폴리글랏-프로그래밍)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
  - [신규 개발 조직의 추가](#신규-개발-조직의-추가)

# 서비스 시나리오

쇼핑몰 커버하기

기능적 요구사항
1. 임직원이 상품 리스트를 확인할 수 있다.
2. 임직원이 상품을 주문함과 동시에 결제, 배송등록, 재고차감이 진행된다.
3. 재고가 차감될 때 재고가 없는 경우 주문을 취소한다.
4. 주문을 취소하면 재고가 증가하고 배송등록이 취소된다.
5. 상품 담당 매니저는 상품을 등록할 수 있다.
6. 결제가 시작되면 결제 승인/거부 할 수 있다.
7. 결제가 승인되면 임직원이 등록한 계좌에서 금액이 차감된다.
8. 결제가 거절되면 주문의 상태가 변경되고, 주문이 취소된다.
9. 전체적인 주문에 대한 정보 및 상태 등을 한 화면에서 확인 할 수 있다.(viewpage)

비기능적 요구사항
1. 트랜잭션
    1. 주문이 되지 않은 결제 건은 성립되지 않아야 한다.  (Sync 호출)
1. 장애격리
    1. 배송 등록 및 결제 기능이 수행되지 않더라도 주문은 365일 24시간 받을 수 있어야 한다  Async (event-driven), Eventual Consistency
1. 성능
    1. 주문에 대한 정보 및 상태 등을 한번에 확인할 수 있어야 한다  (CQRS)
    1. 주문의 상태가 바뀔 때마다 상태 정보를 변경할 수 있어야한다  (Event driven)


# 분석/설계


## 조직 역할 분배 (Vertically-Aligned)  
프론트 엔드, 백엔드 등 기술을 기반으로 한 역할 분배 방식이 아닌 MSA 이론에 따른 서비스 관점의 역할 분배 적용
팀은 3명으로 구성되었고 메인 서비스인 Order와 Invetory서비스 담당자 1명, Payment와 account 서비스 담당자 1명, Delivery 및 배포 담당자 1명으로 역할 분배
한정된 시간내에 병렬적으로 서비스를 개발하기 위해 해당 관점을 중점으로 하여 역할을 나눔


## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과:  https://www.msaez.io/#/72953874/storming/group01

### 부적격 이벤트 탈락
![image](https://private-user-images.githubusercontent.com/77838744/374384245-e6d4ee13-1315-43c5-8681-eee94b8d8faf.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjgzNzE5NjUsIm5iZiI6MTcyODM3MTY2NSwicGF0aCI6Ii83NzgzODc0NC8zNzQzODQyNDUtZTZkNGVlMTMtMTMxNS00M2M1LTg2ODEtZWVlOTRiOGQ4ZmFmLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMDglMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDA4VDA3MTQyNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWI2YTY5YjliYTRkMDBkMTUzNDZmZGI3MGVlMDJjNjhlOGRlOTc5OTZlNmJmNzAwNWI4MzJjZDNjNmVjZmMzNGYmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.i6yBxePYwtMFo41aXhD07mm_nJtNlw4yhAOFDlzor74)

### 완성된 1차 모형

![image](https://private-user-images.githubusercontent.com/77838744/374408284-6154a1d8-0f15-441b-87d2-6219a34ced2a.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjgzNzE5NjUsIm5iZiI6MTcyODM3MTY2NSwicGF0aCI6Ii83NzgzODc0NC8zNzQ0MDgyODQtNjE1NGExZDgtMGYxNS00NDFiLTg3ZDItNjIxOWEzNGNlZDJhLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMDglMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDA4VDA3MTQyNVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTg4OTk3YzNmM2VjOGNmNjg4ZTdmMWIxMWZjZjk4ZTlhNzE2N2RkMDlkZTgwOGYxMWU3MTM1YzUxNGNlY2MyZDkmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.j52Ctn2E1_WPQtVTqhTD-HZ9rNG-UNIaQjE-eBXgn94)


### 1차 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증

    - 임직원이 상품 리스트를 확인할 수 있다.(ok)
    - 임직원이 상품을 주문함과 동시에 결제, 배송등록, 재고차감이 진행된다.(ok)
    - 재고가 차감될 때 재고가 없는 경우 주문을 취소한다.(ok)
    - 주문을 취소하면 재고가 증가하고 배송등록이 취소된다.(ok)
    - 상품 담당 매니저는 상품을 등록할 수 있다.(ok)
    - 결제가 시작되면 결제 승인/거부 할 수 있다.(?)
    - 결제가 승인되면 임직원이 등록한 계좌에서 금액이 차감된다.(?)
    - 결제가 거절되면 주문의 상태가 변경되고, 주문이 취소된다.(?)
    - 전체적인 주문에 대한 정보 및 상태 등을 한 화면에서 확인 할 수 있다.(ok)
    
### 모델 수정

![image](https://private-user-images.githubusercontent.com/77838744/374450158-ad5bb968-04d4-4be3-a59d-37d79545849e.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjgzNzIyODQsIm5iZiI6MTcyODM3MTk4NCwicGF0aCI6Ii83NzgzODc0NC8zNzQ0NTAxNTgtYWQ1YmI5NjgtMDRkNC00YmUzLWE1OWQtMzdkNzk1NDU4NDllLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMDglMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDA4VDA3MTk0NFomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTQ1ODQ4OGVkZmI0MWMxNmYxMDc2YmE1YWFjNmIwMTkxMTFmMjRkMjlkYmJkNjg4YzVmODM5YTEzOGIwNmQxNTQmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.abUMmTgkluHIllgxoVeZCDZrhw9GISjKuciwCOBd8UE)
    
    - 수정된 모델은 모든 요구사항을 커버함.

### 비기능 요구사항에 대한 검증

![image](https://user-images.githubusercontent.com/15603058/119311800-79df3480-bcac-11eb-9c1b-0382d981f92f.png)

- 고객 주문시 결제처리:  계좌에 잔액 정보가 없는 경우 차감이 결제가 거절되도록 변경해야함
- 상품 정보 리스트업:  상품 정보를 확인하는 기능은 결제 등의 중요 기능과 연관성이 없기에 API Request 방식 사용
- 나머지 모든 inter-microservice 트랜잭션: 모든 이벤트에 대해 데이터 일관성의 시점이 크리티컬하지 않은 모든 경우가 대부분이라 판단, Eventual Consistency 를 기본으로 채택함.

### 설계 과정 중에 느낀점

- 설계 단계에서의 세밀함 필요: 빠른 시간내에 설계를 마무리하다 보니 세부적인 사항을 놓쳐서 개발 중에 보완하는 경우가 많았음
- 보상 트랜잭션 복잡성 증가: 서비스가 커질 수록 액션의 결과에 따라 영향을 주는 범위가 많아져 고려할 요소가 증가했음


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
   mvn spring-boot:run
```

## CQRS

주문(Order) 의 주문id, 수량, 제품id, orderStatus 등 주문 정보에 대하여 고객(Customer)이 조회 할 수 있도록 CQRS 로 구현하였다.
- order, delivery 개별 Aggregate Status 를 조회
- 비동기식으로 처리되어 발행된 이벤트 기반 Kafka 를 통해 수신/처리 되어 별도 Table 에 관리한다


## API 게이트웨이
      1. gateway 스프링부트 App을 추가 후 application.yaml내에 각 마이크로 서비스의 routes 를 추가하고 gateway 서버의 포트를 8080 으로 설정함
       
          - application.yaml 예시
            ```
            server:
            port: 8088

            ---

            spring:
            profiles: default
            cloud:
                gateway:
            #<<< API Gateway / Routes
                routes:
                    - id: inventory
                    uri: http://localhost:8082
                    predicates:
                        - Path=/inventories/**, 
                    - id: dashboard
                    uri: http://localhost:8083
                    predicates:
                        - Path=, 
                    - id: order
                    uri: http://localhost:8084
                    predicates:
                        - Path=/orders/**, 
                    - id: delivery
                    uri: http://localhost:8085
                    predicates:
                        - Path=/deliveries/**, 
                    - id: payment
                    uri: http://localhost:8086
                    predicates:
                        - Path=/payments/**, 
                    - id: account
                    uri: http://localhost:8087
                    predicates:
                        - Path=/accounts/**, 
                    - id: frontend
                    uri: http://localhost:8080
                    predicates:
                        - Path=/**
            #>>> API Gateway / Routes
                globalcors:
                    corsConfigurations:
                    '[/**]':
                        allowedOrigins:
                        - "*"
                        allowedMethods:
                        - "*"
                        allowedHeaders:
                        - "*"
                        allowCredentials: true


            ---

            spring:
            profiles: docker
            cloud:
                gateway:
                routes:
                    - id: inventory
                    uri: http://inventory:8080
                    predicates:
                        - Path=/inventories/**, 
                    - id: dashboard
                    uri: http://dashboard:8080
                    predicates:
                        - Path=, 
                    - id: order
                    uri: http://order:8080
                    predicates:
                        - Path=/orders/**, 
                    - id: delivery
                    uri: http://delivery:8080
                    predicates:
                        - Path=/deliveries/**, 
                    - id: payment
                    uri: http://payment:8080
                    predicates:
                        - Path=/payments/**, 
                    - id: account
                    uri: http://account:8080
                    predicates:
                        - Path=/accounts/**, 
                    - id: frontend
                    uri: http://frontend:8080
                    predicates:
                        - Path=/**
                globalcors:
                    corsConfigurations:
                    '[/**]':
                        allowedOrigins:
                        - "*"
                        allowedMethods:
                        - "*"
                        allowedHeaders:
                        - "*"
                        allowCredentials: true

            server:
            port: 8080
            ```

         
      2. Kubernetes용 Deployment.yaml 을 작성하고 Kubernetes에 Deploy를 생성함
          - Deployment.yaml 예시
          

            ```
            apiVersion: apps/v1
            kind: Deployment
            metadata:
            name: gateway
            labels:
                app: gateway
            spec:
            replicas: 1
            selector:
                matchLabels:
                app: gateway
            template:
                metadata:
                labels:
                    app: gateway
                spec:
                containers:
                    - name: gateway
                    image: 9nuj/gateway:20241010
                    ports:
                        - containerPort: 8080

            ```               
            

            ```
            Deploy 생성
            kubectl apply -f deployment.yaml
            ```     
          - Kubernetes에 생성된 Deploy. 확인
            
	    
            
      3. Kubernetes용 Service.yaml을 작성하고 Kubernetes에 Service/LoadBalancer을 생성하여 Gateway 엔드포인트를 확인함. 
          - Service.yaml 예시
          
            ```
            apiVersion: v1
            kind: Service
            metadata:
            name: gateway
            labels:
                app: gateway
            spec:
            ports:
                - port: 8080
                targetPort: 8080
            selector:
                app: gateway
            type: LoadBalancer         
            ```             

           
            ```
            Service 생성
            kubectl apply -f service.yaml            
            ```             
            
            
          - API Gateay 엔드포인트 확인
           
            ```
            Service  및 엔드포인트 확인 
            kubectl get svc -n gateway           
            ```                 
![image](https://user-images.githubusercontent.com/80744273/119318358-2a046b80-bcb4-11eb-9d46-ef2d498c2cff.png)


# 보상 트랜잭션

주문(Order)을 하면 동시에 재고(Inventory), 배송(Delivery), 결제(Payment) 등의 서비스의 상태가 적당하게 변경이 되고,
주문건의 취소를 수행하면 다시 연관된 재고(Inventory), 배송(Delivery), 결제(Payment) 등의 서비스의 상태값 등의 데이터가 적당한 상태로 변경되는 것을
확인할 수 있습니다.

주문 등록 및 등록에 따른 기타 반응
![image](https://private-user-images.githubusercontent.com/77838744/375619566-804764f0-ddef-465b-92f8-06c3413ac6bf.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjg2MTg5MzIsIm5iZiI6MTcyODYxODYzMiwicGF0aCI6Ii83NzgzODc0NC8zNzU2MTk1NjYtODA0NzY0ZjAtZGRlZi00NjViLTkyZjgtMDZjMzQxM2FjNmJmLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMTElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDExVDAzNTAzMlomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTE5N2I1MjQ4NWE1ZDZmMTFmNDgwZDEwMWU3MGEyMzQ5YjQxNmE1MTg1Y2RlNzQxOTEzM2E2MjNkOWE0NmJjOTEmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.wx-NiNAWwy2RFdeu8GsLdpVkiwD6hiEHdyVJzrHMmkw)
재고 부족으로 인한 주문 취소
![image](https://private-user-images.githubusercontent.com/77838744/375619601-2253bdca-4bda-4e05-b309-d6baf1c6af05.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3Mjg2MTg5NDQsIm5iZiI6MTcyODYxODY0NCwicGF0aCI6Ii83NzgzODc0NC8zNzU2MTk2MDEtMjI1M2JkY2EtNGJkYS00ZTA1LWIzMDktZDZiYWYxYzZhZjA1LnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDEwMTElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQxMDExVDAzNTA0NFomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWUzM2E4ZjZiNWU1N2EzZGY1MDE1ODVjYmU0MzYwYTNjY2I0MzliNjY1N2JhZThjNTkxZGU5NWQzZTY2NjI5NTEmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0In0.rHNBIMe226jKHt4fO2bU4E-W4Pc25haaMRPoF0GB4T8)
