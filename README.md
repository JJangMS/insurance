# Simple Auto-Insurance Core Project

> **Java와 헥사고날 아키텍처를 활용한 자동차 보험 가입 심사 코어 시스템**

이 프로젝트는 자동차 보험의 가입 문의, 차량 등록, 보험료 산출 및 승인 프로세스를 다루는 데모용 백엔드 시스템입니다. 비즈니스 로직의 순수성을 유지하기 위해 **Hexagonal Architecture**를 채택하였습니다.

---

## Demo & API Docs
- [데모](http://insurance-demo-env.eba-pepcpfby.ap-northeast-2.elasticbeanstalk.com/)
- [Swagger API 명세서](http://insurance-demo-env.eba-pepcpfby.ap-northeast-2.elasticbeanstalk.com/swagger-ui/index.html)

---

## 핵심 기능
- **고객 정보 조회**: 이름, 생년월일, 연락처를 통한 가입 여부 확인
- **신규 차량 등록**: 가입 정보가 없는 경우 신규 차량 및 운전자 등록
- **보험 심사 로직**: 
  - 만 21세 미만 가입 제한 (Exception Handling)
  - 사고 이력에 따른 보험료 할증 및 거절 로직
- **보험료 산출 및 승인**: 실시간 보험료 계산 및 가입 확정 프로세스

---

## Architecture: Hexagonal Architecture
도메인 모델을 외부 설정(DB, Web)으로부터 분리하여 유지보수성과 테스트 용이성을 극대화했습니다.
- **domain**: 비즈니스 로직 및 엔티티
- **application**: 인바운드/아웃바운드 포트 및 서비스
- **adapter**: REST API(Web) 및 JPA/H2(Persistence) 구현체

---

## Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: H2 Database (In-memory mode)
- **Documentation**: Springdoc OpenAPI (Swagger)
- **Deployment**: AWS Elastic Beanstalk
- **UI**: Tailwind CSS, JavaScript

---

## 테스트 코드
- **JUnit 5 & AssertJ**를 사용하여 도메인 로직 검증
- 특히 가입 거절 케이스(나이 제한, 사고 이력 등)에 대한 예외 발생 테스트를 중점적으로 수행
