package com.zxo.lib.juc;

public class ProsumerTest {

  public static void main(String[] args) {
    Prosumer prosumer = new Prosumer();
    Producer producer = new Producer(prosumer);
    Customer customer = new Customer(prosumer);

    new Thread(producer).start();
    new Thread(customer).start();
  }
}
