# Introduction

This project aims to demonstrate how to implement ETL (Extract, Transform and Load) using Spring Batch. The purpose of this ETL job is about generating a monthly report which 
reveals how many items of each product have been sold in an E-commerce platform.


## Tech stack

Docker Compose, MySQL, JDK 11, Gradle, Spring boot, Spring Batch, Flyway

## Project structure
    .
    ├── docker                    
    ├──── init                    # contain initial SQL script to create databases in MySQL container
    ├──── docker-compose.yml      # build up MySQL containers and initiate needed databases
    ├── src                       
    ├──── main                    
    ├────── java                  # contain all source code including repositories, ETL services and REST endpoints
    ├────── resources             
    └──────── db.migration        # flyway scripts to initiate MySQL tables

## How to get started?
We need to build up MySQL and needed databases (**nht_transaction** and **nht_warehouse**) using docker-compose:
```shell
cd docker
docker-compose up
```
Then you can start the application via:
```shell
gradle build
gradle bootRun
```
The Spring Boot application will start and run flyway scripts to create MySQL tables
and sample data:
1. **nht_transaction.product**: store product data with 18 sample records
2. **nht_transaction.transaction**: store transaction of products with 1000 sample records
3. **nht_warehouse.fact_transaction_product_monthly**: store total quantity of each product in monthly
which will be used to generate the report.

## ETL process
We use a Rest API to trigger the ETL process for a specific month for example:
```shell
curl --location --request POST 'localhost:8080/etl?year=2022&month=4'
```
The ETL process requires the **year** and **month** parameters inputted from the above Rest API and includes three steps: extract, transform, load that are implemented as a corresponding service
in src.main.service including: 
1. **TransactionProductMonthlyExtractor**: extract all products 
2. **TransactionProductMonthlyTransformer**: retrieve total quantity of each project in a month
3. **TransactionProductMonthlyLoader**: persists total quantity of each product into fact_transaction_product_monthly table
