package com.upiiz.examenmali.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.upiiz.examenmali.entities.Factura;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.awt.Color;

@Service
public class PdfService {

    public byte[] generarFacturaPdf(Factura factura, String tecnico) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // Fuentes
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font fuentePagado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.GREEN);

        // Encabezado
        Paragraph titulo = new Paragraph("TECHREPAIR - ORDEN DE SERVICIO", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph("Folio de Servicio: #000" + factura.getId(), fuenteNormal));
        document.add(new Paragraph("Fecha: " + factura.getFecha(), fuenteNormal));
        document.add(Chunk.NEWLINE);

        // Estado de Pago (Solo si está pagada)
        if (factura.isPagada()) {
            Paragraph estado = new Paragraph("ESTADO: PAGADO CON ÉXITO", fuentePagado);
            estado.setAlignment(Element.ALIGN_RIGHT);
            document.add(estado);
        }

        // Datos del Cliente y Técnico
        document.add(new Paragraph("DATOS GENERALES", fuenteSubtitulo));
        document.add(new Paragraph("Cliente: " + factura.getClienteNombre(), fuenteNormal));
        document.add(new Paragraph("Técnico Responsable: " + tecnico, fuenteNormal));
        document.add(Chunk.NEWLINE);

        // Detalle de la Reparación/Producto
        document.add(new Paragraph("DETALLE DEL SERVICIO / PRODUCTO", fuenteSubtitulo));
        document.add(new Paragraph("Concepto: " + factura.getItemNombre(), fuenteNormal));
        document.add(new Paragraph("Cantidad: " + factura.getCantidad(), fuenteNormal));
        document.add(new Paragraph("Total a Pagar: $" + factura.getTotal(), fuenteSubtitulo));
        document.add(Chunk.NEWLINE);

        // Pie de página
        document.add(new Paragraph("________________________________", fuenteNormal));
        document.add(new Paragraph("Firma de Conformidad", fuenteNormal));
        document.add(new Paragraph("\n* 30 días de garantía en mano de obra.", fuenteNormal));

        document.close();
        return out.toByteArray();
    }
}