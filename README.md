# Ordering-App

Curl to Place an Order
----------------------------------------------------------------------------------------------

curl --location --request POST 'http://localhost:9090/api/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "orderRequestSO": [
        {
            "customer": {
                "name": "Sidd Saxena",
                "email": "siddhartha.saxena70@gmail.com",
                "contactInfo": "7055888985"
            },
            "order": {
                "totalAmount": 200.00
            },
            "orderItems": [
                {
                    "name": "Item 1",
                    "quantity": 2,
                    "price": 50.00
                },
                {
                   "name": "Item 2",
                    "quantity": 11,
                    "price": 30.00
                }
            ]
        }
    ]
}'

--------------------------------------------------------------------------------------------------

Curl to get orderHistory

--------------------------------------------------------------------------------------------------

curl --location --request POST 'http://localhost:9090/api/orderHistory?email=siddhartha.saxena70@gmail.com' \
--header 'Content-Type: application/json'

---------------------------------------------------------------------------------------------------
