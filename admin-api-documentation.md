# Admin API Documentation

Bu dokümantasyon, SocialPet uygulamasının admin kullanıcıları için API endpointlerini açıklamaktadır. Tüm admin endpointleri, ADMIN rolüne sahip kullanıcılar tarafından erişilebilir.

## Kimlik Doğrulama

Tüm admin API çağrıları için JWT token gereklidir. Token, `Authorization` header'ında `Bearer` öneki ile gönderilmelidir.

Örnek:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Dashboard Endpointleri

### Onay Bekleyen İlanların Sayılarını Alma

**Endpoint:** `GET /api/v1/admin/pending-counts`

**Açıklama:** Onay bekleyen kullanıcıların, sahiplendirme ve kayıp ilanlarının sayılarını döndürür. Dashboard için kullanışlıdır.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/pending-counts \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "pendingUsersCount": 3,
  "pendingAdoptionsCount": 5,
  "pendingLostPetsCount": 3,
  "totalPendingCount": 11
}
```

### Tüm Onay Bekleyen İlanları Listeleme

**Endpoint:** `GET /api/v1/admin/pending-listings`

**Açıklama:** Onay bekleyen tüm kullanıcıları, sahiplendirme ve kayıp ilanlarını tek bir yanıtta listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/pending-listings \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "pendingUsers": [
    {
      "id": 3,
      "userName": "yenikullanici",
      "firstName": "Yeni",
      "lastName": "Kullanıcı",
      "email": "yeni@example.com",
      "phoneNumber": "+905551234567",
      "avatarUrl": null,
      "role": "MEMBER",
      "approvalStatus": "PENDING",
      "joinDate": "2023-03-20T14:25:30.000+00:00",
      "petCount": 0,
      "questionCount": 0,
      "answerCount": 0,
      "adoptionCount": 0,
      "lostPetCount": 0
    }
  ],
  "pendingAdoptions": [
    {
      "id": 1,
      "animalType": "Kedi",
      "petName": "Pamuk",
      "breed": "Tekir",
      "age": 2,
      "gender": "Dişi",
      "size": "Orta",
      "title": "Sevimli tekir kedi sahiplendirilecek",
      "description": "3 aylık aşıları yapılmış sevimli tekir kedi sahiplendirilecektir.",
      "source": "Bireysel",
      "city": "İstanbul",
      "district": "Kadıköy",
      "fullName": "Ahmet Yılmaz",
      "phone": "+905551234567",
      "imageUrl": "/api/v1/files/pamuk.jpg",
      "slug": "sevimli-tekir-kedi-sahiplendirilecek-a1b2c3d4",
      "createdAt": "2023-05-10T14:30:00.000+00:00",
      "approvalStatus": "PENDING",
      "viewCount": 0,
      "user": {
        "id": 1,
        "userName": "ahmet123"
      }
    }
  ],
  "pendingLostPets": [
    {
      "id": 1,
      "title": "Kayıp Golden Retriever",
      "details": "3 yaşında, sarı tüylü, erkek golden retriever. Boynunda kırmızı tasma var.",
      "location": "İstanbul, Beşiktaş",
      "category": "Köpek",
      "status": "Kayıp",
      "additionalInfo": "Ödüllü",
      "imageUrl": "/api/v1/files/golden.jpg",
      "contactInfo": "+905551234567",
      "lastSeenDate": "2023-05-15",
      "lastSeenLocation": "Beşiktaş Barbaros Bulvarı",
      "viewCount": 0,
      "approvalStatus": "PENDING",
      "user": {
        "id": 2,
        "userName": "mehmet456"
      }
    }
  ]
}
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
    "approvalStatus": "APPROVED",
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
    "approvalStatus": "APPROVED",
    "joinDate": "2023-01-10T08:15:00.000+00:00",
    "petCount": 1,
    "questionCount": 3,
    "answerCount": 15,
    "adoptionCount": 0,
    "lostPetCount": 1
  }
]
```

### Onay Bekleyen Kullanıcıları Listeleme

**Endpoint:** `GET /api/v1/admin/users/pending`

