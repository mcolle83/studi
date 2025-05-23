package com.ccc.jo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccc.jo.model.AchatEpreuve;
import com.ccc.jo.service.AchatEpreuveService;
import com.ccc.jo.service.QRCodeService;
import org.springframework.http.*;

@Controller
public class QRCodeController {
    @Autowired
    private QRCodeService qrCodeService;
    private final AchatEpreuveService achatepreuveService;

    @Autowired
    public QRCodeController(AchatEpreuveService achatepreuveService) {
        this.achatepreuveService = achatepreuveService;
    }

    /**
    * Affiche le QRCode, en format PNG, de l'achat. Lorsqu'il est scanné, il affiche la combinaison des deux clés (Utilisateur + Achat), le nom de l'épreuve (et de la discipline), ainsi que le nombre de places
    */
    @GetMapping(value = "/qrcode/png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCodePNG(Model model,  @RequestParam AchatEpreuve achatepreuve) throws Exception {
        AchatEpreuve achatepreuvechoisi = achatepreuveService.getAchatEpreuveById(achatepreuve.getId());
        String text = "[ " + achatepreuvechoisi.getAchat().getUtilisateur().getCle() + "-" + achatepreuvechoisi.getAchat().getCle() + " , " + achatepreuvechoisi.getEpreuve().getNom() + " (" + achatepreuvechoisi.getEpreuve().getDiscipline() + ") , " + achatepreuvechoisi.getQuantite() + " place.s ]";
        byte[] qrCodeByteValue = qrCodeService.generateQRCodeImage(text, 400, 400);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeByteValue);
    }

    /**
    * Affiche le QRCode en format PDF
    */
    @GetMapping(value = "/qrcode/pdf")
    public ResponseEntity<byte[]> generateQRCodePDF(@RequestParam String text) throws Exception {
        byte[] qrCodeByteValue = qrCodeService.generateQRCodePDF(text, 400, 400);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcode.pdf");
        return new ResponseEntity<>(qrCodeByteValue, headers, HttpStatus.OK);
    }
}