package com.ongraph.demo

import grails.validation.ValidationException
import jxl.LabelCell
import jxl.NumberCell
import jxl.Sheet
import jxl.Workbook

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static org.springframework.http.HttpStatus.*

class PersonController {

    PersonService personService

    private final static int COLUMN_LAST_NAME = 0
    private final static int COLUMN_FIRST_NAME = 1
    private final static int COLUMN_DATE_OF_BIRTH = 2
    private final static int COLUMN_NUMBER_OF_CHILDREN = 3

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond personService.list(params), model:[personCount: personService.count()]
    }

    def show(Long id) {
        respond personService.get(id)
    }

    def create() {
        respond new Person(params)
    }

    def save(Person person) {
        if (person == null) {
            notFound()
            return
        }

        try {
            personService.save(person)
        } catch (ValidationException e) {
            respond person.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), person.id])
                redirect person
            }
            '*' { respond person, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond personService.get(id)
    }

    def update(Person person) {
        if (person == null) {
            notFound()
            return
        }

        try {
            personService.save(person)
        } catch (ValidationException e) {
            respond person.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'person.label', default: 'Person'), person.id])
                redirect person
            }
            '*'{ respond person, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        personService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def upload() {}

    def doUpload(UploadUserFileCO co) {
        Workbook workbook = Workbook.getWorkbook(co.myFile.inputStream);
        Sheet sheet = workbook.getSheet(0);

        // skip first row (row 0) by starting from 1
        for (int row = 1; row < sheet.getRows(); row++) {
            LabelCell lastName = sheet.getCell(COLUMN_LAST_NAME, row) as LabelCell
            LabelCell firstName = sheet.getCell(COLUMN_FIRST_NAME, row) as LabelCell
            LabelCell dateOfBirth = sheet.getCell(COLUMN_DATE_OF_BIRTH, row) as LabelCell
            NumberCell numberOfChildren = sheet.getCell(COLUMN_NUMBER_OF_CHILDREN, row) as NumberCell

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            //LocalDate localDate = LocalDate.parse(dateOfBirth.string, formatter);

            new Person(lastName:lastName.string , firstName:firstName.string , dateOfBirth:dateOfBirth.string, numberOfChildren:numberOfChildren.value).save()

        }
        redirect(controller: 'person')
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
