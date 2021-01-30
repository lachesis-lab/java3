package ru.lachesis.lesson7;

public class TestClass1  {

    @Test(priority = 2)
    private void testMethod1(){
        System.out.println("testMethod1 is started");
    }
    @Test(priority = 2)
    private void testMethod3(){
        System.out.println("testMethod3 is started");
    }
    @Test(priority = 1)
    private void testMethod2(boolean arg1){
        System.out.println("testMethod2 is started with default argument = \""+arg1+"\"");
    }
    private void nonTestMethod(){
        System.out.println("nonTestMethod is started");
    }
    @BeforeSuit
    static void beforeSuitMethod(){
        System.out.println("beforeSuitMethod is started");
    }
    @AfterSuit
    static void afterSuitMethod(){
        System.out.println("afterSuitMethod is started");
    }

}