**Açıklama:** Sistemdeki onay bekleyen tüm kullanıcıları listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/users/pending \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
[
  {
    "id": 3,
    "userName": "yenikullanici",
    "firstName": "Yeni",
    "lastName": "Kullanıcı",
    "email": "yeni@example.com",
    "phoneNumber": "+905551234567",
    "avatarUrl": null,
    "role": "MEMBER",
    "approvalStatus": "PENDING",
    "joinDate": "2023-03-20T14:25:30.000+00:00",
    "petCount": 0,
    "questionCount": 0,
    "answerCount": 0,
    "adoptionCount": 0,
    "lostPetCount": 0
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
  "approvalStatus": "APPROVED",
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
  "approvalStatus": "PENDING",
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
  "approvalStatus": "APPROVED",
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
  "approvalStatus": "APPROVED",
  "joinDate": "2023-01-15T10:30:00.000+00:00",
  "petCount": 2,
  "questionCount": 5,
  "answerCount": 10,
  "adoptionCount": 1,
  "lostPetCount": 0
}
```

### Kullanıcı Onaylama

**Endpoint:** `PUT /api/v1/admin/users/{userId}/approve`

**Açıklama:** Belirtilen ID'ye sahip kullanıcıyı onaylar.

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/users/3/approve \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
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
  "approvalStatus": "APPROVED",
  "joinDate": "2023-03-20T14:25:30.000+00:00",
  "petCount": 0,
  "questionCount": 0,
  "answerCount": 0,
  "adoptionCount": 0,
  "lostPetCount": 0
}
```

### Kullanıcı Reddetme

**Endpoint:** `PUT /api/v1/admin/users/{userId}/reject`

**Açıklama:** Belirtilen ID'ye sahip kullanıcıyı reddeder.

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/users/3/reject \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
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
  "approvalStatus": "REJECTED",
  "joinDate": "2023-03-20T14:25:30.000+00:00",
  "petCount": 0,
  "questionCount": 0,
  "answerCount": 0,
  "adoptionCount": 0,
  "lostPetCount": 0
}
```

## Sahiplendirme İlanları Yönetimi

### Onay Bekleyen İlanların Sayılarını Alma

**Endpoint:** `GET /api/v1/admin/pending-counts`

**Açıklama:** Onay bekleyen sahiplendirme ve kayıp ilanlarının sayılarını döndürür. Dashboard için kullanışlıdır.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/pending-counts \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "pendingAdoptionsCount": 5,
  "pendingLostPetsCount": 3,
  "totalPendingCount": 8
}
```

### Tüm Onay Bekleyen İlanları Listeleme

**Endpoint:** `GET /api/v1/admin/pending-listings`

**Açıklama:** Onay bekleyen tüm sahiplendirme ve kayıp ilanlarını tek bir yanıtta listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/pending-listings \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "pendingAdoptions": [
    {
      "id": 1,
      "animalType": "Kedi",
      "petName": "Pamuk",
      "breed": "Tekir",
      "age": 2,
      "gender": "Dişi",
      "size": "Orta",
      "title": "Sevimli tekir kedi sahiplendirilecek",
      "description": "3 aylık aşıları yapılmış sevimli tekir kedi sahiplendirilecektir.",
      "source": "Bireysel",
      "city": "İstanbul",
      "district": "Kadıköy",
      "fullName": "Ahmet Yılmaz",
      "phone": "+905551234567",
      "imageUrl": "/api/v1/files/pamuk.jpg",
      "slug": "sevimli-tekir-kedi-sahiplendirilecek-a1b2c3d4",
      "createdAt": "2023-05-10T14:30:00.000+00:00",
      "approvalStatus": "PENDING",
      "viewCount": 0,
      "user": {
        "id": 1,
        "userName": "ahmet123"
      }
    }
  ],
  "pendingLostPets": [
    {
      "id": 1,
      "title": "Kayıp Golden Retriever",
      "details": "3 yaşında, sarı tüylü, erkek golden retriever. Boynunda kırmızı tasma var.",
      "location": "İstanbul, Beşiktaş",
      "category": "Köpek",
      "status": "Kayıp",
      "additionalInfo": "Ödüllü",
      "imageUrl": "/api/v1/files/golden.jpg",
      "contactInfo": "+905551234567",
      "lastSeenDate": "2023-05-15",
      "lastSeenLocation": "Beşiktaş Barbaros Bulvarı",
      "viewCount": 0,
      "approvalStatus": "PENDING",
      "user": {
        "id": 2,
        "userName": "mehmet456"
      }
    }
  ]
}
```

### Onay Bekleyen Sahiplendirme İlanlarını Listeleme

**Endpoint:** `GET /api/v1/admin/adoptions/pending`

**Açıklama:** Onay bekleyen tüm sahiplendirme ilanlarını listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/adoptions/pending \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
[
  {
    "id": 1,
    "animalType": "Kedi",
    "petName": "Pamuk",
    "breed": "Tekir",
    "age": 2,
    "gender": "Dişi",
    "size": "Orta",
    "title": "Sevimli tekir kedi sahiplendirilecek",
    "description": "3 aylık aşıları yapılmış sevimli tekir kedi sahiplendirilecektir.",
    "source": "Bireysel",
    "city": "İstanbul",
    "district": "Kadıköy",
    "fullName": "Ahmet Yılmaz",
    "phone": "+905551234567",
    "imageUrl": "/api/v1/files/pamuk.jpg",
    "slug": "sevimli-tekir-kedi-sahiplendirilecek-a1b2c3d4",
    "createdAt": "2023-05-10T14:30:00.000+00:00",
    "approvalStatus": "PENDING",
    "viewCount": 0,
    "user": {
      "id": 1,
      "userName": "ahmet123"
    }
  }
]
```

