package com.zxo.lib.pattern.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
  public static void main(String[] args) {
    RentPerson renter = new RentPerson();

    Person person = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理过程中插入其他操作
        System.out.println("租客和中介交流");
        return method.invoke(proxy, args);
      }
    });

    person.rentHouse();
  }
}
