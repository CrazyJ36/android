package com.crazyj36.useanothermethod;

// Call "new OtherClass()" to get and use this class in other classes(MainActivity).
public class OtherClass {

    // Call "new OtherClass().test()" to use this test method, which only returns A string to the caller.
    public String test() {
        return "\nThis string is from OtherClass.java";
    }

}
