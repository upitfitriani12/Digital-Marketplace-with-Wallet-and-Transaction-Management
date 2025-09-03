# FitMart API Documentation
### Base URL : http://localhost:8080

## ADMIN
### 1. Register Admin
- Endpoint: POST /admins/register
- Content-Type: application/json
- Request Body:
```json
{
  "name": "admin",
  "email": "admin@gmail.com",
  "password": "admin"
}
```
- Response (201 Created):
```json
{
  "data": {
    "id": "90345749-73c4-409c-94b1-4c6c68f9efff",
    "email": "admin@gmail.com"
  },
  "message": "Register Admin created successfully"
}
```

### 2. Login Admin
- Endpoint: POST /admins/login
- Content-Type: application/json
- Request Body:
```json
{
    "email": "admin@gmail.com",
    "password": "admin"
}
```
- Response (200 OK):
```json
{
    "accessToken": "token"
}
```

### 3. Get Admin By ID
- Endpoint: POST /admins/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": {
    "id": "33e2b615-3a99-4f28-a960-54275959d005",
    "name": "admin",
    "email": "admin@gmail.com",
    "password": "$2a$10$MgBE6MthAzcoIMfA3vOlg.lw8UJLQwYtKpBo2XRxlIItO1iGnDbmy"
  },
  "message": "Admin ID Retrieved Successfully"
}
```
- Error Response (no token): 500 Internal Server Error
```json
{
    "data": null,
    "message": "An error occurred: Required request header 'Authorization' for method parameter type String is not present"
}
```

### 4. Get All Admin
- Endpoint: POST /admins
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": {
    "content": [
      {
        "id": "4db0182c-aac3-4f89-b82b-3e49f7a3865f",
        "name": "admin3",
        "email": "admin3@gmail.com",
        "password": "$2a$10$WK0ELYzP0tybGDGN9k5tcugG1D6Jg1dejLc5i9Q14SdrUFF5E.F4G"
      },
      {
        "id": "90345749-73c4-409c-94b1-4c6c68f9efff",
        "name": "admin",
        "email": "admin@gmail.com",
        "password": "$2a$10$tQBIgdulnmBX3BfU6HflEOZfK9U7ziKNdZzzAaMexcMd66i1/idA2"
      },
      {
        "id": "c35c30db-483b-4c5a-ae4c-5c306829d53c",
        "name": "admin2",
        "email": "admin2@gmail.com",
        "password": "$2a$10$Iqemw3JHYdvjFzeTOzllgO./cDVG.Hmf4V/qT4DhufddYhmBm7h.."
      }
    ],
    "total_elements": 3,
    "total_pages": 1,
    "page": 0,
    "size": 10
  },
  "message": "ok"
}
```
- Response Error (no token): 500 Internal Server Error
```json
{
    "data": null,
    "message": "An error occurred: Required request header 'Authorization' for method parameter type String is not present"
}
```

### 5. Update Admin
- Endpoint: PUT /admins/update
- Headers: Authorization: Bearer <admin_token>
- Content-Type: application/json
- Request Body:
```json
{
  "id" : "90345749-73c4-409c-94b1-4c6c68f9efff",
  "name": "admin1",
  "email": "admin1@gmail.com",
  "password": "admin1"
}
```
- Response (200 OK):
```json
{
  "id": "90345749-73c4-409c-94b1-4c6c68f9efff",
  "email": "admin1@gmail.com"
}
```
- Response Error (no id) : 403 Forbidden
```json
{
  "message": "Failed to Find"
}
```

### 6. Delete Admin
- Endpoint: DELETE /admins/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
    "data": null,
    "message": "Admin deleted successfully"
}
```
- Response Error (wrong id) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 11eb5814-0d14-480f-9c59-1b711afda773 is not found"
}
```
### 7. Get User by ID (Admin Only)
- Endpoint: GET /admins/users/{id}
- Headers: Authorization: Bearer <admin_token>
- Response: 200 OK
```json
{
  "data": {
    "id": "09163f79-be22-4e59-9b63-1e7fd22aabb4",
    "email": "user@gmail.com"
  },
  "message": "User ID Retrieved Successfully"
}
```
- Response Error (no token): 500 Internal Server Error
```json
{
    "data": null,
    "message": "An error occurred: Required request header 'Authorization' for method parameter type String is not present"
}
```

