package com.acme.fppdf.web;

import java.util.Arrays;
import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.acme.fppdf.domain.Conversion;
import com.acme.fppdf.domain.ConversionType;
import com.acme.fppdf.service.ConversionService;
import com.acme.fppdf.transformer.Converter;

/**
 * Spring controller for handling file upload, conversion and pdf download.
 * 
 * @author Gergely_Nagy1
 * 
 */
@Controller
@RequestMapping("/")
public class ConversionController {
    private static final String REDIRECT = "redirect:/";
    private static final String UPLOAD = "/upload";

    private final Logger logger = LoggerFactory.getLogger(ConversionController.class);

    @Autowired
    private Converter converter;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ViewResolver viewResolver;

    /**
     * Controller method for displaying the upload page.
     * 
     * This is needed so <spring:message> tag can be used on the page.
     * 
     * @param model the jsp modell.
     * @return view id
     */
    @RequestMapping("/")
    public String index(Model model) {
        buildUiModel(model);

        return UPLOAD;
    }

    /**
     * Controller method for handling the file upload
     * 
     * @param uploadForm DTO backing the form
     * @param bindingResult validation errors
     * @param uiModel Spring UI model map
     * @return a string message
     */
    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    public String create(@Valid UploadForm uploadForm,
            BindingResult bindingResult, Model uiModel) {

        // View view = null;

        String viewId = REDIRECT;
        if (bindingResult.hasErrors()) {
            try {
                logger.error(bindingResult.getFieldError().toString());
            } catch (Exception e) {
                logger.error("Could not resolve view", e);
            }

        } else {
            final Conversion conversion = new Conversion();
            conversion.setName(uploadForm.getFile().getOriginalFilename());
            conversion.setToType(uploadForm.getToType());
            conversion.setCreationDate(new Date());
            conversion.setFromBytes(uploadForm.getFile().getBytes());
            conversion.setOrientation(uploadForm.getOrientationType());
            conversion.setSizeFactor(uploadForm.getSizeFactor());

            converter.convert(conversion);

            conversionService.saveConversion(conversion);
            buildUiModel(uiModel);
            viewId = viewId + "#" + conversion.getId();
        }
        return viewId;
    }

    private void buildUiModel(Model model) {
        model.addAttribute("converterList", conversionService.findAll());
    }

    /**
     * remove an element in database
     * 
     * @param id removed element id
     * @param model jsp modell
     * @return viewname
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String remove(@PathVariable long id, Model model) {

        conversionService.remove(id);
        buildUiModel(model);
        return REDIRECT;
    }

    /**
     * download the actual pdf
     * 
     * @param id selected element id
     * @return viewname
     */
    @RequestMapping(value = "/download/pdf/{id}", method = RequestMethod.POST)
    @ResponseBody
    public View downloadPdf(@PathVariable long id) {
        View view = null;
        Conversion conversion = conversionService.findObject(id);
        if (conversion != null) {
            view = createResponseView(conversion);
        }
        return view;
    }
    
    
    /**
     * download the actual Fo
     * 
     * @param id selected element id
     * @return viewname
     */
    @RequestMapping(value = "/download/fo/{id}", method = RequestMethod.POST)
    @ResponseBody
    public View downloadFo(@PathVariable long id) {
        View view = null;
        Conversion conversion = conversionService.findObject(id);
        if (conversion != null) {
            view = createFOResponseView(conversion);
        }
        return view;
    }

    private View createFOResponseView(Conversion conversion) {
        String fileName = conversion.getName();
        byte[] convertedBytes = conversion.getFoBytes();
        String contentType = conversion.getToType().getMimeType();

        FileView returnView = new FileView();
        returnView.setContent(convertedBytes);
        returnView.setFileName(fileName + ".xml");
        returnView.setContentType(contentType);

        return returnView;
    }

    /**
     * download the actual mm
     * 
     * @param id selected element id
     * @return viewname
     */
    @RequestMapping(value = "/download/mm/{id}", method = RequestMethod.POST)
    @ResponseBody
    public View downloadMM(@PathVariable long id) {
        conversionService.findObject(id);
        View view = null;
        Conversion conversion = conversionService.findObject(id);
        if (conversion != null) {
            view = createMMResponseView(conversion);
        }
        return view;
    }

    private View createResponseView(Conversion conversion) {
        String fileName = conversion.getName();
        byte[] convertedBytes = conversion.getToBytes();
        String contentType = conversion.getToType().getMimeType();

        FileView returnView = new FileView();
        returnView.setContent(convertedBytes);
        returnView.setFileName(fileName + ".pdf");
        returnView.setContentType(contentType);

        return returnView;
    }

    private View createMMResponseView(Conversion conversion) {
        String fileName = conversion.getName();
        byte[] convertedBytes = conversion.getFromBytes();

        FileView returnView = new FileView();
        returnView.setContent(convertedBytes);
        returnView.setFileName(fileName);

        return returnView;
    }

    protected void populateCreateForm(Model uiModel, UploadForm uploadForm) {
        uiModel.addAttribute("uploadForm", uploadForm);

        uiModel.addAttribute("conversiontypes",
                Arrays.asList(ConversionType.values()));
    }
}
