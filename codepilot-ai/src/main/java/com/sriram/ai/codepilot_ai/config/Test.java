package com.sriram.ai.codepilot_ai.config;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(TimeZone.getDefault().getID());

        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/codepilot",
                "postgres",
                "postgres");

        System.out.println("Connected!");
    }
}