### 8. Get All User (Admin Only)
- Endpoint: GET /admins/update/admins/users
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": {
    "content": [
      {
        "id": "1030d6cb-0490-40ec-8e19-51f455b69e8a",
        "email": "user4@gmail.com"
      },
      {
        "id": "51c9259f-1781-4169-b25e-6a445bcbb3c4",
        "email": "user5@gmail.com"
      },
      {
        "id": "7a5dcdaa-0381-41d2-bfa8-3823eb0b58b5",
        "email": "user3@gmail.com"
      },
      {
        "id": "7ad815c8-bfbd-4a77-82d8-7bf398bc6d7c",
        "email": "user2@gmail.com"
      },
      {
        "id": "c527c73a-3013-41f4-bdb8-61a14cdf8e3f",
        "email": "user1@gmail.com"
      }
    ],
    "total_elements": 5,
    "total_pages": 1,
    "page": 0,
    "size": 10
  },
  "message": "ok"
}
```

## USER
### 1. Register User
- Endpoint: POST /users/register
- Content-Type: application/json
- Request Body:
```json
{
    "name": "user",
    "email": "user@gmail.com",
    "password": "user"
}
```
- Response (201 Created):
```json
{
  "data": {
    "id": "c527c73a-3013-41f4-bdb8-61a14cdf8e3f",
    "email": "user@gmail.com"
  },
  "message": "Register User Created Successfully"
}
```
- Response Error (Json request body with empty fields) : 400 Bad Request
```json
{
  "data": {
    "password": "cannot be blank",
    "name": "cannot be blank",
    "email": "Please provide a valid email"
  },
  "message": "Validation Errors"
}
```

### 2. Login User
- Endpoint: POST /users/login
- Content-Type: application/json
- Request Body:
```json
{
    "email": "user@gmail.com",
    "password": "user"
}
```
- Response (200 OK):
```json
{
    "accessToken": "token"
}
```

### 3. Get By Id User
- Endpoint: GET /users/{id}
- Content-Type: application/json
- Headers: Authorization: Bearer <user_token>
- Response (200 OK):
```json
{
  "data": {
    "id": "19538218-f1de-4658-b9ec-da1790e5cff9",
    "email": "user2@gmail.com"
  },
  "message": "User ID Retrieved Successfully"
}
```
- Response Error (no token): 500 Internal Server Error
```json
{
    "data": null,
    "message": "An error occurred: Required request header 'Authorization' for method parameter type String is not present"
}
```
- Response Error (token does not match the requested user): 403 Forbiden
```json
{
    "message": "Access denied"
}
```

### 4. Update User
- Endpoint: PUT /users/update
- Content-Type: application/json
- Headers: Authorization: Bearer <user_token>
- Request Body:
```json
{
  "id": "c527c73a-3013-41f4-bdb8-61a14cdf8e3f",
  "name":"user1",
  "email": "user1@gmail.com",
  "password": "user1"
}
```
- Response (200 OK):
```json
{
  "id": "c527c73a-3013-41f4-bdb8-61a14cdf8e3f",
  "email": "user1@gmail.com"
}
```

### 4. Delete User
- Endpoint: DELETE /users/{id}
- Headers: Authorization: Bearer <user_token>
- Response (200 OK):
```json
{
  "data": null,
  "message": "User deleted successfully"
}
```
- Response Error (token does not match the requested user): 403 Forbidden
```json
{
    "message": "Forbidden: you can only delete your own account."
}
```

## CATEGORY
### 1. Create Category
- Endpoint: POST /categories
- Content-Type: application/json
- Headers: Authorization: Bearer <admin_token>
- Request Body:
```json
{
  "name": "Electronics"
}
```
- Response (201 Created):
```json
{
  "data": {
    "id": "78567dfe-b4a5-475b-b933-3191fe804e5b",
    "name": "Electronics"
  },
  "message": "Category created successfully"
}
```
- Response Error (token using user) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```

