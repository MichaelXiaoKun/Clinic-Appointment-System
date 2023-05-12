# Clinic-Appointment-System
This is the prototype for showing how Clinic Appointment System performs with the help of Java JDBC and Spring Boot Framework 

## How to Run:

### Option 1
• Download and open **Docker Desktop** in PC/Mac/Linux (link->https://www.docker.com/products/docker-desktop/)
• Run the following commands in terminal:
  ```console
  # clinic-appointment-system -> name of jar file generated
  docker build -t clinic-appointment-system:3.0.5 .
  # container_name can be any name preferred
  docker run -d -p 8080:8080 --name <container_name> clinic-appointment-system:3.0.5
  ```
• If container name already exists in Docker, run the following command before the above commands are executed:
  ```console
  # stop running docker container <container_name>
  docker stop <container_name>
  # remove docker container <container_name>
  docker rm <container_name>
  ```
### Option 2
• Again, Download and open **Docker Desktop** in PC/Mac/Linux (link->https://www.docker.com/products/docker-desktop/) \
• Run the bash file named "execute.sh" \

## Testing
I strongly recommend testing the RESTful APIs using **Postman**, which can be downloaded under https://www.postman.com/downloads/.
Once the app is downloaded, open **Postman** and you should see a whole screen similar as below (for more details about how to use **Postman**, please view the document from https://learning.postman.com/docs/introduction/overview/): \
![image info](./postman_overview.png)

## APIs
### Account Management
| Method | Endpoint                                | Description                                         | Parameters                                 | Success Response           |
|--------|-----------------------------------------|-----------------------------------------------------|--------------------------------------------|----------------------------|
| GET    | /api/account/adminView/greetings        | A greeting message endpoint.                        | None                                       | 200: Greeting message       |
| GET    | /api/account/adminView/fullName         | Fetches accounts by first and last name.            | firstName=[string]<br>lastName=[string]    | 200: List of AccountEntity  |
| GET    | /api/account/adminView/all              | Fetches all accounts.                               | None                                       | 200: List of AccountEntity  |
| GET    | /api/account/adminView/email            | Fetches accounts by email.                          | email=[string]                             | 200: List of AccountEntity  |
| GET    | /api/account/adminView/type             | Fetches accounts by account type.                   | type=[string]                              | 200: List of AccountEntity  |
| GET    | /api/account/adminView/count            | Fetches the total number of accounts.               | None                                       | 200: Total account count    |

### Account Authentication
| Method | Endpoint            | Description               | Parameters                          | Success Response                 |
|--------|---------------------|---------------------------|-------------------------------------|----------------------------------|
| POST   | /api/login          | Authenticates a user.     | AuthenticationRequest object        | 201: AuthenticationResponse object |
| POST   | /api/register       | Registers a new user.     | RegisterRequest object              | 201: AuthenticationResponse object |
| POST   | /api/forgetPassword | Resets a user's password. | PasswordResetRequest object         | 201: AuthenticationResponse object |

### Doctor:
| HTTP Verb | API Endpoint                                             | Description                                                           | Request Parameters                                | Response Status |
|-----------|----------------------------------------------------------|-----------------------------------------------------------------------|---------------------------------------------------|-----------------|
| GET       | /api/account/doctor/allView/all                          | Fetches all doctor profiles.                                          | None                                              | 200 OK          |
| GET       | /api/account/doctor/allView/username={username}          | Fetches a doctor profile by the provided username.                    | username                                          | 200 OK          |
| GET       | /api/account/doctor/allView/firstname={firstName}        | Fetches doctor profiles by the provided first name.                    | firstName                                         | 200 OK          |
| GET       | /api/account/doctor/allView/lastname={lastName}          | Fetches doctor profiles by the provided last name.                     | lastName                                          | 200 OK          |
| GET       | /api/account/doctor/allView/firstname={firstName}_lastname={lastName} | Fetches doctor profiles by the provided first name and last name. | firstName, lastName                              | 200 OK          |
| GET       | /api/account/doctor/allView/specialty={specialty}        | Fetches doctor profiles by the provided specialty.                     | specialty                                         | 200 OK          |
| GET       | /api/account/doctor/allView/degree={degree}              | Fetches doctor profiles by the provided degree.                        | degree                                            | 200 OK          |
| GET       | /api/account/doctor/doctorView/myProfile                 | Fetches the profile of the currently logged-in doctor.                 | Authorization (JWT in header)                     | 200 OK          |
| GET       | /api/account/doctor/doctorView/myBreaks                  | Fetches the breaks of the currently logged-in doctor.                  | Authorization (JWT in header)                     | 200 OK          |
| GET       | /api/account/doctor/doctorView/findBreaks                | Fetches the breaks of the currently logged-in doctor within a specified time range. | Authorization (JWT in header), start, end | 200 OK          |
| POST      | /api/account/doctor/doctorView/resetpassword             | Changes the password of the currently logged-in doctor.                | Authorization (JWT in header), password           | 204 NO CONTENT   |
| DELETE    | /api/account/doctor/doctorView/deleteMyProfile           | Deletes the account of the currently logged-in doctor.                 | Authorization (JWT in header), password           | 204 NO CONTENT   |
| PUT       | /api/account/doctor/doctorView/updateMyProfile           | Updates the profile of the currently logged-in doctor.                 | username, first_name, last_name, phone_number, date_of_birth, middle_name, email, specialty, degree, license_number, boardCertification | 202 ACCEPTED    |
| POST      | /api/account/doctor/doctorView/addBreaks                 | Adds a break to the schedule of the currently logged-in doctor.       | Authorization (JWT in header), start, end         | 202 ACCEPTED     |
| POST      | /api/account/doctor/doctorView/cancelBreaks              | Cancels a break in the schedule of the currently logged-in doctor.    | Authorization (JWT in header), start, end         | 202 ACCEPTED     |
| GET       | /api/account/doctor/allView/licenseNumber                | Fetches doctor profiles by the provided license number.                | licenseNumber                                    | 200 OK          |
| GET       | /api/account/doctor/allView/boardCertification           | Fetches doctor profiles by the provided board certification.           | boardCertification                               | 200

### Patient
| HTTP Method | API Endpoint                                                       | Request Body                                                                                                  | Description                                                                                                                  |
|-------------|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| GET         | /api/account/patient/doctorView/all                                |                                                                                                              | Retrieves all patient profiles available in the system. Only accessible to doctors.                                       |
| GET         | /api/account/patient/doctorView/username={username}                |                                                                                                              | Retrieves the profile of the patient with the provided username. Only accessible to doctors.                             |
| GET         | /api/account/patient/doctorView/firstName={firstName}              |                                                                                                              | Retrieves the profiles of all patients with the provided first name. Only accessible to doctors.                          |
| GET         | /api/account/patient/doctorView/lastName={lastName}                |                                                                                                              | Retrieves the profiles of all patients with the provided last name. Only accessible to doctors.                           |
| GET         | /api/account/patient/doctorView/firstName={firstName}_lastName={lastName} |                                                                                                      | Retrieves the profiles of all patients with the provided first and last names. Only accessible to doctors.                |
| GET         | /api/account/patient/patientView/myProfile                         |                                                                                                              | Retrieves the profile of the patient making the request. Only accessible to the patient whose profile is being retrieved. |
| POST        | /api/account/patient/patientView/resetpassword                     | password                                                                                                     | Allows a patient to reset their password. Only accessible to the patient whose password is being reset.                    |
| DELETE      | /api/account/patient/patientView/myProfile                         |                                                                                                              | Allows a patient to delete their own profile. Only accessible to the patient whose profile is being deleted.              |
| PUT         | /api/account/patient/patientView/myProfile                         | firstName, lastName, phoneNumber, date_of_birth, middleName, email, insuranceProvider, policyNumber, groupNumber, balance, insurancePhone, emergencyFirstName, emergencyLastName, emergencyPhoneNumber | Allows a patient to update their own profile. Only accessible to the patient whose profile is being updated.              |
| GET         | /api/account/patient/doctorView/email                              |                                                                                                              | Retrieves the profiles of all patients with the provided email. Only accessible to doctors.                               |
| GET         | /api/account/patient/doctorView/count                              |                                                                                                              | Retrieves the total count of patients. Only accessible to doctors.                                                          |

### Appointment
| HTTP Method | Endpoint                                                         | Description                                                                                          | Authorization                       | Request Body                                         |
|-------------|------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|-------------------------------------|------------------------------------------------------|
| POST        | /api/account/appointment/patient/patientView/make                 | Schedule an appointment for logged-in patients                                                      | JWT token for a patient account      | appointmentTitle, doctorName, description, startTime, endTime |
| DELETE      | /api/account/appointment/patient/patientView/cancel               | Cancel an existing appointment for logged-in patients                                               | JWT token for a patient account      | N/A                                                  |
| GET         | /api/account/appointment/allView/myAppointments                  | Get all appointments for the logged-in user (either patient or doctor)                              | JWT token for a patient or a doctor account | N/A                                                  |
| GET         | /api/account/appointment/doctor/doctorView/{apptID}              | Get specific appointment details by appointment ID                                                  | JWT token for a doctor account        | N/A                                                  |
| GET         | /api/account/appointment/doctor/doctorView/{doctor_username}     | Get all appointments associated with a specific doctor's username                                   | JWT token for a doctor account        | N/A                                                  |
| GET         | /api/account/appointment/patient/doctorView/{patient_username}   | Get all appointments associated with a specific patient's username                                  | JWT token for a patient account       | N/A                                                  |
| GET         | /api/account/appointment/allView/{start_time}_{end_time}         | Get appointments for the logged-in user (either patient or doctor) between a specific start and end time | JWT token for a patient or a doctor account | N/A                                                  |
| PUT         | /api/account/appointment/patient/patientView/update              | Update an existing appointment for logged-in patients                                               | JWT token for a patient account      | appointmentId, appointmentTitle, doctorName, description, startTime, endTime |
| GET         | /api/account/appointment/doctor/doctorView/date/{date}           | Get appointments for a specific date                                                                 | JWT token for a doctor account        | N/A                                                  |
| GET         | /api/account/appointment/doctor/doctorView/count                 | Get total number of appointments for the logged-in doctor                                           | JWT token for a doctor account        | N/A                                                  |
