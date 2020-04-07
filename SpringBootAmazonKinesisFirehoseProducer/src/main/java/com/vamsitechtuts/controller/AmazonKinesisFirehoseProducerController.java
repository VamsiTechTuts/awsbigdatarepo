package com.vamsitechtuts.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;

@RestController
public class AmazonKinesisFirehoseProducerController {
    private static final Logger logger = LoggerFactory.getLogger(AmazonKinesisFirehoseProducerController.class);

    @Value("${aws.auth.accessKey}")
    private String awsAccessKey;

    @Value("${aws.auth.secretKey}")
    private String awsSecretKey;

    @Value("${aws.kinesis.firehose.deliveryStream.name}")
    private String fireHoseDeliveryStreamName;

    @PostMapping(value = "/produce")
    public ResponseEntity<String>  produce(@RequestBody String json) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        AmazonKinesisFirehose firehoseClient = AmazonKinesisFirehoseClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        logger.info("Message to Firehose: " + json.toString());

        PutRecordRequest putRecordRequest = new PutRecordRequest();
        putRecordRequest.setDeliveryStreamName(fireHoseDeliveryStreamName);
        Record record = new Record().withData(ByteBuffer.wrap(json.getBytes()));
        putRecordRequest.setRecord(record);
        PutRecordResult putRecordResult = firehoseClient.putRecord(putRecordRequest);
        logger.info("Message record ID: " + putRecordResult.getRecordId());
        return new ResponseEntity<String>(putRecordResult.getRecordId(), HttpStatus.OK);
    }
}
