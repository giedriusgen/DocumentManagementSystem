package it.akademija.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.model.document.DocumentForClient;
import it.akademija.model.document.DocumentInfoAfterReview;
import it.akademija.model.document.NewDocument;
import it.akademija.model.file.DBFile;
import it.akademija.model.file.UploadFileResponse;
import it.akademija.model.group.GroupForClient;
import it.akademija.model.group.NewGroup;
import it.akademija.service.DBFileStorageService;
import it.akademija.service.DocumentService;
import it.akademija.service.GroupService;

@RestController
@RequestMapping(value = "/api/document")
public class DocumentController {

	private final DocumentService documentService;
	private final DBFileStorageService dbFileStorageService;

	@Autowired
	public DocumentController(DocumentService documentService, DBFileStorageService dbFileStorageService) {
		this.documentService = documentService;
		this.dbFileStorageService = dbFileStorageService;
	}

	@RequestMapping(path = "/{username}", method = RequestMethod.GET)
	@ApiOperation(value = "Get documents by author", notes = "Returns list of documents by author")
	public List<DocumentForClient> getDocumentsForClientByAuthor(@PathVariable String username) {
		return documentService.getDocumentsForClientByAuthor(username);
	}

	@RequestMapping(path = "/{id}/{username}", method = RequestMethod.GET)
	@ApiOperation(value = "Get documents by author", notes = "Returns list of documents by author")
	public DocumentForClient getDocumentForClientById(@PathVariable String username, @PathVariable Long id) {
		return documentService.getDocumentForClientById(username, id);
	}

	@RequestMapping(path = "/one-file", method = RequestMethod.POST)
	@ApiOperation(value = "Save document with one file", notes = "Creates document with one file")
	@ResponseStatus(HttpStatus.CREATED)
	public UploadFileResponse saveDocumentWithOneFile(
			@ApiParam(required = true) @Valid @ModelAttribute final NewDocument newDocument,
			@RequestParam("file") MultipartFile file) {
		DBFile dbFile = documentService.saveDocumentWithOneFile(newDocument, file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(dbFile.getId()) 
				.toUriString();
		return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	
	  @RequestMapping(path = "/multiple-files", method = RequestMethod.POST)
		@ApiOperation(value = "Save document with multiple files", notes = "Creates document with multiple files")
	   	@ResponseStatus(HttpStatus.CREATED)
	    public List<UploadFileResponse> saveDocumentWithMultipleFiles(@ApiParam(required = true) @Valid @ModelAttribute final NewDocument newDocument,
				@RequestParam("files") MultipartFile[] files) {
		  
		  List<DBFile> dBFiles = documentService.saveDocumentWithMultipleFiles(newDocument, files);
		  List<UploadFileResponse> list = new ArrayList<UploadFileResponse>();
		  for(int i = 0; i< files.length; i++) {
			  String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
						.path(dBFiles.get(i).getId()) 
						.toUriString();
			  list.add(new UploadFileResponse(dBFiles.get(i).getFileName(), fileDownloadUri, files[i].getContentType(), files[i].getSize()));
			  
		  }
		  
		 
	        return list;
	    }

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "Save document", notes = "Creates document with data")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveDocument(@ApiParam(required = true) @Valid @RequestBody final NewDocument newDocument) {
		documentService.saveDocument(newDocument);
	}

	@RequestMapping(path = "/submit", method = RequestMethod.POST)
	@ApiOperation(value = "Submit document", notes = "Creates document with data. Status SUBMITTED")
	@ResponseStatus(HttpStatus.CREATED)
	public void submitDocument(@ApiParam(required = true) @Valid @RequestBody final NewDocument newDocument) {
		documentService.submitDocument(newDocument);
	}

	@RequestMapping(path = "/submit-after-save/{id}", method = RequestMethod.PUT)
	@ApiOperation(value = "Update document info after save for later")
	public void submitDocumentAfterSaveForLater(
			@ApiParam(required = true) @Valid @RequestBody final NewDocument newDocument, @PathVariable Long id) {
		documentService.submitDocumentAfterSaveForLater(newDocument, id);
	}

	@RequestMapping(path = "/approve-document", method = RequestMethod.PUT)
	@ApiOperation(value = "Update document info after approval", notes = "Update document info after approval")
	public void approveDocument(
			@ApiParam(required = true) @Valid @RequestBody final DocumentInfoAfterReview documentInfoAfterReview) {
		documentService.approveDocument(documentInfoAfterReview);
	}

	@RequestMapping(path = "/reject-document", method = RequestMethod.PUT)
	@ApiOperation(value = "Update document info after approval", notes = "Update document info after approval")
	public void rejectDocument(
			@ApiParam(required = true) @Valid @RequestBody final DocumentInfoAfterReview documentInfoAfterReview) {
		documentService.rejectDocument(documentInfoAfterReview);
	}

}