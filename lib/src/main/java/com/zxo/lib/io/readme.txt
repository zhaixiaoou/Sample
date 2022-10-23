
1、 父类实现serializable接口， 子类没有实现, 那么序列化情况如何？
可以实现序列化，因为继承关系，子类也能正常实现序列化

2、父类没有实现serializable接口， 子类实现了，那么序列化情况？
  1】如果没有实现serializeable接口的父类 没有无参的构造函数，则会报错 java.io.InvalidClassException no valid constructor
  2】父类有无参的构造函数，则可以实现序列化，但是父类的数据为默认空值

3、类的成员变量中有没有实现serializable接口的，在序列化过程中？
  会报错 java.io.NotSerializableException
  类中成员变量都需要实现serializable接口 该类才能实现序列化


序列化流程
  ObjectOutputStream
  writeObject
    -- writeObject0
       // 创建ObjectStreamClass对象，读取object类信息，检测对象中是否存在 writeObject、readObject、readObjectNoData、writeReplace、readResolve 等方法
       -- ObjectStreamClass.lookup
       // 读取对象数据转换为字节
       -- writeOrdinaryObject
          // 写类描述信息
          -- writeClassDesc
          // 写类对象数据 在此方法里面 判断类是否有writeObject方法，如果有则执行该方法实现的写入方法。
          // 对于transient修饰的变量和静态变量 不参与序列化的规则都将失效，完全交由用户控制
          1、-- writeSerialData
            1、invokeWriteObject // 执行自定义writeObject方法
            2、defaultWriteFields // 默认执行系统策略
          // 如果实现了 Externalizable 接口了 则执行此方法 自定义序列化字段
          2、-- writeExternalData
            // 直接调用obj实现的接口方法
            -- writeExternal

  ObjectInputStream
  readObject
    -- readObject0
      -- readOrdinaryObject
        // 读取类描述信息
        // 创建ObjectStreamClass对象，读取suid、是否有readObject\writeObject等方法
        -- readClassDesc
          -- readNonProxyDesc
            // 获取到类描述信息后，通过反射的得到class对象
            -- readClassDescriptor
        // 创建对象 该方法的注释
        // 如果实现了Extrnalizable接口，调用它的公共无参数构造函数
        // 如果实现了Serializable接口，
        // 1、调用第一个非可序列化超类的无参数构造函数
        // 2、如果这个类描述符与一个类没有关联，如果关联的类是不可序列化的，或者如果适当的无参数构造函数是不可访问/不可用的，则抛出UnsupportedOperationException。
        -- ObjectStreamClass.newInstance()
        // 读取字节数据向对象赋值
        // 如果有readObject对象，直接调用
        -- readSerialData
        // 实现Externalizable接口调用该方法
        -- readExternalData