### Sahiplendirme İlanını Onaylama

**Endpoint:** `PUT /api/v1/admin/adoptions/{id}/approve`

**Açıklama:** Belirtilen ID'ye sahip sahiplendirme ilanını onaylar.

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/adoptions/1/approve \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "animalType": "Kedi",
  "petName": "Pamuk",
  "breed": "Tekir",
  "age": 2,
  "gender": "Dişi",
  "size": "Orta",
  "title": "Sevimli tekir kedi sahiplendirilecek",
  "description": "3 aylık aşıları yapılmış sevimli tekir kedi sahiplendirilecektir.",
  "source": "Bireysel",
  "city": "İstanbul",
  "district": "Kadıköy",
  "fullName": "Ahmet Yılmaz",
  "phone": "+905551234567",
  "imageUrl": "/api/v1/files/pamuk.jpg",
  "slug": "sevimli-tekir-kedi-sahiplendirilecek-a1b2c3d4",
  "createdAt": "2023-05-10T14:30:00.000+00:00",
  "approvalStatus": "APPROVED",
  "viewCount": 0,
  "user": {
    "id": 1,
    "userName": "ahmet123"
  }
}
```

### Sahiplendirme İlanını Reddetme

**Endpoint:** `PUT /api/v1/admin/adoptions/{id}/reject`

**Açıklama:** Belirtilen ID'ye sahip sahiplendirme ilanını reddeder.

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/adoptions/1/reject \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "animalType": "Kedi",
  "petName": "Pamuk",
  "breed": "Tekir",
  "age": 2,
  "gender": "Dişi",
  "size": "Orta",
  "title": "Sevimli tekir kedi sahiplendirilecek",
  "description": "3 aylık aşıları yapılmış sevimli tekir kedi sahiplendirilecektir.",
  "source": "Bireysel",
  "city": "İstanbul",
  "district": "Kadıköy",
  "fullName": "Ahmet Yılmaz",
  "phone": "+905551234567",
  "imageUrl": "/api/v1/files/pamuk.jpg",
  "slug": "sevimli-tekir-kedi-sahiplendirilecek-a1b2c3d4",
  "createdAt": "2023-05-10T14:30:00.000+00:00",
  "approvalStatus": "REJECTED",
  "viewCount": 0,
  "user": {
    "id": 1,
    "userName": "ahmet123"
  }
}
```

## Kayıp İlanları Yönetimi

### Onay Bekleyen Kayıp İlanlarını Listeleme

**Endpoint:** `GET /api/v1/admin/lostpets/pending`

