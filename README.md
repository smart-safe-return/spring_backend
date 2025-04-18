# 🚀 안전 귀가 SMS 알리미 앱 백엔드

지리 데이터 기반 안전 귀가 알리미 서비스의 백엔드 서비스로, Spring Boot 3를 활용한 확장 가능한 REST API를 제공합니다.

## 📋 기술 스택

### 백엔드
- **언어**: Java 21
- **프레임워크**: Spring Boot 3.2.4
- **보안**: Spring Security, JWT (jjwt 0.12.3)
- **ORM**: Spring Data JPA, Hibernate, QueryDSL 5.0.0
- **공간 데이터 처리**: Hibernate Spatial 6.5.0, PostGIS JDBC 2.5.0
- **API 문서화**: [SpringDoc OpenAPI (Swagger UI 3) 2.4.0](https://smart-safe-return-backend-88013499747.asia-northeast2.run.app/swagger-ui/index.html)
- **유효성 검사**: Spring Validation
- **컨테이너**: Dockerfile

### 데이터베이스
- **개발 환경**: PostgreSQL + PostGIS
- **운영 환경**: PostgreSQL + PostGIS (공간 데이터 확장)
- **구조**: [ERD CLOUD](https://www.erdcloud.com/d/qB5R3umPgfB8fMYXq)

### 클라우드 서비스
- **배포 환경**: Google Cloud Run
- **데이터베이스**: Google Cloud SQL (PostgreSQL)
- **파일 저장소**: Google Cloud Storage
- **고정 외부 IP 주소 구성**: CoolSms 등록 ip를 고정
- **보안**: 환경 변수 기반 시크릿 관리

### 외부 API 및 서비스
- **SMS 서비스**: CoolSMS API 연동 (Nurigo SDK 4.3.2)

## 🌟 주요 기능

- **사용자 관리**: 회원가입, 로그인, 프로필 관리
- **JWT 기반 인증**: 액세스 토큰 및 리프레시 토큰 활용한 보안 시스템 + RTR 기법 적용
- **지리적 위치 기반 서비스**: PostGIS를 활용한 지리 데이터 처리 및 분석
- **파일 업로드**: Google Cloud Storage를 활용한 이미지 및 파일 저장 (프로필 기능)
- **SMS 인증**: CoolSMS를 통한 휴대폰 번호 인증 시스템
- **RESTful API**: 프론트엔드와의 효율적인 통신을 위한 API 설계
- **문서화된 API**: Swagger UI를 통한 API 문서 제공

## 📝 프로젝트 구조

```
src/
├── main/
│   ├── java/com/dodo/smartsafereturn/
│   │   ├── admin/          # 관리자 기능
│   │   ├── answer/         # 답변 관련 기능
│   │   ├── auth/           # 인증 관련 기능
│   │   │   ├── controller/ # 인증 컨트롤러
│   │   │   ├── dto/        # 인증 데이터 전송 객체
│   │   │   ├── entity/     # 인증 관련 엔티티
│   │   │   ├── repository/ # 인증 관련 리포지토리
│   │   │   ├── service/    # 인증 관련 서비스
│   │   │   └── utils/      # 인증 관련 유틸리티
│   │   ├── emergencycontact/ # 비상 연락처 기능
│   │   ├── global/          # 공통 컴포넌트
│   │   │   ├── config/      # 애플리케이션 설정
│   │   │   ├── entity/      # 공통 엔티티
│   │   │   ├── handler/     # 공통 핸들러
│   │   │   └── service/     # 공통 서비스
│   │   ├── member/         # 회원 기능
│   │   ├── messagelog/     # 메시지 로그 기능
│   │   ├── question/       # 질문 관련 기능
│   │   │   ├── controller/ # 질문 컨트롤러
│   │   │   ├── dto/        # 질문 데이터 전송 객체
│   │   │   ├── entity/     # 질문 관련 엔티티
│   │   │   ├── repository/ # 질문 관련 리포지토리
│   │   │   └── service/    # 질문 관련 서비스
│   │   ├── questioncategory/ # 질문 카테고리 기능
│   │   ├── saferoute/      # 안전 경로 기능
│   │   ├── sms/            # SMS 관련 기능
│   │   ├── sosmessage/     # SOS 메시지 기능
│   │   └── verification/   # 인증 검증 기능
│   └── resources/
│       ├── application.yml # 기본 설정
│       ├── application-dev.yml # 개발 환경 설정
│       └── application-prod.yml # 운영 환경 설정
└── test/                   # 테스트 코드
    └── java/com/dodo/smartsafereturn/ # 테스트 코드 
```

## 🚀 배포

이 프로젝트는 Google Cloud Run에 배포할 수 있도록 구성되어 있습니다.

```bash
# Google Cloud CLI로 배포
gcloud run deploy smart-safe-return-backend \
  --source . \
  --region asia-northeast2 \
  --platform managed \
  --allow-unauthenticated
```

## 🔐 보안 고려사항

- 모든 비밀 정보는 환경 변수로 관리합니다.
- Google Cloud 환경에서는 Secret Manager를 활용하여 민감한 정보를 관리합니다.
- JWT 를 통한 인증 / 인가 기능 -> Refresh 토큰 및 RTR 기법 도입으로 신뢰성을 높였습니다.

## 📚 API 문서

Swagger UI를 통해 API 문서가 제공됩니다.

- 운영 환경: `https://smart-safe-return-backend-88013499747.asia-northeast2.run.app/swagger-ui/index.html`

## 📊 성능 최적화
- QueryDSL을 활용한 효율적인 쿼리 관리
  - CustomAnswerRepositoryImpl , CustomQuestionRepositoryImpl
    : 조건별 검색, 페이징 처리를 위한 count 쿼리, Dto 직접 조회를 통한 N+1 문제 방지 (fetchJoin을 활용하지 않고 최적화 가능)

## 💡 트러블 슈팅
- CoolSms 배포 환경 인증
  - 고정 아이피 환경이 아닌 클라우드 런에서의 Ip 인증 등록 문제 발생
  - VPC 설정과 서브넷 설정, 서버리스 커넥션 설정을 통해 외부 고정 IP 설정 -> 등록

## 📞 연락처

- 개발자: [이규찬](mailto:hoo788@gmail.com)
- 웹사이트: [포트폴리오 링크](https://github.com/smart-safe-return/spring_backend)
- GitHub: [GitHub 프로필](https://github.com/gyuchanlee)
