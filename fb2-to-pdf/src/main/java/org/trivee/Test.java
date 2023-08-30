package org.trivee;

import org.trivee.fb2pdf.FB2toPDF;

public class Test {

    public static void main(String[] args) {
        try
        {
            FB2toPDF.translate("input.fb2", "output.pdf");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
