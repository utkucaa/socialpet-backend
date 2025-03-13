# Google Places API Kullanım Kılavuzu (Genişletilmiş)

Bu dokümanda, Google Places API entegrasyonunu kullanarak işletme tipi, il ve ilçe bazında filtreleme yapabilmeniz ve işletme fotoğraflarını görüntüleyebilmeniz için gerekli curl isteklerini bulabilirsiniz.

## İçindekiler

1. [İl ve İlçe Bazlı Filtreleme](#il-ve-ilçe-bazlı-filtreleme)
2. [İşletme Detayları](#i̇şletme-detayları)
3. [İşletme Fotoğrafları](#i̇şletme-fotoğrafları)
4. [Metin Araması](#metin-araması)
5. [Otomatik Tamamlama](#otomatik-tamamlama)
6. [Frontend Entegrasyonu](#frontend-entegrasyonu)
7. [Tam İş Akışı Örnekleri](#tam-i̇ş-akışı-örnekleri)
8. [Notlar ve İpuçları](#notlar-ve-i̇puçları)

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

## İşletme Detayları

İşletme detaylarını almak için, önce bir işletmenin `place_id` değerini bilmeniz gerekir. Bu değeri, yukarıdaki arama sonuçlarından elde edebilirsiniz.

```bash
# İşletme detaylarını getirme
curl "http://localhost:8080/api/v1/places/details?placeId=PLACE_ID_BURAYA"
```

Örnek yanıt:

```json
{
  "html_attributions": [],
  "result": {
    "address_components": [...],
    "formatted_address": "Caferağa, Moda Cd. No:120, 34710 Kadıköy/İstanbul, Türkiye",
    "formatted_phone_number": "(0216) 337 05 38",
    "geometry": {
      "location": {
        "lat": 40.9876543,
        "lng": 29.0123456
      }
    },
    "name": "Örnek Veteriner Kliniği",
    "opening_hours": {
      "open_now": true,
      "periods": [...],
      "weekday_text": [...]
    },
    "photos": [
      {
        "height": 1200,
        "html_attributions": [...],
        "photo_reference": "PHOTO_REFERENCE_BURAYA",
        "width": 1600
      }
    ],
    "place_id": "PLACE_ID_BURAYA",
    "rating": 4.7,
    "reviews": [...],
    "types": ["veterinary_care", "health", "point_of_interest", "establishment"],
    "website": "https://www.ornekveteriner.com/"
  },
  "status": "OK"
}
```

## İşletme Fotoğrafları

İşletme fotoğraflarını getirmek için, önce işletme detaylarından bir `photo_reference` değeri almanız gerekir.

```bash
# İşletme fotoğrafını getirme
curl -o isletme_fotografi.jpg "http://localhost:8080/api/v1/places/photo?photoReference=PHOTO_REFERENCE_BURAYA&maxWidth=800"
```

Parametreler:
- `photoReference`: İşletme detaylarından alınan fotoğraf referansı
- `maxWidth`: Fotoğrafın maksimum genişliği (piksel cinsinden)
- `maxHeight`: Fotoğrafın maksimum yüksekliği (piksel cinsinden, isteğe bağlı)

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

## Frontend Entegrasyonu

Aşağıdaki JavaScript kodu, frontend uygulamanızda il ve ilçe bazlı filtreleme özelliğini ve fotoğraf görüntülemeyi uygulamak için kullanılabilir:

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
  return fetch(`http://localhost:8080${endpoint}?` + new URLSearchParams(params))
    .then(response => response.json());
}

// İşletme detaylarını getirme fonksiyonu
function getPlaceDetails(placeId) {
  return fetch(`http://localhost:8080/api/v1/places/details?placeId=${placeId}`)
    .then(response => response.json());
}

// İşletme fotoğrafı URL'si oluşturma fonksiyonu
function getPlacePhotoUrl(photoReference, maxWidth = 400) {
  return `http://localhost:8080/api/v1/places/photo?photoReference=${photoReference}&maxWidth=${maxWidth}`;
}

// Örnek kullanım
async function showVeterinaryClinics() {
  try {
    // İstanbul Kadıköy'deki veteriner kliniklerini getir
    const results = await filterPlacesByLocation('veterinary', 'Istanbul', 'Kadikoy');
    
    // Sonuçları işle
    if (results.status === 'OK' && results.results.length > 0) {
      const places = results.results;
      
      // Her bir işletme için detayları ve fotoğrafları göster
      for (const place of places) {
        // İşletme detaylarını getir
        const details = await getPlaceDetails(place.place_id);
        
        // İşletme bilgilerini göster
        console.log(`İşletme Adı: ${place.name}`);
        console.log(`Adres: ${place.vicinity}`);
        console.log(`Puan: ${place.rating || 'Puan yok'}`);
        
        // Eğer fotoğraf varsa, fotoğrafı göster
        if (details.result.photos && details.result.photos.length > 0) {
          const photoReference = details.result.photos[0].photo_reference;
          const photoUrl = getPlacePhotoUrl(photoReference, 800);
          
          // Fotoğrafı bir img elementinde göster
          const img = document.createElement('img');
          img.src = photoUrl;
          img.alt = place.name;
          document.getElementById('results').appendChild(img);
        }
      }
    } else {
      console.log('Sonuç bulunamadı veya bir hata oluştu.');
    }
  } catch (error) {
    console.error('Hata:', error);
  }
}
```

## Tam İş Akışı Örnekleri

### Örnek 1: Veteriner Klinikleri Bulma ve Fotoğraflarını Görüntüleme

```bash
# 1. İstanbul Kadıköy'deki veteriner kliniklerini bul
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Istanbul&district=Kadikoy&radius=3000&type=veterinary_care" > veteriner_klinikleri.json

# 2. İlk sonucun place_id değerini al (jq kullanarak)
PLACE_ID=$(cat veteriner_klinikleri.json | jq -r '.results[0].place_id')

# 3. İşletme detaylarını getir
curl "http://localhost:8080/api/v1/places/details?placeId=$PLACE_ID" > isletme_detaylari.json

# 4. İlk fotoğrafın referansını al
PHOTO_REF=$(cat isletme_detaylari.json | jq -r '.result.photos[0].photo_reference')

# 5. Fotoğrafı indir
curl -o veteriner_fotografi.jpg "http://localhost:8080/api/v1/places/photo?photoReference=$PHOTO_REF&maxWidth=800"
```

### Örnek 2: Pet Shop'ları Bulma ve Detaylarını Görüntüleme

```bash
# 1. Ankara'daki pet shop'ları bul
curl "http://localhost:8080/api/v1/places/nearby-by-location?city=Ankara&radius=5000&type=pet_store" > pet_shoplar.json

# 2. Tüm sonuçların isimlerini ve adreslerini listele (jq kullanarak)
cat pet_shoplar.json | jq -r '.results[] | "İsim: \(.name), Adres: \(.vicinity)"'

# 3. Belirli bir pet shop'un detaylarını getir
PLACE_ID=$(cat pet_shoplar.json | jq -r '.results[2].place_id')  # Örneğin 3. sonuç
curl "http://localhost:8080/api/v1/places/details?placeId=$PLACE_ID" > pet_shop_detaylari.json

# 4. Çalışma saatlerini görüntüle
cat pet_shop_detaylari.json | jq -r '.result.opening_hours.weekday_text[]'
```

## Notlar ve İpuçları

1. **Türkçe Karakterler**: URL'lerde Türkçe karakterler sorun çıkarabilir. Bu nedenle, `encodeURIComponent` kullanarak parametreleri kodlamanız önerilir:

   ```javascript
   const city = encodeURIComponent("İstanbul");
   const district = encodeURIComponent("Kadıköy");
   ```

2. **Fotoğraf Boyutları**: Fotoğraf boyutlarını `maxWidth` ve `maxHeight` parametreleriyle kontrol edebilirsiniz. En az birini belirtmeniz gerekir.

3. **API Kullanım Limitleri**: Google Places API'nin kullanım limitleri vardır. Aşırı kullanım durumunda ek ücretler ödemeniz gerekebilir.

4. **Önbelleğe Alma**: Performansı artırmak için, sık kullanılan sorguların sonuçlarını ve fotoğrafları önbelleğe almayı düşünebilirsiniz.

5. **Hata İşleme**: API isteklerinde hata durumlarını ele almak için try-catch blokları kullanın ve kullanıcıya uygun hata mesajları gösterin.

6. **Radius Değeri**: Radius değeri metre cinsinden olup, maksimum 50000 (50 km) olabilir.

7. **İl ve İlçe İsimleri**: İl ve ilçe isimleri büyük/küçük harf duyarlı değildir, ancak doğru yazılması önemlidir (örn. "Istanbul" veya "istanbul").

8. **Fotoğraf Referansları**: Fotoğraf referansları geçicidir ve belirli bir süre sonra geçerliliğini yitirebilir. Bu nedenle, fotoğrafları uzun süre saklamak istiyorsanız, kendi sunucunuza indirmeniz önerilir.

9. **API Anahtarı Güvenliği**: API anahtarınızı güvende tutun ve istemci tarafında (frontend) açık bir şekilde kullanmaktan kaçının. Tüm API isteklerini backend üzerinden yapın.

10. **Ülke Bilgisi**: API isteklerinde Türkiye dışındaki konumlar için "Turkey" ülke bilgisi otomatik olarak eklenmektedir. Farklı ülkeler için servisi güncellemeniz gerekebilir. 