package com.youthen.framework.common;

public class DocConverter {

    private final PDFConverter pdfConverter;

    public DocConverter(final PDFConverter aPdfConverter) {
        super();
        this.pdfConverter = aPdfConverter;
    }

    public void convert(final String inputFile) {
        this.pdfConverter.convert2PDF(inputFile);

    }

}
