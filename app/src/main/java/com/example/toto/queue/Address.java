package com.example.toto.queue;

public enum Address {
    RABBITMQ_HOST_ADDRESS("ec2-35-177-99-160.eu-west-2.compute.amazonaws.com"),
    RABBITMQ_PORT("5672");

    private String text;

    private Address(String string){
        text=string;
    }

    @Override
    public String toString() {
        return text;
    }
}
