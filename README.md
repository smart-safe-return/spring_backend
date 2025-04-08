# 🚀 안전 귀가 SMS 알리미 앱 백엔드

지리 데이터 기반 안전 귀가 알리미 서비스의 백엔드 서비스로, Spring Boot 3.2를 활용한 확장 가능한 REST API를 제공합니다.

## 📋 기술 스택

### 백엔드
- **언어**: Java 21
- **프레임워크**: Spring Boot 3.2.4
- **보안**: Spring Security, JWT (jjwt 0.12.3)
- **ORM**: Spring Data JPA, Hibernate, QueryDSL 5.0.0
- **공간 데이터 처리**: Hibernate Spatial 6.5.0, PostGIS JDBC 2.5.0
- **API 문서화**: SpringDoc OpenAPI (Swagger UI 3) 2.4.0
- **유효성 검사**: Spring Validation

### 데이터베이스
- **개발 환경**: H2 Database
- **운영 환경**: PostgreSQL + PostGIS (공간 데이터 확장)

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
- **JWT 기반 인증**: 액세스 토큰 및 리프레시 토큰 활용한 보안 시스템
- **지리적 위치 기반 서비스**: PostGIS를 활용한 지리 데이터 처리 및 분석
- **파일 업로드**: Google Cloud Storage를 활용한 이미지 및 파일 저장
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


## 🔧 설치 및 실행 방법

### 사전 요구사항
- JDK 21
- PostgreSQL + PostGIS 확장
- (선택사항) Google Cloud 계정

### 로컬 개발 환경 설정

1. 저장소 클론
```bash
git clone https://github.com/smart-safe-return/spring_backend.git
cd samrt-safe-return-backend
```

2. 환경 변수 설정
```bash
# .env 파일 생성 또는 IDE 실행 설정에 추가
SPRING_DATABASE_USERNAME=your_db_username
SPRING_DATABASE_PASSWORD=your_db_password
SPRING_DATABASE_NAME=your_db_name
SPRING_JWT_SECRET=your_jwt_secret
COOL_SMS_API_KEY=your_coolsms_api_key
COOL_SMS_SECRET_KEY=your_coolsms_secret_key
COOL_SMS_FROM_NUMBER=your_coolsms_from_number
```

3. 애플리케이션 실행
```bash
./gradlew bootRun
```

4. API 문서 확인
```
https://smart-safe-return-backend-88013499747.asia-northeast2.run.app/swagger-ui/index.html
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

- 개발 환경: `http://localhost:8080/swagger-ui.html`
- 운영 환경: `https://smart-safe-return-backend-88013499747.asia-northeast2.run.app/swagger-ui/index.html`

## 📊 성능 최적화

- QueryDSL을 활용한 효율적인 쿼리 관리
- JPA 캐싱 전략 적용


## 📞 연락처

- 개발자: [이규찬](mailto:hoo788@gmail.com)
- 웹사이트: [포트폴리오 링크](https://github.com/smart-safe-return/spring_backend)
- LinkedIn: [LinkedIn 프로필](https://github.com/gyuchanlee)
