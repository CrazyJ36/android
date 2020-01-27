package com.crazyj36.apiplayground;

class otherMethods {

    String otherMethodsStringFunc() {
        return "Text defined in otherMethods.java, run in MainActivity.";
    }

    int getHourOfDay() {
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        // Here Calendar.HOUR_OF_DAY shows in 24-hr time, Calendar.HOUR shows in 12-hr format.
        return rightNow.get(java.util.Calendar.HOUR);
    }
}



