# Curl Examples for API Endpoints

## Breed Controller

### Get All Breeds
```bash
curl -X GET http://localhost:8080/api/breeds
```

### Get Breed by ID
```bash
curl -X GET http://localhost:8080/api/breeds/1
```

### Get Breeds by Animal Type
```bash
curl -X GET http://localhost:8080/api/breeds/by-animal-type/DOG
```

### Get All Animal Types
```bash
curl -X GET http://localhost:8080/api/breeds/animal-types
```

### Create a New Breed
```bash
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Muhabbet Kuşu",
    "description": "Küçük, renkli ve sosyal bir kuş türü",
    "animalType": "BIRD"
  }'
```

### Update a Breed
```bash
curl -X PUT http://localhost:8080/api/breeds/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Muhabbet Kuşu",
    "description": "Küçük, renkli ve çok sosyal bir kuş türü",
    "animalType": "BIRD"
  }'
```

### Delete a Breed
```bash
curl -X DELETE http://localhost:8080/api/breeds/1
```

## Pet Controller

### Get All Pets
```bash
curl -X GET http://localhost:8080/api/pets
```

### Get Pet by ID
```bash
curl -X GET http://localhost:8080/api/pets/1
```

### Get Pets by Owner ID
```bash
curl -X GET http://localhost:8080/api/pets/owner/1
```

### Create a New Pet
```bash
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buddy",
    "age": 3,
    "gender": "Male",
    "animalType": "DOG",
    "ownerId": 1,
    "breedId": 1
  }'
```

### Update a Pet
```bash
curl -X PUT http://localhost:8080/api/pets/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Buddy",
    "age": 4,
    "gender": "Male",
    "animalType": "DOG",
    "ownerId": 1,
    "breedId": 1
  }'
```

### Delete a Pet
```bash
curl -X DELETE http://localhost:8080/api/pets/1
```

### Get All Animal Types
```bash
curl -X GET http://localhost:8080/api/pets/animal-types
```

## Example Workflow

1. Get all animal types:
```bash
curl -X GET http://localhost:8080/api/breeds/animal-types
```

2. Get all breeds for a specific animal type (e.g., DOG):
```bash
curl -X GET http://localhost:8080/api/breeds/by-animal-type/DOG
```

3. Create a new bird breed:
```bash
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Muhabbet Kuşu",
    "description": "Küçük, renkli ve sosyal bir kuş türü",
    "animalType": "BIRD"
  }'
```

4. Create a new bird breed:
```bash
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Kanarya",
    "description": "Sarı renkli, güzel ötüşlü bir kuş türü",
    "animalType": "BIRD"
  }'
```

5. Create a new bird breed:
```bash
curl -X POST http://localhost:8080/api/breeds \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Papağan",
    "description": "Konuşabilen, zeki ve renkli bir kuş türü",
    "animalType": "BIRD"
  }'
```

6. Get all bird breeds:
```bash
curl -X GET http://localhost:8080/api/breeds/by-animal-type/BIRD
```

7. Update a bird breed:
```bash
curl -X PUT http://localhost:8080/api/breeds/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Muhabbet Kuşu",
    "description": "Küçük, renkli ve çok sosyal bir kuş türü",
    "animalType": "BIRD"
  }'
```