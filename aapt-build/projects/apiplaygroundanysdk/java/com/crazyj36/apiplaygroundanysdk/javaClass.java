package com.crazyj36.apiplaygroundanysdk;

class javaClass {
	public static String myMethod() {
		String text = "Using A normal java class";
		return text;
	}

    public String getHourOfDay() {
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        return Integer.toString(rightNow.get(java.util.Calendar.HOUR));

    }
}
