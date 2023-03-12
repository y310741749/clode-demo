package cn.itcast.order.demo;

import lombok.Getter;
import lombok.Setter;

public class Demo1 {

    public static void main(String[] args) {
        Class<Demo1> demo1Class = Demo1.class;

    }
    public enum enum1{
        A("123",Demo1.class);

        @Getter
        @Setter
        private String key;

        @Getter
        @Setter
        private Class<?> t;

        enum1(String s, Class<Demo1> demo1Class) {
        }
    }
}
