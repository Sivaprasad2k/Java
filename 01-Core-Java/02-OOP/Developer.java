public class Developer {
    public static void main(String a[]) {
        Devops siva = new Devops();
        Computer lap = new Laptop();
        Computer desk = new Desktop();
        siva.devApp(desk);
    }
}

interface Computer {

    public abstract void code();
}

class Laptop implements Computer {
    @Override
    public void code() {
        System.out.println("Code,Compile,Test,Deploy");
    }
}

class Desktop implements Computer {
    @Override
    public void code() {
        System.out.println("Code,Compile,Test,Deploy,but faster");
    }
}

class Devops {

    public void devApp(Computer lap) {
        lap.code();
    }
}

