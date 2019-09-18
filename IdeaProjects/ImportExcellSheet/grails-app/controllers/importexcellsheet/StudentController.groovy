package importexcellsheet

import CO.ImportExcellSheetCO
import grails.validation.ValidationException
import jxl.LabelCell
import jxl.Sheet
import jxl.Workbook
import org.grails.core.io.ResourceLocator
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage


import org.springframework.core.io.Resource


import static org.springframework.http.HttpStatus.*

class StudentController {


    StudentService studentService

    private final static int COLUMN_LAST_NAME = 0
    private final static int COLUMN_FIRST_NAME = 1
    private final static int COLUMN_DATE_OF_BIRTH = 2


    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond studentService.list(params), model: [studentCount: studentService.count()]
       // render file: image.inputStream, contentType: 'image/png'
    }

    def show(Long id) {
        respond studentService.get(id)
    }

    def create() {
        respond new Student(params)
    }

    def save(Student student) {
        if (student == null) {
            notFound()
            return
        }

        try {
            studentService.save(student)
        } catch (ValidationException e) {
            respond student.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'student.label', default: 'Student'), student.id])
                redirect student
            }
            '*' { respond student, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond studentService.get(id)
    }

    def update(Student student) {
        if (student == null) {
            notFound()
            return
        }

        try {
            studentService.save(student)
        } catch (ValidationException e) {
            respond student.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'student.label', default: 'Student'), student.id])
                redirect student
            }
            '*' { respond student, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        studentService.delete(id)


        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'student.label', default: 'Student'), id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def upload() {

    }

    /*def images(long id) {
        Student student = Student.get(id)
        if ( student == null) {
            flash.message = "Document not found."
            redirect (action:'upload')
        } else {

            def student1 = new Student()
            student1.filename = file.originalFilename
            student1.fullPath = grailsApplication.config.uploadFolder + student1.filename
            file.transferTo(new File(student1.fullPath))
            student1.save()
        }
    }*/

    def doUpload(ImportExcellSheetCO co) {

        MultipartFile multipartFile = co.myFile

        String filename = multipartFile.originalFilename
        println(filename)
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1);
            println(extension)
        }
        if(extension in  ["png", "jpg", "jpeg"]) {
            byte[] bytesArray = multipartFile.bytes

            response.contentType = '' // or the appropriate image content type
            response.outputStream << bytesArray
            return
        }
        println("name : ${multipartFile.name} original: ${multipartFile.originalFilename} size: ${multipartFile.size}")

        if(extension == "xls") {
        Workbook workbook = Workbook.getWorkbook(co.myFile.inputStream);
        Sheet sheet = workbook.getSheet(0);

        // skip first row (row 0) by starting from 1
        for (int row = 1; row < sheet.getRows(); row++) {
            LabelCell lastName = sheet.getCell(COLUMN_LAST_NAME, row) as LabelCell
            LabelCell firstName = sheet.getCell(COLUMN_FIRST_NAME, row) as LabelCell
            LabelCell dateOfBirth = sheet.getCell(COLUMN_DATE_OF_BIRTH, row) as LabelCell


            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            //LocalDate localDate = LocalDate.parse(dateOfBirth.string, formatter);

            new Student(lastName: lastName.string, firstName: firstName.string, dateOfBirth: dateOfBirth.string).save()
        }
        }
        redirect(controller: 'student')
    }
   /* def getImage(){
        def path = params.filepath
        //returns an image to display
        BufferedImage originalImage = ImageIO.read(new File(path));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        def filetext = path.substring(path.indexOf(".")+1, path.length())

        println(filetext)
        ImageIO.write( originalImage, filetext, baos );
        baos.flush();

        byte[] img = baos.toByteArray();
        baos.close();
        response.setHeader('Content-length', img.length.toString())
        response.contentType = "image/"+filetext // or the appropriate image content type
        response.outputStream << img
        response.outputStream.flush()
    }*/


    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'student.label', default: 'Student'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }



}
