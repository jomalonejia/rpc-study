package com.jj.protocol;

import com.jj.protocol.protostuff.ProtostuffSerializationUtil;
import org.junit.Test;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class TestProtostuff {

    public final class Foo
    {
        String name;
        int id;

        public Foo(String name, int id)
        {
            this.name = name;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    @Test
    public void test1(){
        Foo foo = new Foo("foo", 1);
        byte[] serialize = ProtostuffSerializationUtil.serialize(foo);
        System.out.println(ProtostuffSerializationUtil.deserialize(serialize,Foo.class));
    }
}
