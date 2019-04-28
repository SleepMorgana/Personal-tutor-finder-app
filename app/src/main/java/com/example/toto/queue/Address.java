package com.example.toto.queue;

public enum Address {
    RABBITMQ_HOST_ADDRESS("ec2-34-240-40-205.eu-west-1.compute.amazonaws.com"),
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
