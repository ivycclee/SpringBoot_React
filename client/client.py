import requests
import json

api_endpoint = "http://localhost:8080/petopia/"

# option 1 for get customer by ID - GET req
# option 2 for add new customer - POST req

option = input("Enter 1 to trigger GET request (get customer by ID): \nEnter 2 to trigger POST request (Add customer to db): \nOption: ")

if option == '1':
    custId = input("Enter customerId for the customer you are searching: ")
    
    # would recommend id 132 as it gives a good result

    api_endpoint = api_endpoint + custId

    response = requests.get(api_endpoint)

    if response.status_code == 404:
        print("No customer found with this ID")

    else:
        formatted = json.dumps(response.json(), indent=2)

        print(formatted)

elif option == '2':
    cust = {
        "customerId": 155,
        "firstName": "Ivy",
        "lastName": "Lee",
        "email": "il@gmail.com",
        "phone": "061 123456",
        "address": "LIT Moylish",
        "city": "Limerick",
        "country": "Ireland",
        "postcode": "V94 XXXX"
    }

    api_endpoint = api_endpoint + "add"

    print(api_endpoint)

    response = requests.post(api_endpoint, data=json.dumps(cust), headers={'Content-Type': 'application/json'})

    print("status code = ", response.status_code)