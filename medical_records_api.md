# Medical Records API Documentation

Bu doküman, evcil hayvanlar için medikal kayıt işlemlerini yapabilmeniz için gerekli API endpointlerini ve örnek curl komutlarını içermektedir.

## Genel Bilgiler

Tüm isteklerde aşağıdaki başlıklar kullanılmalıdır:

```
Content-Type: application/json
Authorization: Bearer {jwt_token}
```

## Tüm Medikal Kayıtları Çekme

Bir evcil hayvanın tüm medikal kayıtlarını (tedaviler, aşılar, alerjiler, randevular, ağırlık kayıtları) tek seferde çekmek için:

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Tedavi (Treatment) İşlemleri

### Tüm Tedavileri Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/treatments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tekil Tedavi Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/treatments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tedavi Ekleme

```bash
curl -X POST "http://localhost:8080/api/pets/1/medical-records/treatments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "treatmentType": "Antibiyotik Tedavisi",
    "description": "Enfeksiyon tedavisi için antibiyotik verildi",
    "treatmentDate": "2023-11-15",
    "veterinarian": "Dr. Ahmet Yılmaz"
  }'
```

### Tedavi Güncelleme

```bash
curl -X PUT "http://localhost:8080/api/pets/1/medical-records/treatments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "treatmentType": "Antibiyotik Tedavisi",
    "description": "Cilt enfeksiyonu tedavisi için antibiyotik verildi",
    "treatmentDate": "2023-11-15",
    "veterinarian": "Dr. Ahmet Yılmaz"
  }'
```

### Tedavi Silme

```bash
curl -X DELETE "http://localhost:8080/api/pets/1/medical-records/treatments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Aşı (Vaccination) İşlemleri

### Tüm Aşıları Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/vaccinations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tekil Aşı Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/vaccinations/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Aşı Ekleme

```bash
curl -X POST "http://localhost:8080/api/pets/1/medical-records/vaccinations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "vaccineName": "Kuduz Aşısı",
    "vaccinationDate": "2023-10-20",
    "veterinarian": "Dr. Ayşe Demir"
  }'
```

### Aşı Güncelleme

```bash
curl -X PUT "http://localhost:8080/api/pets/1/medical-records/vaccinations/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "vaccineName": "Kuduz Aşısı",
    "vaccinationDate": "2023-10-22",
    "veterinarian": "Dr. Mehmet Öz"
  }'
```

### Aşı Silme

```bash
curl -X DELETE "http://localhost:8080/api/pets/1/medical-records/vaccinations/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Alerji (Allergy) İşlemleri

### Tüm Alerjileri Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/allergies" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tekil Alerji Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/allergies/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Alerji Ekleme

```bash
curl -X POST "http://localhost:8080/api/pets/1/medical-records/allergies" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "allergen": "Tavuk",
    "reaction": "Cilt Kızarıklığı",
    "severity": "Orta",
    "notes": "Tavuklu mama yedikten sonra kaşıntı başlıyor"
  }'
```

### Alerji Güncelleme

```bash
curl -X PUT "http://localhost:8080/api/pets/1/medical-records/allergies/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "allergen": "Tavuk Proteini",
    "reaction": "Cilt Kızarıklığı ve Kaşıntı",
    "severity": "Ciddi",
    "notes": "Tavuklu mama yedikten 2 saat sonra belirtiler başlıyor"
  }'
```

### Alerji Silme

```bash
curl -X DELETE "http://localhost:8080/api/pets/1/medical-records/allergies/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Randevu (Appointment) İşlemleri

### Tüm Randevuları Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/appointments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tekil Randevu Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/appointments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Randevu Ekleme

```bash
curl -X POST "http://localhost:8080/api/pets/1/medical-records/appointments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "appointmentDate": "2023-12-05",
    "veterinarian": "Dr. Hakan Yıldız",
    "reason": "Yıllık Sağlık Kontrolü"
  }'
```

### Randevu Güncelleme

```bash
curl -X PUT "http://localhost:8080/api/pets/1/medical-records/appointments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "appointmentDate": "2023-12-10",
    "veterinarian": "Dr. Hakan Yıldız",
    "reason": "Yıllık Sağlık Kontrolü ve Aşı Yenileme"
  }'
```

### Randevu Silme

```bash
curl -X DELETE "http://localhost:8080/api/pets/1/medical-records/appointments/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Ağırlık Kaydı (Weight Record) İşlemleri

### Tüm Ağırlık Kayıtlarını Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/weight-records" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Tekil Ağırlık Kaydı Çekme

```bash
curl -X GET "http://localhost:8080/api/pets/1/medical-records/weight-records/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

### Ağırlık Kaydı Ekleme

```bash
curl -X POST "http://localhost:8080/api/pets/1/medical-records/weight-records" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "recordDate": "2023-11-30",
    "weight": 8.5,
    "unit": "kg",
    "notes": "Aylık kontrol tartımı"
  }'
```

### Ağırlık Kaydı Güncelleme

```bash
curl -X PUT "http://localhost:8080/api/pets/1/medical-records/weight-records/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}" \
  -d '{
    "recordDate": "2023-11-30",
    "weight": 8.7,
    "unit": "kg",
    "notes": "Aylık kontrol tartımı, önceki ölçüm düzeltildi"
  }'
```

### Ağırlık Kaydı Silme

```bash
curl -X DELETE "http://localhost:8080/api/pets/1/medical-records/weight-records/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {jwt_token}"
```

## Notlar

- Tüm örneklerde `{jwt_token}` yerine gerçek JWT token değerinizi koymalısınız.
- Örneklerdeki ID'ler (`/pets/1/` veya `/treatments/1/` gibi) gerçek ID'ler ile değiştirilmelidir.
- Tarihler `YYYY-MM-DD` formatında olmalıdır.
- Tüm POST ve PUT isteklerinde gönderilen JSON verilerinin doğru formatta olduğundan emin olun. 