**Açıklama:** Onay bekleyen tüm kayıp ilanlarını listeler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/admin/lostpets/pending \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
[
  {
    "id": 1,
    "title": "Kayıp Golden Retriever",
    "details": "3 yaşında, sarı tüylü, erkek golden retriever. Boynunda kırmızı tasma var.",
    "location": "İstanbul, Beşiktaş",
    "category": "Köpek",
    "status": "Kayıp",
    "additionalInfo": "Ödüllü",
    "imageUrl": "/api/v1/files/golden.jpg",
    "contactInfo": "+905551234567",
    "lastSeenDate": "2023-05-15",
    "lastSeenLocation": "Beşiktaş Barbaros Bulvarı",
    "viewCount": 0,
    "approvalStatus": "PENDING",
    "user": {
      "id": 2,
      "userName": "mehmet456"
    }
  }
]
```

### Kayıp İlanını Onaylama

**Endpoint:** `PUT /api/v1/admin/lostpets/{id}/approve`

**Açıklama:** Belirtilen ID'ye sahip kayıp ilanını onaylar.

**Curl Örneği:**
```bash
curl -X PUT \
  http://localhost:8080/api/v1/admin/lostpets/1/approve \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt Örneği:**
```json
{
  "id": 1,
  "title": "Kayıp Golden Retriever",
  "details": "3 yaşında, sarı tüylü, erkek golden retriever. Boynunda kırmızı tasma var.",
  "location": "İstanbul, Beşiktaş",
  "category": "Köpek",
  "status": "Kayıp",
  "additionalInfo": "Ödüllü",
  "imageUrl": "/api/v1/files/golden.jpg",
  "contactInfo": "+905551234567",
  "lastSeenDate": "2023-05-15",
  "lastSeenLocation": "Beşiktaş Barbaros Bulvarı",
  "viewCount": 0,
  "approvalStatus": "APPROVED",
  "user": {
    "id": 2,
    "userName": "mehmet456"
  }
}
```

## Dosya Yönetimi Endpointleri

### Dosya Yükleme

**Endpoint:** `POST /api/v1/files/upload`

**Açıklama:** Sisteme tek bir dosya yükler. Genellikle resim dosyaları için kullanılır.

**İstek Parametreleri:**
- `file`: Yüklenecek dosya (multipart/form-data)

**Curl Örneği:**
```bash
curl -X POST \
  http://localhost:8080/api/v1/files/upload \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/your/image.jpg'
```

**Yanıt Örneği:**
```json
{
  "fileName": "a1b2c3d4-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg",
  "fileUrl": "http://localhost:8080/api/v1/files/a1b2c3d4-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg",
  "message": "File uploaded successfully"
}
```

### Çoklu Dosya Yükleme

**Endpoint:** `POST /api/v1/files/upload-multiple`

**Açıklama:** Sisteme birden fazla dosya yükler.

**İstek Parametreleri:**
- `files`: Yüklenecek dosyalar (multipart/form-data)

**Curl Örneği:**
```bash
curl -X POST \
  http://localhost:8080/api/v1/files/upload-multiple \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -H 'Content-Type: multipart/form-data' \
  -F 'files=@/path/to/your/image1.jpg' \
  -F 'files=@/path/to/your/image2.jpg' \
  -F 'files=@/path/to/your/image3.jpg'
```

**Yanıt Örneği:**
```json
{
  "uploadedFiles": {
    "a1b2c3d4-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg": "http://localhost:8080/api/v1/files/a1b2c3d4-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg",
    "b2c3d4e5-6f7g-8h9i-0j1k-l2m3n4o5p6q7.jpg": "http://localhost:8080/api/v1/files/b2c3d4e5-6f7g-8h9i-0j1k-l2m3n4o5p6q7.jpg",
    "c3d4e5f6-7g8h-9i0j-1k2l-m3n4o5p6q7r8.jpg": "http://localhost:8080/api/v1/files/c3d4e5f6-7g8h-9i0j-1k2l-m3n4o5p6q7r8.jpg"
  },
  "message": "Files uploaded successfully"
}
```

### Dosya İndirme

**Endpoint:** `GET /api/v1/files/{fileName}`

**Açıklama:** Belirtilen dosyayı indirir veya görüntüler.

**Curl Örneği:**
```bash
curl -X GET \
  http://localhost:8080/api/v1/files/a1b2c3d4-5e6f-7g8h-9i0j-k1l2m3n4o5p6.jpg \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Yanıt:** Dosya içeriği