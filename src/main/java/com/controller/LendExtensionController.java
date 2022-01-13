package com.controller;

import com.context.LendExtension;
import com.service.LendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/lend-extensions")
public class LendExtensionController {
    private final LendService lendService;

    public LendExtensionController(LendService lendService) {
        this.lendService = lendService;
    }

    @Operation(summary = "Respond to a lend extension", operationId = "respondToLendExtension")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Responded to lend extension"),
            @ApiResponse(responseCode = "400", description = "Incorrect extension decision"),
            @ApiResponse(responseCode = "404", description = "Lend extension not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}/respond", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> respondToLendExtension(@PathVariable Long id, @RequestParam LendExtension.LendExtensionStatus extensionDecision) {
        if (extensionDecision == LendExtension.LendExtensionStatus.PENDING) {
            return ResponseEntity.badRequest().build();
        }

        Optional<LendExtension> existingLendExtension = lendService.getLendExtensionById(id);

        if (existingLendExtension.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Cannot find lend extension with id: %d", id));
        }

        if (existingLendExtension.get().getExtensionStatus() != LendExtension.LendExtensionStatus.PENDING) {
            return ResponseEntity.badRequest().build();
        }

        switch (extensionDecision) {
            case APPROVED:
                lendService.approveLendExtension(existingLendExtension.get());
            case DECLINED:
            default:
                existingLendExtension.get().setExtensionStatus(extensionDecision);
                lendService.saveLendExtension(existingLendExtension.get());
        }

        return ResponseEntity.noContent().build();
    }
}
