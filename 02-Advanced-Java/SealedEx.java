public class SealedEx {
    public static void main(String[] args) {

    }
}

sealed class A permits B, C {

}

non-sealed class B extends A {

}

final class C extends A {

}

final class D extends B {

}
