## Run

### Prerequisites

To run the whole application, a docker machine with at least 6500 MB of RAM is needed.
It is also recommended to set at least 4 GB of RAM to the docker deamon.

### Installing

To run the application, first clone git repository and `cd` into the docker folder inside the project and finally run `docker-compose`:

```
git clone https://gitlab.com/bcdl/jobportal.git

cd jobportal/docker

docker-compose build

docker-compose up

```

### Executing
As the services come up and start running, they register to Eureka Registry. To check if all the services are ready, navigate to this [address](http://localhost:8761).

When all the services are registered to Eureka Registry, the application is ready to be used at this [address](http://localhost:80).

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/) 
* [Flask](https://flask.pocoo.org/)
* [Postgres](https://www.postgresql.org/)

## Authors

* **Andrea Biaggi** - 794873
* **Matteo Coppola** - 793329
* **Lorenzo Di Vito** - 793128
* **Luca Lorusso** - 797805
