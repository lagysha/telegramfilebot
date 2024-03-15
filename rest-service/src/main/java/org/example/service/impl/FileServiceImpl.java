package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.CryptoTool;
import org.example.dao.AppDocumentDAO;
import org.example.dao.AppPhotoDAO;
import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.example.service.FileService;
import org.springframework.stereotype.Service;

@Service
@Log4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String docId) {
        var id = cryptoTool.idOf(docId);
        if(id == null){
            return null;
        }
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        var id = cryptoTool.idOf(photoId);
        if(id == null){
            return null;
        }
        return appPhotoDAO.findById(id).orElse(null);
    }
}
