# Loan Data Feed Application

* reads csv file and publish records of file to kinesis firehose. Note that: csv file header does not feed to firehose  

* Before running the application aws credentials should be configured. Default values are placed on application.conf
    * AWS_ACCESS_KEY_ID = ????? //override or configure via aws cli
    * AWS_SECRET_ACCESS_KEY = ????? //override or configure via aws cli
    * AWS_REGION = us-west-2 // (optional)
    * LOAN_DATA_FILE_PATH = ????? // Loan data file path to be processed 
    * LOAN_DATA_DELIVERY_STREAM_NAME = "loan-data-stream" // Firehose delivery stream name where loan data will be published (optional)
    * LOAN_DATA_DELIVERY_BATCH_SIZE = 500 // firehose client max number of records per batch request

#### Run
```bash
> sbt run
```



