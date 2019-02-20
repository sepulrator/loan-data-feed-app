# Loan Data Feed Application

* Alpakka Kinesis Firehose Connector is used for publishing file to kinesis firehose
Alpakka uses aws kinesis sdk PutRecordsBatch api which has a 500 record limit per request.

* Before running the application aws credentials should be configured. Default values are placed on application.conf
    * AWS_ACCESS_KEY_ID = ????? //override or configure via aws cli
    * AWS_SECRET_ACCESS_KEY = ????? //override or configure via aws cli
    * AWS_REGION = us-west-2 // (optional)
    * LOAN_DATA_FILE_PATH = ????? // Loan data file path to be processed 
    * LOAN_DATA_DELIVERY_STREAM_NAME = "loan-data-stream" // Firehose delivery stream name where loan data will be published (optional)

#### Run
```bash
> sbt run
```



