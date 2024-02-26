# AdMarket API 1.0

The a backend API for the AdMarket site.

### Project stack:
- Spring Boot Web
- Spring Boot Data
- Spring Security
- MySQL
- MinIO
- Docker/Docker Compose

### Run application
```
docker compose up
```

## Mapping scheme

### Auth
```
POST /auth/register
Request {
  "username": "",
  "password": "",
  "passwordConfirm": "",
  "email": "",
  "phoneNumber": "",
  "firstName": "",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "additionalAddressInfo": ""
}
Response {
  "id": 1,
  "username": "",
  "email": "",
  "phoneNumber": "",
  "accountStatus": "noactive",
  "dateRegistered": "2023-12-21T20:14:46.496Z",
  "dateUpdated": "2023-12-21T20:14:46.496Z",
  "firstName": "",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "role": "user",
  "additionalAddressInfo": "",
  "images": [
    "http://.../image.jpg",
    "http://.../image.jpg"
  ]
}
```

```
POST /auth/register/admin
Request {
  "username": "",
  "password": "",
  "passwordConfirm": "",
  "email": "",
  "phoneNumber": "",
  "firstName": "",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "additionalAddressInfo": ""
}
Response {
  "id": 1,
  "username": "",
  "email": "",
  "phoneNumber": "",
  "accountStatus": "noactive",
  "dateRegistered": "2023-12-21T20:14:46.496Z",
  "dateUpdated": "2023-12-21T20:14:46.496Z",
  "firstName": "",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "role": "user",
  "additionalAddressInfo": "",
  "images": [
    "http://.../image.jpg",
    "http://.../image.jpg"
  ]
}
```

```
POST /auth/login
Request {
	"username": "",
	"password": ""
}
Response {
  "id": 0,
  "username": "",
  "accessToken": "",
  "refreshToken": ""
}
```

### User

```
GET /users
Response [
	{
		"id": 1,
		"username": "",
		"email": "",
		"phoneNumber": "",
		"accountStatus": "noactive",
		"dateRegistered": "2023-12-21T20:14:46.496Z",
		"dateUpdated": "2023-12-21T20:14:46.496Z",
		"firstName": "",
		"lastName": "",
		"surName": "",
		"country": "",
		"city": "",
		"street": "",
		"postalCode": "",
		"state": "",
		"role": "user",
		"additionalAddressInfo": "",
		"images": [
		  "http://.../image.jpg",
		  "http://.../image.jpg"
		]
	},
	...
]

GET /users/{id}
Response {
  "id": 1,
  "username": "",
  "email": "",
  "phoneNumber": "",
  "accountStatus": "noactive",
  "dateRegistered": "2023-12-21T20:14:46.496Z",
  "dateUpdated": "2023-12-21T20:14:46.496Z",
  "firstName": "",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "role": "user",
  "additionalAddressInfo": "",
  "images": [
    "http://.../image.jpg",
    "http://.../image.jpg"
  ]
}
```

```
PUT /users/{id}
Request {
	"firstName": "otherFirst",
	"email": "otherEmail"
}
Response {
  "id": 1,
  "username": "",
  "email": "otherEmail",
  "phoneNumber": "",
  "accountStatus": "noactive",
  "dateRegistered": "2023-12-21T20:14:46.496Z",
  "dateUpdated": "2023-12-21T20:14:46.496Z",
  "firstName": "otherFirst",
  "lastName": "",
  "surName": "",
  "country": "",
  "city": "",
  "street": "",
  "postalCode": "",
  "state": "",
  "role": "user",
  "additionalAddressInfo": "",
  "images": [
    "http://.../image.jpg",
    "http://.../image.jpg"
  ]
}
```

```
DELETE /users/{id}
Response "OK"
```

```
GET /users/{userId}/saved/ads
Response [
	{
	  "id": 1,
	  "title": "",
	  "category": "",
	  "price": 0,
	  "currencyCode": "",
	  "description": "",
	  "status": "active",
	  "itemCondition": "new",
	  "sellerUsername": "",
	  "viewsCount": 0,
	  "dateCreated": "2023-12-21T20:29:51.209Z",
	  "images": [
	    "http://.../image.jpg",
	    "http://.../image.jpg"
	  ]
	},
	...
]
```

```
POST /users/{userId}/saved/ads/{adId}
Response {
	"id": 1,
	"title": "",
	"category": "",
	"price": 0,
	"currencyCode": "",
	"description": "",
	"status": "active",
	"itemCondition": "new",
	"sellerUsername": "",
	"viewsCount": 0,
	"dateCreated": "2023-12-21T20:29:51.209Z",
	"images": [
	  "http://.../image.jpg",
	  "http://.../image.jpg"
	]
}
```

