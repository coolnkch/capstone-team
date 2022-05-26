![parkingZoneImage](https://user-images.githubusercontent.com/103608467/170402641-36964800-60c4-4bac-a963-e19968989be5.jpg)

# 주차관리

건물주님을 위한 주차 관리 시스템을 구현하였습니다.

- 체크포인트 : https://workflowy.com/s/2/YsBrOKdTenDMLAfG#/0c6df3a4d126
- 구성원
  - 김영민 : 구현(주), 구축 적용
  - 김용수 : 구축 적용
  - 남궁철 : 구현(부), 문서 작성
  - 변재현 : 구축 적용
  - 이현강 : 구축 적용
  - 
# Table of contents

- [주차관리](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
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

기능적 요구사항
1. 고객이 주차장에 들어온다
1. 고객이 출차시에 자동으로 결제한다
1. 고객이 주차를 하면 주차관리에 전달된다.
1. 고객이 출차를 하면 주차 공간은 반납된다.
1. 결제가 정상적으로 진행이 되야 출차가 가능하다.

비기능적 요구사항
1. 트랜잭션
    1. 주차장이 꽉 찼으면 입차를 할수 없다. Sync 호출 
    1. 결제가 되지 않으면 출차를 할수 없다.
1. 장애격리
    1. 입차후 주차된 내용은 입력이 되지 않을수 있다. Async (event-driven), Eventual Consistency
    1. 결제시스템이 과중되면 결제를 잠시후에 하도록 유도한다  Circuit breaker, fallback
1. 성능
    1. 고객은 주차 정보/이력을 상시로 조회가 가능하다.  CQRS, Event driven


# 체크포인트

- 분석 설계

  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/103608467/170408888-db4234f2-d787-498c-96a8-82e5b97fe27f.png)
## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/103608467/170409183-51e166ac-c116-49d0-a41c-a452d2e61b9c.png)

## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과: https://labs.msaez.io/#/storming/pl1nwukvJIVNMZtjNQZ5ehCuuGE3/64b406f4cfa66e2fea498a88a8406713

### 이벤트 도출
![image](https://user-images.githubusercontent.com/103608467/170409847-83867bee-6e65-42fd-ae50-fcb44056fd20.png)

### 부적격 이벤트 탈락
![image](https://user-images.githubusercontent.com/103608467/170409895-4f88b9ea-3848-45cc-b756-fa43ba59f6ab.png)
    - 과정중 도출된 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함
        - 주차시>주차장선택, 주차시>주차장에 알림 : 업무적인 의미의 이벤트가 아니라서 제외

### 액터, 커맨드 부착하여 읽기 좋게
![image](https://user-images.githubusercontent.com/103608467/170410081-ad1cbb62-5b17-4e06-84d3-7db6e78907fb.png)

### 어그리게잇으로 묶기
![image](https://user-images.githubusercontent.com/103608467/170411272-f9b05f33-1dea-4592-b370-5a19e3386aa8.png)

- 주차Gate에서 입차/출차 처리, 결제의 결제이력, 주차장의 주차중/빈자리 관리는 그와 연결된 command 와 event 들에 의하여 트랜잭션이 유지되어야 하는 단위로 그들 끼리 묶어줌

### 바운디드 컨텍스트로 묶기
![image](https://user-images.githubusercontent.com/103608467/170411252-5b9f519e-e781-42ab-bddf-a24c6b9eeffe.png)
    - 도메인 서열 분리 
        - Core Domain:  parkingGate(front), ParkingArea : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 parkingGate 의 경우 1주일 1회 미만, ParkingArea 의 경우 1개월 1회 미만
        - Supporting Domain:   dashboard : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 1주일 이므로 1주일 1회 이상을 기준으로 함.
        - General Domain:   payment : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음, 복수의 3rd Party를 사용가능하도록 이후 변경

### 폴리시 부착 (괄호는 수행주체, 폴리시 부착을 둘째단계에서 해놔도 상관 없음. 전체 연계가 초기에 드러남)
![image](https://user-images.githubusercontent.com/103608467/170411235-2aab0c97-df88-496a-affd-94a8742884d7.png)

### 폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)
![image](https://user-images.githubusercontent.com/103608467/170411688-6eeeaf4e-3615-457b-82be-3b93c41fc517.png)

![image](https://user-images.githubusercontent.com/487999/79683641-5f938580-8266-11ea-9fdb-4e80ff6642fe.png)

### 완성된 1차 모형
![image](https://user-images.githubusercontent.com/103608467/170411984-9b650116-30f0-498c-8904-53837953d2ab.png)
- View Model 추가 (MyParkingInfo)

### 1차 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증
![image](https://user-images.githubusercontent.com/103608467/170412187-22f88594-e908-4a5a-b276-5a059386af77.png)

    - 고객이 주차장에 들어온다 (ok)
    - 고객이 주차장에 주차를 완료한다 (ok)
    - 고객이 출차를 진행한다 (ok)
    - 고객 차량의 주차비를 계산하여 결제한다 (ok)
    - 출차가 완료된다 (ok)

### 모델 수정

### 비기능 요구사항에 대한 검증
![image](https://user-images.githubusercontent.com/103608467/170412624-c364842e-32a1-452b-8dcc-e1483c4e0eb2.png)

    - 마이크로 서비스를 넘나드는 시나리오에 대한 트랜잭션 처리
        - 고객 출차시 결제처리:  결제가 완료되지 않으면 나갈수 없다는 방침에 따라 Request-Response 방식 처리
        - 고객 주차장 진입시 주차관리시스템에 문제가 잇어도 주차가 가능하도록, Eventual Consistency 방식으로 트랜잭션 처리함.
        - Eventual Consistency 를 기본으로 채택함.


## 헥사고날 아키텍처 다이어그램 도출
![image](https://user-images.githubusercontent.com/103608467/170413154-79646519-0e9f-4eb7-8c93-b3cedd5804fb.png)

# 구현:
분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
cd parkingGate
mvn spring-boot:run

cd parkingArea
mvn spring-boot:run 

cd payment
mvn spring-boot:run  

cd DashBoard
mvn spring-boot:run
```

## DDD 의 적용

- 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다: (예시는 parkingGate 마이크로 서비스).

```
package team.domain;

@Entity
@Table(name="Parking_table")
@Data

public class Parking  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String carNo;
    private Date inTime;
    private Date outTime;
    private String carStatus;
    private Long parkAreaId;
  ....
}

```
- Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리가 없도록 데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다
```
package team.domain;

import team.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="parkings", path="parkings")
public interface ParkingRepository extends PagingAndSortingRepository<Parking, Long>{
...

```
- 적용 후 REST API 의 테스트
```
# 주차장 초기화 10대 지정
http :8082/parkingZoneStatuses id="A" numberOfCars=0 totalParkingSpots=10 availableStatus="Y"
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkingZoneStatuses id="A" numberOfCars=0 totalParkingSpots=10 availableStatus="Y"

# 입차
http :8081/parkings carNo=22아2222 parkAreaId=A
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings carNo=22아2222 parkAreaId=A

# 입차 확인
http :8082/parkAreas
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkAreas

# 주차장 상태 확인
http :8082/parkingZoneStatuses
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkingZoneStatuses

# 출차 - 파라미터 입차에 맞게 변경
http PATCH :8081/parkings/20 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아2222
http PATCH http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings/20 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아2222

# 비용 지불
http :8083/payments parkAreaId="A" parkingId=24 paymentStatus=PAID price=1562666
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/payments parkAreaId="A" parkingId=24 paymentStatus=PAID price=1562666

# 출차 확인
http :8082/parkAreas
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkAreas

# 지불확인
http :8083/payments
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/payments

# 대시보드확인
http :8084/myParkingInfoes
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/myParkingInfoes

```


## 폴리글랏 퍼시스턴스

마음만은 하고싶었으나, 시간관계상 생략

## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 주차시 주차장 만차 여부를 확인하는 트랜잭션(동기식)을 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- 주차장 상태확인(ParkingZoneStatus.java)를 확인하기 위하여 Stub과 (FeignClient) 를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 

- 입차요청시 즉시 주차장 상태 확인을 요청하도록 처리
```
# Parking.java (Entity)

    @PostPersist
    public void onPostPersist(){
        // Get request from ParkingZoneStatus
        team.external.ParkingZoneStatus parkingZoneStatus =
            Application.applicationContext.getBean(team.external.ParkingZoneStatusService.class)
            .getParkingZoneStatus("A");

        if(!"Y".equals(parkingZoneStatus.getAvailableStatus())){
            throw new RuntimeException("NO Paring Zone");
        }
    ....
```
- 서비스(parkingGate의 ParkingZoneStatusService)에 fallback 설정
```
# (parkingGate) ParkingZoneStatusService.java

@FeignClient(name="parkingArea", url="${api.path.parkingArea}", fallback = ParkingZoneStatusServiceImpl.class)
public interface ParkingZoneStatusService {
    
    @RequestMapping(method= RequestMethod.GET, path="/parkingZoneStatuses/{id}")
    public ParkingZoneStatus getParkingZoneStatus(@PathVariable("id") String id);

}

```
fallback 코드
```
@Service
public class ParkingZoneStatusServiceImpl implements ParkingZoneStatusService{

    /**
     * 주차장 fallback
     */
    public ParkingZoneStatus getParkingZoneStatus( String id) {
        System.out.println("@@@@@@@ 주차장 조회가 지연 입니다. @@@@@@@@@@@@ " + id);
        System.out.println("@@@@@@@ 주차장 조회가 지연 입니다. @@@@@@@@@@@@");
        System.out.println("@@@@@@@ 주차장 조회가 지연 입니다. @@@@@@@@@@@@");
        return null;
    }

}
```
- 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 주차관리시스템이 장애가 나면 입차도 안됨을 확인:


```
# 주차관리 (parkArea) 서비스를 잠시 내려놓음 (ctrl+c)

#입차요청
http :8081/parkings carNo=22아2222 parkAreaId=A   #Fail
http :8081/parkings carNo=22아1234 parkAreaId=A   #Fail

http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings carNo=22아2222 parkAreaId=A   #Fail
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings carNo=22아1234 parkAreaId=A   #Fail

#주차관리 재기동
cd parkArea
mvn spring-boot:run

#입차요청 정상
http :8081/parkings carNo=22아2222 parkAreaId=A   #Success
http :8081/parkings carNo=22아1234 parkAreaId=A   #Success

http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings carNo=22아2222 parkAreaId=A
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings carNo=22아1234 parkAreaId=A
```


## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트

 고객의 자동차가 주차자리에서 이동하여 출차를 진행 하면, 주차장관리측에서는 주차 자리가 반납이되어야 한다.
비용 결재 여부와 관계없이 빈자리는 만들어져야한다.
 
- 이를 위하여 parkingGate 이력에 기록을 남긴 후에 출차되었다는 되었다는 도메인 이벤트를 카프카로 송출한다(Publish)
 
```
...
    }
    @PostUpdate
    public void onPostUpdate(){
        ExitRequested exitRequested = new ExitRequested();
        this.outTime = new Date();
        this.carStatus = "VACANT";
        BeanUtils.copyProperties(this, exitRequested);
        //exitRequested.publishAfterCommit();
        exitRequested.publish();

    }
...
```
- 주차장status서비스에서는 출차요청 이벤트에 대해서 이를 수신하여 자신의 정책을 처리하도록 PolicyHandler 를 구현한다:

```
...

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverExitRequested_ParkStatusUpdate(@Payload ExitRequested exitRequested){

        if(!exitRequested.validate()) return;
        ExitRequested event = exitRequested;
        System.out.println("\n\n##### listener ParkStatusUpdate : " + exitRequested.toJson() + "\n\n");

        // Sample Logic //
        ParkingZoneStatus.parkStatusUpdate(event);
    }

```

주차장관리의 주차장상태서비스(ParkingZoneStatus)는 결재 여부에 관계없이 입차/출차 정보를 가지고 구성되므로, payment 시스템에 문제가 있어 출차가 지연되더라도,
주차공간 관리에는 문제가 없다. 다만 주차장상태서비스(ParkingZoneStatus)와 parkArea는 1개의 서비스에서 진행되므로 parkArea 서비스에 문제가 생기면 입차도 제한된다.

```
# 비용 서비스 (payment) 를 잠시 내려놓음 (ctrl+c)

#출차처리
http PATCH :8081/parkings/20 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아2222   #Success
http PATCH :8081/parkings/21 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아2222   #Success

http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings/20 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아2222   #Success
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkings/21 parkAreaId=A outTime=2022-05-26T00:52:14.452+00:00 carNo=22아1234   #Success

#주차장상태 확인
http :8082/parkingZoneStatuses     # 주차 가능대수(numberOfCars) 변경 확인
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkingZoneStatuses

#결제 서비스 기동
cd payment
mvn spring-boot:run

#결제 상태 확인
http :8083/payments     # 모든 결제의 상태가 "PAID"로 확인
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/payments     # 모든 결제의 상태가 "PAID"로 확인

#주차 상태 확인
http :8082/parkAreas  # 주차 상태가 "VACANT"로 확인
http http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkAreas
```


# 운영

## CI/CD 설정

git : https://github.com/coolnkch/capstone-team
```
```


## 동기식 호출 / 서킷 브레이킹 / 장애격리
istio 
```
# team2-gateway.yaml
  gateways:
  - team2-gateway
  http:
  - match: 
    - uri:
        exact: /parkAreas
    route:
    - destination:
        host: parkingarea
        port:
          number: 8080
    timeout: 2s
    retries:
        attempts: 3
        perTryTimeout: 2s
        retryOn: 5xx,retriable-4xx,gateway-error,connect-failure,refused-stream
```

- 피호출 서비스(parkingArea의 ParkingZoneStatus) 의 임의 부하 처리 0.5 ~ 1.5초 전후로 정도 왔다갔다 하게
```
# (parkingGate) ParkingZoneStatus.java (Entity)

    @PostLoad
    public void onPostLoad(){
        try {
            Thread.currentThread().sleep((long) (500 + Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```

- 서비스(parkingGate의 ParkingZoneStatusService)에 fallback 설정
```
# (parkingGate) ParkingZoneStatusService.java

@FeignClient(name="parkingArea", url="${api.path.parkingArea}", fallback = ParkingZoneStatusServiceImpl.class)
public interface ParkingZoneStatusService {
    
    @RequestMapping(method= RequestMethod.GET, path="/parkingZoneStatuses/{id}")
    public ParkingZoneStatus getParkingZoneStatus(@PathVariable("id") String id);

}

```
* 부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:
- 동시사용자 30명
- 40초 동안 실시

```
siege -c30 -t60S -v --content-type "application/json" 'http://a257aa504567c4149bb1f09e3b16157f-1620177609.ap-northeast-3.elb.amazonaws.com/parkingZoneStatuses/A'

.
.
.
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.06 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.06 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.07 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.07 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.06 secs:     402 bytes ==> GET  /parkingZoneStatuses/A
HTTP/1.1 200     0.05 secs:     402 bytes ==> GET  /parkingZoneStatuses/A

:
Lifting the server siege...
Transactions:                  41097 hits
Availability:                 100.00 %
Elapsed time:                  59.09 secs
Data transferred:              15.76 MB
Response time:                  0.04 secs
Transaction rate:             695.50 trans/sec
Throughput:                     0.27 MB/sec
Concurrency:                   29.92
Successful transactions:       41097
Failed transactions:               0
Longest transaction:            0.24
Shortest transaction:           0.02

```
- 운영시스템이 죽지 않은것은 Autoscale (HPA) 적용으로 부하 분산되어 응답
- Retry 의 설정 (istio)

### 오토스케일 아웃
확장 기능을 적용하고자 한다. 

- parkingarea서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 20프로를 넘어서면 replica 를 3개까지 늘려준다.
```
kubectl autoscale deployment parkingarea --cpu-percent=20 --min=1 --max=3
```

## 무정지 재배포

- seige 로 배포작업 직전에 워크로드를 모니터링 함.
```
siege -c20 -t40S -v --content-type "application/json" 'http://localhost:8081/parkings POST {"parkAreaId":"A", "carNo": "22아2222"'
```

```
# deployment.yaml 의 readiness probe 의 설정:
kubectl apply -f kubernetes/deployment.yaml
...
          readinessProbe:
            httpGet:
              path: '/parkings'
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
...
```


# 신규 개발 조직의 추가

  ![image](https://user-images.githubusercontent.com/103608467/170414653-8421fa0b-875d-4597-bbf3-b00158f0161b.png)


## 마케팅팀의 추가
    - KPI: 더많은 주차장을 유치하여 이익 상승(건물주님 네네 어서오세요)
    - 구현계획 마이크로 서비스: 기존 dashboard 와 paekarea 정보를 사용하여, 복수의 주차장 관리기능 추가

## 이벤트 스토밍 
![image](https://user-images.githubusercontent.com/103608467/170415312-f155a881-22ec-4c19-a1c5-d41cd5c31417.png)


## 헥사고날 아키텍처 변화 
![image](https://user-images.githubusercontent.com/103608467/170415078-ef2f7ec1-a3f5-4d46-adf2-60c57ce024ce.png)

## 구현  

기존의 마이크로 서비스에 수정을 발생시키지 않도록 Inbund 요청을 REST 가 아닌 Event 를 Subscribe 하는 방식으로 구현. 기존 마이크로 서비스에 대하여 아키텍처나 기존 마이크로 서비스들의 데이터베이스 구조와 관계없이 추가됨. 

## 운영과 Retirement

Request/Response 방식으로 구현하지 않았기 때문에 서비스가 더이상 불필요해져도 Deployment 에서 제거되면 기존 마이크로 서비스에 어떤 영향도 주지 않음.

