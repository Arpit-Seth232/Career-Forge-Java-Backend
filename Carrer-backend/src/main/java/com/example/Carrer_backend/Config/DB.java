package com.example.Carrer_backend.Config;

import java.sql.Connection;

import javax.sql.DataSource;

import java.sql.*;


import org.springframework.stereotype.Component;



@Component
public class DB {

    private DataSource dataSource;

    public DB(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws RuntimeException {

        try{
            return dataSource.getConnection();
        }catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        
    }


    
}
