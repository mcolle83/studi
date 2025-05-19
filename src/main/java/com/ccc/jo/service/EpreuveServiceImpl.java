package com.ccc.jo.service;

import com.ccc.jo.model.Epreuve;
import com.ccc.jo.repository.EpreuveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.util.List;
import java.io.InputStream;

@Service
public class EpreuveServiceImpl implements EpreuveService {

    private final EpreuveRepository epreuveRepository;
    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;
    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    @Autowired
    public EpreuveServiceImpl(EpreuveRepository epreuveRepository) {
        this.epreuveRepository = epreuveRepository;
    }

    @Override
    public List<Epreuve> getAllEpreuves() {
        return epreuveRepository.findAll();
    }

    @Override
    public Epreuve getEpreuveById(Long id) {
        return epreuveRepository.findById(id).orElse(null);
    }

    @Override
    public void createEpreuve(Epreuve epreuve) {
        Epreuve newEpreuve = new Epreuve();
        newEpreuve.setNom(epreuve.getNom());
		newEpreuve.setDiscipline(epreuve.getDiscipline());
        newEpreuve.setLieu(epreuve.getLieu());
        newEpreuve.setVille(epreuve.getVille());
        newEpreuve.setDate(epreuve.getDate());
        newEpreuve.setCapacite(epreuve.getCapacite());
        newEpreuve.setPrix(epreuve.getPrix());
        newEpreuve.setDescription(epreuve.getDescription());
        newEpreuve.setImage(epreuve.getImage());
        if (epreuve.getImagefile() != null){
            try {
                MultipartFile file = epreuve.getImagefile();
                String s3FileName = file.getOriginalFilename();
                newEpreuve.setImage(s3FileName);
                BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
                AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_WEST_3)
                    .build();
                InputStream inputStream = file.getInputStream();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType("image/jpeg");
                String bucketName = "mcolle83studi";
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);
                amazonS3Client.putObject(putObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        epreuveRepository.save(newEpreuve);
    }

    @Override
    public void updateEpreuve(Epreuve newInfosEpreuve) {
        Epreuve updatedEpreuve = epreuveRepository.findById(newInfosEpreuve.getId()).orElse(null);
        updatedEpreuve.setNom(newInfosEpreuve.getNom());
        updatedEpreuve.setDiscipline(newInfosEpreuve.getDiscipline());
        updatedEpreuve.setLieu(newInfosEpreuve.getLieu());
        updatedEpreuve.setVille(newInfosEpreuve.getVille());
        updatedEpreuve.setDate(newInfosEpreuve.getDate());
        updatedEpreuve.setCapacite(newInfosEpreuve.getCapacite());
        updatedEpreuve.setPrix(newInfosEpreuve.getPrix());
        updatedEpreuve.setDescription(newInfosEpreuve.getDescription());
        updatedEpreuve.setImage(newInfosEpreuve.getImage());
        if (newInfosEpreuve.getImagefile() != null){
            try {
                MultipartFile file = newInfosEpreuve.getImagefile();
                String s3FileName = file.getOriginalFilename();
                updatedEpreuve.setImage(s3FileName);
                BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
                AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_WEST_3)
                    .build();
                InputStream inputStream = file.getInputStream();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType("image/jpeg");
                String bucketName = "mcolle83studi";
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);
                amazonS3Client.putObject(putObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        epreuveRepository.save(updatedEpreuve);
    }

    @Override
    public void deleteEpreuve(Long id) {
        epreuveRepository.deleteById(id);
    }

    @Override
    public void reduceEpreuveCapacite(Long id, Integer quantite) {
        Epreuve updatedEpreuve = epreuveRepository.findById(id).orElse(null);
        updatedEpreuve.setCapacite(updatedEpreuve.getCapacite() - quantite);
        epreuveRepository.save(updatedEpreuve);
    }

}