### 2. Get All Category
- Endpoint: GET /categories
- Headers: Authorization: Bearer <admin_token or user_token>
- Response (200 OK):
```json
{
  "data": {
    "content": [
      {
        "id": "1233c35f-d508-4352-ade2-a42f4c442825",
        "name": "Medicines"
      },
      {
        "id": "24618919-3b49-4a04-aa95-2c7d4689bd89",
        "name": "Beverages"
      },
      {
        "id": "25763117-0138-407c-96b9-06ae7113a50e",
        "name": "Skincare & Cosmetics"
      },
      {
        "id": "3b603322-1f62-4e38-8c1d-46ef3c5c966a",
        "name": "Frozen Food"
      },
      {
        "id": "693181cd-4730-41fe-8a21-4ae3d65603b8",
        "name": "Footwear"
      },
      {
        "id": "73f6eed5-07b4-4b5c-88f2-af01f1f4c19a",
        "name": "Snacks"
      },
      {
        "id": "78567dfe-b4a5-475b-b933-3191fe804e5b",
        "name": "Electronics"
      },
      {
        "id": "8473da54-63e4-4449-9092-a9c8e5adf2d2",
        "name": "Fruits"
      },
      {
        "id": "8b1897bb-ed20-4f4a-9ef4-01fdee446a24",
        "name": "Vegetables"
      },
      {
        "id": "a31e7e5d-0bff-4d26-80bb-23c069d735ee",
        "name": "Meat"
      }
    ],
    "total_elements": 11,
    "total_pages": 2,
    "page": 0,
    "size": 10
  },
  "message": "Categories retrieved successfully"
}
```

### 3. Get By ID Category
- Endpoint: GET /categories/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": {
    "id": "3b603322-1f62-4e38-8c1d-46ef3c5c966a",
    "name": "Frozen Food"
  },
  "message": "Category retrieved successfully"
}
```
- Response Error (ID wrong) : 404 Not Found
```json
{
    "data": null,
    "message": "404 Category with id 2bdad025-62f1-43e2-b83d-f75655427 is not found"
}
```

### 4. Update Category
- Endpoint: PUT /categories
- Content-Type: application/json
- Headers: Authorization: Bearer <admin_token>
- Request Body:
```json
{
  "id": "3b603322-1f62-4e38-8c1d-46ef3c5c966a",
  "name": "Seafood"
}
```
- Response (200 OK):
```json
{
  "id": "3b603322-1f62-4e38-8c1d-46ef3c5c966a",
  "name": "Seafood"
}
```
- Response Error (token using user) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```
- Response Error (wrong id) : 500 Internal Server Error
```json
{
  "data": null,
  "message": "An error occurred: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [com.fitmart.app.entity.Category#df0c2bf6-c617-46a1-8574-6706132jkjk50]"
}
```
### 5. Delete Category
- Endpoint: DELETE /categories/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": null,
  "message": "Category deleted successfully"
}
```
- Response Error (token using user) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```
- Response Error (id wrong) : 500 Internal Server Error
```json
{
  "data": null,
  "message": "Failed to delete category"
}
```


## PRODUCT
### 1. Create Product
- Endpoint: POST /products
- Content-Type: application/json
- Headers: Authorization: Bearer <admin_token>
- Request Body:
```json
{
  "name": "Smartphone (Xiaomi)",
  "price": 2500000,
  "categoryIds": ["78567dfe-b4a5-475b-b933-3191fe804e5b"]
}
```
- Response (200 OK):
```json
{
  "data": {
    "id": "b59ad61a-bcef-49e5-8715-113f5990c7a0",
    "name": "Smartphone (Xiaomi)",
    "price": 2500000,
    "categories": [
      {
        "id": "78567dfe-b4a5-475b-b933-3191fe804e5b",
        "name": "Electronics"
      }
    ]
  },
  "message": "Product created successfully"
}
```
- Response Error (token using user) : 404 Not Found
```json
{
    "data": null,
    "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```

