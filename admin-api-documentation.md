# Admin API Documentation

Bu dokümantasyon, SocialPet uygulamasının admin kullanıcıları için API endpointlerini açıklamaktadır. Tüm admin endpointleri, ADMIN rolüne sahip kullanıcılar tarafından erişilebilir.

## Kimlik Doğrulama

Tüm admin API çağrıları için JWT token gereklidir. Token, `Authorization` header'ında `Bearer` öneki ile gönderilmelidir.

Örnek:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Kullanıcı Yönetimi Endpointleri

### Tüm Kullanıcıları Listeleme

**Endpoint:** `GET /api/v1/admin/users`

**Açıklama:** Sistemdeki tüm kullanıcıları listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/users \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
[
  {
    "id": 1,
    "userName": "johndoe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "+905551234567",
    "avatarUrl": "/api/v1/files/profile1.jpg",
    "role": "MEMBER",
    "joinDate": "2023-01-15T10:30:00.000+00:00",
    "petCount": 2,
    "questionCount": 5,
    "answerCount": 10,
    "adoptionCount": 1,
    "lostPetCount": 0
  },
  {
    "id": 2,
    "userName": "janedoe",
    "firstName": "Jane",
    "lastName": "Doe",
    "email": "jane@example.com",
    "phoneNumber": "+905559876543",
    "avatarUrl": "/api/v1/files/profile2.jpg",
    "role": "ADMIN",
    "joinDate": "2023-01-10T08:15:00.000+00:00",
    "petCount": 1,
    "questionCount": 3,
    "answerCount": 15,
    "adoptionCount": 0,
    "lostPetCount": 1
  }
]
```

### Kullanıcı Detaylarını Görüntüleme

**Endpoint:** `GET /api/v1/admin/users/{userId}`

**Açıklama:** Belirtilen ID'ye sahip kullanıcının detaylarını getirir.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/users/1 \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "userName": "johndoe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+905551234567",
  "avatarUrl": "/api/v1/files/profile1.jpg",
  "role": "MEMBER",
  "joinDate": "2023-01-15T10:30:00.000+00:00",
  "petCount": 2,
  "questionCount": 5,
  "answerCount": 10,
  "adoptionCount": 1,
  "lostPetCount": 0
}
```

### Yeni Kullanıcı Oluşturma

**Endpoint:** `POST /api/v1/admin/users`

**Açıklama:** Yeni bir kullanıcı oluşturur.

**İstek Gövdesi:**
```json
{
  "userName": "yenikullanici",
  "firstName": "Yeni",
  "lastName": "Kullanıcı",
  "email": "yeni@example.com",
  "password": "sifre123",
  "phoneNumber": "+905551234567",
  "role": "MEMBER"
}
```

**Curl Örneği:**
```bash
curl -X POST \
  http://localhost:8080/api/v1/admin/users \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
    "userName": "yenikullanici",
    "firstName": "Yeni",
    "lastName": "Kullanıcı",
    "email": "yeni@example.com",
    "password": "sifre123",
    "phoneNumber": "+905551234567",
    "role": "MEMBER"
}'
```

**Yanıt Örneği:**
```json
{
  "id": 3,
  "userName": "yenikullanici",
  "firstName": "Yeni",
  "lastName": "Kullanıcı",
  "email": "yeni@example.com",
  "phoneNumber": "+905551234567",
  "avatarUrl": null,
  "role": "MEMBER",
  "joinDate": "2023-03-20T14:25:30.000+00:00",
  "petCount": 0,
  "questionCount": 0,
  "answerCount": 0,
  "adoptionCount": 0,
  "lostPetCount": 0
}
```

### Kullanıcı Bilgilerini Güncelleme

**Endpoint:** `PUT /api/v1/admin/users/{userId}`

**Açıklama:** Belirtilen ID'ye sahip kullanıcının bilgilerini günceller.

**İstek Gövdesi:**
```json
{
  "userName": "guncelkullanici",
  "firstName": "Güncel",
  "lastName": "Kullanıcı",
  "email": "guncel@example.com",
  "password": "yenisifre123",
  "phoneNumber": "+905559876543",
  "role": "MEMBER"
}
```

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/users/1 \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
    "userName": "guncelkullanici",
    "firstName": "Güncel",
    "lastName": "Kullanıcı",
    "email": "guncel@example.com",
    "password": "yenisifre123",
    "phoneNumber": "+905559876543",
    "role": "MEMBER"
}'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "userName": "guncelkullanici",
  "firstName": "Güncel",
  "lastName": "Kullanıcı",
  "email": "guncel@example.com",
  "phoneNumber": "+905559876543",
  "avatarUrl": "/api/v1/files/profile1.jpg",
  "role": "MEMBER",
  "joinDate": "2023-01-15T10:30:00.000+00:00",
  "petCount": 2,
  "questionCount": 5,
  "answerCount": 10,
  "adoptionCount": 1,
  "lostPetCount": 0
}
```

### Kullanıcı Silme

**Endpoint:** `DELETE /api/v1/admin/users/{userId}`

**Açıklama:** Belirtilen ID'ye sahip kullanıcıyı siler.

**Curl Örneği:**
```bash
curl -X DELETE \
  http://localhost:8080/api/v1/admin/users/1 \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```
User deleted successfully
```

### Kullanıcı Rolünü Değiştirme

**Endpoint:** `PUT /api/v1/admin/users/{userId}/role`

**Açıklama:** Belirtilen ID'ye sahip kullanıcının rolünü değiştirir.

**Parametreler:**
- `role`: Yeni rol (MEMBER veya ADMIN)

**Curl Örneği:**
```bash
curl -X PUT \
  'http://localhost:8080/api/v1/admin/users/1/role?role=ADMIN' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "userName": "johndoe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+905551234567",
  "avatarUrl": "/api/v1/files/profile1.jpg",
  "role": "ADMIN",
  "joinDate": "2023-01-15T10:30:00.000+00:00",
  "petCount": 2,
  "questionCount": 5,
  "answerCount": 10,
  "adoptionCount": 1,
  "lostPetCount": 0
}
```

## Hata Kodları

- **200 OK**: İşlem başarılı
- **201 Created**: Yeni kayıt başarıyla oluşturuldu
- **400 Bad Request**: İstek formatı hatalı veya eksik
- **401 Unauthorized**: Kimlik doğrulama başarısız
- **403 Forbidden**: Yetkilendirme hatası (admin yetkisi yok)
- **404 Not Found**: İstenen kaynak bulunamadı
- **500 Internal Server Error**: Sunucu hatası

## Notlar

- Tüm admin endpointleri için ADMIN rolüne sahip olmanız gerekmektedir.
- Kullanıcı şifreleri veritabanında şifrelenmiş olarak saklanır.
- Kullanıcı oluşturma ve güncelleme işlemlerinde email ve kullanıcı adı benzersiz olmalıdır. 