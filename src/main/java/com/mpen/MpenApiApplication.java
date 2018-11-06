/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * APP主类.
 * 
 * @author kai
 */
@EnableTransactionManagement
@SpringBootApplication
public class MpenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpenApiApplication.class, args);
    }
}