### 2. Get All Product 
- Endpoint: GET /products
- Headers: Authorization: Bearer <admin_token or user_token>
- Response (200 OK):
```json
{
    "data": {
        "content": [
            {
                "id": "0e9dbd8b-f61d-43de-80ea-b9af07aa44a8",
                "name": "Fitmart brand facial cleanser",
                "price": 30000,
                "categories": [
                    {
                        "id": "25763117-0138-407c-96b9-06ae7113a50e",
                        "name": "Skincare & Cosmetics"
                    }
                ]
            },
            {
                "id": "3e8fecda-5659-4a67-a3d2-823549ac1a72",
                "name": "Detergent (1kg)",
                "price": 25000,
                "categories": [
                    {
                        "id": "e97a0dfa-0f23-40b4-82ec-7a4ee3ae5acc",
                        "name": "Cleaning Supplies"
                    }
                ]
            },
            {
                "id": "5fa15fa8-f9b6-4c2e-97c0-8ef2709fdf00",
                "name": "Powerbank 20.000mAh",
                "price": 350000,
                "categories": [
                    {
                        "id": "78567dfe-b4a5-475b-b933-3191fe804e5b",
                        "name": "Electronics"
                    }
                ]
            },
            {
                "id": "6b37c1e6-2f1a-47ee-a7a8-004b0beace5c",
                "name": "Casual sandals brand FitMart",
                "price": 150000,
                "categories": [
                    {
                        "id": "693181cd-4730-41fe-8a21-4ae3d65603b8",
                        "name": "Footwear"
                    }
                ]
            },
            {
                "id": "78879374-9442-4327-98ce-a872732a90ed",
                "name": "FitMart Branded shoes",
                "price": 950000,
                "categories": [
                    {
                        "id": "693181cd-4730-41fe-8a21-4ae3d65603b8",
                        "name": "Footwear"
                    }
                ]
            },
            {
                "id": "a0952adf-c7ad-434e-b394-3c81229a04d8",
                "name": "Beef (1 kg)",
                "price": 120000,
                "categories": [
                    {
                        "id": "a31e7e5d-0bff-4d26-80bb-23c069d735ee",
                        "name": "Meat"
                    }
                ]
            },
            {
                "id": "b5034e85-64bf-42f6-97dc-97694a2fa65a",
                "name": "Chicken Nuggets",
                "price": 55000,
                "categories": [
                    {
                        "id": "3b603322-1f62-4e38-8c1d-46ef3c5c966a",
                        "name": "Seafood"
                    }
                ]
            },
            {
                "id": "b59ad61a-bcef-49e5-8715-113f5990c7a0",
                "name": "Smartphone (Xiaomi)",
                "price": 2500000,
                "categories": [
                    {
                        "id": "78567dfe-b4a5-475b-b933-3191fe804e5b",
                        "name": "Electronics"
                    }
                ]
            },
            {
                "id": "b8dd50eb-46b5-4ce2-a695-f715fcd71cb0",
                "name": "Broccoli (1 kg)",
                "price": 35000,
                "categories": [
                    {
                        "id": "8b1897bb-ed20-4f4a-9ef4-01fdee446a24",
                        "name": "Vegetables"
                    }
                ]
            },
            {
                "id": "b984e598-3b3f-4db2-9e89-25b29b5c1f59",
                "name": "Mineral Water (1L)",
                "price": 5000,
                "categories": [
                    {
                        "id": "24618919-3b49-4a04-aa95-2c7d4689bd89",
                        "name": "Beverages"
                    }
                ]
            }
        ],
        "total_elements": 12,
        "total_pages": 2,
        "page": 0,
        "size": 10
    },
    "message": "ok"
}
```

### 3. Get Product By ID 
- Endpoint: GET /products/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
  "data": {
    "id": "78879374-9442-4327-98ce-a872732a90ed",
    "name": "FitMart Branded shoes",
    "price": 950000,
    "categories": [
      {
        "id": "693181cd-4730-41fe-8a21-4ae3d65603b8",
        "name": "Footwear"
      }
    ]
  },
  "message": "product ID Retrieved Successfully"
}
```

### 4. Update Product
- Endpoint: PUT /products/{id}
- Headers: Authorization: Bearer <admin_token>
- Request Body:
```json
{
  "name": "Casual sandals brand FitMart",
  "price": 350000
}
```
- Response (200 OK):
```json
{
  "id": "6b37c1e6-2f1a-47ee-a7a8-004b0beace5c",
  "name": "Casual sandals brand FitMart",
  "price": 350000,
  "categories": [
    {
      "id": "693181cd-4730-41fe-8a21-4ae3d65603b8",
      "name": "Footwear"
    }
  ]
}
```
- Response Error (token using user) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```

### 5. Delete Product
- Endpoint: DELETE /products/{id}
- Headers: Authorization: Bearer <admin_token>
- Response (200 OK):
```json
{
    "data": null,
    "message": "Product Deleted Successfully"
}
```
- Response Error (token using user) : 404 Not Found
```json
{
  "data": null,
  "message": "404 Admin with id 09163f79-be22-4e59-9b63-1e7fd22aabb4 is not found"
}
```
- Response Error (id wrong) : 500 Internal Server Error
```json
{
    "data": null,
    "message": "Failed to Delete Product"
}
```


## TRANSACTION