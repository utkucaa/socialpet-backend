# SocialPet API - Curl Örnekleri

Bu dokümanda, SocialPet uygulamasının sahiplendirme ilanı ve kayıp ilanı için CRUD (Create, Read, Update, Delete) işlemlerinin curl örnekleri bulunmaktadır.

## İçindekiler
- [Sahiplendirme İlanı (Adoption)](#sahiplendirme-i̇lanı-adoption)
  - [Yeni Sahiplendirme İlanı Oluşturma](#yeni-sahiplendirme-i̇lanı-oluşturma)
  - [Sahiplendirme İlanına Fotoğraf Yükleme](#sahiplendirme-i̇lanına-fotoğraf-yükleme)
  - [Son Eklenen Sahiplendirme İlanlarını Getirme](#son-eklenen-sahiplendirme-i̇lanlarını-getirme)
  - [Belirli Bir Sahiplendirme İlanını Getirme](#belirli-bir-sahiplendirme-i̇lanını-getirme)
  - [Sahiplendirme İlanını Güncelleme](#sahiplendirme-i̇lanını-güncelleme)
  - [Sahiplendirme İlanını Silme](#sahiplendirme-i̇lanını-silme)
- [Kayıp İlanı (Lost Pet)](#kayıp-i̇lanı-lost-pet)
  - [Yeni Kayıp İlanı Oluşturma](#yeni-kayıp-i̇lanı-oluşturma)
  - [Tüm Kayıp İlanlarını Getirme](#tüm-kayıp-i̇lanlarını-getirme)
  - [Belirli Bir Kayıp İlanını Getirme](#belirli-bir-kayıp-i̇lanını-getirme)
  - [Kayıp İlanını Güncelleme](#kayıp-i̇lanını-güncelleme)
  - [Kayıp İlanını Silme](#kayıp-i̇lanını-silme)

## Sahiplendirme İlanı (Adoption)

### Yeni Sahiplendirme İlanı Oluşturma

```bash
curl -X POST http://localhost:8080/api/v1/adoption/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "petName": "Pamuk",
    "animalType": "Kedi",
    "city": "İstanbul",
    "breed": "Tekir",
    "age": 2,
    "size": "Orta",
    "gender": "Dişi",
    "source": "Barınak",
    "title": "Sevimli Tekir Kedi Sahiplendirilecek",
    "description": "2 yaşında, sağlıklı, kısırlaştırılmış ve aşıları tam olan sevimli bir tekir kedi sahiplendirilecektir.",
    "district": "Kadıköy",
    "fullName": "Ahmet Yılmaz",
    "phone": "05551234567",
    "user": {
      "id": 1
    }
  }'
```

### Sahiplendirme İlanına Fotoğraf Yükleme

```bash
curl -X POST http://localhost:8080/api/v1/adoption/1/upload-photo \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/your/image.jpg"
```

### Son Eklenen Sahiplendirme İlanlarını Getirme

```bash
curl -X GET http://localhost:8080/api/v1/adoption/recent \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Belirli Bir Sahiplendirme İlanını Getirme

```bash
curl -X GET http://localhost:8080/api/v1/adoption/sevimli-tekir-kedi-sahiplendirilecek \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Sahiplendirme İlanını Güncelleme

```bash
curl -X PUT http://localhost:8080/api/v1/adoption/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "petName": "Pamuk",
    "animalType": "Kedi",
    "city": "İstanbul",
    "breed": "Tekir",
    "age": 2,
    "size": "Orta",
    "gender": "Dişi",
    "source": "Barınak",
    "title": "Sevimli Tekir Kedi Sahiplendirilecek - Güncellendi",
    "description": "2 yaşında, sağlıklı, kısırlaştırılmış ve aşıları tam olan sevimli bir tekir kedi sahiplendirilecektir. Çocuklarla arası çok iyidir.",
    "district": "Kadıköy",
    "fullName": "Ahmet Yılmaz",
    "phone": "05551234567"
  }'
```

### Sahiplendirme İlanını Silme

```bash
curl -X DELETE http://localhost:8080/api/v1/adoption/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Kayıp İlanı (Lost Pet)

### Yeni Kayıp İlanı Oluşturma

```bash
curl -X POST http://localhost:8080/api/lostpets/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Kayıp Golden Retriever",
    "details": "3 yaşında, erkek, altın sarısı tüylere sahip Golden Retriever cinsi köpeğim kayboldu. Boynunda kırmızı tasması var.",
    "location": "İstanbul",
    "category": "Köpek",
    "status": "Kayıp",
    "additionalInfo": "Ödüllü",
    "contactInfo": "05551234567",
    "lastSeenDate": "2023-06-15",
    "lastSeenLocation": "Kadıköy Moda Parkı"
  }'
```

### Tüm Kayıp İlanlarını Getirme

```bash
curl -X GET http://localhost:8080/api/lostpets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Belirli Bir Kayıp İlanını Getirme

```bash
curl -X GET http://localhost:8080/api/lostpets/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Kayıp İlanını Güncelleme

```bash
curl -X PUT http://localhost:8080/api/lostpets/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Kayıp Golden Retriever - Güncellendi",
    "details": "3 yaşında, erkek, altın sarısı tüylere sahip Golden Retriever cinsi köpeğim kayboldu. Boynunda kırmızı tasması var. Sol kulağında küçük bir kesik bulunmaktadır.",
    "location": "İstanbul",
    "category": "Köpek",
    "status": "Kayıp",
    "additionalInfo": "Ödüllü - 1000 TL",
    "contactInfo": "05551234567",
    "lastSeenDate": "2023-06-15",
    "lastSeenLocation": "Kadıköy Moda Parkı"
  }'
```

### Kayıp İlanını Silme

```bash
curl -X DELETE http://localhost:8080/api/lostpets/1?userId=1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Notlar

1. Tüm isteklerde `YOUR_JWT_TOKEN` yerine geçerli bir JWT token kullanılmalıdır.
2. Sahiplendirme ilanı oluşturma ve güncelleme işlemlerinde, kullanıcı bilgileri (`user` nesnesi) gereklidir.
3. Kayıp ilanı oluşturma işleminde, kullanıcı ID'si URL'de belirtilmelidir.
4. Kayıp ilanı silme işleminde, kullanıcı ID'si query parameter olarak belirtilmelidir.
5. Sahiplendirme ilanı fotoğraf yükleme işleminde, dosya yolu (`@/path/to/your/image.jpg`) yerine gerçek dosya yolu kullanılmalıdır. 