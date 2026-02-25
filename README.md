# 📸 Fotori

Fotori là nền tảng đặt lịch chụp ảnh, kết nối **khách hàng** với **photographer** thông qua các gói chụp và booking theo lịch.

Backend được thiết kế theo hướng **role-based**, **JWT authentication**, và **booking theo thời gian** để đảm bảo không trùng lịch.

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 2.7.x
- Spring Security (JWT – Stateless)
- Spring Data JPA (Hibernate)
- MySQL
- Flyway
- Lombok

---

## 🔐 Authentication

- Sử dụng **JWT (Bearer Token)**
- Stateless
- Login trả về JWT
- Mỗi request private phải gửi kèm header:

```http
Authorization: Bearer <JWT_TOKEN>