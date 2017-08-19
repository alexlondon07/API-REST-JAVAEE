package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.model.SocialMedia;
import com.example.demo.service.SocialMediaService;
import com.example.demo.util.CustomErrorType;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	public static final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);

	@Autowired
	SocialMediaService _socialMediaService;

	// ------------------- GET SocialMedias-----------------------------------------

	@RequestMapping(value = "/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedias(
			@RequestParam(value = "name", required = false) String name) {

		List<SocialMedia> socialMedias = new ArrayList<>();

		if (name == null) {
			socialMedias = _socialMediaService.findAllSocialMedias();

			if (socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		} else {
			SocialMedia socialMedia = _socialMediaService.findByName(name);
			if (socialMedia == null) {
				return new ResponseEntity(new CustomErrorType("SocialMedia not found"), HttpStatus.NOT_FOUND);
			}
			socialMedias.add(socialMedia);
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		}
	}

	// ------------------- GET SocialMedia By Id-----------------------------------------

	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia> getSocialMediaById(@PathVariable("id") Long idSocialMedia) {
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}

		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (socialMedia == null) {
			logger.error("idSocialMedia {} not found.", idSocialMedia);
			return new ResponseEntity(
					new CustomErrorType("SocialMedia with idSocialMedia " + idSocialMedia + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<SocialMedia>(socialMedia, HttpStatus.OK);
	}

	// ------------------- POST SocialMedia-----------------------------------------

	@RequestMapping(value = "/socialMedias", method = RequestMethod.POST, headers = "Accept=aplication/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia,
			UriComponentsBuilder uriComponentsBuilder) {
		logger.info("Creating SocialMedia : {}", socialMedia);

		if (socialMedia.getName().equals(null) || socialMedia.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("SocialMedia name is required"), HttpStatus.CONFLICT);
		}

		if (_socialMediaService.findByName(socialMedia.getName()) != null) {
			logger.error("Unable to create. A SocialMedia with name {} already exist", socialMedia.getName());

			return new ResponseEntity(
					new CustomErrorType("Unable to create. A SocialMedia with name " + socialMedia.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}

		_socialMediaService.saveSocialMedia(socialMedia);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponentsBuilder.path("/v1/socialMedias{id}")
				.buildAndExpand(socialMedia.getIdSocialMedia()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- UPDATE SocialMedia-----------------------------------------

	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<SocialMedia> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia) {
		logger.info("Updating SocialMedia with id {}", idSocialMedia);
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}

		SocialMedia currentSocialMedia = _socialMediaService.findById(idSocialMedia);
		if (currentSocialMedia == null) {
			logger.error("Unable to update. SocialMedia with id {} not found.", idSocialMedia);
			return new ResponseEntity(new CustomErrorType("Unable to upate. SocialMedia with id " + idSocialMedia + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());

		_socialMediaService.updateSocialMedia(currentSocialMedia);
		return new ResponseEntity<SocialMedia>(currentSocialMedia, HttpStatus.OK);
	}

	// ------------------- DELETE SocialMedia -----------------------------------------

	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=aplication/json")
	public ResponseEntity<SocialMedia> deleteSocialMedia(@PathVariable("id") Long idSocialMedia) {
		logger.info("Fetching & Deleting SocialMedia with id {}", idSocialMedia);
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"), HttpStatus.CONFLICT);
		}

		SocialMedia socialMedia = _socialMediaService.findById(idSocialMedia);
		if (socialMedia == null) {
			logger.error("Unable to delete. SocialMedia with id {} not found.", idSocialMedia);
			return new ResponseEntity(new CustomErrorType("Unable to delete. SocialMedia with id " + idSocialMedia + " not found."),
					HttpStatus.NOT_FOUND);
		}

		_socialMediaService.deleteSocialMediaById(idSocialMedia);
		return new ResponseEntity<SocialMedia>(HttpStatus.OK);
	}

}
