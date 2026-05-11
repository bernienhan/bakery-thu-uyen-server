# Modular Monolith + Clean Architecture

Package gốc của project:

```text
src/main/java/com/thuyen/bakeryshop
├── BakeryShopApplication.java
│
├── common
│   ├── config
│   ├── exception
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ApiException.java
│   │   └── ErrorCode.java
│   ├── response
│   │   ├── ApiResponse.java
│   │   └── PageResponse.java
│   ├── security
│   └── util
│
└── modules
    ├── user
    │   ├── api
    │   │   ├── UserController.java
    │   │   ├── AuthController.java
    │   │   ├── request
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── RegisterRequest.java
    │   │   │   └── UpdateProfileRequest.java
    │   │   └── response
    │   │       ├── LoginResponse.java
    │   │       └── UserResponse.java
    │   │
    │   ├── application
    │   │   ├── UserService.java
    │   │   ├── AuthService.java
    │   │   └── dto
    │   │       └── UserProfileDto.java
    │   │
    │   ├── domain
    │   │   ├── User.java
    │   │   ├── Role.java
    │   │   ├── Permission.java
    │   │   ├── UserRepository.java
    │   │   └── UserDomainService.java
    │   │
    │   ├── infrastructure
    │   │   ├── persistence
    │   │   │   ├── UserJpaEntity.java
    │   │   │   ├── RoleJpaEntity.java
    │   │   │   ├── SpringDataUserRepository.java
    │   │   │   ├── UserRepositoryImpl.java
    │   │   │   └── UserMapper.java
    │   │   └── security
    │   │       ├── JwtService.java
    │   │       └── PasswordEncoderConfig.java
    │   │
    │   └── UserModuleConfig.java
    │
    ├── product
    │   ├── api
    │   │   ├── ProductController.java
    │   │   ├── request
    │   │   │   ├── CreateProductRequest.java
    │   │   │   └── UpdateProductRequest.java
    │   │   └── response
    │   │       └── ProductResponse.java
    │   │
    │   ├── application
    │   │   └── ProductService.java
    │   │
    │   ├── domain
    │   │   ├── Product.java
    │   │   ├── Category.java
    │   │   ├── ProductStatus.java
    │   │   └── ProductRepository.java
    │   │
    │   ├── infrastructure
    │   │   └── persistence
    │   │       ├── ProductJpaEntity.java
    │   │       ├── CategoryJpaEntity.java
    │   │       ├── SpringDataProductRepository.java
    │   │       ├── ProductRepositoryImpl.java
    │   │       └── ProductMapper.java
    │   │
    │   └── ProductModuleConfig.java
    │
    ├── order
    │   ├── api
    │   │   ├── OrderController.java
    │   │   ├── request
    │   │   │   ├── CreateOrderRequest.java
    │   │   │   └── CancelOrderRequest.java
    │   │   └── response
    │   │       ├── OrderResponse.java
    │   │       └── OrderItemResponse.java
    │   │
    │   ├── application
    │   │   ├── OrderService.java
    │   │   └── OrderPricingService.java
    │   │
    │   ├── domain
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   ├── OrderStatus.java
    │   │   ├── OrderRepository.java
    │   │   └── OrderDomainService.java
    │   │
    │   ├── infrastructure
    │   │   ├── persistence
    │   │   │   ├── OrderJpaEntity.java
    │   │   │   ├── OrderItemJpaEntity.java
    │   │   │   ├── SpringDataOrderRepository.java
    │   │   │   ├── OrderRepositoryImpl.java
    │   │   │   └── OrderMapper.java
    │   │   └── client
    │   │       ├── ProductClient.java
    │   │       └── PaymentClient.java
    │   │
    │   └── OrderModuleConfig.java
    │
    ├── payment
    │   ├── api
    │   ├── application
    │   ├── domain
    │   ├── infrastructure
    │   └── PaymentModuleConfig.java
    │
    └── notification
        ├── api
        ├── application
        ├── domain
        ├── infrastructure
        └── NotificationModuleConfig.java
```

## Layer Rules

```text
api -> application -> domain
infrastructure -> domain/application
domain -> no Spring, no JPA, no external client
```

- `api`: Controller, request DTO, response DTO. Chỉ nhận/trả HTTP và gọi application service/use case.
- `application`: Use case layer. Điều phối nghiệp vụ, transaction, gọi repository interface hoặc port.
- `domain`: Model nghiệp vụ, enum, rule, repository interface. Không phụ thuộc framework.
- `infrastructure`: JPA entity, Spring Data repository, repository implementation, mapper, external client.
- `common`: Chỉ chứa thứ thật sự dùng chung như exception, response wrapper, config nền, security constant, util nhỏ.

## Current Skeleton Note

Hiện tại các class `SpringData*Repository`, `*JpaEntity`, `*RepositoryImpl`, `*Mapper` được tạo như skeleton trước để giữ đúng kiến trúc.

Khi bắt đầu nối database thật:

- Thêm annotation JPA vào `*JpaEntity`.
- Cho `SpringData*Repository` extend `JpaRepository`.
- Inject `SpringData*Repository` và `*Mapper` vào `*RepositoryImpl`.
- Annotate `*RepositoryImpl` bằng `@Repository`.
- Annotate application service/use case bằng `@Service`.
- Controller mới thêm `@RestController` và mapping endpoint thật.

## Dependency Direction Example

```text
ProductController
-> ProductService
-> ProductRepository
-> ProductRepositoryImpl
-> SpringDataProductRepository
-> ProductJpaEntity
```

Rule quan trọng: module khác không chọc thẳng vào persistence của nhau. Ví dụ `order` không gọi `ProductJpaEntity` hoặc `ProductRepositoryImpl` của `product`; nếu cần dữ liệu sản phẩm thì đi qua application service, port/client, hoặc DTO snapshot rõ ràng.
