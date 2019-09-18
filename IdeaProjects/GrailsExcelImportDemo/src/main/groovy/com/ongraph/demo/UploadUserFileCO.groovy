package com.ongraph.demo

import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile

class UploadUserFileCO implements Validateable {

    MultipartFile myFile

    static constraints ={
        myFile nullable: false
    }
}
