# Google Places API Kullanım Kılavuzu

Bu dokümanda, Google Places API entegrasyonunu kullanarak işletme tipi, il ve ilçe bazında filtreleme yapabilmeniz için gerekli curl isteklerini bulabilirsiniz.

## İl ve İlçe Bazlı Filtreleme

### Veteriner Klinikleri İçin:

```bash
# İl bazında veteriner klinikleri arama
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Istanbul&radius=5000&type=veterinary_care"

# İl ve ilçe bazında veteriner klinikleri arama
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Istanbul&district=Kadikoy&radius=3000&type=veterinary_care"
```

### Pet Shop'lar İçin:

```bash
# İl bazında pet shop arama
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Istanbul&radius=5000&type=pet_store"

# İl ve ilçe bazında pet shop arama
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Istanbul&district=Besiktas&radius=3000&type=pet_store"
```

### Evcil Hayvanlarla İlgili Tüm İşletmeler:

```bash
# İl bazında evcil hayvanlarla ilgili tüm işletmeleri arama
curl "http://localhost:8080/api/v1/places/pet-places-by-location?city=Istanbul&radius=5000"

# İl ve ilçe bazında evcil hayvanlarla ilgili tüm işletmeleri arama
curl "http://localhost:8080/api/v1/places/pet-places-by-location?city=Istanbul&district=Sisli&radius=3000"

# Anahtar kelime ile filtreleme
curl "http://localhost:8080/api/v1/places/pet-places-by-location?city=Istanbul&district=Kadikoy&radius=3000&keyword=veteriner"
```

## Farklı İller İçin Örnekler

### Ankara:

```bash
# Ankara'daki veteriner klinikleri
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Ankara&radius=5000&type=veterinary_care"

# Ankara Çankaya'daki pet shop'lar
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Ankara&district=Cankaya&radius=3000&type=pet_store"
```

### İzmir:

```bash
# İzmir'deki veteriner klinikleri
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Izmir&radius=5000&type=veterinary_care"

# İzmir Karşıyaka'daki pet shop'lar
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Izmir&district=Karsiyaka&radius=3000&type=pet_store"
```

### Antalya:

```bash
# Antalya'daki veteriner klinikleri
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Antalya&radius=5000&type=veterinary_care"

# Antalya Konyaaltı'ndaki pet shop'lar
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Antalya&district=Konyaalti&radius=3000&type=pet_store"
```

## Metin Araması

İl ve ilçe bazlı metin araması yapabilirsiniz:

```bash
# İstanbul'da "veteriner" araması
curl "http://localhost:8080/api/v1/places/search-by-location?query=veteriner&city=Istanbul&radius=5000"

# Ankara Çankaya'da "pet shop" araması
curl "http://localhost:8080/api/v1/places/search-by-location?query=pet%20shop&city=Ankara&district=Cankaya&radius=3000"
```

## Otomatik Tamamlama

İl ve ilçe bazlı otomatik tamamlama önerileri alabilirsiniz:

```bash
# İstanbul'da "vet" ile başlayan yerler için öneriler
curl "http://localhost:8080/api/v1/places/autocomplete-by-location?input=vet&city=Istanbul&radius=5000"

# İzmir Karşıyaka'da "pet" ile başlayan yerler için öneriler
curl "http://localhost:8080/api/v1/places/autocomplete-by-location?input=pet&city=Izmir&district=Karsiyaka&radius=3000"
```

## Frontend Entegrasyonu İçin Örnek Kod

Aşağıdaki JavaScript kodu, frontend uygulamanızda il ve ilçe bazlı filtreleme özelliğini uygulamak için kullanılabilir:

```javascript
// İl ve ilçe bazlı filtreleme fonksiyonu
function filterPlacesByLocation(businessType, city, district) {
  // İşletme tipine göre endpoint ve parametreleri belirle
  let endpoint, params;
  
  if (businessType === 'veterinary') {
    endpoint = '/api/v1/places/nearby-by-location';
    params = {
      city: city,
      district: district || null,
      radius: 5000,
      type: 'veterinary_care'
    };
  } else if (businessType === 'petshop') {
    endpoint = '/api/v1/places/nearby-by-location';
    params = {
      city: city,
      district: district || null,
      radius: 5000,
      type: 'pet_store'
    };
  } else if (businessType === 'all') {
    endpoint = '/api/v1/places/pet-places-by-location';
    params = {
      city: city,
      district: district || null,
      radius: 5000
    };
  }
  
  // Null parametreleri temizle
  Object.keys(params).forEach(key => {
    if (params[key] === null) {
      delete params[key];
    }
  });
  
  // API isteği yap
  fetch(`http://localhost:8080${endpoint}?` + new URLSearchParams(params))
    .then(response => response.json())
    .then(data => {
      // Sonuçları işle ve UI'da göster
      displayResults(data);
    })
    .catch(error => console.error('Error:', error));
}

// Sonuçları UI'da gösteren fonksiyon
function displayResults(data) {
  // Burada sonuçları UI'da gösterme mantığınızı uygulayabilirsiniz
  console.log('Places API results:', data);
}

// Kullanım örnekleri:
// İstanbul'daki tüm veteriner klinikleri
filterPlacesByLocation('veterinary', 'Istanbul');

// Ankara Çankaya'daki pet shop'lar
filterPlacesByLocation('petshop', 'Ankara', 'Cankaya');

// İzmir'deki tüm evcil hayvan ile ilgili işletmeler
filterPlacesByLocation('all', 'Izmir');
```

Bu kodu, arayüzünüzdeki filtreleme bileşenlerine entegre ederek kullanabilirsiniz. İl ve ilçe seçimlerini dropdown menülerden alabilir ve seçilen işletme tipine göre API isteklerini yapabilirsiniz.

## Notlar

1. Türkçe karakterler URL'de sorun çıkarabilir, bu nedenle `encodeURIComponent` kullanarak parametreleri kodlamanız önerilir.
2. API isteklerinde Türkiye dışındaki konumlar için "Turkey" ülke bilgisi otomatik olarak eklenmektedir. Farklı ülkeler için servisi güncellemeniz gerekebilir.
3. Radius değeri metre cinsinden olup, maksimum 50000 (50 km) olabilir.
4. İl ve ilçe isimleri büyük/küçük harf duyarlı değildir, ancak doğru yazılması önemlidir (örn. "Istanbul" veya "istanbul"). 