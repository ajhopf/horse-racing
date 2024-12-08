package pegasusdatastore

import grails.converters.JSON
import grails.validation.ValidationException
import model.dtos.JockeyDTO
import model.mappers.JockeyMapper

class JockeyController {

    JockeyService jockeyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max, Integer offset) {
        max = Math.min(max ?: 10, 1000)
        offset = offset ?: 0

        List<JockeyDTO> jockeyDTOList = jockeyService.list([max: max, offset: offset])

        render(status: 200, contentType: "application/json") {
            "jockeys" jockeyDTOList
            "items" max
            "offsetItems" offset
        }
    }

    def show(Long id) {
        def jockeyDTO = jockeyService.getJockey(id)

        if (!jockeyDTO) {
            notFound()
            return
        }

        render(status: 200, contentType: "application/json") {
            "jockey" jockeyDTO
        }
    }

    def save(Jockey newJockey) {
        try {
            newJockey = jockeyService.save(newJockey)
        } catch (ValidationException e) {
            render(status: 400, contentType: "application/json") {
                message "Jockey not saved"
                validationErrors newJockey.errors.fieldErrors.collect { fieldError ->
                    [
                            field: fieldError.field,
                            rejectedValue: fieldError.rejectedValue,
                    ]
                }
            }
            return
        }

        render (status: 201, contentType: "application/json") {
            "jockey" JockeyMapper.toDTO(newJockey)
        }
    }


    def update(Jockey updatedJockey) {
        if (updatedJockey == null) {
            notFound()
            return
        }

        try {
            updatedJockey = jockeyService.save(updatedJockey)
        } catch (ValidationException e) {
            render(status: 400, contentType: "application/json") {
                message "Jockey not updated"
                validationErrors updatedJockey.errors.fieldErrors.collect { fieldError ->
                    [
                            field: fieldError.field,
                            rejectedValue: fieldError.rejectedValue,
                    ]
                }
            }
        }


        render(contentType: "application/json", status: 200) {
            "jockey" JockeyMapper.toDTO(updatedJockey)
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        boolean deleted = jockeyService.deleteJockey(id)

        if (deleted) {
            render(status: 204, contentType: "application/json") {}
        } else {
            render(status: 404, contentType: "application/json") {
                message "Jockey not found, could not delete"
            }
        }

    }

    protected void notFound() {
        render (contentType: "application/json", status: 404) {
            message: "Resource not found"
        }
    }
}