```
DELETE /users/{userId}/saved/ads/{adId}
Response "OK"
```

```
GET /users/{userId}/ads
Response [
	{
	  "id": 1,
	  "title": "",
	  "category": "",
	  "price": 0,
	  "currencyCode": "",
	  "description": "",
	  "status": "active",
	  "itemCondition": "new",
	  "sellerUsername": "",
	  "viewsCount": 0,
	  "dateCreated": "2023-12-21T20:29:51.209Z",
	  "images": [
	    "http://.../image.jpg",
	    "http://.../image.jpg"
	  ]
	},
	...
]
```

```
POST /users/{userId}/ads
Request {
	"title": "",
	"category": "",
	"price": 0,
	"currencyCode": "",
	"description": "",
	"itemCondition": "new",
	"sellerUsername": "",
	"images": [
	  "http://.../image.jpg",
	  "http://.../image.jpg"
	]
}
Response {
	"id": 1,
	"title": "",
	"category": "",
	"price": 0,
	"currencyCode": "",
	"description": "",
	"status": "active",
	"itemCondition": "new",
	"sellerUsername": "",
	"viewsCount": 0,
	"dateCreated": "2023-12-21T20:29:51.209Z",
	"images": [
	  "http://.../image.jpg",
	  "http://.../image.jpg"
	]
}
```

```
GET /users/{userId}/images
Response [
	"http://.../image.jpg",
	"http://.../image.jpg"
]
```

```
POST /users/{userId}/images
Request {
	"file": ""
}
Response "http://.../image.jpg"
```

```
DELETE /users/{userId}/images/{filename}
Response "http://.../image.jpg"
```

### Ad

```
GET /ads
Response [
	{
	  "id": 1,
	  "title": "",
	  "category": "",
	  "price": 0,
	  "currencyCode": "",
	  "description": "",
	  "status": "active",
	  "itemCondition": "new",
	  "sellerUsername": "",
	  "viewsCount": 0,
	  "dateCreated": "2023-12-21T20:29:51.209Z",
	  "images": [
	    "http://.../image.jpg",
	    "http://.../image.jpg"
	  ]
	},
	...
]
```

```
GET /ads/{adId}
Response {
	"id": 1,
	"title": "",
	"category": "",
	"price": 0,
	"currencyCode": "",
	"description": "",
	"status": "active",
	"itemCondition": "new",
	"sellerUsername": "",
	"viewsCount": 0,
	"dateCreated": "2023-12-21T20:29:51.209Z",
	"images": [
	  "http://.../image.jpg",
	  "http://.../image.jpg"
	]
}
```

```
PUT /ads/{adId}
Request {
	"title": "otherTitle"
}
Response {
	"id": 1,
	"title": "otherTitle",
	"category": "",
	"price": 0,
	"currencyCode": "",
	"description": "",
	"status": "active",
	"itemCondition": "new",
	"sellerUsername": "",
	"viewsCount": 0,
	"dateCreated": "2023-12-21T20:29:51.209Z",
	"images": [
	  "http://.../image.jpg",
	  "http://.../image.jpg"
	]
}
```

```
DELETE /ads/{adId}
Response "OK"
```

```
GET /ads/{adId}/images
Response [
	"http://.../image.jpg",
	...
]
```
```
POST /ads/{adId}/images
Request {
	"file": ""
}
Response "http://.../image.jpg"
```
```
DELETE /ads/{adId}/images/{filename}
Response "OK"
```

### Category

```
GET /categories/{categoryName}
Response {
  "name": "",
  "parent": "",
  "children": [
    "string"
  ]
}

Example
GET /categories/all
Response {
  "name": "all",
  "parent": null,
  "children": [
    {
  		"name": "other",
  		"parent": "all",
  		"children": []
	},
	...
  ]
}
```
```
POST /categories
Request {
	"name": "",
	"parent": ""
}
Response {
  "name": "",
  "parent": "",
  "children": []
}

Examples
POST /categories
Request {
	"name": "category1"
}
Response {
  "name": "category1",
  "parent": "all",
  "children": []
}
```
```
POST /categories
Request {
	"name": "category1_1",
	"parent": "category1"
}
Response {
  "name": "category1_1",
  "parent": "category1",
  "children": []
}
```

```
PUT /categories/{categoryName}
Request {
	"name": "",
	"parent": ""
}
Response {
  "name": "",
  "parent": "",
  "children": []
}
```
```
DELETE /categories/{categoryName}
Response "OK"
```
