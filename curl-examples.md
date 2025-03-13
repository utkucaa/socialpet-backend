# SocialPet API - CURL Örnekleri

Bu dosya, SocialPet uygulamasının soru ve cevap API'leri için CURL komut örneklerini içerir.

## Soru (Question) Endpoint'leri için CURL Örnekleri

### 1. Tüm Soruları Getirme
```bash
curl -X GET http://localhost:8080/api/questions
```

### 2. ID'ye Göre Soru Getirme
```bash
curl -X GET http://localhost:8080/api/questions/1
```

### 3. Yeni Soru Oluşturma
```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Köpeğim sürekli havlıyor, ne yapmalıyım?",
    "content": "3 yaşındaki golden retriever köpeğim son zamanlarda çok fazla havlamaya başladı. Nasıl sakinleştirebilirim?",
    "user": {
      "id": 1
    }
  }'
```

### 4. Soru Güncelleme
```bash
curl -X PUT http://localhost:8080/api/questions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Köpeğim sürekli havlıyor, acil yardım!",
    "content": "3 yaşındaki golden retriever köpeğim son zamanlarda çok fazla havlamaya başladı. Komşular şikayet ediyor. Nasıl sakinleştirebilirim?",
    "user": {
      "id": 1
    }
  }'
```

### 5. Soru Silme
```bash
curl -X DELETE http://localhost:8080/api/questions/1
```

## Cevap (Answer) Endpoint'leri için CURL Örnekleri

### 1. Tüm Cevapları Getirme
```bash
curl -X GET http://localhost:8080/api/answers
```

### 2. ID'ye Göre Cevap Getirme
```bash
curl -X GET http://localhost:8080/api/answers/1
```

### 3. Yeni Cevap Oluşturma
```bash
curl -X POST http://localhost:8080/api/answers \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Köpeğinizin havlamasının birçok nedeni olabilir. Öncelikle veterinere götürüp sağlık sorunu olmadığından emin olun. Ayrıca düzenli egzersiz yaptırmak ve zihinsel aktivitelerle meşgul etmek de yardımcı olabilir.",
    "question": {
      "id": 1
    },
    "user": {
      "id": 2
    }
  }'
```

### 4. Cevap Güncelleme
```bash
curl -X PUT http://localhost:8080/api/answers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "content": "Köpeğinizin havlamasının birçok nedeni olabilir. Öncelikle veterinere götürüp sağlık sorunu olmadığından emin olun. Ayrıca düzenli egzersiz yaptırmak ve zihinsel aktivitelerle meşgul etmek de yardımcı olabilir. Eğitim için profesyonel bir köpek eğitmeninden yardım almayı da düşünebilirsiniz.",
    "question": {
      "id": 1
    },
    "user": {
      "id": 2
    }
  }'
```

### 5. Cevap Silme
```bash
curl -X DELETE http://localhost:8080/api/answers/1
```

## Admin Endpoint'leri için CURL Örnekleri

### 1. Tüm Kullanıcıları Getirme
```bash
curl -X GET http://localhost:8080/api/v1/admin/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Bu endpoint, admin sayfasında yeni soru oluşturulurken kullanıcı seçimi için kullanılabilir. Endpoint, kullanıcı bilgilerini AdminUserDTO formatında döndürür ve ADMIN rolüne sahip kullanıcılar tarafından erişilebilir.

### 2. ID'ye Göre Kullanıcı Getirme
```bash
curl -X GET http://localhost:8080/api/v1/admin/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Notlar

- Yukarıdaki örneklerde port numarası olarak 8080 kullanılmıştır. Uygulamanızın çalıştığı port numarasına göre değiştirmeniz gerekebilir.
- User ve question ID'lerini kendi veritabanınızdaki gerçek ID'lerle değiştirmeniz gerekecektir.
- Tüm isteklerde JSON formatı kullanılmaktadır.
- Admin endpoint'leri için Authorization header'ında geçerli bir JWT token gerekmektedir ve bu token ADMIN rolüne sahip bir kullanıcıya ait olmalıdır. 