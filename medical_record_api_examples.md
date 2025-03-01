# Medical Record API Curl Examples

Bu dokümanda, Medical Record API'sine yapılabilecek tüm isteklerin curl örnekleri bulunmaktadır. Örnekler mock data ile hazırlanmıştır.

## Medical Record İşlemleri

### 1. Yeni Medical Record Oluşturma

```bash
curl -X POST http://localhost:8080/api/medical-records \
  -H "Content-Type: application/json" \
  -d '{
    "petId": 1
  }'
```

### 2. Medical Record Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1
```

### 3. Pet ID'ye Göre Medical Records Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/pet/1
```

### 4. Medical Record Silme

```bash
curl -X DELETE http://localhost:8080/api/medical-records/1
```

## Aşı (Vaccination) İşlemleri

### 1. Aşı Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/vaccinations \
  -H "Content-Type: application/json" \
  -d '{
    "vaccineName": "Kuduz Aşısı",
    "vaccinationDate": "2023-06-15",
    "veterinarian": "Dr. Ahmet Yılmaz"
  }'
```

### 2. Aşıları Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/vaccinations
```

## Tedavi (Treatment) İşlemleri

### 1. Tedavi Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/treatments \
  -H "Content-Type: application/json" \
  -d '{
    "treatmentType": "Cerrahi Operasyon",
    "description": "Diş çekimi operasyonu",
    "treatmentDate": "2023-07-20"
  }'
```

### 2. Tedavileri Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/treatments
```

## Randevu (Appointment) İşlemleri

### 1. Randevu Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "appointmentDate": "2023-08-10",
    "veterinarian": "Dr. Ayşe Demir",
    "reason": "Yıllık kontrol muayenesi"
  }'
```

### 2. Tüm Randevuları Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/appointments
```

### 3. Gelecek Randevuları Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/appointments/upcoming
```

## İlaç (Medication) İşlemleri

### 1. İlaç Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/medications \
  -H "Content-Type: application/json" \
  -d '{
    "medicationName": "Antibiyotik",
    "dosage": "Günde 2 kez 5ml",
    "startDate": "2023-09-01",
    "endDate": "2023-09-10"
  }'
```

### 2. Tüm İlaçları Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/medications
```

### 3. Güncel İlaçları Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/medications/current
```

## Alerji (Allergy) İşlemleri

### 1. Alerji Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/allergies \
  -H "Content-Type: application/json" \
  -d '{
    "allergen": "Tavuk proteini",
    "reaction": "Kaşıntı ve deri döküntüsü"
  }'
```

### 2. Alerjileri Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/allergies
```

## Kilo Kaydı (Weight Record) İşlemleri

### 1. Kilo Kaydı Ekleme

```bash
curl -X POST http://localhost:8080/api/medical-records/1/weight-records \
  -H "Content-Type: application/json" \
  -d '{
    "recordDate": "2023-10-05",
    "weight": 12.5
  }'
```

### 2. Tüm Kilo Kayıtlarını Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/weight-records
```

### 3. En Son Kilo Kaydını Getirme

```bash
curl -X GET http://localhost:8080/api/medical-records/1/weight-records/latest
``` 